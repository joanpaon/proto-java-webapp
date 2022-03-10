/* 
 * Copyright 2022 JAPO Labs - japolabs@gmail.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.japo.java.bll.commands.usuario;

import org.japo.java.bll.commands.Command;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.japo.java.bll.commands.admin.CommandValidation;
import org.japo.java.dal.DALAvatar;
import org.japo.java.dal.DALPerfil;
import org.japo.java.dal.DALUsuario;
import org.japo.java.entities.Avatar;
import org.japo.java.entities.Perfil;
import org.japo.java.entities.Usuario;
import org.japo.java.libraries.UtilesBase64;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class CommandUsuarioModificacion extends Command {

    @Override
    @SuppressWarnings("ConvertToStringSwitch")
    public void process() throws ServletException, IOException {
        // Salida
        String out = "usuario/usuario-modificacion";

        // Entidad
        Usuario usuario;

        // Sesión
        HttpSession sesion = request.getSession(false);

        // Validar Sesión
        if (!validarSesion(sesion)) {
            out = "message/sesion-invalida";
        } else {
            // Capas de Negocio
            CommandValidation validator = new CommandValidation(sesion);

            // Capas de Datos
            DALAvatar dalAvatar = new DALAvatar(sesion);
            DALPerfil dalPerfil = new DALPerfil(sesion);
            DALUsuario dalUsuario = new DALUsuario(sesion);

            if (validator.validarAccesoComando(getClass().getSimpleName())) {
                // request > ID Usuario a Modificar
                int id = obtenerID();

                // request > ID Operación
                String op = request.getParameter("op");

                // Captura de Datos
                if (op == null || op.equals("captura")) {
                    // ID Usuario + BD > Usuario
                    usuario = obtenerUsuario(id, dalUsuario);

                    // BD > Lista de Abonos
                    List<Perfil> perfiles = dalPerfil.listar();

                    // Inyectar Datos > JSP
                    request.setAttribute("usuario", usuario);
                    request.setAttribute("perfiles", perfiles);
                } else if (op.equals("proceso")) {
                    // Request > Parámetros
                    String user = obtenerUser();
                    String pass = obtenerPass();
                    Avatar avatar = obtenerAvatar(dalAvatar);
                    int avatarId = avatar == null ? 0 : avatar.getId();
                    int perfil = obtenerPerfil();

                    // Parámetros > Entidad
                    usuario = new Usuario(id, user, pass, avatarId, "", perfil, "");

                    // Ejecutar Operación
                    boolean checkOK = dalUsuario.modificar(usuario);

                    // Validar Operación
                    if (checkOK) {
                        out = "controller?cmd=usuario-listado";
                    } else {
                        out = "message/operacion-cancelada";
                    }
                } else {
                    out = "message/operacion-desconocida";
                }
            } else {
                out = "message/acceso-denegado";
            }
        }

        // Redirección
        forward(out);
    }

    private int obtenerID() throws IOException {
        // ID Usuario
        int id;

        try {
            // Request > ID Usuario
            id = Integer.parseInt(request.getParameter("id"));

            // Validar ID Usuario
            if (!Usuario.validarId(id)) {
                throw new IOException("Usuario NO Registrado");
            }
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } catch (NumberFormatException e) {
            throw new IOException("ID de Usuario Incorrecta");
        }

        // Retorno: ID Usuario
        return id;
    }

    private String obtenerUser() throws IOException {
        // Request > User
        String user = request.getParameter("user");

        // Validar User
        if (!Usuario.validarUser(user)) {
            throw new IOException("Nombre de Usuario Incorrecto");
        }

        // Retorno: Nombre de Usuario
        return user;
    }

    private String obtenerPass() throws IOException {
        // Request > Pass
        String pass = request.getParameter("pass");

        // Validar Contraseña
        if (!Usuario.validarPass(pass)) {
            throw new IOException("Contraseña Incorrecta");
        }

        // Retorno: Contraseña
        return pass;
    }

    private Avatar obtenerAvatar(DALAvatar dalAvatar) throws IOException, ServletException {
        // Datos > Avatar
        Avatar avatar = null;

        // Request > Part
        Part part = request.getPart("avatar");

        // Imagen Enviada
        if (part.getSize() > 0) {
            // Part > Nombre Imagen
            String nombre = obtenerNombreAvatar(part);

            // Imagen Base64
            String imagen;

            // Validar Tamaño Avatar
            if (Avatar.MAX_SIZE <= 0) {
                // No hay tamaño máximo
                imagen = UtilesBase64.obtenerImagenBase64(part);
            } else if (part.getSize() <= Avatar.MAX_SIZE) {
                // Tamaño Correcto
                imagen = UtilesBase64.obtenerImagenBase64(part);
            } else {
                // Tamaño Excesivo
                throw new IOException("Tamaño de imagen excesivo");
            }

            // Datos > Avatar
            avatar = new Avatar(0, nombre, imagen);

            // Avatar > BD
            if (dalAvatar.insertar(avatar)) {
                // BD > Avatar
                avatar = dalAvatar.consultar(nombre);
            }
        }

        // Retorno: Avatar
        return avatar;
    }

    private int obtenerPerfil() throws IOException {
        // Request > ID Perfil
        int perfil = Integer.parseInt(request.getParameter("perfil"));

        // Validar ID Perfil
        if (!Usuario.validarPerfil(perfil)) {
            throw new IOException("Perfil Incorrecto");
        }

        // Retorno: ID Perfil
        return perfil;
    }

    private String obtenerNombreAvatar(Part part) {
        // Part > Nombre Fichero Enviado
        String nombre = part.getSubmittedFileName();

        // Elimina Extensión
        nombre = nombre.substring(0, nombre.lastIndexOf("."));

        // Valída Nombre
        if (nombre.length() > Avatar.MAX_CHARS) {
            nombre = nombre.substring(nombre.length() - Avatar.MAX_CHARS - 1);
        } else if (nombre.isEmpty()) {
            nombre = "avatar" + String.format("%6d", new Random().nextInt(100000, 1000000));
        }

        // Retorno: Imagen Base64
        return nombre;
    }

    private Usuario obtenerUsuario(int id, DALUsuario dalUsuario) throws IOException {
        // ID Usuario + BD > Usuario
        Usuario usuario = dalUsuario.consultar(id);

        // Validar Usuario
        if (usuario == null) {
            throw new IOException("Usuario NO Registrado");
        }

        // Retorno: Usuario
        return usuario;
    }
}

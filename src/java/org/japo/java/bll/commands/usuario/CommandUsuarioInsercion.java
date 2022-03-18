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
import org.japo.java.dal.DALAvatar;
import org.japo.java.dal.DALPerfil;
import org.japo.java.dal.DALUsuario;
import org.japo.java.entities.Avatar;
import org.japo.java.entities.Perfil;
import org.japo.java.entities.Usuario;
import org.japo.java.libraries.UtilesUsuario;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class CommandUsuarioInsercion extends Command {

    @Override
    @SuppressWarnings("ConvertToStringSwitch")
    public void process() throws ServletException, IOException {
        // Salida
        String out = "usuario/usuario-insercion";

        // Validar Sesión
        if (validarSesion(request)) {
            // Validador de Acceso
            CommandUsuarioValidation validator = new CommandUsuarioValidation(
                    config, request.getSession(false));

            if (validator.validarAccesoAdmin(request.getSession(false))) {
                // Capas de Datos
                DALAvatar dalAvatar = new DALAvatar(config);
                DALPerfil dalPerfil = new DALPerfil(config);
                DALUsuario dalUsuario = new DALUsuario(config);

                // Obtener Operación
                String op = request.getParameter("op");

                // Formulario Captura Datos
                if (op == null || op.equals("captura")) {
                    // Sesión > Usuario
                    Usuario usuario = (Usuario) request.getSession(false).getAttribute("usuario");

                    // BD > Lista de Perfiles
                    List<Perfil> perfiles;
                    if (usuario.getPerfil() >= Perfil.DEVEL) {
                        perfiles = dalPerfil.listar();
                    } else {
                        perfiles = null;
                    }

                    // Inyectar Datos > JSP
                    request.setAttribute("perfiles", perfiles);
                } else if (op.equals("proceso")) {
                    // Request > Parámetros
                    String user = UtilesUsuario.obtenerUser(request);
                    String pass = UtilesUsuario.obtenerPass(request);
                    Avatar avatar = UtilesUsuario.obtenerAvatar(request);
                    int perfil = UtilesUsuario.obtenerPerfil(request);

                    // Validar Avatar
                    if (avatar == null) {
                        // Avatar no seleccionado - Predeterminado
                        avatar = new Avatar();
                    }

                    // Insertar Avatar
                    if (dalAvatar.insertar(avatar)) {
                        // Insertar Avatar seleccionado - Recuperar de BD
                        avatar = dalAvatar.consultar(avatar.getImagen());
                    } else {
                        // Error Inserción de Avatar
                        throw new ServletException("Error al insertar el avatar");
                    }

                    // Parámetros > Entidad
                    Usuario usuario = new Usuario(0, user, pass, avatar.getId(), "", perfil, "");

                    // Entidad > Inserción BD - true | false
                    boolean checkOK = dalUsuario.insertar(usuario);

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
        } else {
            out = "message/sesion-invalida";
        }

        // Redirección
        forward(out);
    }
}

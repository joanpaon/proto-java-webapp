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
import javax.servlet.http.HttpSession;
import org.japo.java.dal.DALAvatar;
import org.japo.java.dal.DALUsuario;
import org.japo.java.entities.Avatar;
import org.japo.java.entities.Usuario;
import org.japo.java.libraries.UtilesUsuario;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class CommandUsuarioProfile extends Command {

    @Override
    @SuppressWarnings("ConvertToStringSwitch")
    public void process() throws ServletException, IOException {
        // Salida
        String out = "usuario/usuario-perfil";

        if (validarSesion(request)) {
            // Sesión
            HttpSession sesion = request.getSession(false);

            // Usuario Actual
            Usuario usuario = (Usuario) sesion.getAttribute("usuario");

            // Obtener Operación
            String op = request.getParameter("op");

            if (op == null || op.equals("captura")) {
                // Inyectar Datos > JSP
                request.setAttribute("usuario", usuario);
            } else if (op.equals("proceso")) {
                // Capas de Datos
                DALAvatar dalAvatar = new DALAvatar(config);
                DALUsuario dalUsuario = new DALUsuario(config);

                // Request > Parámetros
                String user = UtilesUsuario.obtenerUser(request);
                String pass = UtilesUsuario.obtenerPass(request);
                Avatar avatar = UtilesUsuario.obtenerAvatar(request);
                int perfil = UtilesUsuario.obtenerPerfil(request);

                // Validar Avatar
                if (avatar == null) {
                    // Avatar NO modificado
                    avatar = new Avatar(usuario.getAvatar(), "", "");
                } else if (usuario.getAvatar() == Avatar.DEF_ID) {
                    // Avatar Seleccionado - Avatar Previo Predeterminado
                    if (!dalAvatar.insertar(avatar)) {
                        // Error al insertar el avatar
                        throw new ServletException("Error al insertar el avatar");
                    }

                    // Recuperar Avatar de BD
                    avatar = dalAvatar.consultar(avatar.getImagen());
                } else {
                    // Avatar Seleccionado - Sustituir Avatar Previo
                    avatar.setId(usuario.getAvatar());

                    // Sustituir el Avatar anterior
                    if (!dalAvatar.modificar(avatar)) {
                        // Error al actualizar el avatar
                        throw new ServletException("Error al actualizar el avatar");
                    }
                }

                // Parámetros > Entidad
                usuario = new Usuario(usuario.getId(), user, pass, avatar.getId(), "", perfil, "");

                // Ejecutar Operación
                boolean checkOK = dalUsuario.modificar(usuario);

                // Validar Operación
                if (checkOK) {
                    // Nuevo Usuario > Sesion
                    sesion.setAttribute("usuario", usuario);
                    
                    // Pagina de Aviso
                    out = UtilesUsuario.obtenerComandoPrincipal(request);
                } else {
                    out = "message/operacion-cancelada";
                }
            } else {
                out = "message/operacion-desconocida";
            }
        } else {
            out = "message/sesion-invalida";
        }

        // Redirección
        forward(out);
    }
}

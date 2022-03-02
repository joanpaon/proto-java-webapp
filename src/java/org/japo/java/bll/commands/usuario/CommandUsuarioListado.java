/* 
 * Copyright 2021 José A. Pacheco Ondoño - japolabs@gmail.com.
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
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.japo.java.bll.commands.admin.CommandValidation;
import org.japo.java.dal.DALUsuario;
import org.japo.java.entities.Perfil;
import org.japo.java.entities.Usuario;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class CommandUsuarioListado extends Command {

    @Override
    public void process() throws ServletException, IOException {
        // Salida
        String out = "usuario/usuario-listado";

        // Sesión
        HttpSession sesion = request.getSession(false);

        // Validar Sesión
        if (!validarSesion(sesion)) {
            out = "message/sesion-invalida";
        } else {
            // Capas de Negocio
            CommandValidation bllAdmin = new CommandValidation(sesion);

            // Capas de Datos
            DALUsuario dalUsuario = new DALUsuario(sesion);

            if (bllAdmin.validarAccesoComando(getClass().getSimpleName())) {
                // Sesión > Usuario
                Usuario usuario = (Usuario) sesion.getAttribute("usuario");

                // BD > Lista de Usuarios
                List<Usuario> usuarios;

                // Determinar Perfil Usuario
                switch (usuario.getPerfil()) {
                    case Perfil.DEVEL:
                        // BD > Lista de Usuarios
                        usuarios = dalUsuario.obtenerUsuarios();
                        break;
                    case Perfil.ADMIN:
                        // BD > Lista de Usuarios
                        usuarios = dalUsuario.obtenerUsuarios();
                        break;
                    case Perfil.BASIC:
                    default:
                        // Usuario Actual (Únicamente) > Lista de Usuarios
                        usuarios = new ArrayList<>();
                        usuarios.add(usuario);
                }

                // Inyecta Datos > JSP
                request.setAttribute("usuarios", usuarios);
            } else {
                out = "message/acceso-denegado";
            }
        }

        // Redirección
        forward(out);
    }
}

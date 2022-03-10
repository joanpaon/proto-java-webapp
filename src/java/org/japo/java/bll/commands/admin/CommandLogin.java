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
package org.japo.java.bll.commands.admin;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import org.japo.java.bll.commands.Command;
import org.japo.java.dal.DALUsuario;
import org.japo.java.entities.Perfil;
import org.japo.java.entities.Usuario;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class CommandLogin extends Command {

    // Duración de Sesión - 1800 seg ( 30 min - default )
    private static final int DURACION_SESION = 1800;

    // Nombre de la Base de Datos
    private static final String BD = "proto";

    @Override
    public void process() throws ServletException, IOException {
        // Salida
        String out = "admin/login";

        // Obtener Operación
        String op = request.getParameter("op");

        // Request > Sesión
        HttpSession sesion = request.getSession();

        // Procesar Operación
        if (validarSesion(sesion)) {
            // Sesión > Usuario
            Usuario usuario = (Usuario) sesion.getAttribute("usuario");

            // Perfil > Comando
            switch (usuario.getPerfil()) {
                case Perfil.DEVEL:
                    out = "controller?cmd=main-devel";
                    break;
                case Perfil.ADMIN:
                    out = "controller?cmd=main-admin";
                    break;
                default:
                    out = "controller?cmd=main-basic";
            }
        } else if (op == null || op.equals("captura")) {
            // ---
        } else if (op.equals("proceso")) {
            // Request > Credenciales
            String user = request.getParameter("user");
            String pass = request.getParameter("pass");

            // Validación Formal de la Credencial 
            if (user == null || !Usuario.validarUser(user)) {
                out = "message/acceso-denegado";
            } else if (pass == null || !Usuario.validarPass(pass)) {
                out = "message/acceso-denegado";
            } else {
                // Request > Sesión
                sesion = request.getSession(true);

                // Nombre de Base de Datos > Sesión
                sesion.setAttribute("bd", BD);

                // Capas de Negocio
                DALUsuario dalUsuario = new DALUsuario(sesion);

                // Nombre Usuario + BD > Objeto Usuario
                Usuario usuario = dalUsuario.consultar(user);

                // Validar Objeto Usuario
                if (usuario == null) {
                    out = "message/acceso-denegado";
                } else if (!pass.equals(usuario.getPass())) {
                    out = "message/acceso-denegado";
                } else {
                    // Eliminar Sesión Actual
                    sesion.invalidate();

                    // Crear/Obtener Sesión
                    sesion = request.getSession(true);

                    // Establecer duracion sesion ( Segundos )
                    sesion.setMaxInactiveInterval(DURACION_SESION);

                    // Usuario > Sesión
                    sesion.setAttribute("usuario", usuario);

                    // Nombre de Base de Datos > Sesión
                    sesion.setAttribute("bd", BD);

                    // Perfil > Comando
                    if (usuario.getPerfil() >= Perfil.DEVEL) {
                        out = "controller?cmd=main-devel";
                    } else if (usuario.getPerfil() >= Perfil.ADMIN) {
                        out = "controller?cmd=main-admin";
                    } else {
                        out = "controller?cmd=main-basic";
                    }
                }
            }
        } else {
            out = "message/operacion-desconocida";
        }

        // Redirección
        forward(out);
    }
}

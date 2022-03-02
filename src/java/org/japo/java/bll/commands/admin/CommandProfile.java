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
package org.japo.java.bll.commands.admin;

import org.japo.java.bll.commands.Command;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.http.HttpSession;
import org.japo.java.dal.DALUsuario;
import org.japo.java.entities.Usuario;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class CommandProfile extends Command {

    @Override
    @SuppressWarnings("ConvertToStringSwitch")
    public void process() throws ServletException, IOException {
        // Salida
        String out = "admin/profile";

        // Sesión
        HttpSession sesion = request.getSession(false);

        if (validarSesion(sesion)) {
            out = "message/sesion-invalida";
        } else {
            // Usuario Actual
            Usuario usuario = (Usuario) sesion.getAttribute("usuario");

            // Capas de Datos
            DALUsuario usuarioDAL = new DALUsuario(sesion);

            // Obtener Operación
            String op = request.getParameter("op");

            if (op == null || op.equals("captura")) {
                // Inyectar Datos > JSP
                request.setAttribute("usuario", usuario);
            } else if (op.equals("proceso")) {
                // Request > Parámetros
                String user = request.getParameter("user").trim();
                String pass = request.getParameter("pass").trim();
                int avatar = Integer.parseInt(request.getParameter("avatar"));
                int perfil = Integer.parseInt(request.getParameter("perfil"));

                // Parámetros > Entidad
                usuario = new Usuario(usuario.getId(), user, pass, avatar, perfil);

                // Ejecutar Operación
                boolean checkOK = usuarioDAL.modificar(usuario);

                // Validar Operación
                if (checkOK) {
                    // Pagina de Aviso
                    out = "message/operacion-completada";

                    // Nuevo Usuario > Sesion
                    sesion.setAttribute("usuario", usuario);
                } else {
                    out = "message/operacion-cancelada";
                }
            } else {
                out = "message/operacion-desconocida";
            }
        }

        // Redirección
        forward(out);
    }
}

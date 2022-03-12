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
package org.japo.java.bll.commands.permiso.usuario;

import org.japo.java.bll.commands.Command;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.http.HttpSession;
import org.japo.java.bll.commands.usuario.CommandUsuarioValidation;
import org.japo.java.dal.DALPermisoUsuario;
import org.japo.java.entities.PermisoUsuario;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class CommandPermisoUsuarioConsulta extends Command {

    @Override
    public void process() throws ServletException, IOException {
        // Salida
        String out = "permiso/usuario/permiso-usuario-consulta";

        // Sesión
        HttpSession sesion = request.getSession(false);

        // Validar Sesión
        if (!validarSesion(sesion)) {
            out = "message/sesion-invalida";
        } else {
            // Capas de Negocio
            CommandUsuarioValidation validator = new CommandUsuarioValidation(sesion);

            if (validator.validarAccesoComando(getClass().getSimpleName())) {
                // Capas de Datos
                DALPermisoUsuario dalPermiso = new DALPermisoUsuario(sesion);

                // Request > ID Permiso Usuario
                int id = Integer.parseInt(request.getParameter("id"));

                // ID Permiso Usuario + BD > Permiso Usuario
                PermisoUsuario permiso = dalPermiso.consultar(id);

                // Enlaza Datos > JSP
                request.setAttribute("permiso", permiso);
            } else {
                out = "message/acceso-denegado";
            }
        }

        // Redirección
        forward(out);
    }
}

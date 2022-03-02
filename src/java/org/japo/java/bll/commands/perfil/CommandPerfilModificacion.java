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
package org.japo.java.bll.commands.perfil;

import org.japo.java.bll.commands.Command;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.http.HttpSession;
import org.japo.java.bll.commands.admin.CommandValidation;
import org.japo.java.dal.DALPerfil;
import org.japo.java.entities.Perfil;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class CommandPerfilModificacion extends Command {

    // Redirección Página JSP Proceso
    private static final String PAGINA_PROCESO = "perfiles/perfil-modificacion";

    @Override
    @SuppressWarnings("ConvertToStringSwitch")
    public void process() throws ServletException, IOException {
        // JSP
        String page;

        // Entidad
        Perfil perfil;

        // Sesión
        HttpSession sesion = request.getSession(false);

        // Validar Sesión
        if (validarSesion(sesion)) {
            page = "messages/sesion-invalida";
        } else {
            // Capas de Negocio
            CommandValidation validator = new CommandValidation(sesion);

            // Capas de Datos
            DALPerfil perfilDAL = new DALPerfil(sesion);

            if (validator.validarAccesoComando(getClass().getSimpleName())) {
                // request > ID Entidad
                int id = Integer.parseInt(request.getParameter("id"));

                // request > Operación
                String op = request.getParameter("op");

                // Entidad > JSP
                if (op == null || op.equals("captura")) {
                    // BD > Entidades
                    perfil = perfilDAL.consultar(id);

                    // Inyectar Datos > JSP
                    request.setAttribute("perfil", perfil);

                    // JSP
                    page = PAGINA_PROCESO;
                } else if (op.equals("proceso")) {
                    // ID Entidad > Registro BD > Entidad
                    perfil = perfilDAL.consultar(id);

                    // Request > Parámetros
                    String nombre = request.getParameter("nombre").trim();
                    String info = request.getParameter("info").trim();

                    // Parámetros > Entidad
                    perfil = new Perfil(perfil.getId(), nombre, info);

                    // Ejecutar Operación
                    boolean checkOK = perfilDAL.modificar(perfil);

                    // Validar Operación
                    if (checkOK) {
                        page = "messages/operacion-completada";
                    } else {
                        page = "messages/operacion-cancelada";
                    }
                } else {
                    page = "messages/operacion-desconocida";
                }
            } else {
                page = "messages/acceso-denegado";
            }
        }

        // Redirección JSP
        forward(page);
    }
}

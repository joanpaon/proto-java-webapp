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
package org.japo.java.bll.commands.proceso;

import org.japo.java.bll.commands.Command;
import javax.servlet.ServletException;
import java.io.IOException;
import org.japo.java.bll.commands.usuario.CommandUsuarioValidation;
import org.japo.java.dal.DALProceso;
import org.japo.java.entities.Proceso;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class CommandProcesoModificacion extends Command {

    @Override
    @SuppressWarnings("ConvertToStringSwitch")
    public void process() throws ServletException, IOException {
        // Salida
        String out = "proceso/proceso-modificacion";

        // Validar Sesión
        if (validarSesion(request)) {
            // Validador de Acceso
            CommandUsuarioValidation validator = new CommandUsuarioValidation(
                    config, request.getSession(false));

            if (validator.validarAccesoDev(request.getSession(false))) {
                // Capas de Datos
                DALProceso dalProceso = new DALProceso(config);

                // request > ID Entidad
                int id = Integer.parseInt(request.getParameter("id"));

                // ID Entidad > Registro BD > Entidad
                Proceso proceso = dalProceso.consultar(id);

                // request > Operación
                String op = request.getParameter("op");

                // Entidad > JSP
                if (op == null || op.equals("captura")) {
                    // Inyección de Datos
                    request.setAttribute("proceso", proceso);
                } else if (op.equals("proceso")) {
                    // Request > Parámetros
                    String nombre = request.getParameter("nombre").trim();
                    String info = request.getParameter("info").trim();

                    // Parámetros > Entidad
                    proceso = new Proceso(proceso.getId(), nombre, info);

                    // Ejecutar Operación
                    boolean checkOK = dalProceso.modificar(proceso);

                    // Validar Operación
                    if (checkOK) {
                        out = "controller?cmd=proceso-listado";
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

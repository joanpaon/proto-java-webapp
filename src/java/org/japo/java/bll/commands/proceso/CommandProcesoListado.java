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
package org.japo.java.bll.commands.proceso;

import org.japo.java.bll.commands.Command;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.japo.java.bll.commands.admin.CommandValidation;
import org.japo.java.dal.DALProceso;
import org.japo.java.entities.Proceso;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class CommandProcesoListado extends Command {

    @Override
    public void process() throws ServletException, IOException {
        // Salida
        String out = "proceso/proceso-listado";

        // Sesión
        HttpSession sesion = request.getSession(false);

        // Validar Sesión
        if (!validarSesion(sesion)) {
            out = "message/sesion-invalida";
        } else {
            // Capas de Negocio
            CommandValidation validator = new CommandValidation(sesion);

            // Capas de Datos
            DALProceso dalProceso = new DALProceso(sesion);

            if (validator.validarAccesoComando(getClass().getSimpleName())) {
                // BD > Lista de Procesos
                List<Proceso> procesos = dalProceso.listar();

                // Inyecta Datos Listado > JSP
                request.setAttribute("procesos", procesos);
            } else {
                out = "message/acceso-denegado";
            }
        }

        // Redirección
        forward(out);
    }
}

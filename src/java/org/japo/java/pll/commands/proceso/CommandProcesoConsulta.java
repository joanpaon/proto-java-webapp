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
package org.japo.java.pll.commands.proceso;

import org.japo.java.pll.commands.Command;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.http.HttpSession;
import org.japo.java.bll.BLLAdmin;
import org.japo.java.dal.DALProceso;
import org.japo.java.entities.Proceso;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class CommandProcesoConsulta extends Command {

    @Override
    public void process() throws ServletException, IOException {
        // Salida
        String out = "proceso/proceso-consulta";

        // Sesión
        HttpSession sesion = request.getSession(false);

        // Validar Sesión
        if (!validarSesion(sesion)) {
            out = "message/sesion-invalida";
        } else {
            // Capas de Negocio
            BLLAdmin bllAdmin = new BLLAdmin(sesion);

            // Capas de Datos
            DALProceso dalProceso = new DALProceso(sesion);

            if (bllAdmin.validarAccesoComando(getClass().getSimpleName())) {
                // Request > ID Entidad
                int id = Integer.parseInt(request.getParameter("id"));

                // ID Entidad > Entidad
                Proceso proceso = dalProceso.obtenerProceso(id);

                // Inyecta Datos > JSP
                request.setAttribute("proceso", proceso);
            } else {
                out = "message/acceso-denegado";
            }
        }

        // Redirección
        forward(out);
    }
}

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
package org.japo.java.bll.commands.perfil;

import org.japo.java.bll.commands.Command;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.http.HttpSession;
import org.japo.java.bll.commands.usuario.CommandUsuarioValidation;
import org.japo.java.dal.DALPerfil;
import org.japo.java.entities.Perfil;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class CommandPerfilModificacion extends Command {

    @Override
    @SuppressWarnings("ConvertToStringSwitch")
    public void process() throws ServletException, IOException {
        // Salida
        String out = "perfil/perfil-modificacion";

        // Sesión
        HttpSession sesion = request.getSession(false);

        // Validar Sesión
        if (validarSesion(sesion)) {
            // Capas de Negocio
            CommandUsuarioValidation validator = new CommandUsuarioValidation(config, sesion);

            if (validator.validarAccesoComando(getClass().getSimpleName())) {
                // Capas de Datos
                DALPerfil perfilDAL = new DALPerfil(config);

                // request > ID Entidad
                int id = Integer.parseInt(request.getParameter("id"));

                // BD > Perfil
                Perfil perfil = perfilDAL.consultar(id);

                // request > Operación
                String op = request.getParameter("op");

                // Entidad > JSP
                if (op == null || op.equals("captura")) {
                    // Inyectar Datos > JSP
                    request.setAttribute("perfil", perfil);
                } else if (op.equals("proceso")) {
                    // Request > Parámetros
                    String nombre = request.getParameter("nombre");
                    String info = request.getParameter("info");

                    // Parámetros > Entidad
                    perfil = new Perfil(perfil.getId(), nombre, info);

                    // Ejecutar Operación
                    boolean checkOK = perfilDAL.modificar(perfil);

                    // Validar Operación
                    if (checkOK) {
                        out = "controller?cmd=perfil-listado";
                    } else {
                        out = "messages/operacion-cancelada";
                    }
                } else {
                    out = "messages/operacion-desconocida";
                }
            } else {
                out = "messages/acceso-denegado";
            }
        } else {
            out = "messages/sesion-invalida";
        }

        // Redirección JSP
        forward(out);
    }
}

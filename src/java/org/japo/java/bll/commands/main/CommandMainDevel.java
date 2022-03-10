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
package org.japo.java.bll.commands.main;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import org.japo.java.bll.commands.Command;
import org.japo.java.dal.DALAvatar;
import org.japo.java.entities.Avatar;
import org.japo.java.entities.Usuario;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class CommandMainDevel extends Command {

    @Override
    public void process() throws ServletException, IOException {
        // Salida
        String out;

        // Sesión
        HttpSession sesion = request.getSession(false);

        // Validar Sesión
        if (validarSesion(sesion)) {
            out = "main/main-devel";
        } else {
            out = "message/sesion-invalida";
        }

        // Redirección JSP
        forward(out);
    }
}

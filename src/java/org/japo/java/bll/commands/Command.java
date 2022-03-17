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
package org.japo.java.bll.commands;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public abstract class Command implements ICommand {

    private static final String VIEWS_PATH = "/WEB-INF/views";

    // Referencias
    protected ServletConfig config;
    protected HttpServletRequest request;
    protected HttpServletResponse response;

    // Inicialización del Comando
    @Override
    public final void init(
            ServletConfig config,
            HttpServletRequest request,
            HttpServletResponse response) {
        this.config = config;
        this.request = request;
        this.response = response;
    }

    // Redirección de la Salida
    protected final void forward(String out) throws ServletException, IOException {
        // Validar Tipo de Salida
        if (out.startsWith("controller")) {
            // Elimina el prefijo
            out = out.replace("controller", "");

            // Redirección
            response.sendRedirect(out);
        } else {
            // Nombre Comando ( Petición ) > Nombre Vista ( Respuesta )
            out = String.format("%s/%s.jsp", VIEWS_PATH, out);

            // Contexto + Nombre Vista > Despachador
            RequestDispatcher dispatcher = request.getRequestDispatcher(out);

            // Despachador + Petición + Respuesta > Redirección a Vista
            dispatcher.forward(request, response);
        }
    }

    protected final boolean validarSesion(HttpSession sesion) {
        // Semáforo
        boolean checkOK = false;

        // Validación
        if (sesion != null) {
            // Sesion > Usuario
            Object usuario = sesion.getAttribute("usuario");

            // Valida Usuario
            if (usuario == null) {
                sesion.invalidate();
            } else {
                checkOK = true;
            }
        }

        // Retorno: true | false
        return checkOK;
    }
}

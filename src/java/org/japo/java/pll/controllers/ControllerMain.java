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
package org.japo.java.pll.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.japo.java.pll.commands.ICommand;
import org.japo.java.pll.commands.admin.CommandUnknown;
import org.japo.java.pll.services.IService;
import org.japo.java.pll.services.admin.ServiceUnknown;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
@WebServlet(name = "ControllerMain", urlPatterns = {"", "/public/*"})
public final class ControllerMain extends HttpServlet {

    protected void processRequest(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Análisis de la Petición
        if (request.getPathInfo().equals("/")) {
            if (request.getParameter("svc") != null) {
                // Petición > Nombre de Servicio
                String svcName = request.getParameter("svc");

                // Nombre de Servicio > Servicio ( Interfaz )
                IService svc = obtenerServicio(svcName);

                // ServletContext + Petición + Resuesta > Inicializar Servicio
                svc.init(getServletContext(), request, response);

                // Procesa Servicio
                svc.process();
            } else if (request.getParameter("cmd") != null) {
                // Petición > Nombre de Comando (Kebab Case)
                String cmdName = request.getParameter("cmd");

                // Nombre de Comando > Comando ( Interfaz )
                ICommand cmd = obtenerComando(cmdName);

                // ServletContext + Peticion + Resuesta > Inicializar Comando
                cmd.init(getServletContext(), request, response);

                // Procesa Comando
                cmd.process();
            } else {
                // Comando de Ejecución por Defecto
                response.sendRedirect("?cmd=landing");
            }
        } else {
            // Request > Recurso
            File f = obtenerRecurso(request);

            // Recurso > Salida
            servirRecurso(f, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private IService obtenerServicio(String svc) {
        // Referencia Comando
        IService instancia;

        try {
            // Parámetro cmd > Nombre Cualificado de Clase Comando
            String nombreClase = obtenerNombreServicio(svc);

            // Nombre Clase > Clase Clase
            Class<?> clase = Class.forName(nombreClase);

            // Clase Clase > Constructor Clase
            Constructor<?> constructor = clase.getConstructor();

            // Constructor Clase > Instancia Clase
            instancia = (IService) constructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException
                | SecurityException | InstantiationException
                | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException ex) {
            // Clase Indefinida | Desconocida
            instancia = new ServiceUnknown();
        }

        // Retorno Comando
        return instancia;
    }

    private String obtenerNombreServicio(String cmd) {
        // Paquete
        final String PKG = "org.japo.java.pll.services";

        // Prefijo
        final String PRE = "Service";

        // Subpaquete
        String sub;

        // Parámetro Comando > Subpaquete
        if (cmd == null) {
            sub = "admin";
            cmd = "unknown";
        } else if (false
                || cmd.equals("landing")
                || cmd.equals("login")
                || cmd.equals("logout")) {
            sub = "admin";
        } else if (cmd.contains("-")) {
            // Eliminar Operacion Final
            sub = cmd.substring(0, cmd.lastIndexOf("-"));

            // Notacion paquete: - > .
            sub = sub.replace("-", ".");
        } else {
            sub = "cmd";
        }

        // Kebab Case > Camel Case
        cmd = cambiarKebab2Camel(cmd);

        // Retorno: Nombre Cualificado Clase Comando
        return String.format("%s.%s.%s%s", PKG, sub, PRE, cmd);
    }

    private ICommand obtenerComando(String cmd) {
        // Referencia Comando
        ICommand instancia;

        try {
            // Parámetro cmd > Nombre Cualificado de Clase Comando
            String nombreClase = obtenerNombreComando(cmd);

            // Nombre Clase > Clase Clase
            Class<?> clase = Class.forName(nombreClase);

            // Clase Clase > Constructor Clase
            Constructor<?> constructor = clase.getConstructor();

            // Constructor Clase > Instancia Clase
            instancia = (ICommand) constructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException
                | SecurityException | InstantiationException
                | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException ex) {
            // Clase Indefinida | Desconocida
            instancia = new CommandUnknown();
        }

        // Retorno Comando
        return instancia;
    }

    private String obtenerNombreComando(String cmd) {
        // Paquete
        final String PKG = "org.japo.java.pll.commands";

        // Prefijo
        final String PRE = "Command";

        // Subpaquete
        String sub;

        // Parámetro Comando > Subpaquete
        if (cmd == null) {
            sub = "admin";
            cmd = "unknown";
        } else if (false
                || cmd.equals("landing")
                || cmd.equals("login")
                || cmd.equals("logout")) {
            sub = "admin";
        } else if (cmd.contains("-")) {
            // Eliminar Operacion Final
            sub = cmd.substring(0, cmd.lastIndexOf("-"));

            // Notacion paquete: - > .
            sub = sub.replace("-", ".");
        } else {
            sub = "cmd";
        }

        // Kebab Case > Camel Case
        cmd = cambiarKebab2Camel(cmd);

        // Retorno: Nombre Cualificado Clase Comando
        return String.format("%s.%s.%s%s", PKG, sub, PRE, cmd);
    }

    private String cambiarKebab2Camel(String cmd) {
        // String > String[]
        String[] items = cmd.split("-");

        // Constructor de String
        StringBuilder sb = new StringBuilder();

        // Bucle
        for (String item : items) {
            sb.append(capitalizar(item));
        }

        // Retorno
        return sb.toString();
    }

    private String capitalizar(String item) {
        if (item != null) {
            // Item > Inicial (Mays)
            char head = Character.toUpperCase(item.charAt(0));

            // Item > Resto (Mins)
            String tail = item.substring(1).toLowerCase();

            // head + tail > item
            item = head + tail;
        }

        return item;
    }

    private File obtenerRecurso(HttpServletRequest request) {
        // Request > Ruta Absoluta ( Compilada )
        String ruta = request.getPathTranslated().replace("\\", "/");

        // Request > Ruta Peticion ( Public )
        String rutaPeticion = request.getPathInfo();

        // Ruta Peticion > Ruta Servicio
        String rutaServicio = "/WEB-INF/static" + rutaPeticion;

        // Ruta Absoluta ( Peticion ) > Ruta Absoluta ( Servicio ) 
        ruta = ruta.replace(rutaPeticion, rutaServicio);

        // Retorno: Ruta Absoluta Servicio > Fichero
        return new File(ruta);
    }

    private void servirRecurso(File recurso, HttpServletResponse response)
            throws FileNotFoundException, IOException {
        // Tamaño Máximo Recurso
        final long MAX_SIZE = -1;

        // Analizar Recurso
        if (recurso == null) {
            // No hay recurso - No hacer nada
        } else if (MAX_SIZE > -1 && recurso.length() <= MAX_SIZE) {
            // Demasiado Grande - No hacer nada
        } else {
            // Buffer Temporal
            byte[] buffer = new byte[(int) recurso.length()];

            // Origen > Destino
            try (
                    FileInputStream origen = new FileInputStream(recurso);
                    ServletOutputStream destino = response.getOutputStream()) {
                // Origen > Buffer
                origen.read(buffer);

                // Buffer > Destino
                destino.write(buffer);
            }
        }
    }
}

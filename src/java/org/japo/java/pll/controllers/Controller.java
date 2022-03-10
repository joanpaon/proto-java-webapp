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
package org.japo.java.pll.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.japo.java.bll.commands.ICommand;
import org.japo.java.bll.services.IService;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
@WebServlet(name = "Controller", urlPatterns = {"", "/public/*"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)
public final class Controller extends HttpServlet {

    // Tamaño Máximo Fichero Recurso ( Defecto: Ilimitado )
    private static final long MAX_SIZE = -1;

    // Nombres de Paquetes
    private static final String COMMAND_PKG = "org.japo.java.bll.commands";
    private static final String SERVICE_PKG = "org.japo.java.bll.services";

    // Prefijos
    private static final String COMMAND_PRE = "Command";
    private static final String SERVICE_PRE = "Service";

    protected void processRequest(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Análisis de la Petición
        if (request.getPathInfo().equals("/")) {
            if (request.getParameter("svc") != null) {
                procesarServicio(request, response);
            } else if (request.getParameter("cmd") != null) {
                procesarComando(request, response);
            } else {
                // Redirección Comando por Defecto ( Bienvenida )
                response.sendRedirect("?cmd=landing");
            }
        } else {
            procesarRecurso(request, response);
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

    private IService obtenerServicio(String svcName) throws ServletException {
        // Referencia Comando
        IService svc;

        try {
            // Parámetro cmd > Nombre Cualificado de Clase Comando
            String svcClassName = obtenerNombreServicio(svcName);

            // Nombre Clase > Objeto Class
            Class<?> svcClass = Class.forName(svcClassName);

            // Objeto Class > Constructor Clase
            Constructor<?> svcConstructor = svcClass.getConstructor();

            // Constructor Clase > Instancia Clase
            svc = (IService) svcConstructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException
                | SecurityException | InstantiationException
                | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            // Clase Indefinida | Desconocida
            throw new ServletException(e.getMessage());
        }

        // Retorno Comando
        return svc;
    }

    private String obtenerNombreServicio(String svcName) throws ServletException {
        // Subpaquete
        String sub;

        // Parámetro > Subpaquete
        if (svcName == null) {
            throw new ServletException("Servicio no especificado");
        } else if (false
                || svcName.equals("sample")
                || svcName.equals("token")
                || svcName.equals("check")
                || svcName.equals("clue")) {
            sub = "helper";
        } else if (svcName.contains("-")) {
            // Eliminar Operacion Final
            sub = svcName.substring(0, svcName.lastIndexOf("-"));

            // Notación Kebab-Case > Notación Package: - > . 
            sub = sub.replace("-", ".");
        } else {
            sub = svcName;
        }

        // Kebab Case > Camel Case
        svcName = cambiarKebab2Camel(svcName);

        // Retorno: Nombre Cualificado Clase Servicio
        return String.format("%s.%s.%s%s", SERVICE_PKG, sub, SERVICE_PRE, svcName);
    }

    private ICommand obtenerComando(String cmdName) throws ServletException {
        // Referencia Comando
        ICommand cmd;

        try {
            // Parámetro cmd > Nombre Cualificado de Clase Comando
            String cmdClassName = obtenerNombreComando(cmdName);

            // Nombre Clase > Objeto Class
            Class<?> cmdClass = Class.forName(cmdClassName);

            // Objeto Class > Constructor Clase
            Constructor<?> cmdConstructor = cmdClass.getConstructor();

            // Constructor Clase > Instancia Clase
            cmd = (ICommand) cmdConstructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException
                | SecurityException | InstantiationException
                | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            // Clase Indefinida | Desconocida
            throw new ServletException(e.getMessage());
        }

        // Retorno Comando
        return cmd;
    }

    private String obtenerNombreComando(String cmd) throws ServletException {
        // Subpaquete
        String sub;

        // Parámetro > Subpaquete
        if (cmd == null) {
            throw new ServletException("Comando no especificado");
        } else if (false
                || cmd.equals("landing")
                || cmd.equals("login")
                || cmd.equals("profile")
                || cmd.equals("validation")
                || cmd.equals("signup")
                || cmd.equals("message")
                || cmd.equals("logout")) {
            sub = "admin";
        } else if (cmd.contains("-")) {
            // Eliminar Operacion Final
            sub = cmd.substring(0, cmd.lastIndexOf("-"));

            // Notación Kebab-Case > Notación Package: - > . 
            sub = sub.replace("-", ".");
        } else {
            sub = cmd;
        }

        // Kebab Case > Camel Case
        cmd = cambiarKebab2Camel(cmd);

        // Retorno: Nombre Cualificado Clase Comando
        return String.format("%s.%s.%s%s", COMMAND_PKG, sub, COMMAND_PRE, cmd);
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

    private void servirRecurso(
            File recurso, HttpServletResponse response)
            throws FileNotFoundException, IOException {
        // Analizar Recurso
        if (recurso == null) {
            // No hay recurso - No hacer nada
        } else if (MAX_SIZE <= -1) {
            // No Limitación Tamaño - Servir Recurso
            servirFichero(recurso, response);
        } else if (recurso.length() <= MAX_SIZE) {
            // Si Limitación Tamaño - Tamaño Aceptable - Servir Recurso
            servirFichero(recurso, response);
        } else {
            // Si Limitación Tamaño - Tamaño Excesivo - No Hacer Nada
        }
    }

    private void procesarServicio(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        // Petición > Nombre de Servicio (Kebab Case)
        String svcName = request.getParameter("svc");

        // Nombre de Servicio > Servicio ( Referenciado por Interfaz )
        IService svc = obtenerServicio(svcName);

        // ServletContext + Petición + Resuesta > Inicializar Servicio
        svc.init(getServletContext(), request, response);

        // Procesa Servicio
        svc.process();
    }

    private void procesarComando(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        // Petición > Nombre de Comando (Kebab Case)
        String cmdName = request.getParameter("cmd");

        // Nombre de Comando > Comando ( Referenciado por Interfaz )
        ICommand cmd = obtenerComando(cmdName);

        // ServletContext + Peticion + Resuesta > Inicializar Comando
        cmd.init(getServletContext(), request, response);

        // Procesa Comando
        cmd.process();
    }

    private void procesarRecurso(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {
        // Request > Recurso Público ( CSS | JS | PNG | ... )
        File f = obtenerRecurso(request);

        // Recurso Público > Salida
        servirRecurso(f, response);
    }

    private void servirFichero(
            File recurso,
            HttpServletResponse response)
            throws IOException {
        // Buffer Temporal
        byte[] buffer = new byte[(int) recurso.length()];

        // Origen > Destino
        try (
                 FileInputStream origen = new FileInputStream(recurso);  ServletOutputStream destino = response.getOutputStream()) {
            // Origen > Buffer
            origen.read(buffer);

            // Buffer > Destino
            destino.write(buffer);
        }
    }
}

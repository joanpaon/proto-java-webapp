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
package org.japo.java.libraries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.japo.java.dal.DALUsuario;
import org.japo.java.entities.Avatar;
import org.japo.java.entities.Perfil;
import org.japo.java.entities.Usuario;

/**
 *
 * @author JAPO Labs - japolabs@gmail.com
 */
public final class UtilesUsuario {

    private UtilesUsuario() {
    }

    public static final int obtenerId(HttpServletRequest request) throws IOException {
        // Referencia
        int id;

        // URL > ID Objeto
        try {
            id = Integer.parseInt(request.getParameter("id"));

            if (!Usuario.validarId(id)) {
                throw new IOException("Usuario NO Registrado");
            }
        } catch (NullPointerException e) {
            throw new IOException(e.getMessage());
        } catch (NumberFormatException e) {
            throw new IOException("ID de Usuario Incorrecta");
        }

        // Retorno
        return id;
    }

    public static final String obtenerUser(HttpServletRequest request) throws IOException {
        // Request > User
        String user = request.getParameter("user");

        // Validar User
        if (!Usuario.validarUser(user)) {
            throw new IOException("Nombre de Usuario Incorrecto");
        }

        // Retorno: Nombre de Usuario
        return user;
    }

    public static final String obtenerPass(HttpServletRequest request) throws IOException {
        // Request > Pass
        String pass = request.getParameter("pass");

        // Validar Contraseña
        if (!Usuario.validarPass(pass)) {
            throw new IOException("Contraseña Incorrecta");
        }

        // Retorno: Contraseña
        return pass;
    }

    public static final Avatar obtenerAvatar(HttpServletRequest request) throws IOException, ServletException {
        // Datos > Avatar
        Avatar avatar = null;

        // Request > Part
        Part part = request.getPart("avatar");

        // Imagen Enviada
        if (part.getSize() > 0) {
            // Part > Nombre Imagen
            String nombre = obtenerNombreAvatar(part);

            // Imagen Base64
            String imagen;

            // Validar Tamaño Avatar
            if (Avatar.MAX_SIZE <= 0) {
                // No hay tamaño máximo
                imagen = UtilesBase64.obtenerImagenBase64(part);
            } else if (part.getSize() <= Avatar.MAX_SIZE) {
                // Tamaño Correcto
                imagen = UtilesBase64.obtenerImagenBase64(part);
            } else {
                // Tamaño Excesivo
                throw new IOException("Tamaño de imagen excesivo");
            }

            // Datos > Avatar
            avatar = new Avatar(0, nombre, imagen);
        }

        // Retorno: Avatar
        return avatar;
    }

    private static String obtenerNombreAvatar(Part part) {
        // Part > Nombre Fichero Enviado
        String nombre = part.getSubmittedFileName();

        // Elimina Extensión
        nombre = nombre.substring(0, nombre.lastIndexOf("."));

        // Valída Nombre
        if (nombre.length() > Avatar.MAX_CHARS) {
            nombre = nombre.substring(nombre.length() - Avatar.MAX_CHARS - 1);
        } else if (nombre.isEmpty()) {
            nombre = "avatar" + String.format("%6d", new Random().nextInt(100000, 1000000));
        }

        // Retorno: Imagen Base64
        return nombre;
    }

    public static final int obtenerPerfil(HttpServletRequest request) throws IOException {
        // Request > ID Perfil
        int perfil = Integer.parseInt(request.getParameter("perfil"));

        // Validar ID Perfil
        if (!Usuario.validarPerfil(perfil)) {
            throw new IOException("Perfil Incorrecto");
        }

        // Retorno: ID Perfil
        return perfil;
    }

    public static final List<Usuario> obtenerUsuariosPerfil(
            ServletConfig config,
            HttpServletRequest request) {
        // Referencia
        List<Usuario> usuarios;

        // Request > Sesión
        HttpSession sesion = request.getSession(false);

        // Sesión > Usuario               
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");

        // Capas de Datos
        DALUsuario dalUsuario = new DALUsuario(config);

        // Determinar Perfil Usuario
        switch (usuario.getPerfil()) {
            case Perfil.DEVEL:
                // BD > Lista de Usuarios
                usuarios = dalUsuario.listar();
                break;
            case Perfil.ADMIN:
                // BD > Lista de Usuarios
                usuarios = dalUsuario.listar();
                break;
            case Perfil.BASIC:
            default:
                // Usuario Actual (Únicamente) > Lista de Usuarios
                usuarios = new ArrayList<>();
                usuarios.add(usuario);
        }

        // Retorno: Lista de usuarios visibles por el perfil
        return usuarios;
    }

    public static final Usuario obtenerUsuarioId(
            ServletConfig config, 
            HttpServletRequest request) 
            throws IOException {
        // Referencia
        Usuario usuario = null;

        if (validarCredencial(request)) {
            // Capas de Negocio
            DALUsuario dalUsuario = new DALUsuario(config);

            // Request > Id de Usuario
            int id = obtenerId(request);

            // Nombre Usuario + BD > Objeto Usuario
            usuario = dalUsuario.consultar(id);
        }

        // Retorno
        return usuario;
    }

    public static final Usuario obtenerUsuarioUser(
            ServletConfig config, 
            HttpServletRequest request) {
        // Referencia
        Usuario usuario = null;

        if (validarCredencial(request)) {
            // Capas de Negocio
            DALUsuario dalUsuario = new DALUsuario(config);

            // Request > Nombre de Usuario
            String user = request.getParameter("user");

            // Nombre Usuario + BD > Objeto Usuario
            usuario = dalUsuario.consultar(user);
        }

        // Retorno
        return usuario;
    }

    private static boolean validarCredencial(
            HttpServletRequest request) {
        // Request > Credenciales
        String user = request.getParameter("user");
        String pass = request.getParameter("pass");

        // Validación Formal de la Credencial 
        return true
                && user != null && Usuario.validarUser(user)
                && pass != null && Usuario.validarPass(pass);
    }

    public static final HttpSession regenerarSesion(
            ServletConfig config, 
            HttpServletRequest request) {
        // Request > Session
        HttpSession sesion = request.getSession(false);

        // Eliminar Sesión
        if (sesion != null) {
            // Eliminar Sesión Actual
            sesion.invalidate();
        }

        // Crear Sesión
        sesion = request.getSession(true);

        // Tiempo Maximo Sesion Inactiva ( Segundos )
        int lapso = UtilesServlet.obtenerLapsoInactividad(config);

        // Establecer duracion sesion ( Segundos )
        sesion.setMaxInactiveInterval(lapso);

        // Retorno: Sesion
        return sesion;
    }

    public static final String obtenerComandoPrincipal(HttpServletRequest request) {
        // Request > Session
        HttpSession sesion = request.getSession(false);

        // Sesión > Usuario
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");

        // Perfil > Comando
        return switch (usuario.getPerfil()) {
            case Perfil.DEVEL ->
                "controller?cmd=main-devel";
            case Perfil.ADMIN ->
                "controller?cmd=main-admin";
            default ->
                "controller?cmd=main-basic";
        };
    }
}

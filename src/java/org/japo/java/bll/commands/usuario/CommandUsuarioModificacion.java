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
package org.japo.java.bll.commands.usuario;

import org.japo.java.bll.commands.Command;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import org.japo.java.dal.DALPerfil;
import org.japo.java.dal.DALUsuario;
import org.japo.java.entities.Perfil;
import org.japo.java.entities.Usuario;
import org.japo.java.libraries.UtilesUsuario;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class CommandUsuarioModificacion extends Command {

    @Override
    @SuppressWarnings("ConvertToStringSwitch")
    public void process() throws ServletException, IOException {
        // Salida
        String out = "usuario/usuario-modificacion";

        // Validar Sesión
        if (validarSesion(request)) {
            // Validador de Acceso
            CommandUsuarioValidation validator = new CommandUsuarioValidation(
                    config, request.getSession(false));

            if (validator.validarAccesoAdmin(request.getSession(false))) {
                // Capas de Datos
                DALPerfil dalPerfil = new DALPerfil(config);
                DALUsuario dalUsuario = new DALUsuario(config);

                // request > ID Operación
                String op = request.getParameter("op");

                // ID Usuario + BD > Usuario
                Usuario usuario = UtilesUsuario.obtenerUsuarioIdRequest(config, request);

                // Captura de Datos
                if (op == null || op.equals("captura")) {
                    // BD > Lista de Perfiles
                    List<Perfil> perfiles = dalPerfil.listar();

                    // Inyectar Datos > JSP
                    request.setAttribute("usuario", usuario);
                    request.setAttribute("perfiles", perfiles);
                } else if (op.equals("proceso")) {
                    // Request > Parámetros
                    String user = UtilesUsuario.obtenerUser(request);
                    String pass = UtilesUsuario.obtenerPass(request);
                    String avatar = UtilesUsuario.obtenerAvatar(request);
                    int perfil = UtilesUsuario.obtenerPerfilRequest(request);

                    // Parámetros > Usuario a Modificar
                    usuario = new Usuario(usuario.getId(), user, pass, avatar, perfil, "");

                    // Validar Operación
                    if (dalUsuario.modificar(usuario)) {
                        out = "controller?cmd=usuario-listado";
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

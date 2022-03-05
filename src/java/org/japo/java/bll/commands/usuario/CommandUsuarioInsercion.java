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
package org.japo.java.bll.commands.usuario;

import org.japo.java.bll.commands.Command;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.japo.java.bll.commands.admin.CommandValidation;
import org.japo.java.dal.DALAvatar;
import org.japo.java.dal.DALPerfil;
import org.japo.java.dal.DALUsuario;
import org.japo.java.entities.Avatar;
import org.japo.java.entities.Perfil;
import org.japo.java.entities.Usuario;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class CommandUsuarioInsercion extends Command {

    @Override
    @SuppressWarnings("ConvertToStringSwitch")
    public void process() throws ServletException, IOException {
        // Salida
        String out = "usuario/usuario-insercion";

        // Sesión
        HttpSession sesion = request.getSession(false);

        // Validar Sesión
        if (!validarSesion(sesion)) {
            out = "message/sesion-invalida";
        } else {
            // Capas de Negocio
            CommandValidation validator = new CommandValidation(sesion);

            if (validator.validarAccesoComando(getClass().getSimpleName())) {
                // Capas de Datos
                DALUsuario dalUsuario = new DALUsuario(sesion);
                DALPerfil dalPerfil = new DALPerfil(sesion);
                DALAvatar dalAvatar = new DALAvatar(sesion);

                // Obtener Operación
                String op = request.getParameter("op");

                // Formulario Captura Datos
                if (op == null || op.equals("captura")) {
                    // BD > Lista de Perfiles
                    List<Perfil> perfiles = dalPerfil.listar();

                    // BD > Lista de Avatares
                    List<Avatar> avatares = dalAvatar.listar();

                    // Inyección Datos
                    request.setAttribute("perfiles", perfiles);
                    request.setAttribute("avatares", avatares);
                } else if (op.equals("proceso")) {
                    // Request > Parámetros
                    String user = request.getParameter("user").trim();
                    String pass = request.getParameter("pass").trim();
                    int avatar = Integer.parseInt(request.getParameter("avatar"));
                    int perfil = Integer.parseInt(request.getParameter("perfil"));

                    // Parámetros > Entidad
                    Usuario usuario = new Usuario(0, user, pass, avatar, "", perfil, "");

                    // Entidad > Inserción BD - true | false
                    boolean checkOK = dalUsuario.insertar(usuario);

                    // Validar Operación
                    if (checkOK) {
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
        }

        // Redirección
        forward(out);
    }
}

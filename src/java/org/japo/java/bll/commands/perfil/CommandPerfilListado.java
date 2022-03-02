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
package org.japo.java.bll.commands.perfil;

import org.japo.java.bll.commands.Command;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.japo.java.bll.commands.admin.CommandValidation;
import org.japo.java.dal.DALPerfil;
import org.japo.java.entities.Perfil;
import org.japo.java.entities.Usuario;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class CommandPerfilListado extends Command {

    @Override
    public void process() throws ServletException, IOException {
        // Salida
        String out = "perfil/perfil-listado";

        // Sesión
        HttpSession sesion = request.getSession(false);

        // Validar Sesión
        if (!validarSesion(sesion)) {
            out = "message/sesion-invalida";
        } else {
            // Capas de Negocio
            CommandValidation validator = new CommandValidation(sesion);

            // Capas de Datos
            DALPerfil dalPerfil = new DALPerfil(sesion);

            if (validator.validarAccesoComando(getClass().getSimpleName())) {
                // Sesión > Usuario
                Usuario usuario = (Usuario) sesion.getAttribute("usuario");

                // BD > Lista de Perfiles
                List<Perfil> perfiles;

                // Determinar Perfil Usuario
                switch (usuario.getPerfil()) {
                    case Perfil.DEVEL:
                        // BD > Lista de Pefiles
                        perfiles = dalPerfil.listar();
                        break;
                    case Perfil.ADMIN:
                        // BD > Lista de Pefiles
                        perfiles = dalPerfil.listar();
                        break;
                    case Perfil.BASIC:
                    default:
                        // Usuario Actual (Únicamente) > Lista de Usuarios
                        Perfil perfil = dalPerfil.consultar(usuario.getPerfil());
                        perfiles = new ArrayList<>();
                        perfiles.add(perfil);
                }

                // Inyecta Datos Listado > JSP
                request.setAttribute("perfiles", perfiles);
            } else {
                out = "message/acceso-denegado";
            }
        }

        // Redirección
        forward(out);
    }
}

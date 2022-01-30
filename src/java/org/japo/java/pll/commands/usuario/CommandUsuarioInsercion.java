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
package org.japo.java.pll.commands.usuario;

import org.japo.java.pll.commands.Command;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.japo.java.bll.BLLAdmin;
import org.japo.java.dal.DALPerfil;
import org.japo.java.dal.DALUsuario;
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
            BLLAdmin bllAdmin = new BLLAdmin(sesion);

            // Capas de Datos
            DALPerfil dalPperfil = new DALPerfil(sesion);
            DALUsuario dalUsuario = new DALUsuario(sesion);

            if (bllAdmin.validarAccesoComando(getClass().getSimpleName())) {
                // Obtener Operación
                String op = request.getParameter("op");

                // Formulario Captura Datos
                if (op == null || op.equals("captura")) {
                    // BD > Lista de Perfiles
                    List<Perfil> perfiles = dalPperfil.obtenerPerfiles();

                    // Inyección Datos
                    request.setAttribute("perfiles", perfiles);
                } else if (op.equals("proceso")) {
                    // Request > Parámetros
                    String user = request.getParameter("user").trim();
                    String pass = request.getParameter("pass").trim();
                    int perfil = Integer.parseInt(request.getParameter("perfil").trim());

                    // Parámetros > Entidad
                    Usuario usuario = new Usuario(0, user, pass, perfil);

                    // Entidad > Inserción BD - true | false
                    boolean checkOK = dalUsuario.insertarUsuario(usuario);

                    // Validar Operación
                    if (checkOK) {
                        out = "message/operacion-completada";
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

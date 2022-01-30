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
package org.japo.java.pll.commands.permiso.usuario;

import org.japo.java.pll.commands.Command;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.http.HttpSession;
import org.japo.java.bll.BLLAdmin;
import org.japo.java.dal.DALPermisoUsuario;
import org.japo.java.entities.PermisoUsuario;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class CommandPermisoUsuarioBorrado extends Command {

    @Override
    @SuppressWarnings("ConvertToStringSwitch")
    public void process() throws ServletException, IOException {
        // Salida
        String out = "permiso/usuario/permiso-usuario-borrado";

        // Sesión
        HttpSession sesion = request.getSession(false);

        // Validar Sesión
        if (!validarSesion(sesion)) {
            out = "message/sesion-invalida";
        } else {
            // Capas de Negocio
            BLLAdmin bllAdmin = new BLLAdmin(sesion);

            // Capas de Datos
            DALPermisoUsuario dalPermiso = new DALPermisoUsuario(sesion);

            if (bllAdmin.validarAccesoComando(getClass().getSimpleName())) {
                // URL > ID Objeto
                int id = Integer.parseInt(request.getParameter("id"));

                // request > ID Operación
                String op = request.getParameter("op");

                // ID Entidad + BD > JSP Modificación
                if (op == null || op.equals("captura")) {
                    // ID Entidad + BD > Entidad
                    PermisoUsuario permiso = dalPermiso.obtenerPermiso(id);

                    // Enlaza Datos > JSP
                    request.setAttribute("permiso", permiso);
                } else if (op.equals("proceso")) {
                    // ID > Registro Borrado - true | false
                    boolean checkOK = dalPermiso.borrarPermiso(id);

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

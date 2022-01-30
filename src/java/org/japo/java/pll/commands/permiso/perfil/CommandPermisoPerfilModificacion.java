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
package org.japo.java.pll.commands.permiso.perfil;

import org.japo.java.pll.commands.Command;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.japo.java.bll.BLLAdmin;
import org.japo.java.dal.DALPerfil;
import org.japo.java.dal.DALPermisoPerfil;
import org.japo.java.dal.DALProceso;
import org.japo.java.entities.PermisoPerfil;
import org.japo.java.entities.Perfil;
import org.japo.java.entities.Proceso;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class CommandPermisoPerfilModificacion extends Command {

    @Override
    @SuppressWarnings("ConvertToStringSwitch")
    public void process() throws ServletException, IOException {
        // Salida
        String out = "permiso/perfil/permiso-perfil-modificacion";

        // Entidad
        PermisoPerfil permiso;

        // Sesión
        HttpSession sesion = request.getSession(false);

        // Validar Sesión
        if (!validarSesion(sesion)) {
            out = "message/sesion-invalida";
        } else {
            // Capas de Negocio
            BLLAdmin bllAdmin = new BLLAdmin(sesion);

            // Capas de Datos
            DALPerfil dalPerfil = new DALPerfil(sesion);
            DALPermisoPerfil dalPermiso = new DALPermisoPerfil(sesion);
            DALProceso dalProceso = new DALProceso(sesion);

            if (bllAdmin.validarAccesoComando(getClass().getSimpleName())) {
                // request > ID Entidad
                int id = Integer.parseInt(request.getParameter("id"));

                // request > ID Operación
                String op = request.getParameter("op");

                // Captura de Datos
                if (op == null || op.equals("captura")) {
                    // ID Entidad > Objeto Entidad
                    permiso = dalPermiso.obtenerPermiso(id);

                    // BD > Lista de Procesos
                    List<Proceso> procesos = dalProceso.obtenerProcesos();

                    // BD > Lista de Perfiles            
                    List<Perfil> perfiles = dalPerfil.obtenerPerfiles();

                    // Inyectar Datos > JSP
                    request.setAttribute("permiso", permiso);
                    request.setAttribute("procesos", procesos);
                    request.setAttribute("perfiles", perfiles);
                } else if (op.equals("proceso")) {
                    // ID Permiso Perfil > Objeto Entidad
                    permiso = dalPermiso.obtenerPermiso(id);

                    // Request > Parámetros
                    int proceso = Integer.parseInt(request.getParameter("proceso"));
                    int perfil = Integer.parseInt(request.getParameter("perfil"));
                    String info = request.getParameter("info");

                    // Parámetros > Entidad
                    permiso = new PermisoPerfil(permiso.getId(), proceso, perfil, info);

                    // Entidad > Modificación Registro BD
                    boolean checkOK = dalPermiso.modificarPermiso(permiso);

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

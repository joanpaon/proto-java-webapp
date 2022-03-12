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
package org.japo.java.bll.commands.permiso.usuario;

import org.japo.java.bll.commands.Command;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.japo.java.bll.commands.usuario.CommandUsuarioValidation;
import org.japo.java.dal.DALUsuario;
import org.japo.java.dal.DALPermisoUsuario;
import org.japo.java.dal.DALProceso;
import org.japo.java.entities.PermisoUsuario;
import org.japo.java.entities.Usuario;
import org.japo.java.entities.Proceso;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class CommandPermisoUsuarioModificacion extends Command {

    @Override
    @SuppressWarnings("ConvertToStringSwitch")
    public void process() throws ServletException, IOException {
        // Salida
        String out = "permiso/usuario/permiso-usuario-modificacion";

        // Entidad Inicial
        PermisoUsuario permiso;

        // Sesión
        HttpSession sesion = request.getSession(false);

        // Validar Sesión
        if (!validarSesion(sesion)) {
            out = "message/sesion-invalida";
        } else {
            // Capas de Negocio
            CommandUsuarioValidation validator = new CommandUsuarioValidation(sesion);

            // Capas de Datos
            DALUsuario dalUsuario = new DALUsuario(sesion);
            DALPermisoUsuario dalPermiso = new DALPermisoUsuario(sesion);
            DALProceso dalProceso = new DALProceso(sesion);

            if (validator.validarAccesoComando(getClass().getSimpleName())) {
                // request > ID Entidad
                int id = Integer.parseInt(request.getParameter("id"));

                // request > ID Operación
                String op = request.getParameter("op");

                // Captura de Datos
                if (op == null || op.equals("captura")) {
                    // ID Entidad > Objeto Entidad
                    permiso = dalPermiso.consultar(id);

                    // BD > Lista de Procesos
                    List<Proceso> procesos = dalProceso.listar();

                    // BD > Lista de Usuarios            
                    List<Usuario> usuarios = dalUsuario.listar();

                    // Inyectar Datos > JSP
                    request.setAttribute("permiso", permiso);
                    request.setAttribute("procesos", procesos);
                    request.setAttribute("usuarios", usuarios);
                } else if (op.equals("proceso")) {
                    // ID Permiso Usuario > Objeto Entidad
                    permiso = dalPermiso.consultar(id);

                    // Request > Parámetros
                    int usuario = Integer.parseInt(request.getParameter("usuario"));
                    int proceso = Integer.parseInt(request.getParameter("proceso"));
                    String info = request.getParameter("info");

                    // Entidad Final
                    permiso = new PermisoUsuario(permiso.getId(), usuario, "", proceso, "", info);

                    // Entidad > Modificación Registro BD
                    boolean checkOK = dalPermiso.modificar(permiso);

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

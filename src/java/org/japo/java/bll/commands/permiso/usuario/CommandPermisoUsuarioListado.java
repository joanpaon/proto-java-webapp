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
import org.japo.java.bll.commands.usuario.CommandUsuarioValidation;
import org.japo.java.dal.DALPermisoUsuario;
import org.japo.java.entities.PermisoUsuario;
import org.japo.java.libraries.UtilesListado;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class CommandPermisoUsuarioListado extends Command {

    @Override
    public void process() throws ServletException, IOException {
        // Salida
        String out = "permiso/usuario/permiso-usuario-listado";

        // Validar Sesión
        if (validarSesion(request)) {
            // Validador de Acceso
            CommandUsuarioValidation validator = new CommandUsuarioValidation(
                    config, request.getSession(false));

            // Validar Acceso Comando
            if (validator.validarAccesoComando(getClass().getSimpleName())) {
                // Capas de Datos
                DALPermisoUsuario dalPermiso = new DALPermisoUsuario(config);

                // BD > Parámetros Listado
                long rowCount = dalPermiso.contar();

                // Request > Índice de pagina            
                long rowIndex = UtilesListado.obtenerRowIndex(request);

                // Request > Líneas por Pagina            
                int rowsPage = UtilesListado.obtenerRowsPage(request);

                // Indice Navegación - Inicio
                long rowIndexIni = UtilesListado.obtenerRowIndexIni();

                // Indice Navegación - Anterior
                long rowIndexAnt = UtilesListado.obtenerRowIndexAnt(rowIndex, rowsPage);

                // Indice Navegación - Siguiente
                long rowIndexSig = UtilesListado.obtenerRowIndexSig(rowIndex, rowsPage, rowCount);

                // Indice Navegación - Final
                long rowIndexFin = UtilesListado.obtenerRowIndexFin(rowIndex, rowsPage, rowCount);

                // BD > Lista de Permisos de Perfil
                List<PermisoUsuario> permisos = dalPermiso.listar();

                // Inyecta Datos Listado > JSP
                request.setAttribute("permisos", permisos);

                // Inyecta Parámetros Listado > JSP
                request.setAttribute("row-index", rowIndex);
                request.setAttribute("row-index-ini", rowIndexIni);
                request.setAttribute("row-index-ant", rowIndexAnt);
                request.setAttribute("row-index-sig", rowIndexSig);
                request.setAttribute("row-index-fin", rowIndexFin);
                request.setAttribute("rows-page", rowsPage);
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

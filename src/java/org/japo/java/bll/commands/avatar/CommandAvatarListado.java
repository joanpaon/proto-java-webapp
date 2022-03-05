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
package org.japo.java.bll.commands.avatar;

import org.japo.java.bll.commands.Command;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.japo.java.bll.commands.admin.CommandValidation;
import org.japo.java.dal.DALAvatar;
import org.japo.java.entities.Avatar;
import org.japo.java.entities.Perfil;
import org.japo.java.entities.Usuario;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class CommandAvatarListado extends Command {

    @Override
    public void process() throws ServletException, IOException {
        // Salida
        String out = "avatar/avatar-listado";

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
                DALAvatar dalAvatar = new DALAvatar(sesion);

                // BD > Parámetros Listado
                long rowCount = dalAvatar.contar();

                // Request > Índice de pagina            
                long rowIndex;
                try {
                    // String > long
                    rowIndex = Long.parseLong(request.getParameter("row-index"));
                } catch (NumberFormatException e) {
                    rowIndex = 0;
                }

                // Request > Líneas por Pagina            
                int rowsPage;
                try {
                    // String > long
                    rowsPage = Integer.parseInt(request.getParameter("rows-page"));

                    // Validar Escalones
                    rowsPage = rowsPage == 80
                            || rowsPage == 40
                            || rowsPage == 20 ? rowsPage : 10;
                } catch (NumberFormatException e) {
                    rowsPage = 10;
                }

                // Indice Navegación - Inicio
                long rowIndexIni = 0;

                // Indice Navegación - Anterior
                long rowIndexAnt = rowIndex - rowsPage < 0 ? 0 : rowIndex - rowsPage;

                // Indice Navegación - Siguiente
                long rowIndexSig = rowIndex + rowsPage > rowCount - 1 ? rowIndex : rowIndex + rowsPage;

                // Indice Navegación - Final
                long rowIndexFin = rowCount == 0 ? 0
                        : rowCount / rowsPage == 0 ? 0
                                : rowCount % rowsPage == 0 ? (rowCount / rowsPage - 1) * rowsPage
                                        : rowCount / rowsPage * rowsPage;

                // Sesión > Usuario
                Usuario usuario = (Usuario) sesion.getAttribute("usuario");

                // BD > Lista de Avatares
                List<Avatar> avatares;

                // Determinar Avatar Usuario
                switch (usuario.getPerfil()) {
                    case Perfil.DEVEL:
                        // BD > Lista de Pefiles
                        avatares = dalAvatar.listar();
                        break;
                    case Perfil.ADMIN:
                        // BD > Lista de Pefiles
                        avatares = dalAvatar.listar();
                        break;
                    case Perfil.BASIC:
                    default:
                        // Usuario Actual (Únicamente) > Lista de Usuarios
                        Avatar avatar = dalAvatar.consultar(usuario.getAvatar());
                        avatares = new ArrayList<>();
                        avatares.add(avatar);
                }

                // Inyecta Datos Listado > JSP
                request.setAttribute("avatares", avatares);

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
        }

        // Redirección
        forward(out);
    }
}
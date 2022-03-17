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

import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.japo.java.dal.DALAvatar;
import org.japo.java.entities.Avatar;
import org.japo.java.entities.Perfil;
import org.japo.java.entities.Usuario;

/**
 *
 * @author JAPO Labs - japolabs@gmail.com
 */
public final class UtilesAvatar {

    private UtilesAvatar() {
    }

    public static final List<Avatar> obtenerAvataresUsuario(
            ServletConfig config,
            HttpServletRequest request) {
        // BD > Lista de Avatares
        List<Avatar> avatares;

        // Request > Session
        HttpSession sesion = request.getSession(false);

        // Sesión > Usuario
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");
        // Capas de Datos
        DALAvatar dalAvatar = new DALAvatar(config);

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

        // Retorno: Lista de usuarios visibles por el perfil
        return avatares;
    }
}

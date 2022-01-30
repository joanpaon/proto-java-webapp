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
package org.japo.java.bll;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.japo.java.dal.DALPermisoUsuario;
import org.japo.java.dal.DALProceso;
import org.japo.java.entities.Perfil;
import org.japo.java.entities.PermisoUsuario;
import org.japo.java.entities.Proceso;
import org.japo.java.entities.Usuario;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class BLLAdmin {

    // Sesión 
    HttpSession sesion;

    // Capas de Datos
    private final DALPermisoUsuario dalPermisoUsuario;
    private final DALProceso dalProceso;

    public BLLAdmin(HttpSession sesion) {
        this.sesion = sesion;

        dalPermisoUsuario = new DALPermisoUsuario(sesion);
        dalProceso = new DALProceso(sesion);
    }

    public final boolean validarAccesoComando(String comando) {
        // Semáforo
        boolean checkOK;

        try {
            // Sesion > Usuario
            Usuario usuario = (Usuario) sesion.getAttribute("usuario");

            // Usuario > Perfil
            int perfil = usuario.getPerfil();

            // Validar Perfil Desarrollador
            if (perfil >= Perfil.DEVEL) {
                checkOK = true;
            } else {
                // Usuario + BD > Lista de Permisos
                List<PermisoUsuario> permisos = dalPermisoUsuario.obtenerPermisos(usuario.getId());

                // Nombre Comando > Entidad Comando
                Proceso proceso = dalProceso.obtenerProceso(comando);

                // Semaforo: true | false
                checkOK = validarPermisoProceso(permisos, proceso);
            }
        } catch (Exception e) {
            checkOK = false;
        }

        // Retorno: true | false
        return checkOK;
    }

    public final boolean validarAccesoServicio(String servicio) {
        return true;
    }

    private boolean validarPermisoProceso(List<PermisoUsuario> permisos, Proceso proceso) {
        return buscarProcesoPermisos(permisos, proceso) > -1;
    }

    private int buscarProcesoPermisos(List<PermisoUsuario> permisos, Proceso proceso) {
        // Posicion del proceso en la lista de permisos
        int posicion = -1;

        // Bucle de Búsqueda
        for (int i = 0; i < permisos.size(); i++) {
            if (permisos.get(i).getProceso() == proceso.getId()) {
                posicion = i;
                i = permisos.size();
            }
        }

        // Retorno: Posición
        return posicion;
    }
}

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
package org.japo.java.entities;

import java.io.Serializable;
import org.japo.java.libraries.UtilesValidacion;

/**
 *
 * @author JAPO Labs - japolabs@gmail.com
 */
public final class PermisoUsuario implements Serializable {

    // Valores por Defecto
    public static final int DEF_ID = 0;
    public static final int DEF_USUARIO = Usuario.DEF_ID;
    public static final String DEF_USUARIO_NAME = Usuario.DEF_USER;
    public static final int DEF_PROCESO = Proceso.DEF_ID;
    public static final String DEF_PROCESO_INFO = Proceso.DEF_INFO;
    public static final String DEF_INFO = "Permiso de Usuario NO Definido";

    // Expresiones regulares
    public static final String REG_INFO = "[\\wáéíóúüñÁÉÍÓÚÜÑ\\- ]{6,50}";

    // Campos
    private int id;
    private int usuario;
    private String usuarioName;
    private int proceso;
    private String procesoInfo;
    private String info;

    public PermisoUsuario() {
        id = DEF_ID;
        usuario = DEF_USUARIO;
        usuarioName = DEF_USUARIO_NAME;
        proceso = DEF_PROCESO;
        procesoInfo = DEF_PROCESO_INFO;
        info = DEF_INFO;
    }

    public PermisoUsuario(int id,
            int usuario, String usuarioName,
            int proceso, String procesoInfo,
            String info) {
        this.id = id;
        this.usuario = usuario;
        this.usuarioName = usuarioName;
        this.proceso = proceso;
        this.procesoInfo = procesoInfo;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProceso() {
        return proceso;
    }

    public void setProceso(int proceso) {
        this.proceso = proceso;
    }

    public String getProcesoInfo() {
        return procesoInfo;
    }

    public void setProcesoInfo(String procesoInfo) {
        this.procesoInfo = procesoInfo;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    public String getUsuarioName() {
        return usuarioName;
    }

    public void setUsuarioName(String usuarioName) {
        this.usuarioName = usuarioName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    // ---
    //
    public final boolean validarId() {
        return validarId(id);
    }

    public final boolean validarUsuario() {
        return validarUsuario(usuario);
    }

    public final boolean validarUsuarioName() {
        return validarUsuarioName(usuarioName);
    }

    public final boolean validarProceso() {
        return validarProceso(proceso);
    }

    public final boolean validarProcesoInfo() {
        return validarProcesoInfo(procesoInfo);
    }

    public final boolean validarInfo() {
        return validarInfo(info);
    }

    // ---
    //
    public static final boolean validarId(int id) {
        return id >= 0;
    }

    public static final boolean validarUsuario(int usuario) {
        return Usuario.validarId(usuario);
    }

    public static final boolean validarUsuarioName(String usuarioName) {
        return Usuario.validarUser(usuarioName);
    }

    public static final boolean validarProceso(int proceso) {
        return Proceso.validarId(proceso);
    }

    public static final boolean validarProcesoInfo(String procesoInfo) {
        return Proceso.validarInfo(procesoInfo);
    }

    public static boolean validarInfo(String info) {
        return UtilesValidacion.validarDato(info, REG_INFO);
    }
}

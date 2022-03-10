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
public final class Proceso implements Serializable {

    // Valores por Defecto
    public static final int DEF_ID = 0;
    public static final String DEF_NOMBRE = "CommandUnknown";
    public static final String DEF_INFO = "Proceso Desconocido";
    
    // Expresiones regulares
    public static final String REG_NOMBRE = "[\\wáéíóúüñÁÉÍÓÚÜÑ]{6,20}";
    public static final String REG_INFO = "[\\wáéíóúüñÁÉÍÓÚÜÑ\\- ]{6,50}";
    
    private int id;
    private String nombre;
    private String info;

    public Proceso() {
        id = DEF_ID;
        nombre = DEF_NOMBRE;
        info = DEF_INFO;
    }

    public Proceso(int id, String nombre, String info) {
        this.id = id;
        this.nombre = nombre;
        this.info = info;
    }

    public Proceso(String nombre, String info) {
        this.nombre = nombre;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public final boolean validarNombre() {
        return validarNombre(nombre);
    }

    public final boolean validarInfo() {
        return validarInfo(info);
    }

    // ---
    //
    public static final boolean validarId(int id) {
        return id >= 0;
    }

    public static final boolean validarNombre(String nombre) {
        return UtilesValidacion.validarDato(nombre, REG_NOMBRE);
    }

    public static boolean validarInfo(String info) {
        return UtilesValidacion.validarDato(info, REG_INFO);
    }
}

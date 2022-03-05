package org.japo.java.entities;

import java.io.Serializable;

/**
 *
 * @author JAPO Labs - japolabs@gmail.com
 */
public final class Proceso implements Serializable {

    // Valores por Defecto
    public static final int DEF_ID = 0;
    public static final String DEF_NOMBRE = "ProcesoDesconocido";
    public static final String DEF_INFO = "Proceso Desconocido";

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
}

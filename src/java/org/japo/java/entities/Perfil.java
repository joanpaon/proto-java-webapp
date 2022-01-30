package org.japo.java.entities;

/**
 *
 * @author JAPO Labs - japolabs@gmail.com
 */
public final class Perfil {
    // Códigos de Perfiles Básicos

    public static final int VISIT = 0;
    public static final int BASIC = 100;
    public static final int ADMIN = 800;
    public static final int DEVEL = 900;

    // Campos
    private int id;
    private String nombre;
    private String info;

    public Perfil(int id, String nombre, String info) {
        this.id = id;
        this.nombre = nombre;
        this.info = info;
    }

    public Perfil(String nombre, String info) {
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

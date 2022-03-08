package org.japo.java.entities;

/**
 *
 * @author JAPO Labs - japolabs@gmail.com
 */
public class Avatar {

    public static final int MAX_SIZE = 65536;
    public static final int MAX_CHARS = 20; 

    private int id;
    private String nombre;
    private String imagen;

    public Avatar() {
        nombre = "";
        imagen = "";
    }

    public Avatar(int id, String nombre, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}

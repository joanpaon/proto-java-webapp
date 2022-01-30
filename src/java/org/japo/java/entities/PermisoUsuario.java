package org.japo.java.entities;

/**
 *
 * @author JAPO Labs - japolabs@gmail.com
 */
public final class PermisoUsuario {

    private int id;
    private int proceso;
    private int usuario;
    private String info;

    public PermisoUsuario(int id, int proceso, int usuario, String info) {
        this.id = id;
        this.proceso = proceso;
        this.usuario = usuario;
        this.info = info;
    }

    public PermisoUsuario(int proceso, int usuario, String info) {
        this.proceso = proceso;
        this.usuario = usuario;
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

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

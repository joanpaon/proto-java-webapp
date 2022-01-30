package org.japo.java.entities;

/**
 *
 * @author JAPO Labs - japolabs@gmail.com
 */
public final class PermisoPerfil {

    private int id;
    private int proceso;
    private int perfil;
    private String info;

    public PermisoPerfil(int id, int proceso, int perfil, String info) {
        this.id = id;
        this.proceso = proceso;
        this.perfil = perfil;
        this.info = info;
    }

    public PermisoPerfil(int proceso, int perfil, String info) {
        this.proceso = proceso;
        this.perfil = perfil;
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

    public int getPerfil() {
        return perfil;
    }

    public void setPerfil(int perfil) {
        this.perfil = perfil;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

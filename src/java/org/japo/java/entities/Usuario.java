package org.japo.java.entities;

import java.io.Serializable;
import org.japo.java.libraries.UtilesValidacion;

/**
 *
 * @author JAPO Labs - japolabs@gmail.com
 */
public final class Usuario implements Serializable {

    // Expresiones regulares
    public static final String ER_USER = "[\\w]{3,30}";
    public static final String ER_PASS = "[\\w]{6,30}";

    // Campos
    private int id;
    private String user;
    private String pass;
    private int perfil;

    public Usuario() {
    }

    public Usuario(int id, String user, String pass, int perfil) {
        this.id = id;
        this.user = user;
        this.pass = pass;
        this.perfil = perfil;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getPerfil() {
        return perfil;
    }

    public void setPerfil(int perfil) {
        this.perfil = perfil;
    }

    public final boolean validarId() {
        return id > 0;
    }

    public final boolean validarUser() {
        return UtilesValidacion.validarDato(user, ER_USER);
    }

    public final boolean validarPass() {
        return UtilesValidacion.validarDato(pass, ER_PASS);
    }

    public final boolean validarPerfil() {
        return perfil > 0;
    }

    public static final boolean validarUser(String user) {
        return UtilesValidacion.validarDato(user, ER_USER);
    }

    public static final boolean validarPass(String pass) {
        return UtilesValidacion.validarDato(pass, ER_PASS);
    }
}

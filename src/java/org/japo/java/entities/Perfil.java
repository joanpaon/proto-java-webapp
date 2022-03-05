package org.japo.java.entities;

import java.io.Serializable;
import org.japo.java.libraries.UtilesValidacion;

/**
 *
 * @author JAPO Labs - japolabs@gmail.com
 */
public final class Perfil implements Serializable {

    // Códigos de Perfiles Básicos
    public static final int VISIT = 0;
    public static final int BASIC = 100;
    public static final int ADMIN = 800;
    public static final int DEVEL = 900;

    // Nombres de Perfiles Básicos
    public static final String VISIT_NAME = "Visitante";
    public static final String BASIC_NAME = "Usuario";
    public static final String ADMIN_NAME = "Administrador";
    public static final String DEVEL_NAME = "Desarrollador";

    // Descripción de Perfiles Básicos
    public static final String VISIT_INFO = "Usuario NO Identificado";
    public static final String BASIC_INFO = "Usuario Identificado";
    public static final String ADMIN_INFO = "Usuario con Derechos Administrativos";
    public static final String DEVEL_INFO = "Usuario con Derechos de Desarrollo";

    // Expresiones regulares
    public static final String ER_NOMBRE = "[\\wáéíóúüñÁÉÍÓÚÜÑ]{6,20}";
    public static final String ER_INFO = "[\\wáéíóúüñÁÉÍÓÚÜÑ\\- ]{6,50}";

    // Campos
    private int id;
    private String nombre;
    private String info;

    public Perfil() {
        id = VISIT;
        nombre = VISIT_NAME;
        info = VISIT_INFO;
    }

    public Perfil(int id, String nombre, String info) {
        this.id = id;
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

    public final boolean validarId() {
        return getId() > 0;
    }

    public final boolean validarNombre() {
        return UtilesValidacion.validarDato(nombre, ER_NOMBRE);
    }

    public final boolean validarInfo() {
        return UtilesValidacion.validarDato(info, ER_INFO);
    }

    public static boolean validarInfo(String info) {
        return UtilesValidacion.validarDato(info, ER_INFO);
    }
}

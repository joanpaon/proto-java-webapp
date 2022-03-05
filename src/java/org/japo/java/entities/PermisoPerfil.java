package org.japo.java.entities;

import java.io.Serializable;

/**
 *
 * @author JAPO Labs - japolabs@gmail.com
 */
public final class PermisoPerfil implements Serializable {

    // Valores por Defecto
    public static final int DEF_ID = 0;
    public static final int DEF_PERFIL = Perfil.VISIT;
    public static final String DEF_PERFIL_INFO = Perfil.VISIT_INFO;
    public static final int DEF_PROCESO = Proceso.DEF_ID;
    public static final String DEF_PROCESO_INFO = Proceso.DEF_INFO;
    public static final String DEF_INFO = "Permiso de Perfil NO Definido";

    // Expresiones regulares
    public static final String REG_PERFIL_INFO = "[\\wáéíóúüñÁÉÍÓÚÜÑ\\- ]{6,50}";
    public static final String REG_PROCESO_INFO = "[\\wáéíóúüñÁÉÍÓÚÜÑ\\- ]{6,50}";
    public static final String REG_INFO = "[\\wáéíóúüñÁÉÍÓÚÜÑ\\- ]{6,50}";

    // Campos
    private int id;
    private int perfil;
    private String perfilInfo;
    private int proceso;
    private String procesoInfo;
    private String info;

    public PermisoPerfil() {
        id = DEF_ID;
        perfil = DEF_PERFIL;
        perfilInfo = DEF_PERFIL_INFO;
        proceso = DEF_PROCESO;
        procesoInfo = DEF_PROCESO_INFO;
        info = DEF_INFO;
    }

    public PermisoPerfil(int id,
            int perfil, String perfilInfo,
            int proceso, String procesoInfo,
            String info) {
        this.id = id;
        this.perfil = perfil;
        this.perfilInfo = perfilInfo;
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

    public int getPerfil() {
        return perfil;
    }

    public void setPerfil(int perfil) {
        this.perfil = perfil;
    }

    public String getPerfilInfo() {
        return perfilInfo;
    }

    public void setPerfilInfo(String perfilInfo) {
        this.perfilInfo = perfilInfo;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public final boolean validarId() {
        return getId() >= 0;
    }

    public final boolean validarProceso() {
        return getProceso() >= 0;
    }

    public final boolean validarPerfil() {
        return getPerfil() >= 0;
    }

}

package org.japo.java.entities;

/**
 *
 * @author JAPO Labs - japolabs@gmail.com
 */
public final class PermisoUsuario {

    // Valores por Defecto
    public static final int DEF_ID = 0;
    public static final int DEF_USUARIO = Usuario.DEF_ID;
    public static final String DEF_USUARIO_NAME = Usuario.DEF_USER;
    public static final int DEF_PROCESO = Proceso.DEF_ID;
    public static final String DEF_PROCESO_INFO = Proceso.DEF_INFO;
    public static final String DEF_INFO = "Permiso de Usuario NO Definido";

    // Expresiones regulares
    public static final String REG_USUARIO_NAME = "[\\wáéíóúüñÁÉÍÓÚÜÑ\\- ]{6,50}";
    public static final String REG_PROCESO_INFO = "[\\wáéíóúüñÁÉÍÓÚÜÑ\\- ]{6,50}";
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

    public final boolean validarId() {
        return getId() >= 0;
    }

    public final boolean validarProceso() {
        return getProceso() >= 0;
    }

    public final boolean validarUsuario() {
        return getUsuario() >= 0;
    }

}

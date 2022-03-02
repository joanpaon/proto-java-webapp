package org.japo.java.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.japo.java.entities.PermisoUsuario;
import org.japo.java.libraries.UtilesServlet;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class DALPermisoUsuario {

    // Campos
    private final HttpSession sesion;

    // Nombre de la Base de datos
    private final String bd;

    public DALPermisoUsuario(HttpSession sesion) {
        this.sesion = sesion;

        bd = (String) sesion.getAttribute("bd");
    }

    public List<PermisoUsuario> obtenerPermisos() {
        // SQL
        String sql = ""
                + "SELECT "
                + "* "
                + "FROM permisos_usuario";

        // Lista Vacía
        List<PermisoUsuario> permisos = new ArrayList<>();

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                     Connection conn = ds.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
                // BD > Lista de Entidades
                try ( ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        // Fila Actual > Campos 
                        int id = rs.getInt("id");
                        int proceso = rs.getInt("proceso");
                        int usuario = rs.getInt("usuario");
                        String info = rs.getString("info");

                        // Campos > Entidad
                        PermisoUsuario permiso = new PermisoUsuario(id, proceso, usuario, info);

                        // Entidad > Lista
                        permisos.add(permiso);
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno Lista
        return permisos;
    }

    public PermisoUsuario obtenerPermiso(int id) {
        // SQL
        String sql = ""
                + "SELECT "
                + "* "
                + "FROM "
                + "permisos_usuario "
                + "WHERE "
                + "id=?";

        // Entidad
        PermisoUsuario permiso = null;

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                     Connection conn = ds.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
                // Parametrizar Sentencia
                ps.setInt(1, id);

                // BD > Lista de Entidades
                try ( ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Fila Actual > Campos 
                        int proceso = rs.getInt("proceso");
                        int usuario = rs.getInt("usuario");
                        String info = rs.getString("info");

                        // Campos > Entidad
                        permiso = new PermisoUsuario(id, proceso, usuario, info);
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno Entidad
        return permiso;
    }

    public List<PermisoUsuario> obtenerPermisos(int usuario) {
        // SQL
        String sql = ""
                + "SELECT "
                + "* "
                + "FROM "
                + "permisos_usuario "
                + "WHERE "
                + "usuario=?";

        // Lista Vacía
        List<PermisoUsuario> permisos = new ArrayList<>();

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                     Connection conn = ds.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
                // Parametrizar Sentencia
                ps.setInt(1, usuario);

                // BD > Lista de Entidades
                try ( ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        // Fila Actual > Campos 
                        int id = rs.getInt("id");
                        int proceso = rs.getInt("proceso");
                        String info = rs.getString("info");

                        // Campos > Entidad
                        PermisoUsuario permiso = new PermisoUsuario(id, proceso, usuario, info);

                        // Entidad > Lista
                        permisos.add(permiso);
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno Lista
        return permisos;
    }

    public boolean insertarPermiso(PermisoUsuario permiso) {
        // SQL
        final String SQL = ""
                + "INSERT INTO "
                + "permisos_usuario "
                + "("
                + "proceso, usuario, info"
                + ") "
                + "VALUES (?, ?, ?)";

        // Número de registros afectados
        int numReg = 0;

        // Obtención del Contexto
        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                     Connection conn = ds.getConnection();  PreparedStatement ps = conn.prepareStatement(SQL)) {
                // Parametrizar Sentencia
                ps.setInt(1, permiso.getProceso());
                ps.setInt(2, permiso.getUsuario());
                ps.setString(3, permiso.getInfo());

                // Ejecutar Sentencia
                numReg = ps.executeUpdate();
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno: true | false
        return numReg == 1;
    }

    public boolean borrarPermiso(int id) {
        // SQL
        final String SQL = ""
                + "DELETE FROM "
                + "permisos_usuario "
                + "WHERE id=?";

        // Número de registros afectados
        int numReg = 0;

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                     Connection conn = ds.getConnection();  PreparedStatement ps = conn.prepareStatement(SQL)) {
                // Parametrizar Sentencia
                ps.setInt(1, id);

                // Ejecutar Sentencia
                numReg = ps.executeUpdate();
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno: true | false
        return numReg == 1;
    }

    // Este método se utiliza cuando no se conoce la Id
    // pero si el proceso y el usuario 
    public boolean borrarPermiso(PermisoUsuario permiso) {
        // SQL
        final String SQL
                = "DELETE FROM "
                + "permisos_usuario "
                + "WHERE "
                + "proceso=? AND usuario=?";

        // Número de Registros Afectados
        int numReg = 0;

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                     Connection conn = ds.getConnection();  PreparedStatement ps = conn.prepareStatement(SQL)) {
                // Parametrizar Sentencia
                ps.setInt(1, permiso.getProceso());
                ps.setInt(2, permiso.getUsuario());

                // Ejecutar Sentencia
                numReg = ps.executeUpdate();
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno: true | false
        return numReg == 1;
    }

    public boolean modificarPermiso(PermisoUsuario permiso) {
        // SQL
        final String SQL = ""
                + "UPDATE "
                + "permisos_usuario "
                + "SET "
                + "proceso=?, usuario=?, info=? "
                + "WHERE "
                + "id=?";

        // Número de Registros Afectados
        int numReg = 0;

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                     Connection conn = ds.getConnection();  PreparedStatement ps = conn.prepareStatement(SQL)) {
                // Parametrizar Sentencia
                ps.setInt(1, permiso.getProceso());
                ps.setInt(2, permiso.getUsuario());
                ps.setString(3, permiso.getInfo());
                ps.setInt(4, permiso.getId());

                // Ejecutar Sentencia
                numReg = ps.executeUpdate();
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno: true | false
        return numReg == 1;
    }

    public Long contarPermisos() {
        // Número de Filas
        long filas = 0;

        // SQL
        String sql = ""
                + "SELECT "
                + "COUNT(*) "
                + "FROM "
                + "permisos_usuario";

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                     Connection conn = ds.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
                try ( ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        filas = rs.getLong(1);
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno: Filas Contadas
        return filas;
    }

    public List<PermisoUsuario> obtenerPaginaPermisos(long indice, int longitud) {
        // SQL
        String sql = ""
                + "SELECT "
                + "* "
                + "FROM "
                + "permisos_usuario "
                + "LIMIT ?, ?";

        // Lista Vacía
        List<PermisoUsuario> permisos = new ArrayList<>();

        // Obtención del Contexto
        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                     Connection conn = ds.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
                // Parametrizar Sentencia
                ps.setLong(1, indice);
                ps.setLong(2, longitud);

                // BD > Lista de Entidades
                try ( ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        // Fila Actual > Campos 
                        int id = rs.getInt("id");
                        int proceso = rs.getInt("proceso");
                        int usuario = rs.getInt("usuario");
                        String info = rs.getString("info");

                        // Campos > Entidad
                        PermisoUsuario permiso = new PermisoUsuario(id, proceso, usuario, info);

                        // Entidad > Lista
                        permisos.add(permiso);
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno Lista
        return permisos;
    }
}

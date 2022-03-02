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
import org.japo.java.entities.PermisoPerfil;
import org.japo.java.libraries.UtilesServlet;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class DALPermisoPerfil {

    // Campos
    private final HttpSession sesion;

    // Nombre de la Base de datos
    private final String bd;

    public DALPermisoPerfil(HttpSession sesion) {
        this.sesion = sesion;

        bd = (String) sesion.getAttribute("bd");
    }

    public List<PermisoPerfil> listar() {
        // SQL
        String sql = ""
                + "SELECT "
                + "* "
                + "FROM permisos_perfil";

        // Lista Vacía
        List<PermisoPerfil> permisos = new ArrayList<>();

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                    Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                // BD > Lista de Entidades
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        // Fila Actual > Campos 
                        int id = rs.getInt("id");
                        int proceso = rs.getInt("proceso");
                        int perfil = rs.getInt("perfil");
                        String info = rs.getString("info");

                        // Campos > Entidad
                        PermisoPerfil permiso = new PermisoPerfil(id, proceso, perfil, info);

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

    public PermisoPerfil consultar(int id) {
        // SQL
        String sql = ""
                + "SELECT "
                + "* "
                + "FROM "
                + "permisos_perfil "
                + "WHERE "
                + "id=?";

        // Entidad
        PermisoPerfil permiso = null;

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                    Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                // Parametrizar Sentencia
                ps.setInt(1, id);

                // BD > Lista de Entidades
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Fila Actual > Campos 
                        int proceso = rs.getInt("proceso");
                        int perfil = rs.getInt("perfil");
                        String info = rs.getString("info");

                        // Campos > Entidad
                        permiso = new PermisoPerfil(id, proceso, perfil, info);
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno Entidad
        return permiso;
    }

    public List<PermisoPerfil> listar(int perfil) {
        // SQL
        String sql = ""
                + "SELECT "
                + "* "
                + "FROM "
                + "permisos_perfil "
                + "WHERE "
                + "perfil=?";

        // Lista Vacía
        List<PermisoPerfil> permisos = new ArrayList<>();

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                    Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                // Parametrizar Sentencia
                ps.setInt(1, perfil);

                // BD > Lista de Entidades
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        // Fila Actual > Campos 
                        int id = rs.getInt("id");
                        int proceso = rs.getInt("proceso");
                        String info = rs.getString("info");

                        // Campos > Entidad
                        PermisoPerfil permiso = new PermisoPerfil(id, proceso, perfil, info);

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

    public boolean insertar(PermisoPerfil permiso) {
        // SQL
        final String SQL = ""
                + "INSERT INTO "
                + "permisos_perfil "
                + "("
                + "proceso, perfil, info"
                + ") "
                + "VALUES (?, ?, ?)";

        // Número de registros afectados
        int numReg = 0;

        // Obtención del Contexto
        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                    Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)) {
                // Parametrizar Sentencia
                ps.setInt(1, permiso.getProceso());
                ps.setInt(2, permiso.getPerfil());
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

    public boolean borrar(int id) {
        // SQL
        final String SQL = ""
                + "DELETE FROM "
                + "permisos_perfil "
                + "WHERE id=?";

        // Número de registros afectados
        int numReg = 0;

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                    Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)) {
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
    // pero si el proceso y el perfil 
    public boolean borrar(PermisoPerfil permiso) {
        // SQL
        final String SQL
                = "DELETE FROM "
                + "permisos_perfil "
                + "WHERE "
                + "proceso=? AND perfil=?";

        // Número de Registros Afectados
        int numReg = 0;

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                    Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)) {
                // Parametrizar Sentencia
                ps.setInt(1, permiso.getProceso());
                ps.setInt(2, permiso.getPerfil());

                // Ejecutar Sentencia
                numReg = ps.executeUpdate();
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno: true | false
        return numReg == 1;
    }

    public boolean modificar(PermisoPerfil permiso) {
        // SQL
        final String SQL = ""
                + "UPDATE "
                + "permisos_perfil "
                + "SET "
                + "proceso=?, perfil=?, info=? "
                + "WHERE "
                + "id=?";

        // Número de Registros Afectados
        int numReg = 0;

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                    Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)) {
                // Parametrizar Sentencia
                ps.setInt(1, permiso.getProceso());
                ps.setInt(2, permiso.getPerfil());
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

    public Long contar() {
        // Número de Filas
        long filas = 0;

        // SQL
        String sql = ""
                + "SELECT "
                + "COUNT(*) "
                + "FROM "
                + "permisos_perfil";

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                    Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
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

    public List<PermisoPerfil> paginar(long indice, int longitud) {
        // SQL
        String sql = ""
                + "SELECT "
                + "* "
                + "FROM "
                + "permisos_perfil "
                + "LIMIT ?, ?";

        // Lista Vacía
        List<PermisoPerfil> permisos = new ArrayList<>();

        // Obtención del Contexto
        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                    Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                // Parametrizar Sentencia
                ps.setLong(1, indice);
                ps.setLong(2, longitud);

                // BD > Lista de Entidades
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        // Fila Actual > Campos 
                        int id = rs.getInt("id");
                        int proceso = rs.getInt("proceso");
                        int perfil = rs.getInt("perfil");
                        String info = rs.getString("info");

                        // Campos > Entidad
                        PermisoPerfil permiso = new PermisoPerfil(id, proceso, perfil, info);

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

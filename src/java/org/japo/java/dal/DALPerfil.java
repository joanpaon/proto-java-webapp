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
import org.japo.java.entities.Perfil;
import org.japo.java.libraries.UtilesServlet;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class DALPerfil {

    // Campos
    private final HttpSession sesion;

    // Nombre de la Base de datos
    private final String bd;

    public DALPerfil(HttpSession sesion) {
        this.sesion = sesion;

        bd = (String) sesion.getAttribute("bd");
    }

    public List<Perfil> obtenerPerfiles() {
        // SQL 
        String sql = ""
                + "SELECT "
                + "* "
                + "FROM "
                + "perfiles";

        // Lista Vacía
        List<Perfil> perfiles = new ArrayList<>();

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
                        String nombre = rs.getString("nombre");
                        String info = rs.getString("info");

                        // Campos > Entidad
                        Perfil perfil = new Perfil(id, nombre, info);

                        // Entidad > Lista
                        perfiles.add(perfil);
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno Lista
        return perfiles;
    }

    public Perfil obtenerPerfil(int id) {
        // SQL
        String sql = ""
                + "SELECT "
                + "* "
                + "FROM "
                + "perfiles "
                + "WHERE "
                + "perfiles.id=" + id;

        // Perfil Buscado
        Perfil perfil = null;

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                     Connection conn = ds.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
                // BD > Lista de Entidades
                try ( ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Fila Actual > Campos 
                        String nombre = rs.getString("nombre");
                        String info = rs.getString("info");

                        // Campos > Entidad
                        perfil = new Perfil(id, nombre, info);
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno Lista
        return perfil;
    }

    public boolean insertarPerfil(Perfil perfil) {
        // SQL
        String sql = ""
                + "INSERT INTO "
                + "perfiles "
                + "("
                + "nombre, info "
                + ") "
                + "VALUES (?, ?)";

        // Número de registros afectados
        int numReg = 0;

        // Obtención del Contexto
        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                     Connection conn = ds.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
                // Parametrizar Sentencia
                ps.setString(1, perfil.getNombre());
                ps.setString(2, perfil.getInfo());

                // Ejecutar Sentencia
                numReg = ps.executeUpdate();
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno: true | false
        return numReg == 1;
    }

    public boolean borrarPerfil(int id) {
        // SQL
        final String SQL = ""
                + "DELETE FROM "
                + "perfiles "
                + "WHERE "
                + "id=?";

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

    public boolean modificarPerfil(Perfil perfil) {
        // SQL
        final String SQL = ""
                + "UPDATE "
                + "perfiles "
                + "SET "
                + "nombre=?, info=? "
                + "WHERE id=?";

        // Número de Registros Afectados
        int numReg = 0;

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                     Connection conn = ds.getConnection();  PreparedStatement ps = conn.prepareStatement(SQL)) {
                // Parametrizar Sentencia
                ps.setString(1, perfil.getNombre());
                ps.setString(2, perfil.getInfo());
                ps.setInt(3, perfil.getId());

                // Ejecutar Sentencia
                numReg = ps.executeUpdate();
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno: true | false
        return numReg == 1;
    }

    public Long contarPerfiles() {
        // Número de Filas
        long filas = 0;

        // SQL
        String sql = ""
                + "SELECT "
                + "COUNT(*) "
                + "FROM "
                + "perfiles";

        // Obtención del Contexto
        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                     Connection conn = ds.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
                // BD > Lista de Entidades
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

    public List<Perfil> obtenerPaginaPerfiles(long indice, long longitud) {
        // SQL
        String sql = ""
                + "SELECT "
                + "* "
                + "FROM "
                + "perfiles "
                + "LIMIT ?, ?";

        // Lista Vacía
        List<Perfil> perfiles = new ArrayList<>();

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
                        String nombre = rs.getString("nombre");
                        String info = rs.getString("info");

                        // Campos > Entidad
                        Perfil perfil = new Perfil(id, nombre, info);

                        // Entidad > Lista
                        perfiles.add(perfil);
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno Lista
        return perfiles;
    }
}

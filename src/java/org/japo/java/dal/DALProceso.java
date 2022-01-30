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
import org.japo.java.entities.Proceso;
import org.japo.java.libraries.UtilesServlet;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class DALProceso {

    // Campos
    private final String bd;

    public DALProceso(HttpSession sesion) {
        bd = (String) sesion.getAttribute("bd");
    }

    public List<Proceso> obtenerProcesos() {
        // SQL
        String sql = ""
                + "SELECT "
                + "* "
                + "FROM "
                + "procesos";

        // Lista Vacía
        List<Proceso> procesos = new ArrayList<>();

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                    Connection conn = ds.getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql)) {
                // BD > Lista de Entidades
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        // Fila Actual > Campos 
                        int id = rs.getInt("id");
                        String nombre = rs.getString("nombre");
                        String info = rs.getString("info");

                        // Campos > Entidad
                        Proceso proceso = new Proceso(id, nombre, info);

                        // Entidad > Lista
                        procesos.add(proceso);
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno Lista
        return procesos;
    }

    public Proceso obtenerProceso(int id) {
        // SQL
        String sql = ""
                + "SELECT "
                + "* "
                + "FROM procesos "
                + "WHERE "
                + "procesos.id=?";

        // Entidad
        Proceso proceso = null;

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                    Connection conn = ds.getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql)) {
                // Parametrizar Sentencia
                ps.setInt(1, id);

                // BD > Entidad
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Fila Actual > Campos 
                        String nombre = rs.getString("nombre");
                        String info = rs.getString("info");

                        // Campos > Entidad
                        proceso = new Proceso(id, nombre, info);
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno Entidad
        return proceso;
    }

    public Proceso obtenerProceso(String nombre) {
        // SQL
        String sql = ""
                + "SELECT "
                + "* "
                + "FROM procesos "
                + "WHERE "
                + "procesos.nombre=?";

        // Entidad
        Proceso proceso = null;

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                    Connection conn = ds.getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql)) {
                // Parametrizar Sentencia
                ps.setString(1, nombre);

                // BD > Entidad
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Fila Actual > Campos 
                        int id = rs.getInt("id");
                        String info = rs.getString("info");

                        // Campos > Entidad
                        proceso = new Proceso(id, nombre, info);
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno Entidad
        return proceso;
    }

    public boolean insertarProceso(Proceso proceso) {
        // SQL
        final String SQL = ""
                + "INSERT INTO "
                + "procesos "
                + "("
                + "nombre, info"
                + ") "
                + "VALUES (?, ?)";

        // Número de registros afectados
        int numReg = 0;

        // Obtención del Contexto
        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                    Connection conn = ds.getConnection();
                    PreparedStatement ps = conn.prepareStatement(SQL)) {
                // Parametrizar Sentencia
                ps.setString(1, proceso.getNombre());
                ps.setString(2, proceso.getInfo());

                // Ejecutar Sentencia
                numReg = ps.executeUpdate();
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno: true | false
        return numReg == 1;
    }

    public boolean borrarProceso(int id) {
        // SQL
        final String SQL = ""
                + "DELETE FROM "
                + "procesos "
                + "WHERE id=?";

        // Número de registros afectados
        int numReg = 0;

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                    Connection conn = ds.getConnection();
                    PreparedStatement ps = conn.prepareStatement(SQL)) {
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

    public boolean modificarProceso(Proceso proceso) {
        // SQL
        final String SQL = ""
                + "UPDATE "
                + "procesos "
                + "SET "
                + "nombre=?, info=? "
                + "WHERE "
                + "id=?";

        // Número de Registros Afectados
        int numReg = 0;

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                    Connection conn = ds.getConnection();
                    PreparedStatement ps = conn.prepareStatement(SQL)) {
                // Parametrizar Sentencia
                ps.setString(1, proceso.getNombre());
                ps.setString(2, proceso.getInfo());
                ps.setInt(3, proceso.getId());

                // Ejecutar Sentencia
                numReg = ps.executeUpdate();
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno: true | false
        return numReg == 1;
    }

    public Long contarProcesos() {
        // Número de Filas
        long filas = 0;

        // SQL
        String sql = ""
                + "SELECT "
                + "COUNT(*) "
                + "FROM "
                + "procesos";

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                    Connection conn = ds.getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql)) {
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

    public List<Proceso> obtenerPaginaProcesos(long indice, int longitud) {
        // SQL
        String sql = ""
                + "SELECT "
                + "* "
                + "FROM "
                + "procesos "
                + "LIMIT ?, ?";

        // Lista Vacía
        List<Proceso> procesos = new ArrayList<>();

        // Obtención del Contexto
        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                    Connection conn = ds.getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql)) {
                // Parametrizar Sentencia
                ps.setLong(1, indice);
                ps.setLong(2, longitud);

                // BD > Lista de Entidades
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        // Fila Actual > Campos 
                        int id = rs.getInt("id");
                        String nombre = rs.getString("nombre");
                        String info = rs.getString("info");

                        // Campos > Entidad
                        Proceso proceso = new Proceso(id, nombre, info);

                        // Entidad > Lista
                        procesos.add(proceso);
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno Lista
        return procesos;
    }
}

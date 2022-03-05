package org.japo.java.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
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

    // Logger
    private static final Logger logger = Logger.getLogger(DALPermisoPerfil.class.getName());

    // Nombre de la Base de datos
    private final String bd;

    public DALPermisoPerfil(HttpSession sesion) {
        bd = (String) sesion.getAttribute("bd");
    }

    public List<PermisoPerfil> listar() {
        // SQL
        String sql = ""
                + "SELECT "
                + "permisos_perfil.id AS id, "
                + "permisos_perfil.perfil AS perfil, "
                + "perfiles.info AS perfil_info, "
                + "permisos_perfil.proceso AS proceso, "
                + "procesos.info AS proceso_info, "
                + "permisos_perfil.info AS info "
                + "FROM "
                + "permisos_perfil "
                + "INNER JOIN "
                + "perfiles ON perfiles.id = permisos_perfil.perfil "
                + "INNER JOIN "
                + "procesos ON procesos.id = permisos_perfil.proceso";

        // Lista Vacía
        List<PermisoPerfil> permisos = new ArrayList<>();

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
                        int perfil = rs.getInt("perfil");
                        String perfilInfo = rs.getString("perfil_info");
                        int proceso = rs.getInt("proceso");
                        String procesoInfo = rs.getString("proceso_info");
                        String info = rs.getString("info");

                        // Campos > Entidad
                        PermisoPerfil permiso = new PermisoPerfil(id, perfil, perfilInfo, proceso, procesoInfo, info);

                        // Entidad > Lista
                        permisos.add(permiso);
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            logger.info(ex.getMessage());
        }

        // Retorno Lista
        return permisos;
    }

    public PermisoPerfil consultar(int id) {
        // SQL
        String sql = ""
                + "SELECT "
                + "permisos_perfil.id AS id, "
                + "permisos_perfil.perfil AS perfil, "
                + "perfiles.info AS perfil_info, "
                + "permisos_perfil.proceso AS proceso, "
                + "procesos.info AS proceso_info, "
                + "permisos_perfil.info AS info "
                + "FROM "
                + "permisos_perfil "
                + "INNER JOIN "
                + "perfiles ON perfiles.id = permisos_perfil.perfil "
                + "INNER JOIN "
                + "procesos ON procesos.id = permisos_perfil.proceso "
                + "WHERE "
                + "permisos_perfil.id=?";

        // Entidad
        PermisoPerfil permiso = null;

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
                        int perfil = rs.getInt("perfil");
                        String perfilInfo = rs.getString("perfil_info");
                        int proceso = rs.getInt("proceso");
                        String procesoInfo = rs.getString("proceso_info");
                        String info = rs.getString("info");

                        // Campos > Entidad
                        permiso = new PermisoPerfil(id, perfil, perfilInfo, proceso, procesoInfo, info);
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            logger.info(ex.getMessage());
        }

        // Retorno Entidad
        return permiso;
    }

    public List<PermisoPerfil> listar(int perfil) {
        // SQL
        String sql = ""
                + "SELECT "
                + "permisos_perfil.id AS id, "
                + "permisos_perfil.perfil AS perfil, "
                + "perfiles.info AS perfil_info, "
                + "permisos_perfil.proceso AS proceso, "
                + "procesos.info AS proceso_info, "
                + "permisos_perfil.info AS info "
                + "FROM "
                + "permisos_perfil "
                + "INNER JOIN "
                + "perfiles ON perfiles.id = permisos_perfil.perfil "
                + "INNER JOIN "
                + "procesos ON procesos.id = permisos_perfil.proceso "
                + "WHERE "
                + "permisos_perfil.perfil=?";

        // Lista Vacía
        List<PermisoPerfil> permisos = new ArrayList<>();

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                     Connection conn = ds.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
                // Parametrizar Sentencia
                ps.setInt(1, perfil);

                // BD > Lista de Entidades
                try ( ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        // Fila Actual > Campos 
                        int id = rs.getInt("id");
                        String perfilInfo = rs.getString("perfil_info");
                        int proceso = rs.getInt("proceso");
                        String procesoInfo = rs.getString("proceso_info");
                        String info = rs.getString("info");

                        // Campos > Entidad
                        PermisoPerfil permiso = new PermisoPerfil(id, perfil, perfilInfo, proceso, procesoInfo, info);

                        // Entidad > Lista
                        permisos.add(permiso);
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            logger.info(ex.getMessage());
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
                + "perfil, proceso, info"
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
                ps.setInt(1, permiso.getPerfil());
                ps.setInt(2, permiso.getProceso());
                ps.setString(3, permiso.getInfo());

                // Ejecutar Sentencia
                numReg = ps.executeUpdate();
            }
        } catch (NamingException | SQLException ex) {
            logger.info(ex.getMessage());
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
                     Connection conn = ds.getConnection();  PreparedStatement ps = conn.prepareStatement(SQL)) {
                // Parametrizar Sentencia
                ps.setInt(1, id);

                // Ejecutar Sentencia
                numReg = ps.executeUpdate();
            }
        } catch (NamingException | SQLException ex) {
            logger.info(ex.getMessage());
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
                     Connection conn = ds.getConnection();  PreparedStatement ps = conn.prepareStatement(SQL)) {
                // Parametrizar Sentencia
                ps.setInt(1, permiso.getProceso());
                ps.setInt(2, permiso.getPerfil());

                // Ejecutar Sentencia
                numReg = ps.executeUpdate();
            }
        } catch (NamingException | SQLException ex) {
            logger.info(ex.getMessage());
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
                + "perfil=?, proceso=?, info=? "
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
                ps.setInt(1, permiso.getPerfil());
                ps.setInt(2, permiso.getProceso());
                ps.setString(3, permiso.getInfo());
                ps.setInt(4, permiso.getId());

                // Ejecutar Sentencia
                numReg = ps.executeUpdate();
            }
        } catch (NamingException | SQLException ex) {
            logger.info(ex.getMessage());
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
                     Connection conn = ds.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
                try ( ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        filas = rs.getLong(1);
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            logger.info(ex.getMessage());
        }

        // Retorno: Filas Contadas
        return filas;
    }

    public List<PermisoPerfil> paginar(long indice, int longitud) {
        // SQL
        String sql = ""
                + "SELECT "
                + "permisos_perfil.id AS id, "
                + "permisos_perfil.perfil AS perfil, "
                + "perfiles.info AS perfil_info, "
                + "permisos_perfil.proceso AS proceso, "
                + "procesos.info AS proceso_info, "
                + "permisos_perfil.info AS info "
                + "FROM "
                + "permisos_perfil "
                + "INNER JOIN "
                + "perfiles ON perfiles.id = permisos_perfil.perfil "
                + "INNER JOIN "
                + "procesos ON procesos.id = permisos_perfil.proceso "
                + "LIMIT ?, ?";

        // Lista Vacía
        List<PermisoPerfil> permisos = new ArrayList<>();

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
                        int perfil = rs.getInt("perfil");
                        String perfilInfo = rs.getString("perfil_info");
                        int proceso = rs.getInt("proceso");
                        String procesoInfo = rs.getString("proceso_info");
                        String info = rs.getString("info");

                        // Campos > Entidad
                        PermisoPerfil permiso = new PermisoPerfil(id, perfil, perfilInfo, proceso, procesoInfo, info);

                        // Entidad > Lista
                        permisos.add(permiso);
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            logger.info(ex.getMessage());
        }

        // Retorno Lista
        return permisos;
    }
}

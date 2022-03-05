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
import org.japo.java.entities.PermisoUsuario;
import org.japo.java.libraries.UtilesServlet;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class DALPermisoUsuario {

    // Logger
    private static final Logger logger = Logger.getLogger(DALPermisoUsuario.class.getName());

    // Nombre de la Base de datos
    private final String bd;

    public DALPermisoUsuario(HttpSession sesion) {
        bd = (String) sesion.getAttribute("bd");
    }

    public List<PermisoUsuario> listar() {
        // SQL
        String sql = ""
                + "SELECT "
                + "permisos_usuario.id AS id, "
                + "permisos_usuario.usuario AS usuario, "
                + "usuarios.user AS usuario_name, "
                + "permisos_usuario.proceso AS proceso, "
                + "procesos.info AS proceso_info, "
                + "permisos_usuario.info AS info "
                + "FROM "
                + "permisos_usuario "
                + "INNER JOIN "
                + "usuarios ON usuarios.id = permisos_usuario.usuario "
                + "INNER JOIN "
                + "procesos ON procesos.id = permisos_usuario.proceso";

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
                        int usuario = rs.getInt("usuario");
                        String usuarioName = rs.getString("usuario_name");
                        int proceso = rs.getInt("proceso");
                        String procesoInfo = rs.getString("proceso_info");
                        String info = rs.getString("info");

                        // Campos > Entidad
                        PermisoUsuario permiso = new PermisoUsuario(id, usuario, usuarioName, proceso, procesoInfo, info);

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

    public PermisoUsuario consultar(int id) {
        // SQL
        String sql = ""
                + "SELECT "
                + "permisos_usuario.id AS id, "
                + "permisos_usuario.usuario AS usuario, "
                + "usuarios.user AS usuario_name, "
                + "permisos_usuario.proceso AS proceso, "
                + "procesos.info AS proceso_info, "
                + "permisos_usuario.info AS info "
                + "FROM "
                + "permisos_usuario "
                + "INNER JOIN "
                + "usuarios ON usuarios.id = permisos_usuario.usuario "
                + "INNER JOIN "
                + "procesos ON procesos.id = permisos_usuario.proceso "
                + "WHERE "
                + "permisos_usuario.id=?";

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
                        int usuario = rs.getInt("usuario");
                        String usuarioName = rs.getString("usuario_name");
                        int proceso = rs.getInt("proceso");
                        String procesoInfo = rs.getString("proceso_info");
                        String info = rs.getString("info");

                        // Campos > Entidad
                        permiso = new PermisoUsuario(id, usuario, usuarioName, proceso, procesoInfo, info);
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            logger.info(ex.getMessage());
        }

        // Retorno Entidad
        return permiso;
    }

    public List<PermisoUsuario> listar(int usuario) {
        // SQL
        String sql = ""
                + "SELECT "
                + "permisos_usuario.id AS id, "
                + "permisos_usuario.usuario AS usuario, "
                + "usuarios.user AS usuario_name, "
                + "permisos_usuario.proceso AS proceso, "
                + "procesos.info AS proceso_info, "
                + "permisos_usuario.info AS info "
                + "FROM "
                + "permisos_usuario "
                + "INNER JOIN "
                + "usuarios ON usuarios.id = permisos_usuario.usuario "
                + "INNER JOIN "
                + "procesos ON procesos.id = permisos_usuario.proceso "
                + "WHERE "
                + "permisos_usuario.usuario=?";

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
                        String usuarioName = rs.getString("usuario_name");
                        int proceso = rs.getInt("proceso");
                        String procesoInfo = rs.getString("proceso_info");
                        String info = rs.getString("info");

                        // Campos > Entidad
                        PermisoUsuario permiso = new PermisoUsuario(id, usuario, usuarioName, proceso, procesoInfo, info);

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

    public boolean insertar(PermisoUsuario permiso) {
        // SQL
        final String SQL = ""
                + "INSERT INTO "
                + "permisos_usuario "
                + "("
                + "usuario, proceso, info"
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
                ps.setInt(1, permiso.getUsuario());
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
            logger.info(ex.getMessage());
        }

        // Retorno: true | false
        return numReg == 1;
    }

    // Este método se utiliza cuando no se conoce la Id
    // pero si el proceso y el usuario 
    public boolean borrar(PermisoUsuario permiso) {
        // SQL
        final String SQL
                = "DELETE FROM "
                + "permisos_usuario "
                + "WHERE "
                + "usuario=? AND proceso=?";

        // Número de Registros Afectados
        int numReg = 0;

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                     Connection conn = ds.getConnection();  PreparedStatement ps = conn.prepareStatement(SQL)) {
                // Parametrizar Sentencia
                ps.setInt(1, permiso.getUsuario());
                ps.setInt(2, permiso.getProceso());

                // Ejecutar Sentencia
                numReg = ps.executeUpdate();
            }
        } catch (NamingException | SQLException ex) {
            logger.info(ex.getMessage());
        }

        // Retorno: true | false
        return numReg == 1;
    }

    public boolean modificar(PermisoUsuario permiso) {
        // SQL
        final String SQL = ""
                + "UPDATE "
                + "permisos_usuario "
                + "SET "
                + "usuario=?, proceso=?, info=? "
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
                ps.setInt(1, permiso.getUsuario());
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
            logger.info(ex.getMessage());
        }

        // Retorno: Filas Contadas
        return filas;
    }

    public List<PermisoUsuario> paginar(long indice, int longitud) {
        // SQL
        String sql = ""
                + "SELECT "
                + "permisos_usuario.id AS id, "
                + "permisos_usuario.usuario AS usuario, "
                + "usuarios.user AS usuario_name, "
                + "permisos_usuario.proceso AS proceso, "
                + "procesos.info AS proceso_info, "
                + "permisos_usuario.info AS info "
                + "FROM "
                + "permisos_usuario "
                + "INNER JOIN "
                + "usuarios ON usuarios.id = permisos_usuario.usuario "
                + "INNER JOIN "
                + "procesos ON procesos.id = permisos_usuario.proceso "
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
                        int usuario = rs.getInt("usuario");
                        String usuarioName = rs.getString("usuario_name");
                        int proceso = rs.getInt("proceso");
                        String procesoInfo = rs.getString("proceso_info");
                        String info = rs.getString("info");

                        // Campos > Entidad
                        PermisoUsuario permiso = new PermisoUsuario(id, usuario, usuarioName, proceso, procesoInfo, info);

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

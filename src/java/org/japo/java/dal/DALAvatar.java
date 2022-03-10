/* 
 * Copyright 2022 JAPO Labs - japolabs@gmail.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import org.japo.java.entities.Avatar;
import org.japo.java.libraries.UtilesServlet;

/**
 *
 * @author José A. Pacheco Ondoño - japolabs@gmail.com
 */
public final class DALAvatar {

    // Campos
    private final HttpSession sesion;

    // Nombre de la Base de datos
    private final String bd;

    public DALAvatar(HttpSession sesion) {
        this.sesion = sesion;

        bd = (String) sesion.getAttribute("bd");
    }

    public List<Avatar> listar() {
        // SQL
        String sql = ""
                + "SELECT "
                + "* "
                + "FROM "
                + "avatares";

        // Lista Vacía
        List<Avatar> avatares = new ArrayList<>();

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
                        String nombre = rs.getString("nombre");
                        String imagen = rs.getString("imagen");

                        // Campos > Entidad
                        Avatar avatar = new Avatar(id, nombre, imagen);

                        // Entidad > Lista
                        avatares.add(avatar);
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno Lista
        return avatares;
    }

    public Avatar consultar(int id) {
        // SQL
        String sql = ""
                + "SELECT "
                + "* "
                + "FROM "
                + "avatares "
                + "WHERE "
                + "id=?";

        // Entidad
        Avatar avatar = null;

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
                        String nombre = rs.getString("nombre");
                        String imagen = rs.getString("imagen");

                        // Campos > Entidad
                        avatar = new Avatar(id, nombre, imagen);
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno Entidad
        return avatar;
    }

    public Avatar consultar(String nombre) {
        // SQL
        String sql = ""
                + "SELECT "
                + "* "
                + "FROM "
                + "avatares "
                + "WHERE "
                + "nombre=?";

        // Entidad
        Avatar avatar = null;

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                    Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                // Parametrizar Sentencia
                ps.setString(1, nombre);

                // BD > Lista de Entidades
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Fila Actual > Campos 
                        int id = rs.getInt("id");
                        String imagen = rs.getString("imagen");

                        // Campos > Entidad
                        avatar = new Avatar(id, nombre, imagen);
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno Entidad
        return avatar;
    }

    public boolean insertar(Avatar avatar) {
        // SQL
        String sql = ""
                + "INSERT INTO "
                + "avatares "
                + "("
                + "nombre, imagen"
                + ") "
                + "VALUES (?, ?)";

        // Número de registros afectados
        int numReg = 0;

        // Obtención del Contexto
        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                    Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                // Parametrizar Sentencia
                ps.setString(1, avatar.getNombre());
                ps.setString(2, avatar.getImagen());

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
        String sql = ""
                + "DELETE FROM "
                + "avatares "
                + "WHERE id=?";

        // Número de registros afectados
        int numReg = 0;

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                    Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
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

    public boolean modificar(Avatar avatar) {
        // SQL
        String sql = ""
                + "UPDATE "
                + "avatares "
                + "SET "
                + "nombre=?, imagen=? "
                + "WHERE "
                + "id=?";

        // Número de Registros Afectados
        int numReg = 0;

        try {
            // Contexto Inicial > DataSource
            DataSource ds = UtilesServlet.obtenerDataSource(bd);

            try (
                    Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                // Parametrizar Sentencia
                ps.setString(1, avatar.getNombre());
                ps.setString(2, avatar.getImagen());
                ps.setInt(3, avatar.getId());

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
                + "avatares";

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

    public List<Avatar> paginar(long indice, int longitud) {
        // SQL
        String sql = ""
                + "SELECT "
                + "* "
                + "FROM "
                + "avatares "
                + "LIMIT ?, ?";

        // Lista Vacía
        List<Avatar> avatares = new ArrayList<>();

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
                        String nombre = rs.getString("nombre");
                        String imagen = rs.getString("imagen");

                        // Campos > Entidad
                        Avatar avatar = new Avatar(id, nombre, imagen);

                        // Entidad > Lista
                        avatares.add(avatar);
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }

        // Retorno Lista
        return avatares;
    }
}

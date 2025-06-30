package utp.edu.pe.isi.dwi.sistematickets.dao;

import utp.edu.pe.isi.dwi.sistematickets.dto.RolDTO;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.*;
import java.util.*;

@ApplicationScoped
public class RolDAO {

    private final String url = "jdbc:postgresql://my-db-instance.cytmkkegmp14.us-east-1.rds.amazonaws.com:5432/db-dev-web";
    private final String user = "userutp";
    private final String pass = "VU4B5np-8EyU";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<RolDTO> listarRoles() {
        List<RolDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM Rol";
        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                RolDTO r = new RolDTO();
                r.setIdRol(rs.getInt("id_rol"));
                r.setNombreRol(rs.getString("nombre_rol"));
                lista.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void registrarRol(RolDTO r) {
        String sql = "INSERT INTO Rol(nombre_rol) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getNombreRol());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarRol(RolDTO r) {
        String sql = "UPDATE Rol SET nombre_rol=? WHERE id_rol=?";
        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getNombreRol());
            ps.setInt(2, r.getIdRol());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarRol(int idRol) {
        String sql = "DELETE FROM Rol WHERE id_rol=?";
        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idRol);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

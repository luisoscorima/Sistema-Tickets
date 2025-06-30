package utp.edu.pe.isi.dwi.sistematickets.dao;

import utp.edu.pe.isi.dwi.sistematickets.dto.ColaboradorDTO;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.*;
import java.util.*;

@ApplicationScoped
public class ColaboradorDAO {

    private final String url = "jdbc:postgresql://my-db-instance.cytmkkegmp14.us-east-1.rds.amazonaws.com:5432/db-dev-web";
    private final String user = "userutp";
    private final String pass = "VU4B5np-8EyU";

    // BLOQUE ESTÁTICO PARA EL DRIVER
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<ColaboradorDTO> listarColaboradores() {
        List<ColaboradorDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM Colaborador ORDER BY id_colaborador ASC";
        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ColaboradorDTO c = new ColaboradorDTO();
                c.setIdColaborador(rs.getInt("id_colaborador"));
                c.setNombreColab(rs.getString("nombre_colab"));
                c.setApellidoColab(rs.getString("apellido_colab"));
                c.setEmailColab(rs.getString("email_colab"));
                c.setPasswordColab(rs.getString("password_colab"));
                c.setIdRol(rs.getInt("id_rol"));
                c.setEstadoColab(rs.getBoolean("estado_colab"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void registrarColaborador(ColaboradorDTO c) {
        String sql = "INSERT INTO Colaborador (nombre_colab, apellido_colab, email_colab, password_colab, id_rol, estado_colab) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombreColab());
            ps.setString(2, c.getApellidoColab());
            ps.setString(3, c.getEmailColab());
            ps.setString(4, c.getPasswordColab());
            ps.setInt(5, c.getIdRol());
            ps.setBoolean(6, c.getEstadoColab() != null ? c.getEstadoColab() : true);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarColaborador(ColaboradorDTO c) {
        String sql = "UPDATE Colaborador SET nombre_colab=?, apellido_colab=?, email_colab=?, password_colab=?, id_rol=?, estado_colab=? WHERE id_colaborador=?";
        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombreColab());
            ps.setString(2, c.getApellidoColab());
            ps.setString(3, c.getEmailColab());
            ps.setString(4, c.getPasswordColab());
            ps.setInt(5, c.getIdRol());
            ps.setBoolean(6, c.getEstadoColab());
            ps.setInt(7, c.getIdColaborador());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cambiarEstadoColaborador(int idColaborador, boolean estado) {
        String sql = "UPDATE Colaborador SET estado_colab=? WHERE id_colaborador=?";
        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, estado);
            ps.setInt(2, idColaborador);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

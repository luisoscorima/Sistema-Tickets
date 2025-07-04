package utp.edu.pe.isi.dwi.sistematickets.dao;

import utp.edu.pe.isi.dwi.sistematickets.dto.ClienteDTO;
import utp.edu.pe.isi.dwi.sistematickets.dto.ColaboradorDTO;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.*;

@ApplicationScoped
public class LoginDAO {
    // Usa los mismos datos de conexión
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

    public ClienteDTO loginCliente(String email, String password) {
        String sql = "SELECT * FROM Cliente WHERE email_cliente=? AND password_cliente=? AND estado_cliente='A'";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password); // Si usas hash, cámbialo aquí
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ClienteDTO c = new ClienteDTO();
                    c.setIdCliente(rs.getInt("id_cliente"));
                    c.setNombreCliente(rs.getString("nombre_cliente"));
                    //... rellena el resto
                    return c;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public ColaboradorDTO loginColaborador(String email, String password) {
        String sql = "SELECT * FROM Colaborador WHERE email_colab=? AND password_colab=? AND estado_colab=true";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ColaboradorDTO c = new ColaboradorDTO();
                    c.setIdColaborador(rs.getInt("id_colaborador"));
                    c.setNombreColab(rs.getString("nombre_colab"));
                    c.setApellidoColab(rs.getString("apellido_colab"));
                    //... rellena el resto
                    return c;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}

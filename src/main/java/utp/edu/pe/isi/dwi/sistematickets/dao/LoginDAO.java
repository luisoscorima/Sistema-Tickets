package utp.edu.pe.isi.dwi.sistematickets.dao;

import utp.edu.pe.isi.dwi.sistematickets.dto.ClienteDTO;
import utp.edu.pe.isi.dwi.sistematickets.dto.ColaboradorDTO;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.*;

@ApplicationScoped
public class LoginDAO {

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

    /* ---------- CLIENTE ---------- */
    public ClienteDTO loginCliente(String email, String password) {
        String sql
            = "SELECT id_cliente, nombre_cliente, apellido_cliente, email_cliente, id_empresa "
            + "FROM   Cliente "
            + "WHERE  email_cliente   = ? "
            + "  AND  password_cliente = ? "
            + "  AND  estado_cliente   = 'A'";
        try (Connection c = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                ClienteDTO cli = new ClienteDTO();
                cli.setIdCliente(rs.getInt("id_cliente"));
                cli.setNombreCliente(rs.getString("nombre_cliente"));
                cli.setApellidoCliente(rs.getString("apellido_cliente"));
                cli.setEmailCliente(rs.getString("email_cliente"));
                // <-- Aquí capturamos id_empresa
                cli.setIdEmpresa(rs.getObject("id_empresa") != null
                                 ? rs.getInt("id_empresa") : null);
                return cli;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* ---------- COLABORADOR (incluye Admin) ---------- */
    public ColaboradorDTO loginColaborador(String email, String password) {
        String sql
            = "SELECT c.id_colaborador, c.nombre_colab, c.apellido_colab, "
            + "       c.email_colab, c.password_colab, c.id_rol, "
            + "       r.nombre_rol, c.estado_colab "
            + "FROM   Colaborador c "
            + "JOIN   Rol r ON r.id_rol = c.id_rol "
            + "WHERE  c.email_colab   = ? "
            + "  AND  c.password_colab = ? "
            + "  AND  c.estado_colab   = true";
        try (Connection c = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                ColaboradorDTO col = new ColaboradorDTO();
                col.setIdColaborador(rs.getInt("id_colaborador"));
                col.setNombreColab(rs.getString("nombre_colab"));
                col.setApellidoColab(rs.getString("apellido_colab"));
                col.setEmailColab(rs.getString("email_colab"));
                col.setPasswordColab(rs.getString("password_colab"));
                col.setIdRol(rs.getInt("id_rol"));
                col.setNombreRol(rs.getString("nombre_rol"));
                col.setEstadoColab(rs.getBoolean("estado_colab"));
                return col;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

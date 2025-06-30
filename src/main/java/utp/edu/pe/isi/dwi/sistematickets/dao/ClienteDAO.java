package utp.edu.pe.isi.dwi.sistematickets.dao;

import utp.edu.pe.isi.dwi.sistematickets.dto.ClienteDTO;
import utp.edu.pe.isi.dwi.sistematickets.enums.EstadoEnum;
import utp.edu.pe.isi.dwi.sistematickets.enums.TipoClienteEnum;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.*;
import java.util.*;

@ApplicationScoped
public class ClienteDAO {

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

    public List<ClienteDTO> listarClientes() {
        List<ClienteDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM Cliente ORDER BY id_cliente ASC";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ClienteDTO c = new ClienteDTO();
                c.setIdCliente(rs.getInt("id_cliente"));
                c.setNombreCliente(rs.getString("nombre_cliente"));
                c.setApellidoCliente(rs.getString("apellido_cliente"));
                c.setEmailCliente(rs.getString("email_cliente"));
                c.setPasswordCliente(rs.getString("password_cliente"));
                c.setTipoCliente(TipoClienteEnum.valueOf(rs.getString("tipo_cliente")));
                c.setEstadoCliente(EstadoEnum.valueOf(rs.getString("estado_cliente")));
                c.setIdEmpresa(rs.getObject("id_empresa") != null ? rs.getInt("id_empresa") : null);
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void registrarCliente(ClienteDTO c) {
        String sql = "INSERT INTO Cliente (nombre_cliente, apellido_cliente, email_cliente, password_cliente, tipo_cliente, estado_cliente, id_empresa) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombreCliente());
            ps.setString(2, c.getApellidoCliente());
            ps.setString(3, c.getEmailCliente());
            ps.setString(4, c.getPasswordCliente());
            ps.setObject(5, c.getTipoCliente().name(), java.sql.Types.OTHER);
            ps.setObject(6, c.getEstadoCliente() != null ? c.getEstadoCliente().name() : "A", java.sql.Types.OTHER);
            if (c.getIdEmpresa() != null) {
                ps.setInt(7, c.getIdEmpresa());
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarCliente(ClienteDTO c) {
        String sql = "UPDATE Cliente SET nombre_cliente=?, apellido_cliente=?, email_cliente=?, password_cliente=?, tipo_cliente=?, estado_cliente=?, id_empresa=? WHERE id_cliente=?";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombreCliente());
            ps.setString(2, c.getApellidoCliente());
            ps.setString(3, c.getEmailCliente());
            ps.setString(4, c.getPasswordCliente());
            ps.setObject(5, c.getTipoCliente().name(), java.sql.Types.OTHER);
            ps.setObject(6, c.getEstadoCliente().name(), java.sql.Types.OTHER);
            if (c.getIdEmpresa() != null) {
                ps.setInt(7, c.getIdEmpresa());
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            ps.setInt(8, c.getIdCliente());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cambiarEstadoCliente(int idCliente, EstadoEnum estado) {
        String sql = "UPDATE Cliente SET estado_cliente=? WHERE id_cliente=?";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, estado.name(), java.sql.Types.OTHER);
            ps.setInt(2, idCliente);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package utp.edu.pe.isi.dwi.sistematickets.dao;

import utp.edu.pe.isi.dwi.sistematickets.dto.EmpresaDTO;
import utp.edu.pe.isi.dwi.sistematickets.enums.EstadoEnum;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class EmpresaDAO {

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

    public List<EmpresaDTO> listarEmpresas() {
        List<EmpresaDTO> lista = new ArrayList<>();
        String sql = "SELECT id_empresa, ruc, razon_social, direccion, telefono, estado_empresa FROM Empresa ORDER BY id_empresa";
        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                EmpresaDTO e = new EmpresaDTO();
                e.setIdEmpresa(rs.getInt("id_empresa"));
                e.setRuc(rs.getString("ruc"));
                e.setRazonSocial(rs.getString("razon_social"));
                e.setDireccion(rs.getString("direccion"));
                e.setTelefono(rs.getString("telefono"));
                e.setEstadoEmpresa(EstadoEnum.valueOf(rs.getString("estado_empresa")));
                lista.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public void registrarEmpresa(EmpresaDTO empresa) {
        String sql = "INSERT INTO Empresa(ruc, razon_social, direccion, telefono, estado_empresa) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empresa.getRuc());
            ps.setString(2, empresa.getRazonSocial());
            ps.setString(3, empresa.getDireccion());
            ps.setString(4, empresa.getTelefono());
            ps.setObject(5, empresa.getEstadoEmpresa() != null ? empresa.getEstadoEmpresa().name() : "A", java.sql.Types.OTHER);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarEmpresa(EmpresaDTO empresa) {
        String sql = "UPDATE Empresa SET ruc=?, razon_social=?, direccion=?, telefono=?, estado_empresa=? WHERE id_empresa=?";
        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empresa.getRuc());
            ps.setString(2, empresa.getRazonSocial());
            ps.setString(3, empresa.getDireccion());
            ps.setString(4, empresa.getTelefono());
            ps.setObject(5, empresa.getEstadoEmpresa() != null ? empresa.getEstadoEmpresa().name() : "A", java.sql.Types.OTHER);
            ps.setInt(6, empresa.getIdEmpresa());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cambiarEstadoEmpresa(int idEmpresa, EstadoEnum nuevoEstado) {
        String sql = "UPDATE Empresa SET estado_empresa=? WHERE id_empresa=?";
        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, nuevoEstado.name(), java.sql.Types.OTHER);
            ps.setInt(2, idEmpresa);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

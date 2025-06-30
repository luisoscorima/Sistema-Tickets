package utp.edu.pe.isi.dwi.sistematickets.dao;

import utp.edu.pe.isi.dwi.sistematickets.dto.AplicacionDTO;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.*;
import java.util.*;

@ApplicationScoped
public class AplicacionDAO {
    private final String url = "jdbc:postgresql://my-db-instance.cytmkkegmp14.us-east-1.rds.amazonaws.com:5432/db-dev-web";
    private final String user = "userutp";
    private final String pass = "VU4B5np-8EyU";

    static {
        try { Class.forName("org.postgresql.Driver"); }
        catch (ClassNotFoundException e) { e.printStackTrace(); }
    }

    public List<AplicacionDTO> listarAplicaciones() {
        List<AplicacionDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM Aplicacion";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                AplicacionDTO a = new AplicacionDTO();
                a.setIdAplicacion(rs.getInt("id_aplicacion"));
                a.setIdEmpresa(rs.getInt("id_empresa"));
                a.setTipoAplicacion(rs.getString("tipo_aplicacion"));
                lista.add(a);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public void registrarAplicacion(AplicacionDTO a) {
        String sql = "INSERT INTO Aplicacion(id_empresa, tipo_aplicacion) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, a.getIdEmpresa());
            ps.setString(2, a.getTipoAplicacion());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void actualizarAplicacion(AplicacionDTO a) {
        String sql = "UPDATE Aplicacion SET id_empresa=?, tipo_aplicacion=? WHERE id_aplicacion=?";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, a.getIdEmpresa());
            ps.setString(2, a.getTipoAplicacion());
            ps.setInt(3, a.getIdAplicacion());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void eliminarAplicacion(int idAplicacion) {
        String sql = "DELETE FROM Aplicacion WHERE id_aplicacion=?";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAplicacion);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}

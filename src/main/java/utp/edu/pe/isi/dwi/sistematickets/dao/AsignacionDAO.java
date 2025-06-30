package utp.edu.pe.isi.dwi.sistematickets.dao;

import utp.edu.pe.isi.dwi.sistematickets.dto.AsignacionDTO;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class AsignacionDAO {

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

    public List<AsignacionDTO> listarAsignacionesPorSolicitud(int idSolicitud) {
        List<AsignacionDTO> lista = new ArrayList<>();
        String sql = "SELECT a.*, c.nombre_colab AS nombreColaborador "
                + "FROM Asignacion a "
                + "LEFT JOIN Colaborador c ON c.id_colaborador = a.id_colaborador "
                + "WHERE a.id_solicitud = ?";
        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSolicitud);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AsignacionDTO dto = new AsignacionDTO();
                    dto.setIdAsignacion(rs.getInt("id_asignacion"));
                    dto.setIdSolicitud(rs.getInt("id_solicitud"));
                    dto.setIdColaborador(rs.getObject("id_colaborador") != null ? rs.getInt("id_colaborador") : null);
                    dto.setFechaAsignacion(rs.getTimestamp("fecha_asignacion"));
                    dto.setEsCoordinador(rs.getBoolean("es_coordinador"));
                    dto.setNombreColaborador(rs.getString("nombreColaborador"));
                    lista.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void asignarColaborador(AsignacionDTO dto) {
        String sql = "INSERT INTO Asignacion (id_solicitud, id_colaborador, es_coordinador) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dto.getIdSolicitud());
            ps.setInt(2, dto.getIdColaborador());
            ps.setBoolean(3, dto.getEsCoordinador() != null ? dto.getEsCoordinador() : false);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Integer obtenerIdAsignacionPorSolicitudYColaborador(int idSolicitud, int idColaborador) {
        String sql = "SELECT id_asignacion FROM Asignacion WHERE id_solicitud=? AND id_colaborador=?";
        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSolicitud);
            ps.setInt(2, idColaborador);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_asignacion");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Métodos adicionales para actualizar o eliminar asignación si los necesitas
}

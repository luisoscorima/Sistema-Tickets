package utp.edu.pe.isi.dwi.sistematickets.dao;

import utp.edu.pe.isi.dwi.sistematickets.dto.ActividadDTO;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.*;
import java.util.*;

@ApplicationScoped
public class ActividadDAO {

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

    public List<ActividadDTO> listarActividadesPorSolicitud(int idSolicitud) {
        List<ActividadDTO> lista = new ArrayList<>();
        String sql = "SELECT act.*, colab.nombre_colab, colab.apellido_colab, asg.id_colaborador "
                + "FROM Actividad act "
                + "JOIN Asignacion asg ON act.id_asignacion = asg.id_asignacion "
                + "LEFT JOIN Colaborador colab ON asg.id_colaborador = colab.id_colaborador "
                + "WHERE asg.id_solicitud = ? "
                + "ORDER BY act.inicio ASC";
        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSolicitud);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ActividadDTO dto = new ActividadDTO();
                    dto.setIdActividad(rs.getInt("id_actividad"));
                    dto.setIdAsignacion(rs.getInt("id_asignacion"));
                    dto.setDescripcion(rs.getString("descripcion"));
                    dto.setInicio(rs.getTimestamp("inicio"));
                    dto.setFin(rs.getTimestamp("fin"));
                    dto.setEsFinal(rs.getBoolean("es_final"));
                    dto.setIdColaborador(rs.getObject("id_colaborador") != null ? rs.getInt("id_colaborador") : null);
                    dto.setNombreColaborador(
                            rs.getString("nombre_colab") + " " + rs.getString("apellido_colab")
                    );
                    lista.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void registrarActividad(ActividadDTO dto) {
        String sql = "INSERT INTO Actividad (id_asignacion, descripcion, inicio, es_final) VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dto.getIdAsignacion());
            ps.setString(2, dto.getDescripcion());
            ps.setBoolean(3, dto.getEsFinal() != null ? dto.getEsFinal() : false);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Puedes agregar update/eliminar si lo necesitas
}

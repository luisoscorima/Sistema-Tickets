package utp.edu.pe.isi.dwi.sistematickets.dao;

import utp.edu.pe.isi.dwi.sistematickets.dto.TicketDTO;
import utp.edu.pe.isi.dwi.sistematickets.enums.EstadoSolicitudEnum;
import utp.edu.pe.isi.dwi.sistematickets.enums.PrioridadEnum;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.*;
import java.util.*;

@ApplicationScoped
public class TicketDAO {

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

    public List<TicketDTO> listarTicketsPorCliente(int idCliente) {
        List<TicketDTO> lista = new ArrayList<>();
        String sql = "SELECT s.*, ts.descripcion AS nombreTipoSolicitud, a.tipo_aplicacion AS nombreAplicacion "
                + "FROM Solicitud s "
                + "LEFT JOIN tipoSolicitud ts ON ts.id_tipoSolicitud = s.id_tipoSolicitud "
                + "LEFT JOIN Aplicacion a ON a.id_aplicacion = s.id_aplicacion "
                + "WHERE s.id_cliente = ? ORDER BY s.fecha_creacion DESC";
        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TicketDTO t = new TicketDTO();
                    t.setIdSolicitud(rs.getInt("id_solicitud"));
                    t.setIdCliente(rs.getInt("id_cliente"));
                    t.setIdTipoSolicitud(rs.getInt("id_tipoSolicitud"));
                    t.setIdAplicacion(rs.getObject("id_aplicacion") != null ? rs.getInt("id_aplicacion") : null);
                    t.setAsunto(rs.getString("asunto"));
                    t.setMotivo(rs.getString("motivo"));
                    t.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                    t.setEstado(EstadoSolicitudEnum.valueOf(rs.getString("estado")));
                    t.setPrioridad(PrioridadEnum.valueOf(rs.getString("prioridad")));
                    t.setFechaCierre(rs.getTimestamp("fecha_cierre"));
                    t.setNombreTipoSolicitud(rs.getString("nombreTipoSolicitud"));
                    t.setNombreAplicacion(rs.getString("nombreAplicacion"));
                    lista.add(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<TicketDTO> listarTicketsPorColaborador(int idColaborador) {
        List<TicketDTO> lista = new ArrayList<>();
        String sql = "SELECT s.*, c.nombre_cliente, ts.descripcion AS nombreTipoSolicitud, a.tipo_aplicacion AS nombreAplicacion "
                + "FROM Solicitud s "
                + "JOIN Asignacion agn ON agn.id_solicitud = s.id_solicitud "
                + "JOIN Cliente c ON c.id_cliente = s.id_cliente "
                + "LEFT JOIN tipoSolicitud ts ON ts.id_tipoSolicitud = s.id_tipoSolicitud "
                + "LEFT JOIN Aplicacion a ON a.id_aplicacion = s.id_aplicacion "
                + "WHERE agn.id_colaborador = ? ORDER BY s.fecha_creacion DESC";
        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idColaborador);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TicketDTO t = new TicketDTO();
                    t.setIdSolicitud(rs.getInt("id_solicitud"));
                    t.setIdCliente(rs.getInt("id_cliente"));
                    t.setIdTipoSolicitud(rs.getInt("id_tipoSolicitud"));
                    t.setIdAplicacion(rs.getObject("id_aplicacion") != null ? rs.getInt("id_aplicacion") : null);
                    t.setAsunto(rs.getString("asunto"));
                    t.setMotivo(rs.getString("motivo"));
                    t.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                    t.setEstado(EstadoSolicitudEnum.valueOf(rs.getString("estado")));
                    t.setPrioridad(PrioridadEnum.valueOf(rs.getString("prioridad")));
                    t.setFechaCierre(rs.getTimestamp("fecha_cierre"));
                    t.setNombreCliente(rs.getString("nombre_cliente"));
                    t.setNombreTipoSolicitud(rs.getString("nombreTipoSolicitud"));
                    t.setNombreAplicacion(rs.getString("nombreAplicacion"));
                    lista.add(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Registrar Ticket (Cliente)
    public void registrarTicket(TicketDTO t) {
        String sql = "INSERT INTO Solicitud (id_cliente, id_tipoSolicitud, id_aplicacion, asunto, motivo, estado, prioridad) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, t.getIdCliente());
            ps.setInt(2, t.getIdTipoSolicitud());
            if (t.getIdAplicacion() != null) {
                ps.setInt(3, t.getIdAplicacion());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setString(4, t.getAsunto());
            ps.setString(5, t.getMotivo());
            ps.setString(6, t.getEstado().name());
            ps.setString(7, t.getPrioridad().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Actualizar Ticket (Cliente)
    public void actualizarTicket(TicketDTO t) {
        String sql = "UPDATE Solicitud SET asunto=?, motivo=?, prioridad=? WHERE id_solicitud=?";
        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getAsunto());
            ps.setString(2, t.getMotivo());
            ps.setString(3, t.getPrioridad().name());
            ps.setInt(4, t.getIdSolicitud());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cambiar Estado (ej: cerrar por cliente)
    public void cerrarTicket(int idSolicitud) {
        String sql = "UPDATE Solicitud SET estado = 'S', fecha_cierre=NOW() WHERE id_solicitud=?";
        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSolicitud);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarTicketPorColaborador(TicketDTO t) {
        String sql = "UPDATE Solicitud SET motivo=?, prioridad=?::prioridad_enum, estado=?::estado_solicitud_enum WHERE id_solicitud=?";
        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getMotivo());
            ps.setString(2, t.getPrioridad().name());  // PrioridadEnum
            ps.setString(3, t.getEstado().name());     // EstadoSolicitudEnum
            ps.setInt(4, t.getIdSolicitud());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarEstadoTicket(int idSolicitud, EstadoSolicitudEnum estado) {
        String sql = "UPDATE Solicitud SET estado=?::estado_solicitud_enum WHERE id_solicitud=?";
        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado.name());
            ps.setInt(2, idSolicitud);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Puedes añadir más métodos para uso del colaborador aquí...
}

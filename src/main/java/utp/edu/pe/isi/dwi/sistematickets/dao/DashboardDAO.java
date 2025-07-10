package utp.edu.pe.isi.dwi.sistematickets.dao;

import jakarta.enterprise.context.ApplicationScoped;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utp.edu.pe.isi.dwi.sistematickets.dto.TicketDTO;
import utp.edu.pe.isi.dwi.sistematickets.enums.EstadoSolicitudEnum;
import utp.edu.pe.isi.dwi.sistematickets.enums.PrioridadEnum;

/**
 * DAO del panel de control.
 *  ▸ Se añadió el *cast* "::estado_solicitud_enum" en TODAS las sentencias
 *    que comparan el parámetro «estado» contra la columna enum,
 *    evitando el error “operator does not exist: enum = character varying”.
 */
@ApplicationScoped
public class DashboardDAO {

    // ⚠️ Lleva estas credenciales a un DataSource JNDI o a variables de entorno
    private final String url  = "jdbc:postgresql://my-db-instance.cytmkkegmp14.us-east-1.rds.amazonaws.com:5432/db-dev-web";
    private final String user = "userutp";
    private final String pass = "VU4B5np-8EyU";

    /* ---------- LISTADOS ---------- */

    public List<TicketDTO> ticketsPorEstado(String estado) {
        List<TicketDTO> lista = new ArrayList<>();
        String sql =
            "SELECT s.*, c.nombre_cliente, ts.descripcion AS nombreTipoSolicitud, a.tipo_aplicacion AS nombreAplicacion " +
            "FROM   Solicitud s " +
            "LEFT  JOIN Cliente      c  ON c.id_cliente   = s.id_cliente " +
            "LEFT  JOIN tipoSolicitud ts ON ts.id_tipoSolicitud = s.id_tipoSolicitud " +
            "LEFT  JOIN Aplicacion    a  ON a.id_aplicacion    = s.id_aplicacion " +
            "WHERE  s.estado = ?::estado_solicitud_enum " +                 // ← cast
            "ORDER BY s.fecha_creacion DESC";

        ejecutarQueryTickets(lista, sql, ps -> ps.setString(1, estado));
        return lista;
    }

    public List<TicketDTO> ticketsPorEstadoYColaborador(String estado, int idColab) {
        List<TicketDTO> lista = new ArrayList<>();
        String sql =
            "SELECT s.*, c.nombre_cliente, ts.descripcion AS nombreTipoSolicitud, a.tipo_aplicacion AS nombreAplicacion " +
            "FROM   Solicitud  s " +
            "JOIN   Asignacion agn ON agn.id_solicitud = s.id_solicitud " +
            "JOIN   Cliente    c   ON c.id_cliente    = s.id_cliente " +
            "LEFT  JOIN tipoSolicitud ts ON ts.id_tipoSolicitud = s.id_tipoSolicitud " +
            "LEFT  JOIN Aplicacion    a  ON a.id_aplicacion    = s.id_aplicacion " +
            "WHERE  s.estado        = ?::estado_solicitud_enum " +          // ← cast
            "  AND  agn.id_colaborador = ? " +
            "ORDER BY s.fecha_creacion DESC";

        ejecutarQueryTickets(lista, sql, ps -> {
            ps.setString(1, estado);
            ps.setInt(2, idColab);
        });
        return lista;
    }

    public List<TicketDTO> ticketsPorEstadoYCliente(String estado, int idCliente) {
        List<TicketDTO> lista = new ArrayList<>();
        String sql =
            "SELECT s.*, ts.descripcion AS nombreTipoSolicitud, a.tipo_aplicacion AS nombreAplicacion " +
            "FROM   Solicitud s " +
            "LEFT  JOIN tipoSolicitud ts ON ts.id_tipoSolicitud = s.id_tipoSolicitud " +
            "LEFT  JOIN Aplicacion    a  ON a.id_aplicacion    = s.id_aplicacion " +
            "WHERE  s.estado     = ?::estado_solicitud_enum " +             // ← cast
            "  AND  s.id_cliente = ? " +
            "ORDER BY s.fecha_creacion DESC";

        ejecutarQueryTickets(lista, sql, ps -> {
            ps.setString(1, estado);
            ps.setInt(2, idCliente);
        });
        return lista;
    }

    /* ---------- KPI / CONTEOS ---------- */

    public int contarTicketsTotal() {
        return ejecutarConteo("SELECT COUNT(*) FROM Solicitud");
    }

    public int contarTicketsPorEstado(String estado) {
        String sql = "SELECT COUNT(*) FROM Solicitud WHERE estado = ?::estado_solicitud_enum";
        return ejecutarConteo(sql, ps -> ps.setString(1, estado));
    }

    public int contarTicketsPorCliente(int idCliente) {
        String sql = "SELECT COUNT(*) FROM Solicitud WHERE id_cliente = ?";
        return ejecutarConteo(sql, ps -> ps.setInt(1, idCliente));
    }

    public int contarTicketsPorColaborador(int idColab) {
        String sql = "SELECT COUNT(*) FROM Asignacion WHERE id_colaborador = ?";
        return ejecutarConteo(sql, ps -> ps.setInt(1, idColab));
    }

    public int contarTicketsPorEstadoYColaborador(String estado, int idColab) {
        String sql =
            "SELECT COUNT(*) " +
            "FROM   Solicitud  s " +
            "JOIN   Asignacion a ON a.id_solicitud = s.id_solicitud " +
            "WHERE  s.estado       = ?::estado_solicitud_enum " +           // ← cast
            "  AND  a.id_colaborador = ?";
        return ejecutarConteo(sql, ps -> {
            ps.setString(1, estado);
            ps.setInt(2, idColab);
        });
    }

    public int contarTicketsPorEstadoYCliente(String estado, int idCliente) {
        String sql = "SELECT COUNT(*) FROM Solicitud WHERE estado = ?::estado_solicitud_enum AND id_cliente = ?";
        return ejecutarConteo(sql, ps -> {
            ps.setString(1, estado);
            ps.setInt(2, idCliente);
        });
    }

    /* ---------- UTILIDADES PRIVADAS ---------- */

    /** Ejecuta consultas que devuelven tickets y los mapea a la lista dada. */
    private void ejecutarQueryTickets(List<TicketDTO> destino, String sql, SQLConsumer<PreparedStatement> binder) {
        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = con.prepareStatement(sql)) {

            binder.accept(ps);

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
                    // columnas opcionales según SELECT
                    try { t.setNombreCliente(rs.getString("nombre_cliente")); } catch (SQLException ignored) {}
                    try { t.setNombreTipoSolicitud(rs.getString("nombreTipoSolicitud")); } catch (SQLException ignored) {}
                    try { t.setNombreAplicacion(rs.getString("nombreAplicacion")); } catch (SQLException ignored) {}
                    destino.add(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Ejecuta un COUNT(*) sin parámetros. */
    private int ejecutarConteo(String sql) {
        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /** Ejecuta un COUNT(*) con parámetros. */
    private int ejecutarConteo(String sql, SQLConsumer<PreparedStatement> binder) {
        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = con.prepareStatement(sql)) {

            binder.accept(ps);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /* ---------- INTERFAZ FUNCIONAL AUXILIAR ---------- */

    /** Functional interface para evitar manejo explícito de SQLException en lambdas. */
    @FunctionalInterface
    private interface SQLConsumer<T> {
        void accept(T t) throws SQLException;
    }
}

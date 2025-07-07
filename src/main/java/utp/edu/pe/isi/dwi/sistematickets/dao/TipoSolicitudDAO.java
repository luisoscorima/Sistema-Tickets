package utp.edu.pe.isi.dwi.sistematickets.dao;

import utp.edu.pe.isi.dwi.sistematickets.dto.TipoSolicitudDTO;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.*;
import java.util.*;

@ApplicationScoped
public class TipoSolicitudDAO {

    private final String url = "jdbc:postgresql://my-db-instance.cytmkkegmp14.us-east-1.rds.amazonaws.com:5432/db-dev-web";
    private final String user = "userutp";
    private final String pass = "VU4B5np-8EyU";

    public List<TipoSolicitudDTO> listarTiposSolicitud() {
        List<TipoSolicitudDTO> lista = new ArrayList<>();
        String sql = "SELECT id_tipoSolicitud, tipoSolicitud, descripcion FROM tipoSolicitud ORDER BY id_tipoSolicitud";
        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TipoSolicitudDTO tipo = new TipoSolicitudDTO(
                        rs.getInt("id_tipoSolicitud"),
                        rs.getString("tipoSolicitud"),
                        rs.getString("descripcion")
                );
                lista.add(tipo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}

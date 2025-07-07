package utp.edu.pe.isi.dwi.sistematickets.bean;

import utp.edu.pe.isi.dwi.sistematickets.dao.TipoSolicitudDAO;
import utp.edu.pe.isi.dwi.sistematickets.dto.TipoSolicitudDTO;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import java.io.Serializable;
import java.util.List;

@Named("tipoSolicitudBean")
@SessionScoped
public class TipoSolicitudBean implements Serializable {
    @Inject
    private TipoSolicitudDAO tipoSolicitudDAO;

    private List<TipoSolicitudDTO> tipos;

    @jakarta.annotation.PostConstruct
    public void init() {
        tipos = tipoSolicitudDAO.listarTiposSolicitud();
    }

    public List<TipoSolicitudDTO> getTipos() {
        if (tipos == null) {
            tipos = tipoSolicitudDAO.listarTiposSolicitud();
        }
        return tipos;
    }
}

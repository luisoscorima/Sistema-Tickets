package utp.edu.pe.isi.dwi.sistematickets.bean;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import utp.edu.pe.isi.dwi.sistematickets.dao.DashboardDAO;
import java.util.List;
import utp.edu.pe.isi.dwi.sistematickets.dto.TicketDTO;

@Named("dashboardBean")
@RequestScoped
@Getter
public class DashboardBean implements Serializable {

    @Inject
    private LoginBean loginBean;

    @Inject
    private DashboardDAO dashboardDAO;

    private Map<String, Integer> kpis;

    private Map<String, List<TicketDTO>> ticketsPorEstado;

    @PostConstruct
    public void init() {
        kpis = new HashMap<>();
        ticketsPorEstado = new HashMap<>();

        if (loginBean.esAdmin()) {
            kpis.put("total", dashboardDAO.contarTicketsTotal());
            kpis.put("A", dashboardDAO.contarTicketsPorEstado("A"));
            kpis.put("B", dashboardDAO.contarTicketsPorEstado("B"));
            kpis.put("C", dashboardDAO.contarTicketsPorEstado("C"));
            kpis.put("P", dashboardDAO.contarTicketsPorEstado("P"));
            kpis.put("S", dashboardDAO.contarTicketsPorEstado("S"));
            kpis.put("N", dashboardDAO.contarTicketsPorEstado("N"));
        } else if (loginBean.esColaborador()) {
            int idColab = loginBean.getColaboradorLogueado().getIdColaborador();
            kpis.put("total", dashboardDAO.contarTicketsPorColaborador(idColab));
            kpis.put("A", dashboardDAO.contarTicketsPorEstadoYColaborador("A", idColab));
            kpis.put("B", dashboardDAO.contarTicketsPorEstadoYColaborador("B", idColab));
            kpis.put("C", dashboardDAO.contarTicketsPorEstadoYColaborador("C", idColab));
            kpis.put("P", dashboardDAO.contarTicketsPorEstadoYColaborador("P", idColab));
            kpis.put("S", dashboardDAO.contarTicketsPorEstadoYColaborador("S", idColab));
            kpis.put("N", dashboardDAO.contarTicketsPorEstadoYColaborador("N", idColab));
        } else if (loginBean.esCliente()) {
            int idCliente = loginBean.getClienteLogueado().getIdCliente();
            kpis.put("total", dashboardDAO.contarTicketsPorCliente(idCliente));
            kpis.put("A", dashboardDAO.contarTicketsPorEstadoYCliente("A", idCliente));
            kpis.put("B", dashboardDAO.contarTicketsPorEstadoYCliente("B", idCliente));
            kpis.put("C", dashboardDAO.contarTicketsPorEstadoYCliente("C", idCliente));
            kpis.put("P", dashboardDAO.contarTicketsPorEstadoYCliente("P", idCliente));
            kpis.put("S", dashboardDAO.contarTicketsPorEstadoYCliente("S", idCliente));
            kpis.put("N", dashboardDAO.contarTicketsPorEstadoYCliente("N", idCliente));
        }

        if (loginBean.esAdmin()) {
            for (String estado : new String[]{"A", "B", "C", "P", "S", "N"}) {
                ticketsPorEstado.put(estado, dashboardDAO.ticketsPorEstado(estado));
            }
        } else if (loginBean.esColaborador()) {
            int idColab = loginBean.getColaboradorLogueado().getIdColaborador();
            for (String estado : new String[]{"A", "B", "C", "P", "S", "N"}) {
                ticketsPorEstado.put(estado, dashboardDAO.ticketsPorEstadoYColaborador(estado, idColab));
            }
        } else if (loginBean.esCliente()) {
            int idCliente = loginBean.getClienteLogueado().getIdCliente();
            for (String estado : new String[]{"A", "B", "C", "P", "S", "N"}) {
                ticketsPorEstado.put(estado, dashboardDAO.ticketsPorEstadoYCliente(estado, idCliente));
            }
        }

    }

    public int kpi(String key) {
        return kpis.getOrDefault(key, 0);
    }

    public List<TicketDTO> getTicketsPorEstado(String estado) {
        return ticketsPorEstado.getOrDefault(estado, List.of());
    }
}

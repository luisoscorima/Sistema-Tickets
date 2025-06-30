package utp.edu.pe.isi.dwi.sistematickets.enums;

public enum EstadoSolicitudEnum {
    A("Abierto"),
    B("Asignado"),
    C("Programado"),
    P("Pendiente"),
    S("Solucionado"),
    N("Anulado");

    private final String label;

    EstadoSolicitudEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

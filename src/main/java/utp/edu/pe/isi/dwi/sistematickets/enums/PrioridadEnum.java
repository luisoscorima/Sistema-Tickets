package utp.edu.pe.isi.dwi.sistematickets.enums;

public enum PrioridadEnum {
    B("Baja"),
    M("Media"),
    A("Alta");

    private final String label;

    PrioridadEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

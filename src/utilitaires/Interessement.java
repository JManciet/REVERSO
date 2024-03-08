package utilitaires;

public enum Interessement {
    OUI("oui"),
    NON("non");

    private String value;
    public String getValue() {
        return value;
    }
    private Interessement(String value) {
        this.value = value;
    }
}

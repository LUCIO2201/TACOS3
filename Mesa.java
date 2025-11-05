package modelo;

/**
 * Representa una mesa en la taquería.
 */
public class Mesa {
    private int id;
    private int numero;
    private boolean ocupada;

    public Mesa() {}

    public Mesa(int id, int numero, boolean ocupada) {
        this.id = id;
        this.numero = numero;
        this.ocupada = ocupada;
    }

    public int getId() {
        return id;
    }

    public int getNumero() {
        return numero;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }
}


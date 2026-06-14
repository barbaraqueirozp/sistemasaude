package src;

public class Posologia {
    private int id;
    private String descricao;

    public Posologia(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }
}

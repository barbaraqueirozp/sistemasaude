package src;
public class Medicacao {
    private int id;
    private String nome;
    private String posologia;
    private int vezesAoDia;

    public Medicacao(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Medicacao(int id, String nome, String posologia, int vezesAoDia) {
        this.id = id;
        this.nome = nome;
        this.posologia = posologia;
        this.vezesAoDia = vezesAoDia;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getPosologia() {
        return posologia;
    }

    public int getVezesAoDia() {
        return vezesAoDia;
    }
}

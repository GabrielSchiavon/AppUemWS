package AppUemWS.dados;

public class Departamento {

    private int id;
    private String nome;
    private String descricao;
    private int status;

    public Departamento() {
        id = -1;
    }

    public Departamento(int id, String nome, String descricao, int status) {
        super();
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
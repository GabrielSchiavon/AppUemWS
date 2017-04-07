package AppUemWS.dados;

public class Sala {
    private int id;
    private int numero;
    private int id_departamento;
    private int classificacao;
    private String descricao;
    private int status;

    public Sala() {
        id = -1;
    }

    public Sala(int id, int numero, int id_departamento, int classificacao, String descricao,
            int status) {
        super();
        this.id = id;
        this.numero = numero;
        this.id_departamento = id_departamento;
        this.classificacao = classificacao;
        this.descricao = descricao;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getId_departamento() {
        return id_departamento;
    }

    public void setId_departamento(int id_departamento) {
        this.id_departamento = id_departamento;
    }

    public int getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(int classificacao) {
        this.classificacao = classificacao;
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
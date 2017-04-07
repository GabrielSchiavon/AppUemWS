package AppUemWS.dados;

public class IndiceReserva {

    private int id;
    private int iddepartamento;
    private String data;
    private int periodo;
    private int classificacaosala;
    private int idprimeiro;
    private int status;

    public IndiceReserva() {
        this.id = -1;
    }

    public IndiceReserva(int id, int iddepartamento, String data, int periodo, int classificacaosala, int idprimeiro, int status) {
        super();
        this.id = id;
        this.iddepartamento = iddepartamento;
        this.data = data;
        this.periodo = periodo;
        this.classificacaosala = classificacaosala;
        this.idprimeiro = idprimeiro;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIddepartamento() {
        return iddepartamento;
    }

    public void setIddepartamento(int iddepartamento) {
        this.iddepartamento = iddepartamento;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public int getClassificacaosala() {
        return classificacaosala;
    }

    public void setClassificacaosala(int classificacaosala) {
        this.classificacaosala = classificacaosala;
    }

    public int getIdprimeiro() {
        return idprimeiro;
    }

    public void setIdprimeiro(int idprimeiro) {
        this.idprimeiro = idprimeiro;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void clonar(IndiceReserva indiceReserva) {
        this.id = indiceReserva.getId();
        this.iddepartamento = indiceReserva.getIddepartamento();
        this.data = indiceReserva.getData();
        this.periodo = indiceReserva.getPeriodo();
        this.classificacaosala = indiceReserva.getClassificacaosala();
        this.idprimeiro = indiceReserva.getIdprimeiro();
        this.status = indiceReserva.getStatus();
    }
}
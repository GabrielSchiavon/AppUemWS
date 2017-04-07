package AppUemWS.dados;

public class Reserva {

    private int id;
    private int iddepartamento;
    private int idusuario;
    private int tipoaula;
    private int iddisciplina;
    private int tipo;
    private String dataefetuacao;
    private int proximoid;
    private String datareserva;
    private int periodo;
    private int tiposala; //// solicitação para tipoaula de sala (projecao, laboratorio)
    private int idsala;
    private String observacao;
    private int status;

    public Reserva() {
        this.id = -1;
        this.iddisciplina = -1;
        this.idsala = -1;
        this.tiposala = -1;
    }

    public Reserva(int id, int iddepartamento, int idusuario, int tipoaula, int iddisciplina,
            int tipo, String dataefetuacao, int proximoid, String datareserva, int periodo, int tiposala, int idsala,
            String observacao, int status) {
        super();
        this.id = id;
        this.iddepartamento = iddepartamento;
        this.idusuario = idusuario;
        this.tipoaula = tipoaula;
        this.iddisciplina = iddisciplina;
        this.tipo = tipo;
        this.dataefetuacao = dataefetuacao;
        this.proximoid = proximoid;
        this.datareserva = datareserva;
        this.periodo = periodo;
        this.tiposala = tiposala;
        this.idsala = idsala;
        this.observacao = observacao;
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

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public int getTipoaula() {
        return tipoaula;
    }

    public void setTipoaula(int tipoaula) {
        this.tipoaula = tipoaula;
    }

    public int getIddisciplina() {
        return iddisciplina;
    }

    public void setIddisciplina(int iddisciplina) {
        this.iddisciplina = iddisciplina;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getDataefetuacao() {
        return dataefetuacao;
    }

    public void setDataefetuacao(String dataefetuacao) {
        this.dataefetuacao = dataefetuacao;
    }

    public int getProximoid() {
        return proximoid;
    }

    public void setProximoid(int proximoid) {
        this.proximoid = proximoid;
    }

    public String getDatareserva() {
        return datareserva;
    }

    public void setDatareserva(String datareserva) {
        this.datareserva = datareserva;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public int getTiposala() {
        return tiposala;
    }

    public void setTiposala(int tiposala) {
        this.tiposala = tiposala;
    }

    public int getIdsala() {
        return idsala;
    }

    public void setIdsala(int idsala) {
        this.idsala = idsala;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void clonarReserva(Reserva r) {
        id = r.getId();
        iddepartamento = r.getIddepartamento();
        idusuario = r.getIdusuario();
        tipoaula = r.getTipoaula();
        iddisciplina = r.getIddisciplina();
        tipo = r.getTipo();
        dataefetuacao = r.getDataefetuacao();
        proximoid = r.getProximoid();
        datareserva = r.getDatareserva();
        periodo = r.getPeriodo();
        tiposala = r.getTiposala();
        idsala = r.getIdsala();
        observacao = r.getObservacao();
        status = r.getStatus();
    }
}
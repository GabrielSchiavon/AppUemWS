package AppUemWS.dados;

public class AnoLetivo {

    private int id;
    private int iddepartamento;
    private String iniciop;
    private String fimp;
    private String inicios;
    private String fims;
    private int status;

    public AnoLetivo() {
        id = -1;
    }

    public AnoLetivo(int id, int iddepartamento, String iniciop, String fimp, String inicios, String fims, int status) {
        super();
        this.id = id;
        this.iddepartamento = iddepartamento;
        this.iniciop = iniciop;
        this.fimp = fimp;
        this.inicios = inicios;
        this.fims = fims;
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

    public String getIniciop() {
        return iniciop;
    }

    public void setIniciop(String iniciop) {
        this.iniciop = iniciop;
    }

    public String getFimp() {
        return fimp;
    }

    public void setFimp(String fimp) {
        this.fimp = fimp;
    }

    public String getInicios() {
        return inicios;
    }

    public void setInicios(String inicios) {
        this.inicios = inicios;
    }

    public String getFims() {
        return fims;
    }

    public void setFims(String fims) {
        this.fims = fims;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
package AppUemWS.dados;

/**
 *
 * @author gabriel
 */
public class ReservaUsuario extends Reserva {
    
    private int numero;
    private String descricao;
    private String nomeMateria;
    
    public ReservaUsuario(int id, int iddepartamento, int idusuario, int tipoaula, 
            int iddisciplina, int tipo, String dataefetuacao, int proximoid, 
            String datareserva, int periodo, int tiposala, int idsala, int status,
            int numero, String descricao, String nomeMateria) {
        
        super(id, iddepartamento, idusuario, tipoaula, iddisciplina, tipo, dataefetuacao,
                proximoid, datareserva, periodo, tiposala, idsala, status);
        this.numero = numero;
        this.descricao = descricao;
        this.nomeMateria = nomeMateria;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNomeMateria() {
        return nomeMateria;
    }

    public void setNomeMateria(String nomeMateria) {
        this.nomeMateria = nomeMateria;
    }
    
}

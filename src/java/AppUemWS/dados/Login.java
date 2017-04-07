package AppUemWS.dados;

public class Login {

    private int id;
    private String email;
    private String senha;
    private int permissao;

    public Login() {
        id = -1;
        permissao = -1;
    }

    public Login(int id, String email, String senha, int permissao) {
        super();
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.permissao = permissao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getPermissao() {
        return permissao;
    }

    public void setPermissao(int permissao) {
        this.permissao = permissao;
    }

    public void clonar(Login login) {
        id = login.getId();
        email = login.getEmail();
        senha = login.getSenha();
        permissao = login.getPermissao();
    }
}
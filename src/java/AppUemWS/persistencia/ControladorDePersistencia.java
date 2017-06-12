package AppUemWS.persistencia;

import AppUemWS.dados.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControladorDePersistencia {
	
    private Connection connection = null;
    
    public ControladorDePersistencia() throws SQLException {        
        // conecta com o banco de dados
        connection = Conexao.conecta();
    }

    
    public boolean alteraSenha(Login login){
        try{
            String query = "UPDATE usuario SET senha=?  WHERE email=?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setString(1, login.getSenha());
                statement.setString(2, login.getEmail());
                
                statement.executeUpdate();
            }
            connection.close();
            return true;
        }
        catch(SQLException sqlex){
            sqlex.printStackTrace();
        }
        return false;
    }
    
    public ArrayList<Login> carregaLoginAtivo(){
        ArrayList<Login> listaLogin = null;
        try{
            String query = "SELECT id, email, senha, permissao  FROM usuario WHERE status=1";
            
            //obtendo os resultados (O parenteses antes do valor atribuido serve apenas para nao causar conflito entre os tipos)
            try ( //preparando para a consulta citada acima (O parentese antes do valor atribuido serve apenas para evitar erros)
                    PreparedStatement statement = (com.mysql.jdbc.PreparedStatement)
                            this.connection.prepareStatement(query)) {
                //obtendo os resultados (O parenteses antes do valor atribuido serve apenas para nao causar conflito entre os tipos)
                ResultSet rs = (com.mysql.jdbc.ResultSet)statement.executeQuery(query);
                //declarando variaveis para usar a seguir
                Login login1;
                String Email, Senha;
                int Id, Permissao;
                listaLogin = new ArrayList<>();
                while(rs.next()){
                    Id = rs.getInt("id");
                    
                    Email = rs.getString("email");
                    Senha = rs.getString("senha");
                    Permissao = rs.getInt("permissao");
                    login1 = new Login(Id, Email, Senha, Permissao);
                    listaLogin.add(login1);
                }
            }
            connection.close();
            return listaLogin;
         }
         catch(SQLException ex){
             ex.printStackTrace();
             return listaLogin;
         }
    }
    
    public ArrayList<Departamento> carregaDepartamentoAtivo(){
        ArrayList<Departamento> listaDepartamento = null;
        try{
            String query = "SELECT * FROM departamento WHERE status=1";
            
            try (PreparedStatement statement = (com.mysql.jdbc.PreparedStatement)this.connection.prepareStatement(query)) {
                ResultSet rs = (com.mysql.jdbc.ResultSet)statement.executeQuery(query);
                Departamento dep1;
                String Nome, Descricao;
                int Id, Status;
                listaDepartamento = new ArrayList<>();
                while(rs.next()){
                    Id = rs.getInt("id");
                    Nome = rs.getString("nome");
                    Descricao = rs.getString("descricao");
                    Status = 1;
                    dep1 = new Departamento(Id,Nome,Descricao, Status);
                    listaDepartamento.add(dep1);
                }
            }
            connection.close();
            return listaDepartamento;
         }
         catch(SQLException ex){
             ex.printStackTrace();
             return listaDepartamento;
         }
     }
    /*
    public Departamento carregaDepartamentoID(int id_dep){
        Departamento departamento = null;
        try{
            String query = "SELECT * FROM departamento WHERE status=1";
            
            try (PreparedStatement statement = (com.mysql.jdbc.PreparedStatement)this.connection.prepareStatement(query)) {
                ResultSet rs = (com.mysql.jdbc.ResultSet)statement.executeQuery(query);
                String Nome, Descricao;
                int Id, Status;
                while(rs.next()){
                    Id = rs.getInt("id");
                    if(Id != id_dep){
                        continue;
                    }
                    Nome = rs.getString("nome");
                    Descricao = rs.getString("descricao");
                    Status = 1;
                    
                    departamento = new Departamento(Id,Nome,Descricao, Status);
                    
                }
            }
            connection.close();
            return departamento;
         }
         catch(SQLException ex){
             ex.printStackTrace();
             return departamento;
         }
     }
    */
    public boolean cadastraDepartamento(Departamento departamento) throws Exception{
 
        try{
            String query = "INSERT INTO departamento (nome, descricao, status)  VALUES (?,?,?)";
                
  
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setString(1, departamento.getNome());
                statement.setString(2, departamento.getDescricao());
                statement.setInt(3, 1);
                
                statement.executeUpdate();
            }
            connection.close();
            return true;
        }
        catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }

     }
    
    public boolean alteraDepartamento(Departamento departamento){
    	try{
            String query = "UPDATE departamento SET nome=?, descricao=? WHERE id=?";
                try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                    statement.setString(1, departamento.getNome());
                    statement.setString(2, departamento.getDescricao());
                    statement.setInt(3, departamento.getId());
                    
                    statement.executeUpdate();
                }
            connection.close();
            return true;
    	}
    	catch(SQLException sqlex){
    		sqlex.printStackTrace();
    	}
    	return false;
    }  
    
    
        
    public boolean removeDepartamento(int id){
    	try{
    		String query = "UPDATE departamento SET status=0 WHERE id=?";
                try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                    statement.setInt(1, id);
                    
                    statement.executeUpdate();
                }
    		connection.close();
    		return true;
        }
    	catch(SQLException ex){
    		ex.printStackTrace();
    	}
    	return false;
    }
    
    public ArrayList<Sala> carregaSalaAtivo(){
        ArrayList<Sala> listaSala = null;
        try{
            String query = "SELECT * FROM sala WHERE status=1";
            
            try (PreparedStatement statement = (com.mysql.jdbc.PreparedStatement)this.connection.prepareStatement(query)) {
                ResultSet rs = (com.mysql.jdbc.ResultSet)statement.executeQuery(query);
                Sala sala1;
                String Descricao;
                int Id, Id_Departamento, Numero, Classificacao, Status;
                listaSala = new ArrayList<>();
                while(rs.next()){
                    Id = rs.getInt("id");
                    Numero = rs.getInt("numero");
                    Id_Departamento = rs.getInt("iddepartamento");
                    Classificacao = rs.getInt("classificacao");
                    Descricao = rs.getString("descricao");
                    Status = 1;
                    sala1 = new Sala(Id,Numero, Id_Departamento,Classificacao,Descricao, Status);
                    listaSala.add(sala1);
                }
            }
            connection.close();
            return listaSala;
         }
         catch(SQLException ex){
             ex.printStackTrace();
             return listaSala;
         }
     }
    
    public boolean cadastraSala(Sala sala) throws Exception{
 
        try{
            String query = "INSERT INTO sala (numero, iddepartamento, classificacao, descricao, status)  VALUES (?,?,?,?,?)";
                
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, sala.getNumero());
                statement.setInt(2, sala.getId_departamento());
                statement.setInt(3, sala.getClassificacao());
                statement.setString(4, sala.getDescricao());
                statement.setInt(5, 1);
                
                statement.executeUpdate();
            }
            connection.close();
            return true;
        }
        catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }

     }
    
    public boolean alteraSala(Sala sala){
    	try{
            String query = "UPDATE sala SET numero=?, iddepartamento=?, classificacao=?, descricao=? WHERE id=?";
                try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                    statement.setInt(1, sala.getNumero());
                    statement.setInt(2, sala.getId_departamento());
                    statement.setInt(3, sala.getClassificacao());
                    statement.setString(4, sala.getDescricao());
                    statement.setInt(5, sala.getId());
                    
                    statement.executeUpdate();
                }
            connection.close();
            return true;
    	}
    	catch(SQLException sqlex){
    		sqlex.printStackTrace();
    	}
    	return false;
    }   
        
    public boolean removeSala(int id){
    	try{
            String query = "UPDATE sala SET status=0 WHERE id=?";
                try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                    statement.setInt(1, id);
                    
                    statement.executeUpdate();
                }
            connection.close();
            return true;
        }
    	catch(SQLException ex){
    		ex.printStackTrace();
    	}
    	return false;
    }
    
    public ArrayList<Usuario> carregaUsuarioAtivo(){
        ArrayList<Usuario> listaUsuario = null;
        try{
            String query = "SELECT * FROM usuario WHERE status=1";
            
            try (PreparedStatement statement = (com.mysql.jdbc.PreparedStatement)
                    this.connection.prepareStatement(query)) {
                ResultSet rs = (com.mysql.jdbc.ResultSet)statement.executeQuery(query);
                Usuario user1;
                String Nome, Email, Senha, Telefone, Disciplinas;
                int Id, Id_Departamento, Permissao, ProblemaLocomocao, Status;
                listaUsuario = new ArrayList<>();
                while(rs.next()){
                    Id = rs.getInt("id");
                    Id_Departamento = rs.getInt("iddepartamento");
                    Nome = rs.getString("nome");
                    Email = rs.getString("email");
                    Senha = rs.getString("senha");
                    Telefone = rs.getString("telefone");
                    Permissao = rs.getInt("permissao");
                    Disciplinas = rs.getString("disciplinas");
                    ProblemaLocomocao = rs.getInt("problemalocomocao");
                    Status = 1;

                    user1 = new Usuario(Id, Id_Departamento, Nome, Email, Senha, Telefone, Permissao, Disciplinas, ProblemaLocomocao, Status);
                    listaUsuario.add(user1);
                }
            }
            connection.close();
            return listaUsuario;
         }
         catch(SQLException ex){
             ex.printStackTrace();
             return listaUsuario;
         }
     }
    
    public boolean cadastraUsuario(Usuario usuario) throws Exception{
 
        try{
            String dp1;
            dp1 = usuario.getId_disciplinas();
        	
            String query = "INSERT INTO usuario (iddepartamento, nome, email, senha, telefone, permissao, disciplinas, problemalocomocao, status)  VALUES (?,?,?,?,?,?,?,?,?)";
                
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, usuario.getId_departamento());
                statement.setString(2, usuario.getNome());
                statement.setString(3, usuario.getEmail());
                statement.setString(4, usuario.getSenha());
                statement.setString(5, usuario.getTelefone());
                statement.setInt(6, usuario.getPermissao());
                statement.setString(7, dp1);
                statement.setInt(8, usuario.getProblema_locomocao());
                statement.setInt(9, 1);
                
                statement.executeUpdate();
            }
            connection.close();
            return true;
        }
        catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }

     }
    
    public boolean alteraUsuario(Usuario usuario){
    	try{
            String query = "UPDATE usuario SET  iddepartamento=?, nome=?, email=?, telefone=?, permissao=?, problemalocomocao=? WHERE id=?";
                try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                    statement.setInt(1, usuario.getId_departamento());
                    statement.setString(2, usuario.getNome());
                    statement.setString(3, usuario.getEmail());
                    statement.setString(4, usuario.getTelefone());
                    statement.setInt(5, usuario.getPermissao());
                    statement.setInt(6, usuario.getProblema_locomocao());
                    statement.setInt(7, usuario.getId());
                    
                    
                    statement.executeUpdate();
                }
            connection.close();
            return true;
    	}
    	catch(SQLException sqlex){
            sqlex.printStackTrace();
    	}
    	return false;
    }  
    
    public boolean insereDisciplinaUsuario(Usuario usuario, int id_disciplina){
    	try{
    		String dp1;
    		dp1 = usuario.getId_disciplinas();
    		if(dp1.length() != 0){
    			dp1 += "-";
    		}
    		dp1 += id_disciplina;
    		           
    		String query = "UPDATE usuario SET disciplinas=? WHERE id=?";
                try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                    statement.setString(1, dp1);
                    statement.setInt(2, usuario.getId());
                    
                    statement.executeUpdate();
                }
            connection.close();
            return true;
    	}
    	catch(SQLException sqlex){
            sqlex.printStackTrace();
    	}
    	return false;
    }  
        
    public boolean removeUsuario(int id){
    	try{
            String query = "UPDATE usuario SET status=0 WHERE id=?";
                try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                    statement.setInt(1, id);
                    
                    statement.executeUpdate();
                }
            connection.close();
            return true;
        }
    	catch(SQLException ex){
            ex.printStackTrace();
    	}
    	return false;
    }
  
    public ArrayList<Curso> carregaCursoAtivo(){
        ArrayList<Curso> listaCurso = null;
        try{
            String query = "SELECT * FROM curso WHERE status=1";
            
            try (PreparedStatement statement = (com.mysql.jdbc.PreparedStatement)
                    this.connection.prepareStatement(query)) {
                ResultSet rs = (com.mysql.jdbc.ResultSet)statement.executeQuery(query);
                Curso curso1;
                String Nome, Descricao;
                int Id, Id_Departamento, Tipo, Status;
                listaCurso = new ArrayList<>();
                while(rs.next()){
                    Id = rs.getInt("id");
                    Id_Departamento = rs.getInt("iddepartamento");
                    Nome = rs.getString("nome");
                    Descricao = rs.getString("descricao");
                    Tipo = rs.getInt("tipo");
                    Status = 1;
                    
                    curso1 = new Curso(Id, Id_Departamento, Nome, Tipo, Descricao, Status);
                    listaCurso.add(curso1);
                }
            }
            connection.close();
            return listaCurso;
         }
         catch(SQLException ex){
             ex.printStackTrace();
             return listaCurso;
         }
     }
    
    public boolean cadastraCurso(Curso curso) throws Exception{
        try{
            String query = "INSERT INTO curso (iddepartamento, nome, tipo, descricao, status)  VALUES (?,?,?,?,?)";
                
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, curso.getId_departamento());
                statement.setString(2, curso.getNome());
                statement.setInt(3, curso.getTipo());
                statement.setString(4, curso.getDescricao());
                statement.setInt(5, 1);
                
                statement.executeUpdate();
            }
            connection.close();
            return true;
        }
        catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }

     }
    
    public boolean alteraCurso(Curso curso){
    	try{
            String query = "UPDATE curso SET iddepartamento=?, nome=?, tipo=?, descricao=? WHERE id=?";
                try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                    statement.setInt(1, curso.getId_departamento());
                    statement.setString(2, curso.getNome());
                    statement.setInt(3, curso.getTipo());
                    statement.setString(4, curso.getDescricao());
                    statement.setInt(5, curso.getId());
                    
                    statement.executeUpdate();
                }
            connection.close();
            return true;
    	}
    	catch(SQLException sqlex){
    		sqlex.printStackTrace();
    	}
    	return false;
    }   
        
    public boolean removeCurso(int id){
    	try{
            String query = "UPDATE curso SET status=0 WHERE id=?";
                try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                    statement.setInt(1, id);
                    
                    statement.executeUpdate();
                }
            connection.close();
            return true;
        }
    	catch(SQLException ex){
    		ex.printStackTrace();
    	}
    	return false;
    }
    
    public ArrayList<Disciplina> carregaDisciplinaAtivo(){
        ArrayList<Disciplina> listaDisciplina = null;
        try{
            String query = "SELECT * FROM disciplina WHERE status=1";
            
            try (PreparedStatement statement = (com.mysql.jdbc.PreparedStatement)
                    this.connection.prepareStatement(query)) {
                ResultSet rs = (com.mysql.jdbc.ResultSet)statement.executeQuery(query);
                Disciplina d1;
                String Nome;
                int Id, Id_Departamento, Id_Curso, Codigo, Periodo, Turma, Classificacao, Status;
                listaDisciplina = new ArrayList<>();
                while(rs.next()){
                    Id = rs.getInt("id");
                    Id_Departamento = rs.getInt("iddepartamento");
                    Id_Curso = rs.getInt("idcurso");
                    Codigo = rs.getInt("codigo");
                    Periodo = rs.getInt("periodo");
                    Turma = rs.getInt("turma");
                    Nome = rs.getString("nome");
                    Classificacao = rs.getInt("classificacao");
                    Status = 1;
                    
                    d1 = new Disciplina(Id, Id_Departamento,Id_Curso, Codigo, 
                            Periodo, Turma, Nome, Classificacao, Status);
                    listaDisciplina.add(d1);
                }
            }
            connection.close();
            return listaDisciplina;
         }
         catch(SQLException ex){
             ex.printStackTrace();
             return listaDisciplina;
         }
     }
    
    public boolean cadastraDisciplina(Disciplina disciplina) throws Exception{
        try{
            String query = "INSERT INTO disciplina (iddepartamento, idcurso, codigo, periodo, turma, nome, classificacao, status)  VALUES (?,?,?,?,?,?,?,?)";
                
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, disciplina.getId_departamento());
                statement.setInt(2, disciplina.getId_curso());
                statement.setInt(3, disciplina.getCodigo());
                statement.setInt(4, disciplina.getPeriodo());
                statement.setInt(5, disciplina.getTurma());
                statement.setString(6, disciplina.getNome());
                statement.setInt(7, disciplina.getClassificacao());
                statement.setInt(8, 1);
                
                statement.executeUpdate();
            }
            connection.close();
            return true;
        }
        catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }

     }
    
    public boolean alteraDisciplina(Disciplina disciplina){
    	try{
            String query = "UPDATE disciplina SET iddepartamento=?, idcurso=?, codigo=?, periodo=?, turma=?, nome=?, classificacao=? WHERE id=?";
                try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                    statement.setInt(1, disciplina.getId_departamento());
                    statement.setInt(2, disciplina.getId_curso());
                    statement.setInt(3, disciplina.getCodigo());
                    statement.setInt(4, disciplina.getPeriodo());
                    statement.setInt(5, disciplina.getTurma());
                    statement.setString(6, disciplina.getNome());
                    statement.setInt(7, disciplina.getClassificacao());
                    statement.setInt(8, disciplina.getId());
                    
                    statement.executeUpdate();
                }
            connection.close();
            return true;
    	}
    	catch(SQLException sqlex){
    		sqlex.printStackTrace();
    	}
    	return false;
    }   
        
    public boolean removeDisciplina(int id){
    	try{
            String query = "UPDATE disciplina SET status=0 WHERE id=?";
                try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                    statement.setInt(1, id);
                    
                    statement.executeUpdate();
                }
            connection.close();
            return true;
        }
    	catch(SQLException ex){
    		ex.printStackTrace();
    	}
    	return false;
    }
    
    
    public ArrayList<IndiceReserva> carregaIndiceReservaAtivo(){
        ArrayList<IndiceReserva> listaIndiceReserva = null;
        try{
            String query = "SELECT * FROM indicereserva WHERE status=1";
            
            try (PreparedStatement statement = (com.mysql.jdbc.PreparedStatement)
                    this.connection.prepareStatement(query)) {
                ResultSet rs = (com.mysql.jdbc.ResultSet)statement.executeQuery(query);
                IndiceReserva ir1;
                String Data;
                int Id, Iddepartamento, Periodo, ClassificacaoSala, Idprimeiro, Status;
                listaIndiceReserva = new ArrayList<>();
                while(rs.next()){
                    Id = rs.getInt("id");
                    Iddepartamento = rs.getInt("iddepartamento");
                    Data = rs.getString("data");
                    Periodo = rs.getInt("periodo");
                    ClassificacaoSala = rs.getInt("classificacaosala");
                    Idprimeiro = rs.getInt("idprimeiro");
                    Status = rs.getInt("status");
                    ir1 = new IndiceReserva(Id,Iddepartamento,Data,Periodo,ClassificacaoSala,Idprimeiro,Status);
                    listaIndiceReserva.add(ir1);
                }
            }
            connection.close();
         }
         catch(SQLException ex){
             ex.printStackTrace();
         }
        return listaIndiceReserva;
     }
    
    public boolean cadastraIndiceReserva(IndiceReserva indiceReserva) throws Exception{
        try{
            String query = "INSERT INTO indicereserva (iddepartamento, data, periodo, classificacaosala, idprimeiro, status)  VALUES (?,?,?,?,?,?)";
                
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, indiceReserva.getIddepartamento());
                statement.setString(2, indiceReserva.getData());
                statement.setInt(3, indiceReserva.getPeriodo());
                statement.setInt(4, indiceReserva.getClassificacaosala());
                statement.setInt(5, indiceReserva.getIdprimeiro());
                statement.setInt(6, 1);
                
                statement.executeUpdate();
            }
            connection.close();
            return true;
        }
        catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }
     }
    
    
    public boolean alteraIndiceReserva(IndiceReserva indiceReserva){
    	try{
            String query = "UPDATE indicereserva SET iddepartamento=?, data=?, periodo=?, classificacaosala=?, idprimeiro=? WHERE id=?";
                try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                    statement.setInt(1, indiceReserva.getIddepartamento());
                    statement.setString(2, indiceReserva.getData());
                    statement.setInt(3, indiceReserva.getPeriodo());
                    statement.setInt(4, indiceReserva.getClassificacaosala());
                    statement.setInt(5, indiceReserva.getIdprimeiro());
                    statement.setInt(6, indiceReserva.getId());
                    
                    statement.executeUpdate();
                }
            connection.close();
            return true;
    	}
    	catch(SQLException sqlex){
    		sqlex.printStackTrace();
    	}
    	return false;
    }   
    
    public boolean mudaInicioFilaIndiceReserva(int id, int idprimeiro){
    	try{
            String query = "UPDATE indicereserva SET idprimeiro=? WHERE id=?";
                try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                    statement.setInt(1, idprimeiro);
                    statement.setInt(1, id);
                    
                    statement.executeUpdate();
                }
            connection.close();
            return true;
        }
    	catch(SQLException ex){
    		ex.printStackTrace();
    	}
    	return false;
    }
    
    public ArrayList<Reserva> carregaReserva(){
        ArrayList<Reserva> listaReserva = null;
        try{
            String query = "SELECT * FROM reserva";
            
            try (PreparedStatement statement = (com.mysql.jdbc.PreparedStatement)
                    this.connection.prepareStatement(query)) {
                ResultSet rs = (com.mysql.jdbc.ResultSet)statement.executeQuery(query);
                Reserva r1;
                String Dataefetuacao, Datareserva;
                int Id,Iddepartamento, Idusuario, Tipoaula, Iddisciplina, Tipo, 
                        Proximoid, Periodo, Tiposala, Idsala, Status;
                listaReserva = new ArrayList<>();
                while(rs.next()){
                    Id = rs.getInt("id");
                    Iddepartamento = rs.getInt("iddepartamento");
                    Idusuario = rs.getInt("idusuario");
                    Tipoaula = rs.getInt("tipoaula");
                    Iddisciplina = rs.getInt("iddisciplina");
                    Tipo = rs.getInt("tipo");
                    Dataefetuacao = rs.getString("dataefetuacao");
                    Proximoid = rs.getInt("proximoid");
                    Datareserva = rs.getString("datareserva");
                    Periodo = rs.getInt("periodo");
                    Tiposala = rs.getInt("tiposala");
                    Idsala = rs.getInt("idsala");
                    Status = rs.getInt("status");
                    r1 = new Reserva(Id, Iddepartamento, Idusuario, Tipoaula, 
                            Iddisciplina, Tipo, Dataefetuacao, Proximoid, 
                            Datareserva, Periodo, Tiposala, Idsala, Status);
                    listaReserva.add(r1);
                }
            }
            connection.close();
         }
         catch(SQLException ex){
             ex.printStackTrace();
         }
        return listaReserva;
     }
    
    public Reserva buscaReservaId(int id){
    	Reserva r1 = new Reserva();
        try{
            String query = "SELECT * FROM reserva WHERE id=?";
            
                try (PreparedStatement statement = (com.mysql.jdbc.PreparedStatement)
                        this.connection.prepareStatement(query)) {
                    statement.setInt(1, id);
                    ResultSet rs = (com.mysql.jdbc.ResultSet)statement.executeQuery(query);
                    String Dataefetuacao, Datareserva;
                    int Id, Iddepartamento, Idusuario, Tipoaula, Iddisciplina, 
                            Tipo, Proximoid, Periodo, Tiposala, Idsala, Status;
                    while(rs.next()){
                        Id = rs.getInt("id");
                        Iddepartamento = rs.getInt("iddepartamento");
                        Idusuario = rs.getInt("idusuario");
                        Tipoaula = rs.getInt("tipoaula");
                        Iddisciplina = rs.getInt("iddisciplina");
                        Tipo = rs.getInt("tipo");
                        Dataefetuacao = rs.getString("dataefetuacao");
                        Proximoid = rs.getInt("proximoid");
                        Datareserva = rs.getString("datareserva");
                        Periodo = rs.getInt("periodo");
                        Tiposala = rs.getInt("tiposala");
                        Idsala = rs.getInt("idsala");
                        Status = rs.getInt("status");
                        r1 = new Reserva(Id, Iddepartamento, Idusuario, Tipoaula, 
                                Iddisciplina, Tipo, Dataefetuacao, Proximoid, 
                                Datareserva, Periodo, Tiposala, Idsala, Status);
                        return r1;
                    }   }
            connection.close();
         }
         catch(SQLException ex){
             ex.printStackTrace();
         }
         return r1;
     }
    
    public boolean cadastraReserva(Reserva reserva) throws Exception{
 
        try{
            String query = "INSERT INTO reserva (iddepartamento, idusuario, tipoaula, iddisciplina, tipo, dataefetuacao, proximoid, datareserva, periodo, tiposala, idsala, status)  VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
                
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, reserva.getIddepartamento());
                statement.setInt(2, reserva.getIdusuario());
                statement.setInt(3, reserva.getTipoaula());
                statement.setInt(4, reserva.getIddisciplina());
                statement.setInt(5, reserva.getTipo());
                statement.setString(6, reserva.getDataefetuacao());
                statement.setInt(7, reserva.getProximoid());
                statement.setString(8, reserva.getDatareserva());
                statement.setInt(9, reserva.getPeriodo());
                statement.setInt(10, reserva.getTiposala());
                statement.setInt(11, reserva.getIdsala());
                statement.setInt(12, 1);
                
                statement.executeUpdate();
            }
            connection.close();
            return true;
        }
        catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }

     }
    
    
    public boolean alteraReserva(Reserva reserva){
    	try{
            String query = "UPDATE reserva SET iddepartamento=?, idusuario=?, tipoaula=?, iddisciplina=?, tipo=?, dataefetuacao=?, proximoid=?, datareserva=?, periodo=?, tiposala=?, idsala=? WHERE id=?";
                try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                    statement.setInt(1, reserva.getIddepartamento());
                    statement.setInt(2, reserva.getIdusuario());
                    statement.setInt(3, reserva.getTipoaula());
                    statement.setInt(4, reserva.getIddisciplina());
                    statement.setInt(5, reserva.getTipo());
                    statement.setString(6, reserva.getDataefetuacao());
                    statement.setInt(7, reserva.getProximoid());
                    statement.setString(8, reserva.getDatareserva());
                    statement.setInt(9, reserva.getPeriodo());
                    statement.setInt(10, reserva.getTiposala());
                    statement.setInt(11, reserva.getIdsala());
                    statement.setInt(12, reserva.getId());
                    
                    statement.executeUpdate();
                }
            connection.close();
            return true;
    	}
    	catch(SQLException sqlex){
    		sqlex.printStackTrace();
    	}
    	return false;
    }   
    
    public boolean alteraSalaReserva(Reserva reserva){
    	try{
            String query = "UPDATE reserva SET idsala=? WHERE id=?";
                try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                    statement.setInt(1, reserva.getIdsala());
                    statement.setInt(2, reserva.getId());
                    
                    statement.executeUpdate();
                }
            connection.close();
            return true;
    	}
    	catch(SQLException sqlex){
    		sqlex.printStackTrace();
    	}
    	return false;
    }   
        
    public boolean removeReserva(int id){
    	try{
            String query = "UPDATE reserva SET status=0 WHERE id=?";
                try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                    statement.setInt(1, id);
                    
                    statement.executeUpdate();
                }
            connection.close();
            return true;
        }
    	catch(SQLException ex){
    		ex.printStackTrace();
    	}
    	return false;
    }
    
    public boolean alteraStatusReserva(int id, int status){
    	try{
            String query = "UPDATE reserva SET status=? WHERE id=?";
                try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                    statement.setInt(1, status);
                    statement.setInt(2, id);
                    
                    statement.executeUpdate();
                }
            connection.close();
            return true;
        }
    	catch(SQLException ex){
    		ex.printStackTrace();
    	}
    	return false;
    }
    
    
    public ArrayList<AnoLetivo> carregaAnoLetivoAtivo(){
        ArrayList<AnoLetivo> listaA = null;
        try{
            String query = "SELECT * FROM anoletivo WHERE status=1";
            
            try (PreparedStatement statement = (com.mysql.jdbc.PreparedStatement)
                    this.connection.prepareStatement(query)) {
                ResultSet rs = (com.mysql.jdbc.ResultSet)statement.executeQuery(query);
                AnoLetivo anol;
                String Iniciop, Fimp, Inicios, Fims;
                int Id, Id_Departamento, Status;
                listaA = new ArrayList<>();
                while(rs.next()){
                    Id = rs.getInt("id");
                    Iniciop = rs.getString("iniciop");
                    Fimp = rs.getString("fimp");
                    Inicios = rs.getString("inicios");
                    Fims = rs.getString("fims");
                    Id_Departamento = rs.getInt("iddepartamento");
                    Status = 1;
                    
                    anol = new AnoLetivo(Id,Id_Departamento,Iniciop,Fimp, Inicios, Fims, Status);
                    listaA.add(anol);
                }
            }
            connection.close();
         }
         catch(SQLException ex){
             ex.printStackTrace();
         }
        return listaA;
     }
    
    public boolean cadastraAnoLetivo(AnoLetivo anoLetivo) throws Exception{
        try{
            String query = "INSERT INTO anoletivo (iddepartamento, iniciop, fimp, inicios, fims, status)  VALUES (?,?,?,?,?,?)";
                
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setInt(1, anoLetivo.getIddepartamento());
                statement.setString(2, anoLetivo.getIniciop());
                statement.setString(3, anoLetivo.getFimp());
                statement.setString(4, anoLetivo.getInicios());
                statement.setString(5, anoLetivo.getFims());
                statement.setInt(6, 1);
                
                statement.executeUpdate();
            }
            connection.close();
            return true;
        }
        catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }

     }
    
    public boolean alteraAnoLetivo(AnoLetivo anoLetivo){
    	try{
            String query = "UPDATE salaanoletivo SET iddepartamento=?, iniciop=?, fimp=?, inicios=?, fims=? WHERE id=?";
                try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                    statement.setInt(1, anoLetivo.getIddepartamento());
                    statement.setString(2, anoLetivo.getIniciop());
                    statement.setString(3, anoLetivo.getFimp());
                    statement.setString(4, anoLetivo.getInicios());
                    statement.setString(5, anoLetivo.getFims());
                    statement.setInt(6, anoLetivo.getId());
                    
                    statement.executeUpdate();
                }
            connection.close();
            return true;
    	}
    	catch(SQLException sqlex){
            sqlex.printStackTrace();
    	}
    	return false;
    }   
        
    public boolean removeAnoLetivo(int id){
    	try{
            String query = "UPDATE anoletivo SET status=0 WHERE id=?";
                try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                    statement.setInt(1, id);
                    
                    statement.executeUpdate();
                }
            connection.close();
            return true;
        }
    	catch(SQLException ex){
            ex.printStackTrace();
    	}
    	return false;
    }

    public boolean removeDisciplinaUsuario(Usuario usuario, String idDisciplina) {
        String listaDisc = usuario.getId_disciplinas();
        
        try {
            String[] idMaterias = listaDisc.split("-");
            listaDisc = "";
            for(String s : idMaterias) {
                if (!s.equals(idDisciplina)) {
                    if (listaDisc.length() != 0) {
                        listaDisc += "-";
                    }
                    listaDisc += s;
                }
            }
            
            String query = "UPDATE usuario SET disciplinas=? WHERE id=?";
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                statement.setString(1, listaDisc);
                statement.setInt(2, usuario.getId());
                    
                statement.executeUpdate();
            }
            connection.close();
            return true;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}

package AppUemWS.persistencia;

import java.sql.*;

import java.sql.SQLException;

public class Conexao {
	private static final String URL = "jdbc:mysql://localhost:3306/appreserva";
	private static final String USER = "root";
	private static final String SENHA = "4874524";
	
	public static Connection conecta() throws SQLException{
		Connection con;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, SENHA);
            return(con);
        }
        catch(  ClassNotFoundException | SQLException ex){
            System.err.print("Erro: "+ex);
            System.exit(1);
	}
        return null;
	}
}

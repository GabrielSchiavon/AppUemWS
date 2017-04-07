/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AppUemWS;

import AppUemWS.dados.*;
import AppUemWS.persistencia.ControladorDePersistencia;
import com.google.gson.Gson;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author Gabriel H. Tasso Schiavon
 */
@Path("reserva")
public class AppReservaUemWS {

    @Context
    private UriInfo context;
    private ControladorDePersistencia con;

    /**
     * Creates a new instance of AppUemWS
     */
    public AppReservaUemWS() {
    }
    
    private int verificarPrioridadeLogin(Login login) {
        login.setPermissao(-1);
        ArrayList<Login> listaLogin;
        try {
            con = new ControladorDePersistencia();
            listaLogin = con.carregaLoginAtivo();
            for (Login l1 : listaLogin) {
                if (l1.getEmail().equals(login.getEmail())) {
                    if (l1.getSenha().equals(login.getSenha())) {
                        login.clonar(l1);
                        return login.getPermissao();
                    }
                }
            }
        } catch (SQLException ex) {
        }
        return login.getPermissao();
    }
    
    private boolean emailUsado(Usuario usuario) {
        try {
            ArrayList<Login> listaLogin;
            con = new ControladorDePersistencia();
            listaLogin = con.carregaLoginAtivo();
            if (listaLogin.stream().anyMatch((lg) -> 
                    (usuario.getEmail().toUpperCase().equals(lg.getEmail().toUpperCase())))) {
                return true;
            }
        } catch (SQLException ex) {
        }
        return false;
    }
    
    private Login buscarLogin(Login login) {
        ArrayList<Login> listaLogin;
        try {
            con = new ControladorDePersistencia();
            listaLogin = con.carregaLoginAtivo();
            for (Login l1 : listaLogin) {
                if (l1.getEmail().equals(login.getEmail())) {
                    if (l1.getSenha().equals(login.getSenha())) {
                        login.clonar(l1);
                        return login;
                    }
                }
            }
        } catch (SQLException ex) {
        }
        return login;
    }
    
    private boolean emailUsadoPorOutroUsuario(Usuario usuario) {
        try {
            ArrayList<Login> listaLogin;
            con = new ControladorDePersistencia();
            listaLogin = con.carregaLoginAtivo();
            if (listaLogin.stream().filter((lg) 
                    -> (usuario.getEmail().toUpperCase().equals(lg.getEmail().toUpperCase()))).anyMatch((lg) 
                        -> (usuario.getId() != lg.getId()))) {
                return true;
            }
        } catch (SQLException ex) {
        }
        return false;
    }
    
    private Login buscarSenha(String email) {
        ArrayList<Login> listaLogin;
        Login login = new Login();
        try {
            con = new ControladorDePersistencia();
            listaLogin = con.carregaLoginAtivo();
            for (Login l1 : listaLogin) {
                if (l1.getEmail().equals(email)) {
                    login.clonar(l1);
                    return login;
                }
            }
        } catch (SQLException ex) {
        }
        return login;
    }
    
    private Reserva getReserva(int id) {
        Reserva r = new Reserva();
        try {
            ControladorDePersistencia controlador = new ControladorDePersistencia();
            ArrayList<Reserva> listaR = controlador.carregaReserva();
            for (Reserva r1 : listaR) {
                if (r1.getId() == id) {
                    return r1;
                }
            }
        } catch (SQLException ex) {
        }
        return r;
    }
    
    private Usuario getUsuario(int id) {
        Usuario user = new Usuario();
        try {
            ControladorDePersistencia controlador = new ControladorDePersistencia();
            ArrayList<Usuario> lstU = controlador.carregaUsuarioAtivo();
            for (Usuario u : lstU) {
                if (u.getId() == id) {
                    return u;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return user;
    }
    
    private int verificarSemanaAnterior(Reserva reserva) {
        int usou = 0;
        try {
            ControladorDePersistencia controlador = new ControladorDePersistencia();
            for (Reserva r : controlador.carregaReserva()) {
                if (r.getIdusuario() == reserva.getIdusuario()) {
                    if (r.getPeriodo() == reserva.getPeriodo()) {

                        DateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
                        dt.setLenient(false);
                        java.util.Date dataReserva = dt.parse(reserva.getDatareserva());

                        java.util.Date semanaAnterior;
                        semanaAnterior = new java.util.Date();
                        semanaAnterior.setDate(dataReserva.getDate() - 7);

                        String sa = dt.format(semanaAnterior);

                        if (sa.equals(r.getDatareserva())) {
                            if (r.getStatus() == 2) {
                                return 1;
                            }
                        }
                    }
                }
            }
        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
        }
        return usou;
    }
    
    private int getValorPrioridade(Reserva reserva) {
        int valor = 20;
        if (reserva.getStatus() == -1) {
            return 0;
        }
        if (reserva.getTipoaula() == 1) { // pratica
            valor = 20;
            if (getUsuario(reserva.getIdusuario()).getProblema_locomocao() == 0) {
                valor -= 5;
            }
            if (verificarSemanaAnterior(reserva) == 1) {
                valor -= 1;
            }
        }
        if (reserva.getTipoaula() == 21) { // teorica
            valor = 10;
            if (verificarSemanaAnterior(reserva) == 1) {
                valor -= 1;
            }
        }
        if (reserva.getTipoaula() == 3) { // mestrado
            return 90;
        }
        if (reserva.getTipoaula() == 4) { // especializacao
            return 80;
        }
        if (reserva.getTipoaula() == 5) { // defesa
            return 100;
        }
        if (reserva.getTipoaula() == 6) { // minicurso
            return 70;
        }

        return valor;
    }
    
    private void atualizaListaReserva() {
        try {
            ControladorDePersistencia controlador = new ControladorDePersistencia();
            ArrayList<Reserva> lstR = controlador.carregaReserva();
            Reserva reserva = lstR.get(lstR.size() - 1);
            boolean indice_existe = false;
            controlador = new ControladorDePersistencia();
            ArrayList<IndiceReserva> lstIndice = controlador.carregaIndiceReservaAtivo();
            IndiceReserva indiceR = new IndiceReserva();
            for (IndiceReserva ir : lstIndice) {
                if (ir.getIddepartamento() == reserva.getIddepartamento()) {
                    if (ir.getData().equals(reserva.getDatareserva())) {
                        if (ir.getPeriodo() == reserva.getPeriodo()) {
                            if (ir.getClassificacaosala() == reserva.getTiposala()) {
                                indice_existe = true;
                                indiceR.clonar(ir);
                            }
                        }
                    }
                }
            }
            if (indice_existe) {
                // calcular
                if (indiceR.getIdprimeiro() == -1) {
                    // indice exixte mas sem elemento na lista
                    indiceR.setIdprimeiro(reserva.getId());
                    controlador = new ControladorDePersistencia();
                    controlador.alteraIndiceReserva(indiceR);
                } else {
                    Reserva r1 = getReserva(indiceR.getIdprimeiro());

                    if (r1.getStatus() == 2) {
                        java.util.Date atual = new java.util.Date();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(atual);

                        int day = cal.get(Calendar.DAY_OF_WEEK);
                        java.util.Date prox;
                        prox = new java.util.Date();
                        prox.setDate(atual.getDate() + 14 - day);

                        DateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
                        dt.setLenient(false);
                        java.util.Date date = dt.parse(r1.getDatareserva());

                        if (date.getTime() > prox.getTime()) {
                            return;
                        }
                    }

                    if (getValorPrioridade(reserva) > getValorPrioridade(r1)) {
                        reserva.setProximoid(r1.getId());
                        indiceR.setIdprimeiro(reserva.getId());

                        controlador = new ControladorDePersistencia();
                        controlador.alteraIndiceReserva(indiceR);

                        controlador = new ControladorDePersistencia();
                        controlador.alteraReserva(reserva);
                    } else {
                        if (r1.getProximoid() == -1) {
                            r1.setProximoid(reserva.getId());
                            controlador = new ControladorDePersistencia();
                            controlador.alteraReserva(r1);
                        } else {
                            while (true) {
                                Reserva r2 = getReserva(r1.getProximoid());
                                if (getValorPrioridade(reserva) > getValorPrioridade(r2)) {
                                    r1.setProximoid(reserva.getId());
                                    controlador = new ControladorDePersistencia();
                                    controlador.alteraReserva(r1);

                                    reserva.setProximoid(r2.getId());
                                    controlador = new ControladorDePersistencia();
                                    controlador.alteraReserva(reserva);
                                    return;
                                } else {
                                    if (r2.getProximoid() == -1) {
                                        r2.setProximoid(reserva.getId());
                                        controlador = new ControladorDePersistencia();
                                        controlador.alteraReserva(r2);
                                        return;
                                    } else {
                                        r1.clonarReserva(r2);
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                indiceR.setId(-1);
                indiceR.setIddepartamento(reserva.getIddepartamento());
                indiceR.setData(reserva.getDatareserva());
                indiceR.setPeriodo(reserva.getPeriodo());
                indiceR.setClassificacaosala(reserva.getTiposala());
                indiceR.setIdprimeiro(reserva.getId());
                indiceR.setStatus(1);
                controlador = new ControladorDePersistencia();
                controlador.cadastraIndiceReserva(indiceR);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void atualizaSala() {

        try {
            ControladorDePersistencia controlador = new ControladorDePersistencia();
            ArrayList<Reserva> lstR = controlador.carregaReserva();
            Reserva r1 = lstR.get(lstR.size() - 1);

            ArrayList<Sala> lstS = controlador.carregaSalaAtivo();
            java.util.Date atual = new java.util.Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(atual);

            int day = cal.get(Calendar.DAY_OF_WEEK);
            java.util.Date prox;
            prox = new java.util.Date();
            prox.setDate(atual.getDate() + 14 - day);

            DateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
            dt.setLenient(false);
            java.util.Date date = dt.parse(r1.getDatareserva());

            if (date.getTime() > prox.getTime()) {
                return;
            }
            ArrayList<Sala> lstSalaDepartamento = new ArrayList<>();
            lstS.stream().filter((s) 
                    -> (s.getId_departamento() == r1.getIddepartamento())).forEachOrdered((s) 
                            -> {lstSalaDepartamento.add(s);});
            for (Sala sala : lstSalaDepartamento) {
                int usado = 0;
                for (Reserva r : lstR) {
                    if (r.getDatareserva().equals(r1.getDatareserva())) {
                        if (r.getIdsala() == sala.getId()) {
                            usado = 1;
                        }
                    }
                }
                if (usado == 0) {
                    r1.setIdsala(sala.getId());
                    controlador = new ControladorDePersistencia();
                    controlador.alteraReserva(r1);

                    controlador = new ControladorDePersistencia();
                    controlador.alteraStatusReserva(r1.getId(), 2);
                    return;
                }
            }
        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
        }
    }
    
    private IndiceReserva buscarIndiceReserva(ArrayList<IndiceReserva> lstI, 
            String data, int periodo, int tipoSala, int iddepartamento) {
        IndiceReserva indiceReserva = new IndiceReserva();
        indiceReserva.setId(-1);
        lstI.stream().filter((ir) 
                -> (ir.getData().equals(data))).filter((ir) 
                        -> (ir.getPeriodo() == periodo)).filter((ir) 
                                -> (ir.getClassificacaosala() == tipoSala)).filter((ir) 
                                        -> (ir.getIddepartamento() == iddepartamento)).forEachOrdered((ir) 
                                                -> {indiceReserva.clonar(ir);});
        return indiceReserva;
    }

    private void resolveConfrito() {
        ArrayList<IndiceReserva> lstI;
        ArrayList<Reserva> lstR; // lista de todas as reservas
        ArrayList<Sala> lstS; // lista de todas as salas
        ArrayList<Integer> lstP ; // indice do professor que já solicitou uma reserva nesse dia e horario;

        try {
            ControladorDePersistencia controlador = new ControladorDePersistencia();
            lstI = controlador.carregaIndiceReservaAtivo();

            controlador = new ControladorDePersistencia();
            lstR = controlador.carregaReserva();

            controlador = new ControladorDePersistencia();
            lstS = controlador.carregaSalaAtivo();

            java.util.Date atual;
            atual = new java.util.Date();

            Calendar cal = Calendar.getInstance();
            cal.setTime(atual);

            //System.out.println(cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH)+"-"+cal.get(Calendar.DAY_OF_WEEK));
            int diaAtual = cal.get(Calendar.DAY_OF_WEEK);

            int iddepartamento = 1; // verificar para cada departamento, cada um pode ter regra diferente

            ArrayList<Sala> lstProj = new ArrayList<>();
            ArrayList<Sala> lstLab = new ArrayList<>();
            lstS.stream().filter((s) 
                    -> (s.getId_departamento() == iddepartamento)).forEachOrdered((s) 
                            -> {
                if (s.getClassificacao() == 1) {
                    lstLab.add(s);
                } else {
                    lstProj.add(s);
                }
            });
            int qtLab = lstLab.size();
            int qtProj = lstProj.size();

            for (int dia = 2; dia < 7; dia++) {
                java.util.Date dataConflito = new java.util.Date();
                dataConflito.setDate(atual.getDate() + 14 - diaAtual + dia);

                String dt = new SimpleDateFormat("dd/MM/yyyy").format(dataConflito);
                //System.out.println(dt);
                for (int periodo = 1; periodo < 7; periodo++) {
                    lstP = new ArrayList<>();
                    for (int tipoSala = 1; tipoSala < 3; tipoSala++) {

                        IndiceReserva ir = buscarIndiceReserva(lstI, dt, periodo, tipoSala, iddepartamento);
                        int idprox = ir.getIdprimeiro();
                        if (ir.getId() != -1) {
                            //System.out.println(periodo+"-"+tipoSala);
                            if (tipoSala == 1) {
                                if (qtLab > 0) {
                                    for (Sala s : lstLab) {
                                        while (idprox != -1) {
                                            Reserva res = getReserva(idprox);
                                            boolean profJaReservou = false;
                                            if (lstP.size() > 0) {
                                                for (int p : lstP) {
                                                    if (p == res.getIdusuario()) {
                                                        profJaReservou = true;
                                                    }
                                                }
                                            }
                                            if (profJaReservou) {
                                                idprox = res.getProximoid();
                                            } else {

                                                res.setIdsala(s.getId());
                                                controlador = new ControladorDePersistencia();
                                                controlador.alteraReserva(res);

                                                controlador = new ControladorDePersistencia();
                                                controlador.alteraStatusReserva(res.getId(), 2);
                                                idprox = res.getProximoid();
                                                break;
                                            }
                                        }
                                    }
                                    while (idprox != -1) {
                                        Reserva res = getReserva(idprox);
                                        controlador = new ControladorDePersistencia();
                                        controlador.alteraStatusReserva(res.getId(), -2);
                                        idprox = res.getProximoid();
                                    }
                                }
                            } else {
                                if (qtProj > 0) {
                                    for (Sala s : lstProj) {
                                        while (idprox != -1) {
                                            Reserva res = getReserva(idprox);
                                            boolean profJaReservou = false;
                                            if (lstP.size() > 0) {
                                                for (int p : lstP) {
                                                    if (p == res.getIdusuario()) {
                                                        profJaReservou = true;
                                                    }
                                                }
                                            }
                                            if (profJaReservou) {
                                                idprox = res.getProximoid();
                                            } else {

                                                res.setIdsala(s.getId());
                                                controlador = new ControladorDePersistencia();
                                                controlador.alteraReserva(res);

                                                controlador = new ControladorDePersistencia();
                                                controlador.alteraStatusReserva(res.getId(), 2);
                                                idprox = res.getProximoid();
                                                break;
                                            }
                                        }
                                    }
                                    while (idprox != -1) {
                                        Reserva res = getReserva(idprox);
                                        controlador = new ControladorDePersistencia();
                                        controlador.alteraStatusReserva(res.getId(), -2);
                                        idprox = res.getProximoid();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
    
    /**
     * Retrieves representation of an instance of AppUemWS.AppReservaUemWS
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        return "Teste de Retorno";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/login/confirmarLogin/{login}")
    public String confirmarLogin(@PathParam("login") String log) {
        Gson g = new Gson();
        Login login = g.fromJson(log, Login.class);
        return g.toJson(verificarPrioridadeLogin(login));
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/departamento/carregarDepartamento")
    public String carregarDepartamento() {
        Gson g = new Gson();
        ArrayList<Departamento> listDepartamento = new ArrayList<>();
        try {
            con = new ControladorDePersistencia();
            listDepartamento = con.carregaDepartamentoAtivo();
        } catch (SQLException ex) {
        }
        return g.toJson(listDepartamento);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/usuario/carregarUsuario")
    public String carregarUsuario() {
        Gson g = new Gson();
        ArrayList<Usuario> listaUsuario = new ArrayList<>();
        
        try {
            con = new ControladorDePersistencia();
            listaUsuario = con.carregaUsuarioAtivo();
        } catch (SQLException ex) {
        }
        for (Usuario user : listaUsuario) {
            user.setSenha("");
        }
        return g.toJson(listaUsuario);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/usuario/cadastrarUsuario")
    public String cadastrarUsuario(String encap) {
        Gson g = new Gson();
        Encapsular encapsular = g.fromJson(encap, Encapsular.class);
        Login login = g.fromJson(encapsular.getCampo1(), Login.class);
        Usuario usuario = g.fromJson(encapsular.getCampo2(), Usuario.class);
        int permissao = this.verificarPrioridadeLogin(login);
        int resultado = -1; // -1 = sem permissao
        
        if ((permissao == 4) || (permissao > usuario.getPermissao())) {
            try {
                if (emailUsado(usuario)) {
                    resultado = -2;
                } else {
                    con = new ControladorDePersistencia();
                    Boolean ok = con.cadastraUsuario(usuario);
                    if (ok) {
                        resultado = 1; // sucesso
                    } else {
                        resultado = 0; // falha
                    }
                }
            } catch (Exception ex) {
                resultado = 0; // falha
            }
        }
        return g.toJson(resultado);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/usuario/alterarUsuario")
    public String alterarUsuario(String encap) {
        Gson g = new Gson();
        Encapsular encapsular = g.fromJson(encap, Encapsular.class);
        Login login = g.fromJson(encapsular.getCampo1(), Login.class);
        Usuario usuario = g.fromJson(encapsular.getCampo2(), Usuario.class);
        int permissao = this.verificarPrioridadeLogin(login);
        int resultado = -1; // -1 = sem permissao
        login.clonar(buscarLogin(login));
        
        if ((permissao == 4) || (permissao > usuario.getPermissao()) || login.getId() == usuario.getId()) {
            try {
                if (emailUsadoPorOutroUsuario(usuario)) {
                    resultado = -2;
                } else {
                    con = new ControladorDePersistencia();
                    Boolean ok = con.alteraUsuario(usuario);
                    if (ok) {
                        resultado = 1; // sucesso
                    } else {
                        resultado = 0; // falha
                    }
                }
            } catch (SQLException ex) {
                resultado = 0; // falha
            }
        }
        return g.toJson(resultado);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/usuario/removeUsuario")
    public String removeUsuario(String encap) {
        Gson g = new Gson();
        Encapsular encapsular = g.fromJson(encap, Encapsular.class);
        Login login = g.fromJson(encapsular.getCampo1(), Login.class);
        Usuario usuario = g.fromJson(encapsular.getCampo2(), Usuario.class);
        int permissao = this.verificarPrioridadeLogin(login);
        int resultado = -1; // -1 = sem permissao
        
        if ((permissao == 4) || (permissao > usuario.getPermissao())) {
            try {
                con = new ControladorDePersistencia();
                Boolean ok = con.removeUsuario(usuario.getId());
                if (ok) {
                    resultado = 1; // sucesso
                } else {
                    resultado = 0; // falha
                }
            } catch (SQLException ex) {
                resultado = 0; // falha
            }
        }
        return g.toJson(resultado);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/usuario/alterarSenha")
    public String alterarSenha(String encap) {
        Gson g = new Gson();
        Encapsular encapsular = g.fromJson(encap, Encapsular.class);
        Login login = g.fromJson(encapsular.getCampo1(), Login.class);
        String senha = encapsular.getCampo2();
        int permissao = this.verificarPrioridadeLogin(login);
        int resultado = -1; // -1 = sem permissao
        
        if (permissao != -1) {
            try {
                login.setSenha(senha);
                con = new ControladorDePersistencia();
                Boolean ok = con.alteraSenha(login);
                if (ok) {
                    resultado = 1; // sucesso
                } else {
                    resultado = 0; // falha
                }
            } catch (SQLException ex) {
                resultado = 0; // falha
            }
        }
        return g.toJson(resultado);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/usuario/recuperarSenha/{email}")
    public String recuperarSenha(@PathParam("email") String email) {
        Gson g = new Gson();
        Login login = this.buscarSenha(email);
        int resultado = -1; // -1 = sem permissao
        if (login.getId() != -1) {
            //enviaEmail(login);
            resultado = 1;
        }
        return g.toJson(resultado);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/sala/carregarSala")
    public String carregarSala() {
        Gson g = new Gson();
        ArrayList<Sala> listaSala = new ArrayList<>();
        try {
            con = new ControladorDePersistencia();
            listaSala = con.carregaSalaAtivo();
        } catch (SQLException ex) {
        }
        return g.toJson(listaSala);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/sala/cadastrarSala")
    public String cadastrarSala(String encap) {
        Gson g = new Gson();
        Encapsular encapsular = g.fromJson(encap, Encapsular.class);
        Login login = g.fromJson(encapsular.getCampo1(), Login.class);
        Sala sala = g.fromJson(encapsular.getCampo2(), Sala.class);
        int permissao = this.verificarPrioridadeLogin(login);
        int resultado = -1; // -1 = sem permissao
        
        if (permissao > 1) {
            try {
                con = new ControladorDePersistencia();
                Boolean ok = con.cadastraSala(sala);
                if (ok) {
                    resultado = 1; // sucesso
                } else {
                    resultado = 0; // falha
                }
            } catch (Exception ex) {
                resultado = 0; // falha
            }
        }
        return g.toJson(resultado);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/sala/alterarSala")
    public String alterarSala(String encap) {
        Gson g = new Gson();
        Encapsular encapsular = g.fromJson(encap, Encapsular.class);
        Login login = g.fromJson(encapsular.getCampo1(), Login.class);
        Sala sala = g.fromJson(encapsular.getCampo2(), Sala.class);
        int permissao = this.verificarPrioridadeLogin(login);
        int resultado = -1; // -1 = sem permissao
        
        if (permissao > 1) {
            try {
                con = new ControladorDePersistencia();
                Boolean ok = con.alteraSala(sala);
                if (ok) {
                    resultado = 1; // sucesso
                } else {
                    resultado = 0; // falha
                }
            } catch (SQLException ex) {
                resultado = 0; // falha
            }
        }
        return g.toJson(resultado);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/sala/removeSala")
    public String removeSala(String encap) {
        Gson g = new Gson();
        Encapsular encapsular = g.fromJson(encap, Encapsular.class);
        Login login = g.fromJson(encapsular.getCampo1(), Login.class);
        Sala sala = g.fromJson(encapsular.getCampo2(), Sala.class);
        int permissao = this.verificarPrioridadeLogin(login);
        int resultado = -1; // -1 = sem permissao
        
        if (permissao > 1) {
            try {
                con = new ControladorDePersistencia();
                Boolean ok = con.removeSala(sala.getId());
                if (ok) {
                    resultado = 1; // sucesso
                } else {
                    resultado = 0; // falha
                }
            } catch (SQLException ex) {
                resultado = 0; // falha
            }
        }
        return g.toJson(resultado);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/curso/carregarCurso")
    public String carregarCurso() {
        Gson g = new Gson();
        ArrayList<Curso> listaCurso = new ArrayList<>();
        try {
            con = new ControladorDePersistencia();
            listaCurso = con.carregaCursoAtivo();
        } catch (SQLException ex) {
        }
        return g.toJson(listaCurso);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/curso/cadastrarCurso")
    public String cadastrarCurso(String encap) {
        Gson g = new Gson();
        Encapsular encapsular = g.fromJson(encap, Encapsular.class);
        Login login = g.fromJson(encapsular.getCampo1(), Login.class);
        Curso curso = g.fromJson(encapsular.getCampo2(), Curso.class);
        int permissao = this.verificarPrioridadeLogin(login);
        int resultado = -1; // -1 = sem permissao
        if (permissao > 1) {
            try {
                con = new ControladorDePersistencia();
                Boolean ok = con.cadastraCurso(curso);
                if (ok) {
                    resultado = 1; // sucesso
                } else {
                    resultado = 0; // falha
                }
            } catch (Exception ex) {
                resultado = 0; // falha
            }
        }
        return g.toJson(resultado);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/curso/alterarCurso")
    public String alterarCurso(String encap) {
        Gson g = new Gson();
        Encapsular encapsular = g.fromJson(encap, Encapsular.class);
        Login login = g.fromJson(encapsular.getCampo1(), Login.class);
        Curso curso = g.fromJson(encapsular.getCampo2(), Curso.class);
        int permissao = this.verificarPrioridadeLogin(login);
        int resultado = -1; // -1 = sem permissao
        if (permissao > 1) {
            try {
                con = new ControladorDePersistencia();
                Boolean ok = con.alteraCurso(curso);
                if (ok) {
                    resultado = 1; // sucesso
                } else {
                    resultado = 0; // falha
                }
            } catch (SQLException ex) {
                resultado = 0; // falha
            }
        }
        return g.toJson(resultado);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/curso/removerCurso")
    public String removeCurso(String encap) {
        Gson g = new Gson();
        Encapsular encapsular = g.fromJson(encap, Encapsular.class);
        Login login = g.fromJson(encapsular.getCampo1(), Login.class);
        Curso curso = g.fromJson(encapsular.getCampo2(), Curso.class);
        int permissao = this.verificarPrioridadeLogin(login);
        int resultado = -1; // -1 = sem permissao
        if (permissao > 1) {
            try {
                con = new ControladorDePersistencia();
                Boolean ok = con.removeCurso(curso.getId());
                if (ok) {
                    resultado = 1; // sucesso
                } else {
                    resultado = 0; // falha
                }
            } catch (SQLException ex) {
                resultado = 0; // falha
            }
        }
        return g.toJson(resultado);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/disciplina/carregarDisciplina")
    public String carregarDisciplina() {
        Gson g = new Gson();
        ArrayList<Disciplina> listaDisciplina = new ArrayList<>();
        try {
            con = new ControladorDePersistencia();
            listaDisciplina = con.carregaDisciplinaAtivo();
        } catch (SQLException ex) {
        }
        return g.toJson(listaDisciplina);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/disciplina/cadastrarDisciplina")
    public String cadastrarDisciplina(String encap) {
        Gson g = new Gson();
        Encapsular encapsular = g.fromJson(encap, Encapsular.class);
        Login login = g.fromJson(encapsular.getCampo1(), Login.class);
        Disciplina disciplina = g.fromJson(encapsular.getCampo2(), Disciplina.class);
        int permissao = this.verificarPrioridadeLogin(login);
        int resultado = -1; // -1 = sem permissao
        
        if (permissao > 1) {
            try {

                con = new ControladorDePersistencia();
                Boolean ok = con.cadastraDisciplina(disciplina);
                if (ok) {
                    resultado = 1; // sucesso
                } else {
                    resultado = 0; // falha
                }
            } catch (Exception ex) {
                resultado = 0; // falha
            }
        }
        return g.toJson(resultado);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/disciplina/alterarDisciplina")
    public String alterarDisciplina(String encap) {
        Gson g = new Gson();
        Encapsular encapsular = g.fromJson(encap, Encapsular.class);
        Login login = g.fromJson(encapsular.getCampo1(), Login.class);
        Disciplina disciplina = g.fromJson(encapsular.getCampo2(), Disciplina.class);
        int permissao = this.verificarPrioridadeLogin(login);
        int resultado = -1; // -1 = sem permissao
        if (permissao > 1) {
            try {

                con = new ControladorDePersistencia();
                Boolean ok = con.alteraDisciplina(disciplina);
                if (ok) {
                    resultado = 1; // sucesso
                } else {
                    resultado = 0; // falha
                }
            } catch (SQLException ex) {
                resultado = 0; // falha
            }
        }
        return g.toJson(resultado);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/disciplina/removeDisciplina")
    public String removeDisciplina(String encap) {
        Gson g = new Gson();
        Encapsular encapsular = g.fromJson(encap, Encapsular.class);
        Login login = g.fromJson(encapsular.getCampo1(), Login.class);
        Disciplina disciplina = g.fromJson(encapsular.getCampo2(), Disciplina.class);
        int permissao = this.verificarPrioridadeLogin(login);
        int resultado = -1; // -1 = sem permissao
        
        if (permissao > 1) {
            try {
                con = new ControladorDePersistencia();
                Boolean ok = con.removeDisciplina(disciplina.getId());
                if (ok) {
                    resultado = 1; // sucesso
                } else {
                    resultado = 0; // falha
                }
            } catch (SQLException ex) {
                resultado = 0; // falha
            }
        }
        return g.toJson(resultado);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/reserva/carregarReserva")
    public String carregarReserva() {
        Gson g = new Gson();
        ArrayList<Reserva> listaReserva = new ArrayList<>();
        try {
            con = new ControladorDePersistencia();
            listaReserva = con.carregaReserva();
        } catch (SQLException ex) {
        }
        return g.toJson(listaReserva);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("usuario/insereDisciplinaDocente")
    public String insereDisciplinaDocente(String encap) {
        Gson g = new Gson();
        Encapsular encapsular = g.fromJson(encap, Encapsular.class);
        Login login = g.fromJson(encapsular.getCampo1(), Login.class);
        Usuario usuario = g.fromJson(encapsular.getCampo2(), Usuario.class);
        Disciplina disciplina = g.fromJson(encapsular.getCampo3(), Disciplina.class);
        
        int permissao = this.verificarPrioridadeLogin(login);
        int resultado = -1; // -1 = sem permissao
        if (permissao > 1) {
            try {
                if (disciplina.getId_departamento() != usuario.getId_departamento()) {
                    return g.toJson(2); // disciplina não é relacionado ao departamento do usuario
                }
                con = new ControladorDePersistencia();
                Boolean ok = con.insereDisciplinaUsuario(usuario, disciplina.getId());
                if (ok) {
                    resultado = 1; // sucesso
                } else {
                    resultado = 0; // falha
                }
            } catch (SQLException ex) {
                resultado = 0; // falha
            }
        }
        return g.toJson(resultado);
    }
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/reserva/solicitarReserva")
    public String solicitarReserva(String encap) {
        Gson g = new Gson();
        Encapsular encapsular = g.fromJson(encap, Encapsular.class);
        Login login = g.fromJson(encapsular.getCampo1(), Login.class);
        Reserva reserva = g.fromJson(encapsular.getCampo2(), Reserva.class);
        int permissao = this.verificarPrioridadeLogin(login);
        int resultado = -1; // -1 = sem permissao
        
        if (permissao >= 1) {
            try {
                con = new ControladorDePersistencia();
                //reserva.setProximoid(this.getProxReserva(reserva));
                Boolean ok = con.cadastraReserva(reserva);
                if (ok) {
                    //atualizaIndiceReserva();
                    atualizaListaReserva();
                    atualizaSala();

                    resultado = 1; // sucesso
                } else {
                    resultado = 0; // falha
                }
            } catch (Exception ex) {
                resultado = 0; // falha
            }
        }

        return g.toJson(resultado);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/reserva/alterarReserva")
    public String alterarReserva(String encap) {
        Gson g = new Gson();
        Encapsular encapsular = g.fromJson(encap, Encapsular.class);
        Login login = g.fromJson(encapsular.getCampo1(), Login.class);
        Reserva reserva = g.fromJson(encapsular.getCampo2(), Reserva.class);
        int permissao = this.verificarPrioridadeLogin(login);
        int resultado = -1; // -1 = sem permissao
        
        if (permissao > 1) {
            try {

                con = new ControladorDePersistencia();
                Boolean ok = con.alteraReserva(reserva);
                if (ok) {
                    resultado = 1; // sucesso
                } else {
                    resultado = 0; // falha
                }
            } catch (SQLException ex) {
                resultado = 0; // falha
            }
        }
        return g.toJson(resultado);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/reserva/removeReserva")
    public String removeReserva(String encap) {
        Gson g = new Gson();
        Encapsular encapsular = g.fromJson(encap, Encapsular.class);
        Login login = g.fromJson(encapsular.getCampo1(), Login.class);
        Reserva reserva = g.fromJson(encapsular.getCampo2(), Reserva.class);
        int permissao = this.verificarPrioridadeLogin(login);
        int resultado = -1; // -1 = sem permissao
        if (permissao > 1) {
            try {
                con = new ControladorDePersistencia();
                Boolean ok = con.removeReserva(reserva.getId());
                if (ok) {
                    resultado = 1; // sucesso
                } else {
                    resultado = 0; // falha
                }
            } catch (SQLException ex) {
                resultado = 0; // falha
            }
        }
        return g.toJson(resultado);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/reserva/alteraStatusReserva")
    public String alteraStatusReserva(String encap) {
        Gson g = new Gson();
        Encapsular encapsular = g.fromJson(encap, Encapsular.class);
        Login login = g.fromJson(encapsular.getCampo1(), Login.class);
        Reserva reserva = g.fromJson(encapsular.getCampo2(), Reserva.class);
        int permissao = this.verificarPrioridadeLogin(login);
        int resultado = -1; // -1 = sem permissao
        if (permissao > 1) {
            try {
                con = new ControladorDePersistencia();
                Boolean ok = con.alteraStatusReserva(reserva.getId(), reserva.getStatus());
                if (ok) {
                    resultado = 1; // sucesso
                } else {
                    resultado = 0; // falha   
                }
            } catch (SQLException ex) {
                resultado = 0; // falha
            }
        }
        return g.toJson(resultado);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/anoLetivo/carregarAnoLetivo")
    public String carregarAnoLetivo() {
        Gson g = new Gson();
        ArrayList<AnoLetivo> lstAnoL = new ArrayList<>();
        try {
            lstAnoL = con.carregaAnoLetivoAtivo();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return g.toJson(lstAnoL);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/anoLetivo/cadastrarAnoLetivo")
    public String cadastrarAnoLetivo(String encap) {
        Gson g = new Gson();
        Encapsular encapsular = g.fromJson(encap, Encapsular.class);
        Login login = g.fromJson(encapsular.getCampo1(), Login.class);
        AnoLetivo anoLetivo = g.fromJson(encapsular.getCampo2(), AnoLetivo.class);
        int permissao = this.verificarPrioridadeLogin(login);
        int resultado = -1; // -1 = sem permissao
        
        if (permissao > 1) {
            try {
                con = new ControladorDePersistencia();
                Boolean ok = con.cadastraAnoLetivo(anoLetivo);
                if (ok) {
                    resultado = 1; // sucesso
                } else {
                    resultado = 0; // falha
                }
            } catch (Exception ex) {
                resultado = 0; // falha
            }
        }
        return g.toJson(resultado);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/anoLetivo/alterarAnoLetivo")
    public String alterarAnoLetivo(String encap) {
        Gson g = new Gson();
        Encapsular encapsular = g.fromJson(encap, Encapsular.class);
        Login login = g.fromJson(encapsular.getCampo1(), Login.class);
        AnoLetivo anoLetivo = g.fromJson(encapsular.getCampo2(), AnoLetivo.class);
        int permissao = this.verificarPrioridadeLogin(login);
        int resultado = -1; // -1 = sem permissao
        if (permissao > 1) {
            try {
                con = new ControladorDePersistencia();
                Boolean ok = con.alteraAnoLetivo(anoLetivo);
                if (ok) {
                    resultado = 1; // sucesso
                } else {
                    resultado = 0; // falha
                }
            } catch (SQLException ex) {
                resultado = 0; // falha
            }
        }
        return g.toJson(resultado);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/anoLetivo/removeAnoLetivo")
    public String removeAnoLetivo(String encap) {
        Gson g = new Gson();
        Encapsular encapsular = g.fromJson(encap, Encapsular.class);
        Login login = g.fromJson(encapsular.getCampo1(), Login.class);
        AnoLetivo anoLetivo = g.fromJson(encapsular.getCampo2(), AnoLetivo.class);
        int permissao = this.verificarPrioridadeLogin(login);
        int resultado = -1; // -1 = sem permissao
        if (permissao > 1) {
            try {
                con = new ControladorDePersistencia();
                Boolean ok = con.removeAnoLetivo(anoLetivo.getId());
                if (ok) {
                    resultado = 1; // sucesso
                } else {
                    resultado = 0; // falha
                }
            } catch (SQLException ex) {
                resultado = 0; // falha
            }
        }
        return g.toJson(resultado);
    }
}

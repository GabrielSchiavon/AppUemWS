/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AppUemWS.dados;

/**
 *
 * @author Gabriel H. Tasso Schiavon
 */
public class Encapsular {
    private String campo1;
    private String campo2;
    private String campo3;
    
    public Encapsular(){}
    public Encapsular(String c1, String c2){
        campo1 = c1;
        campo2 = c2;
    }
    public Encapsular(String c1, String c2, String c3) {
        campo1 = c1;
        campo2 = c2;
        campo3 = c3;
    }

    public String getCampo1() {
        return campo1;
    }

    public void setCampo1(String campo1) {
        this.campo1 = campo1;
    }

    public String getCampo2() {
        return campo2;
    }

    public void setCampo2(String campo2) {
        this.campo2 = campo2;
    }

    public String getCampo3() {
        return campo3;
    }

    public void setCampo3(String campo3) {
        this.campo3 = campo3;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package print;

/**
 *
 * @author hdglo
 */
public class Configuracoes {
    
    private String ip;
    private String porta;
    private String username;
    private String password;

    public Configuracoes(String ip, String porta, String username, String password) {
        this.ip = ip;
        this.porta = porta;
        this.username = username;
        this.password = password;
    }

    public Configuracoes() {
    }
    
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPorta() {
        return porta;
    }

    public void setPorta(String porta) {
        this.porta = porta;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    
    
}

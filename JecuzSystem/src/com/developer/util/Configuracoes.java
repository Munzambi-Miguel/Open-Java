/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.util;

/**
 *
 * @author hdglo
 */
public class Configuracoes {
    
    private String ip;
    private String porta;
    private String username;
    private String password;
    private String director;

    public Configuracoes() {
    }
    
    

    public Configuracoes(String ip, String porta, String username, String password, String director) {
        this.ip = ip;
        this.porta = porta;
        this.username = username;
        this.password = password;
        this.director = director;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    
    
    
}

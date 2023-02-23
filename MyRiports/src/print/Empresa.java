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
public class Empresa {
    
    private String nome;
    private String nContribuente;
    private String telefone;
    private String email;
    private String localizacao;

    public Empresa(String nome, String nContribuente, String telefone, String email, String localizacao) {
        this.nome = nome;
        this.nContribuente = nContribuente;
        this.telefone = telefone;
        this.email = email;
        this.localizacao = localizacao;
    }

    public Empresa() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getnContribuente() {
        return nContribuente;
    }

    public void setnContribuente(String nContribuente) {
        this.nContribuente = nContribuente;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }
    
    
    
}

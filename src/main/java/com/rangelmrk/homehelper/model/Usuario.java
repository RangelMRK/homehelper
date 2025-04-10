package com.rangelmrk.homehelper.model;

public class Usuario {
    private String id;
    private String username;
    private String senha;

    public Usuario() {}

    public Usuario(String id, String username, String senha) {
        this.id = id;
        this.username = username;
        this.senha = senha;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
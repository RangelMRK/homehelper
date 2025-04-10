package com.rangelmrk.homehelper.dto;

public class UsuarioDTO {

    public static class RegistroRequest {
        private String username;
        private String senha;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getSenha() { return senha; }
        public void setSenha(String senha) { this.senha = senha; }
    }
}

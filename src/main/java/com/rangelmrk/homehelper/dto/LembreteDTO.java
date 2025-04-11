package com.rangelmrk.homehelper.dto;

public class LembreteDTO {

    public static class CriarLembreteRequest {
        private String nome;
        private String descricao;
        private String dataAlvo; // Data do lembrete
        private String hora; // Hora específica
        private String autor;

        // Getters e setters

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }
        public String getDataAlvo() { return dataAlvo; }
        public void setDataAlvo(String dataAlvo) { this.dataAlvo = dataAlvo; }
        public String getHora() { return hora; }
        public void setHora(String hora) { this.hora = hora; }
        public String getAutor() { return autor; }
        public void setAutor(String autor) { this.autor = autor; }
    }
}

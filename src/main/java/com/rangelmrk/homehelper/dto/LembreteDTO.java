package com.rangelmrk.homehelper.dto;

public class LembreteDTO {

    public static class CriarLembreteRequest {
        private String titulo;
        private String descricao;
        private String autor;

        public CriarLembreteRequest() {}

        public CriarLembreteRequest(String titulo, String descricao, String autor) {
            this.titulo = titulo;
            this.descricao = descricao;
            this.autor = autor;
        }

        public String getTitulo() { return titulo; }
        public String getDescricao() { return descricao; }
        public String getAutor() { return autor; }

        public void setTitulo(String titulo) { this.titulo = titulo; }
        public void setDescricao(String descricao) { this.descricao = descricao; }
        public void setAutor(String autor) { this.autor = autor; }
    }
}

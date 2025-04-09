package com.rangelmrk.homehelper.dto;

public class ListaComprasDTO {

    public static class RemoverItemRequest {
        private String nome;

        public RemoverItemRequest() {}

        public RemoverItemRequest(String nome) {
            this.nome = nome;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }
    }

    public static class AtualizarStatusItemRequest {
        private String nome;
        private boolean comprado;

        public AtualizarStatusItemRequest() {}

        public AtualizarStatusItemRequest(String nome, boolean comprado) {
            this.nome = nome;
            this.comprado = comprado;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public boolean isComprado() {
            return comprado;
        }

        public void setComprado(boolean comprado) {
            this.comprado = comprado;
        }
    }
}

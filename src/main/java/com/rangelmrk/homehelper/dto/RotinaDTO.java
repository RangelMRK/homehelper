package com.rangelmrk.homehelper.dto;

import java.util.List;

public class RotinaDTO {

    public static class CriarRotinaRequest {
        private String nome;
        private String descricao;
        private boolean repetir;
        private List<String> dias;

        public CriarRotinaRequest() {}

        public CriarRotinaRequest(String nome, String descricao, boolean repetir, List<String> dias) {
            this.nome = nome;
            this.descricao = descricao;
            this.repetir = repetir;
            this.dias = dias;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public boolean isRepetir() {
            return repetir;
        }

        public void setRepetir(boolean repetir) {
            this.repetir = repetir;
        }

        public List<String> getDias() {
            return dias;
        }

        public void setDias(List<String> dias) {
            this.dias = dias;
        }
    }

    public static class ConcluirTarefaRequest {
        private String id;

        public ConcluirTarefaRequest() {}

        public ConcluirTarefaRequest(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}

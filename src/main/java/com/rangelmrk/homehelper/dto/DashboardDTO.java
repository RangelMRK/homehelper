package com.rangelmrk.homehelper.dto;

public class DashboardDTO {

    public static class ItemHoje {
        private String id;
        private String descricao;
        private String tipo; // "rotina" ou "lembrete"
        private boolean concluido;
        private String autor;

        public ItemHoje() {}

        public ItemHoje(String id, String descricao, String tipo, boolean concluido, String autor) {
            this.id = id;
            this.descricao = descricao;
            this.tipo = tipo;
            this.concluido = concluido;
            this.autor = autor;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }

        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }

        public boolean isConcluido() { return concluido; }
        public void setConcluido(boolean concluido) { this.concluido = concluido; }

        public String getAutor() { return autor; }
        public void setAutor(String autor) { this.autor = autor; }
    }

    public static class ConcluirTarefaRequest {
        private String id;
        private boolean concluido;
        private String autor;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public boolean isConcluido() { return concluido; }
        public void setConcluido(boolean concluido) { this.concluido = concluido; }

        public String getAutor() { return autor; }
        public void setAutor(String autor) { this.autor = autor; }
    }
}

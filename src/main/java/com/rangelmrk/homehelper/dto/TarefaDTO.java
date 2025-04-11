package com.rangelmrk.homehelper.dto;

import java.util.List;

public class TarefaDTO {

    public static class CriarTarefa {
        private String nome;
        private String descricao;
        private String autor;
        private String dataAlvo; // Para tarefas únicas
        private List<String> dias; // Para tarefas repetitivas (dias da semana)
        private boolean repetir; // Se a tarefa for repetitiva ou não

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }

        public String getAutor() { return autor; }
        public void setAutor(String autor) { this.autor = autor; }

        public String getDataAlvo() { return dataAlvo; }
        public void setDataAlvo(String dataAlvo) { this.dataAlvo = dataAlvo; }

        public List<String> getDias() { return dias; }
        public void setDias(List<String> dias) { this.dias = dias; }

        public boolean isRepetir() { return repetir; }
        public void setRepetir(boolean repetir) { this.repetir = repetir; }
    }

    public static class ToggleRequest {
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

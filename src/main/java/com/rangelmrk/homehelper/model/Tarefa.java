package com.rangelmrk.homehelper.model;

import java.util.List;

public class Tarefa {

    private String id;
    private String nome;
    private String descricao;
    private boolean repetir;
    private List<String> dias;
    private String dataAlvo;
    private String autor;
    private boolean concluido;
    private String ultimaAtualizacao;

    public Tarefa(){};

    public Tarefa(String id, String nome, String descricao, boolean repetir, List<String> dias, String dataAlvo, String autor) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.repetir = repetir;
        this.dias = dias;
        this.dataAlvo = dataAlvo;
        this.autor = autor;
        this.concluido = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public boolean isRepetir() { return repetir; }
    public void setRepetir(boolean repetir) { this.repetir = repetir; }
    public List<String> getDias() { return dias; }
    public void setDias(List<String> dias) { this.dias = dias; }
    public String getDataAlvo() { return dataAlvo; }
    public void setDataAlvo(String dataAlvo) { this.dataAlvo = dataAlvo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public boolean isConcluido() { return concluido; }
    public void setConcluido(boolean concluido) { this.concluido = concluido; }
    public String getUltimaAtualizacao(){ return ultimaAtualizacao; }
}

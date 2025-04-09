package com.rangelmrk.homehelper.model;

public class Lembrete {
    private String id;
    private String titulo;
    private String descricao;
    private String autor;
    private boolean ativo;

    public Lembrete() {}

    public Lembrete(String id, String titulo, String descricao, String autor, boolean ativo) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.autor = autor;
        this.ativo = ativo;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public String getAutor() { return autor; }
    public boolean isAtivo() { return ativo; }

    public void setId(String id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setAutor(String autor) { this.autor = autor; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}

package com.rangelmrk.homehelper.model;

public class Lembrete {

    private String id;
    private String nome;
    private String descricao;
    private String dataAlvo; // Data única do lembrete
    private String hora; // Hora específica
    private String autor;
    private boolean concluidoHoje;

    public Lembrete() {}

    public Lembrete(String id, String nome, String descricao, String dataAlvo, String hora, String autor) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.dataAlvo = dataAlvo;
        this.hora = hora;
        this.autor = autor;
        this.concluidoHoje = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
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
    public boolean isConcluidoHoje() { return concluidoHoje; }
    public void setConcluidoHoje(boolean concluidoHoje) { this.concluidoHoje = concluidoHoje; }
}

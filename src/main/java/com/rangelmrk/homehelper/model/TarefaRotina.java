package com.rangelmrk.homehelper.model;

import java.util.List;

public class TarefaRotina {
    private String id;
    private String nome;
    private String descricao;
    private List<String> dias;
    private boolean repetir;
    private boolean concluidoHoje;
    private String ultimaAtualizacao;

    public TarefaRotina() {}

    public TarefaRotina(String id, String descricao, List<String> dias, boolean repetir, boolean concluidoHoje, String ultimaAtualizacao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.dias = dias;
        this.repetir = repetir;
        this.concluidoHoje = concluidoHoje;
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public List<String> getDias() { return dias; }
    public boolean isRepetir() { return repetir; }
    public boolean isConcluidoHoje() { return concluidoHoje; }
    public String getUltimaAtualizacao() { return ultimaAtualizacao; }

    public void setId(String id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setDias(List<String> dias) { this.dias = dias; }
    public void setRepetir(boolean repetir) { this.repetir = repetir; }
    public void setConcluidoHoje(boolean concluidoHoje) { this.concluidoHoje = concluidoHoje; }
    public void setUltimaAtualizacao(String ultimaAtualizacao) { this.ultimaAtualizacao = ultimaAtualizacao; }
}

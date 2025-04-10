package com.rangelmrk.homehelper.model;

import java.util.List;

public class TarefaRotina {
    private String id;
    private String nome;
    private String descricao;
    private List<String> dias;
    private boolean repetir;
    private boolean concluidoHoje;
    private String concluidoPor;
    private String ultimaAtualizacao;

    public TarefaRotina() {}

    public TarefaRotina(String id, String descricao, List<String> dias, boolean repetir, boolean concluidoHoje, String ultimaAtualizacao) {
        this.id = id;
        this.descricao = descricao;
        this.dias = dias;
        this.repetir = repetir;
        this.concluidoHoje = concluidoHoje;
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public List<String> getDias() { return dias; }
    public void setDias(List<String> dias) { this.dias = dias; }

    public boolean isRepetir() { return repetir; }
    public void setRepetir(boolean repetir) { this.repetir = repetir; }

    public boolean isConcluidoHoje() { return concluidoHoje; }
    public void setConcluidoHoje(boolean concluidoHoje) { this.concluidoHoje = concluidoHoje; }

    public String getUltimaAtualizacao() { return ultimaAtualizacao; }
    public void setUltimaAtualizacao(String ultimaAtualizacao) { this.ultimaAtualizacao = ultimaAtualizacao; }

    public String getConcluidoPor() { return concluidoPor; }
    public void setConcluidoPor(String concluidoPor) { this.concluidoPor = concluidoPor; }
}

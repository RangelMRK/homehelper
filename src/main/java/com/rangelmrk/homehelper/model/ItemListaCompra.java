package com.rangelmrk.homehelper.model;

public class ItemListaCompra {
    private String nome;
    private boolean comprado;

    public ItemListaCompra() {}

    public ItemListaCompra(String nome, boolean comprado) {
        this.nome = nome;
        this.comprado = comprado;
    }

    public String getNome() {
        return nome;
    }

    public boolean isComprado() {
        return comprado;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setComprado(boolean comprado) {
        this.comprado = comprado;
    }
}

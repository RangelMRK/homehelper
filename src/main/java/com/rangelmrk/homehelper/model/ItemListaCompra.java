package com.rangelmrk.homehelper.model;

public class ItemListaCompra {
    private String id;
    private String nome;
    private boolean comprado;

    public ItemListaCompra() {}

    public ItemListaCompra(String id, String nome, boolean comprado) {
        this.id = id;
        this.nome = nome;
        this.comprado = comprado;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public boolean isComprado() { return comprado; }
    public void setComprado(boolean comprado) { this.comprado = comprado; }
}

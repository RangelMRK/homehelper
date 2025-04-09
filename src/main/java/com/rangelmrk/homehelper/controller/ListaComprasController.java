package com.rangelmrk.homehelper.controller;

import com.rangelmrk.homehelper.dto.ListaComprasDTO;
import com.rangelmrk.homehelper.model.ItemListaCompra;
import com.rangelmrk.homehelper.service.ListaComprasService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/lista-compras")
class ListaComprasController {

    private final ListaComprasService service;

    @Autowired
    public ListaComprasController(ListaComprasService service) {
        this.service = service;
    }

    @Operation(summary = "Lista todos os itens da lista de compras")
    @GetMapping
    public List<ItemListaCompra> listar() throws ExecutionException, InterruptedException {
        return service.listarItens();
    }

    @Operation(summary = "Adiciona um novo item à lista de compras")
    @PostMapping
    public void adicionar(@RequestBody ItemListaCompra item) {
        service.adicionarItem(item);
    }

    @Operation(summary = "Atualiza o status de um item para comprado ou não")
    @PutMapping("/{nome}")
    public void atualizar(@PathVariable String nome, @RequestParam boolean comprado) {
        service.atualizarStatus(nome, comprado);
    }

    @Operation(summary = "Remove um item da lista de compras")
    @DeleteMapping("/{nome}")
    public void remover(@PathVariable String nome) {
        service.removerItem(nome);
    }

    @Operation(summary = "Remove todos os itens da lista de compras")
    @DeleteMapping
    public void limpar() throws ExecutionException, InterruptedException {
        service.limparLista();
    }
}
package com.rangelmrk.homehelper.controller;

import com.rangelmrk.homehelper.dto.LembreteDTO;
import com.rangelmrk.homehelper.model.Lembrete;
import com.rangelmrk.homehelper.service.LembreteService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/lembretes")
public class LembreteController {

    private final LembreteService service;

    @Autowired
    public LembreteController(LembreteService service) {
        this.service = service;
    }

    @Operation(summary = "Lista todos os lembretes")
    @GetMapping
    public List<Lembrete> listar() throws ExecutionException, InterruptedException {
        return service.listarTodos();
    }

    @Operation(summary = "Adiciona um novo lembrete")
    @PostMapping
    public void adicionar(@RequestBody LembreteDTO.CriarLembreteRequest item) {
        service.criarLembrete(item);
    }

    @Operation(summary = "Remove um lembrete por ID")
    @DeleteMapping("/{id}")
    public void remover(@PathVariable String id) {
        service.removerLembrete(id);
    }

    @Operation(summary = "Marca o lembrete como concluído")
    @PutMapping("/{id}/concluir")
    public void concluirLembrete(@PathVariable String id) throws ExecutionException, InterruptedException {
        service.concluirLembrete(id);
    }
}

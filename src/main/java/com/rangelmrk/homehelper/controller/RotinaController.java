package com.rangelmrk.homehelper.controller;

import com.rangelmrk.homehelper.dto.RotinaDTO;
import com.rangelmrk.homehelper.model.TarefaRotina;
import com.rangelmrk.homehelper.service.RotinaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/rotinas")
class RotinaController {

    private final RotinaService service;

    @Autowired
    public RotinaController(RotinaService service) {
        this.service = service;
    }

    @Operation(summary = "Lista as tarefas da rotina para o dia atual")
    @GetMapping
    public List<TarefaRotina> listar() throws ExecutionException, InterruptedException {
        return service.listarDoDia();
    }

    @Operation(summary = "Adiciona uma nova tarefa à rotina")
    @PostMapping
    public void adicionar(@RequestBody RotinaDTO.CriarRotinaRequest dto) {
        service.adicionar(dto);
    }

    @Operation(summary = "Marca uma tarefa como concluída no dia")
    @PostMapping("/concluir")
    public void concluir(@RequestBody RotinaDTO.ConcluirTarefaRequest dto) {
        service.concluirTarefa(dto.getId());
    }

    @Operation(summary = "Remove uma tarefa da rotina")
    @DeleteMapping("/{id}")
    public void remover(@PathVariable String id) {
        service.remover(id);
    }

    @Operation(summary = "Remove todas as tarefas da rotina")
    @DeleteMapping
    public void removerTodas() throws ExecutionException, InterruptedException {
        service.removerTodas();
    }
}

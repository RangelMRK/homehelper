package com.rangelmrk.homehelper.controller;

import com.rangelmrk.homehelper.dto.TarefaDTO;
import com.rangelmrk.homehelper.dto.TarefaToggleDTO;
import com.rangelmrk.homehelper.model.Tarefa;
import com.rangelmrk.homehelper.service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @GetMapping("/hoje")
    public List<Tarefa> listarHoje() throws Exception {
        tarefaService.verificarEResetarSeNecessario();
        return tarefaService.listarDoDia();
    }

    @GetMapping("/semana")
    public List<Tarefa> listarSemana() throws Exception {
        return tarefaService.listarSemanaExpandida();
    }

    @PostMapping
    public void adicionar(@RequestBody TarefaDTO dto) {
        if (dto.nome() == null || dto.nome().isEmpty()) {
            throw new IllegalArgumentException("O nome da tarefa é obrigatório.");
        }

        if (dto.descricao() == null || dto.descricao().isEmpty()) {
            throw new IllegalArgumentException("A descrição da tarefa é obrigatória.");
        }

        Tarefa nova = new Tarefa(
                UUID.randomUUID().toString(),
                dto.nome(),
                dto.descricao(),
                dto.repetir(),
                dto.dias(),
                dto.dataAlvo(),
                dto.autor()
        );
        tarefaService.adicionar(nova);
    }

    @PutMapping("/toggle")
    public void toggle(@RequestBody TarefaToggleDTO dto) {
        if (dto.id() == null || dto.id().isEmpty()) {
            throw new IllegalArgumentException("ID da tarefa é obrigatório.");
        }
        tarefaService.alternarConclusao(dto.id(), dto.concluido(), dto.autor());
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable String id) {
        tarefaService.remover(id);
    }

    @DeleteMapping
    public void removerTodas() throws Exception {
        tarefaService.removerTodas();
    }
}

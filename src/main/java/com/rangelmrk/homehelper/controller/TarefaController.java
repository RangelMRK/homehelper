package com.rangelmrk.homehelper.controller;

import com.rangelmrk.homehelper.dto.TarefaDTO;
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
        return tarefaService.listarDoDia();
    }

    @GetMapping("/semana")
    public List<Tarefa> listarSemana() throws Exception {
        return tarefaService.listarSemanaExpandida();
    }

    @PostMapping
    public void adicionar(@RequestBody TarefaDTO.CriarTarefa dto) {
        if (dto.getNome() == null || dto.getNome().isEmpty()) {
            throw new IllegalArgumentException("O nome da tarefa é obrigatório.");
        }

        if (dto.getDescricao() == null || dto.getDescricao().isEmpty()) {
            throw new IllegalArgumentException("A descrição da tarefa é obrigatória.");
        }

        Tarefa nova = new Tarefa(
                UUID.randomUUID().toString(),
                dto.getNome(),
                dto.getDescricao(),
                dto.isRepetir(),
                dto.getDias(),
                dto.getDataAlvo(),
                dto.getAutor()
        );
        tarefaService.adicionar(nova);
    }

    @PutMapping("/toggle")
    public void toggle(@RequestBody TarefaDTO.ToggleRequest dto) {
        // Verificar se o ID é válido
        if (dto.getId() == null || dto.getId().isEmpty()) {
            throw new IllegalArgumentException("ID da tarefa é obrigatório.");
        }

        // Alternar conclusão da tarefa
        tarefaService.alternarConclusao(dto.getId(), dto.isConcluido(), dto.getAutor());
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

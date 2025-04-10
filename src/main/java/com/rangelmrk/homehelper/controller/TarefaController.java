package com.rangelmrk.homehelper.controller;

import com.rangelmrk.homehelper.dto.TarefaDTO;
import com.rangelmrk.homehelper.model.Tarefa;
import com.rangelmrk.homehelper.service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @GetMapping("/hoje")
    public List<Tarefa> listarHoje() throws ExecutionException, InterruptedException {
        return tarefaService.listarDoDia();
    }

    @PostMapping
    public void adicionar(@RequestBody TarefaDTO.CriarTarefa dto) {
        Tarefa nova = new Tarefa();
        nova.setTipo(Tarefa.TipoTarefa.valueOf(dto.getTipo()));
        nova.setNome(dto.getNome());
        nova.setDescricao(dto.getDescricao());
        nova.setAutor(dto.getAutor());
        nova.setFrequencia(dto.getFrequencia());
        nova.setDataAlvo(dto.getDataAlvo());
        nova.setDias(dto.getDias());
        nova.setRepetir(dto.isRepetir());
        tarefaService.adicionar(nova);
    }

    @PutMapping("/toggle")
    public void toggle(@RequestBody TarefaDTO.ToggleRequest dto) {
        tarefaService.alternarConclusao(dto.getId(), dto.isConcluido(), dto.getAutor());
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable String id) {
        tarefaService.remover(id);
    }
}

package com.rangelmrk.homehelper.controller;

import com.rangelmrk.homehelper.dto.DashboardDTO;
import com.rangelmrk.homehelper.model.TarefaRotina;
import com.rangelmrk.homehelper.service.RotinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/rotinas")
public class RotinaController {

    @Autowired
    private RotinaService rotinaService;

    @GetMapping
    public List<TarefaRotina> listar() throws ExecutionException, InterruptedException {
        return rotinaService.listarDoDia();
    }

    @PostMapping
    public void adicionar(@RequestBody TarefaRotina rotina) {
        rotinaService.adicionar(rotina);
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable String id) {
        rotinaService.remover(id);
    }

    @DeleteMapping
    public void removerTodas() throws ExecutionException, InterruptedException {
        rotinaService.removerTodas();
    }

    @PutMapping("/{id}/concluir")
    public void concluir(@PathVariable String id, @RequestBody DashboardDTO.ConcluirTarefaRequest dto) {
        dto.setId(id);
        rotinaService.concluirTarefa(dto);
    }

    @PutMapping("/{id}/toggle")
    public void alternar(@PathVariable String id, @RequestBody DashboardDTO.ConcluirTarefaRequest dto) {
        dto.setId(id);
        rotinaService.alternarConclusao(dto);
    }
}

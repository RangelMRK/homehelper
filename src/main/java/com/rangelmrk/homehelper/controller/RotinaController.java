package com.rangelmrk.homehelper.controller;

import com.rangelmrk.homehelper.dto.DashboardDTO;
import com.rangelmrk.homehelper.model.TarefaRotina;
import com.rangelmrk.homehelper.service.RotinaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/rotinas")
@Tag(name = "Rotinas", description = "Gerenciamento de tarefas de rotina")
public class RotinaController {

    @Autowired
    private RotinaService rotinaService;

    @Operation(summary = "Listar tarefas de rotina do dia")
    @GetMapping
    public List<TarefaRotina> listar() throws ExecutionException, InterruptedException {
        return rotinaService.listarDoDia();
    }

    @Operation(summary = "Adicionar nova tarefa de rotina")
    @PostMapping
    public void adicionar(@RequestBody TarefaRotina rotina) {
        rotinaService.adicionar(rotina);
    }

    @Operation(summary = "Remover uma rotina pelo ID")
    @DeleteMapping("/{id}")
    public void remover(@PathVariable String id) {
        rotinaService.remover(id);
    }

    @Operation(summary = "Remover todas as rotinas")
    @DeleteMapping
    public void removerTodas() throws ExecutionException, InterruptedException {
        rotinaService.removerTodas();
    }

    @Operation(summary = "Alternar status de conclusão da rotina (ID via corpo JSON)")
    @PutMapping("/toggle")
    public void alternar(@RequestBody DashboardDTO.ConcluirTarefaRequest dto) {
        rotinaService.alternarConclusao(dto);
    }
}

package com.rangelmrk.homehelper.controller;

import com.rangelmrk.homehelper.model.Tarefa;
import com.rangelmrk.homehelper.service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private TarefaService tarefaService;

    @GetMapping("/hoje")
    public List<Map<String, Object>> listarItensDoDia() throws ExecutionException, InterruptedException {
        List<Tarefa> tarefas = tarefaService.listarDoDia();
        List<Map<String, Object>> resultado = new ArrayList<>();

        for (Tarefa t : tarefas) {
            resultado.add(Map.of(
                    "id", t.getId(),
                    "descricao", t.getDescricao(),
                    "concluido", t.isConcluido(),
                    "autor", t.getAutor()
            ));
        }

        return resultado;
    }

    @GetMapping("/resumo")
    public Map<String, Object> resumoDoDia() throws ExecutionException, InterruptedException {
        List<Tarefa> tarefas = tarefaService.listarDoDia();
        long total = tarefas.stream().filter(t -> t.getDias() != null).count(); // Filtrando para tarefas repetitivas
        long concluidas = tarefas.stream().filter(t -> t.isConcluido()).count();

        return Map.of(
                "total", total,
                "concluidas", concluidas,
                "pendentes", total - concluidas
        );
    }

    @GetMapping("/semana")
    public Map<String, Object> resumoDaSemana() throws ExecutionException, InterruptedException {
        List<Tarefa> todas = tarefaService.listarSemanaExpandida();
        LocalDate hoje = LocalDate.now();
        LocalDate inicio = hoje.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate fim = hoje.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        int totalPossivel = 0;
        int totalRealizado = 0;

        for (Tarefa t : todas) {
            if (t.getDias() == null) continue;

            for (LocalDate dia = inicio; !dia.isAfter(fim); dia = dia.plusDays(1)) {
                if (t.getDias().contains(dia.getDayOfWeek().name())) {
                    totalPossivel++;
                    if (t.isConcluido()) {
                        totalRealizado++;
                    }
                }
            }
        }

        double percentual = totalPossivel > 0 ? (totalRealizado * 100.0) / totalPossivel : 0.0;

        return Map.of(
                "totalPossivel", totalPossivel,
                "totalRealizado", totalRealizado,
                "percentual", percentual
        );
    }

    @GetMapping("/semana/detalhado")
    public Map<String, Object> resumoPorDia() throws ExecutionException, InterruptedException {
        List<Tarefa> todas = tarefaService.listarSemanaExpandida();
        LocalDate hoje = LocalDate.now();
        LocalDate inicio = hoje.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate fim = hoje.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<Map<String, Object>> dias = new ArrayList<>();

        for (LocalDate dia = inicio; !dia.isAfter(fim); dia = dia.plusDays(1)) {
            int previstas = 0;
            int concluidas = 0;
            String diaStr = dia.toString();
            String semana = dia.getDayOfWeek().name();

            for (Tarefa t : todas) {
                if (t.getDias() == null || !t.getDias().contains(semana)) continue;
                previstas++;
                if (t.isConcluido()) {
                    concluidas++;
                }
            }

            dias.add(Map.of(
                    "data", diaStr,
                    "concluidas", concluidas,
                    "previstas", previstas
            ));
        }

        return Map.of("dias", dias);
    }
}

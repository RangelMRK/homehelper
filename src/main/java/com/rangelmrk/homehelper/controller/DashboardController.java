package com.rangelmrk.homehelper.controller;

import com.rangelmrk.homehelper.dto.DashboardDTO;
import com.rangelmrk.homehelper.model.Lembrete;
import com.rangelmrk.homehelper.model.TarefaRotina;
import com.rangelmrk.homehelper.service.LembreteService;
import com.rangelmrk.homehelper.service.RotinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private RotinaService rotinaService;

    @Autowired
    private LembreteService lembreteService;

    @GetMapping("/hoje")
    public List<DashboardDTO.ItemHoje> listarItensDoDia() throws ExecutionException, InterruptedException {
        List<DashboardDTO.ItemHoje> resultado = new ArrayList<>();
        DayOfWeek hoje = LocalDate.now().getDayOfWeek();

        List<TarefaRotina> rotinas = rotinaService.listarDoDia();
        for (TarefaRotina r : rotinas) {
            resultado.add(new DashboardDTO.ItemHoje(
                    r.getId(),
                    r.getDescricao(),
                    "rotina",
                    r.isConcluidoHoje(),
                    r.getNome()
            ));
        }

        List<Lembrete> lembretes = lembreteService.listarTodos();
        for (Lembrete l : lembretes) {
            resultado.add(new DashboardDTO.ItemHoje(
                    l.getId(),
                    l.getTitulo(),
                    "lembrete",
                    true,
                    l.getAutor()
            ));
        }

        return resultado;
    }

    @GetMapping("/resumo")
    public Map<String, Object> resumoDoDia() throws ExecutionException, InterruptedException {
        List<TarefaRotina> rotinas = rotinaService.listarDoDia();

        long concluidas = rotinas.stream().filter(TarefaRotina::isConcluidoHoje).count();
        long total = rotinas.size();

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("total", total);
        resposta.put("concluidas", concluidas);
        resposta.put("pendentes", total - concluidas);

        return resposta;
    }

    @GetMapping("/semana")
    public Map<String, Object> resumoDaSemana() throws ExecutionException, InterruptedException {
        List<TarefaRotina> todas = rotinaService.listarTodas();

        LocalDate hoje = LocalDate.now();
        LocalDate inicioSemana = hoje.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate fimSemana = hoje.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        int totalRealizado = 0;
        int totalPossivel = 0;

        for (TarefaRotina rotina : todas) {
            if (rotina.getDias() == null || rotina.getHistorico() == null) continue;

            for (LocalDate dia = inicioSemana; !dia.isAfter(fimSemana); dia = dia.plusDays(1)) {
                DayOfWeek diaSemana = dia.getDayOfWeek();
                if (rotina.getDias().contains(diaSemana.name())) {
                    totalPossivel++;
                    if (rotina.getHistorico().contains(dia.toString())) {
                        totalRealizado++;
                    }
                }
            }
        }

        double percentual = totalPossivel > 0 ? (totalRealizado * 100.0) / totalPossivel : 0.0;

        Map<String, Object> resumo = new HashMap<>();
        resumo.put("totalPossivel", totalPossivel);
        resumo.put("totalRealizado", totalRealizado);
        resumo.put("percentual", percentual);

        return resumo;
    }
}

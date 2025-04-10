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
import java.util.ArrayList;
import java.util.List;
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
                    true, // lembrete não é concluído
                    l.getAutor()
            ));
        }

        return resultado;
    }
}

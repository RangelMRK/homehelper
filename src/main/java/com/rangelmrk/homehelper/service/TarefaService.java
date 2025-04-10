package com.rangelmrk.homehelper.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.rangelmrk.homehelper.model.Tarefa;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class TarefaService {

    private static final String COLLECTION = "tarefas";
    private static final String META_COLLECTION = "meta";
    private static final String RESET_DOC_ID = "reset_diario";

    public List<Tarefa> listarTodas() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        return db.collection(COLLECTION).get().get().getDocuments().stream()
                .map(doc -> doc.toObject(Tarefa.class))
                .collect(Collectors.toList());
    }

    public List<Tarefa> listarDoDia() throws ExecutionException, InterruptedException {
        this.verificarEResetarSeNecessario();

        LocalDate hoje = LocalDate.now();
        DayOfWeek diaSemana = hoje.getDayOfWeek();
        String dataHoje = hoje.toString();

        return listarTodas().stream()
                .filter(tarefa -> tarefa.getTipo() != null &&
                        ((tarefa.getTipo() == Tarefa.TipoTarefa.ROTINA &&
                                tarefa.getDias() != null && tarefa.getDias().contains(diaSemana.name())) ||
                                (tarefa.getTipo() == Tarefa.TipoTarefa.LEMBRETE &&
                                        isLembreteValidoHoje(tarefa, dataHoje, diaSemana))))
                .collect(Collectors.toList());
    }

    private boolean isLembreteValidoHoje(Tarefa tarefa, String dataHoje, DayOfWeek diaSemana) {
        if (tarefa.getFrequencia() == null || tarefa.getDataAlvo() == null) return false;

        return switch (tarefa.getFrequencia().toUpperCase()) {
            case "DIARIO" -> true;
            case "SEMANAL" -> {
                LocalDate alvo = LocalDate.parse(tarefa.getDataAlvo());
                yield diaSemana == alvo.getDayOfWeek();
            }
            case "UNICO" -> dataHoje.equals(tarefa.getDataAlvo());
            default -> false;
        };
    }

    public void adicionar(Tarefa nova) {
        Firestore db = FirestoreClient.getFirestore();
        nova.setId(UUID.randomUUID().toString());
        if (nova.getTipo() == Tarefa.TipoTarefa.ROTINA) {
            nova.setConcluidoHoje(false);
            nova.setUltimaAtualizacao(null);
            nova.setHistorico(new ArrayList<>());
        }
        db.collection(COLLECTION).document(nova.getId()).set(nova);
    }

    public void remover(String id) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection(COLLECTION).document(id).delete();
    }

    public void alternarConclusao(String id, boolean concluido, String autor) {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference ref = db.collection(COLLECTION).document(id);
        String hoje = LocalDate.now().toString();

        try {
            Tarefa tarefa = ref.get().get().toObject(Tarefa.class);
            if (tarefa == null || tarefa.getTipo() != Tarefa.TipoTarefa.ROTINA) return;

            Map<String, Object> update = new HashMap<>();

            if (concluido) {
                update.put("concluidoHoje", true);
                update.put("concluidoPor", autor);
                update.put("ultimaAtualizacao", hoje);

                List<String> historico = tarefa.getHistorico() != null ? tarefa.getHistorico() : new ArrayList<>();
                if (!historico.contains(hoje)) historico.add(hoje);
                update.put("historico", historico);
            } else {
                update.put("concluidoHoje", false);
                update.put("concluidoPor", null);
                update.put("ultimaAtualizacao", null);

                List<String> historico = tarefa.getHistorico();
                if (historico != null && historico.contains(hoje)) {
                    historico.remove(hoje);
                    update.put("historico", historico);
                }
            }

            ref.update(update);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void verificarEResetarSeNecessario() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference ref = db.collection(META_COLLECTION).document(RESET_DOC_ID);

        DocumentSnapshot snapshot = ref.get().get();
        String hoje = LocalDate.now().toString();

        if (!snapshot.exists() || !hoje.equals(snapshot.getString("ultimaData"))) {
            this.resetarRotinas();
            ref.set(Map.of("ultimaData", hoje));
        }
    }

    private void resetarRotinas() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        for (Tarefa tarefa : listarTodas()) {
            if (tarefa.getTipo() == Tarefa.TipoTarefa.ROTINA && tarefa.isConcluidoHoje()) {
                DocumentReference ref = db.collection(COLLECTION).document(tarefa.getId());
                ref.update("concluidoHoje", false);
                ref.update("concluidoPor", null);
                ref.update("ultimaAtualizacao", null);
            }
        }
    }
}

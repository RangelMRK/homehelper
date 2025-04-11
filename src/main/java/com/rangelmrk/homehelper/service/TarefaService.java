package com.rangelmrk.homehelper.service;

import com.google.api.core.ApiFuture;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.*;
import com.rangelmrk.homehelper.model.Tarefa;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.concurrent.ExecutionException;
import java.time.LocalDate;

@Service
public class TarefaService {

    private static final String COLLECTION = "tarefas";
    private static final String META_COLLECTION = "meta";
    private static final String RESET_DOC_ID = "reset_meta";

    public void adicionar(Tarefa nova) {
        Firestore db = FirestoreClient.getFirestore();
        nova.setId(UUID.randomUUID().toString());

        // Se for uma tarefa repetitiva, garantir que não cria para datas passadas
        if (nova.isRepetir()) {
            List<String> diasValidos = nova.getDias().stream()
                    .filter(dia -> {
                        LocalDate hoje = LocalDate.now();
                        LocalDate dataProximaRepeticao = hoje.with(TemporalAdjusters.next(DayOfWeek.valueOf(dia)));
                        return !dataProximaRepeticao.isBefore(hoje);
                    })
                    .collect(Collectors.toList());
            nova.setDias(diasValidos);
        }

        db.collection(COLLECTION).document(nova.getId()).set(nova);
    }

    public List<Tarefa> listarDoDia() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        LocalDate hoje = LocalDate.now();
        DayOfWeek diaSemana = hoje.getDayOfWeek();

        return documents.stream()
                .map(doc -> doc.toObject(Tarefa.class))
                .filter(tarefa -> tarefa.getDias() != null && tarefa.getDias().contains(diaSemana.name()) ||
                        (tarefa.getDataAlvo() != null && tarefa.getDataAlvo().equals(hoje.toString())))
                .collect(Collectors.toList());
    }

    public List<Tarefa> listarSemanaExpandida() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        LocalDate hoje = LocalDate.now();
        DayOfWeek diaSemana = hoje.getDayOfWeek();

        return documents.stream()
                .map(doc -> doc.toObject(Tarefa.class))
                .filter(t -> t.getDias().contains(diaSemana.name()) || t.getDataAlvo().equals(hoje.toString()))
                .collect(Collectors.toList());
    }

    public void remover(String id) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection(COLLECTION).document(id).delete();
    }

    public void removerTodas() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION).get();
        for (QueryDocumentSnapshot doc : future.get().getDocuments()) {
            doc.getReference().delete();
        }
    }

    public void alternarConclusao(String id, boolean concluido, String autor) {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference ref = db.collection(COLLECTION).document(id);

        try {
            Tarefa tarefa = ref.get().get().toObject(Tarefa.class);
            if (tarefa == null) return;

            Map<String, Object> update = new HashMap<>();

            if (concluido) {
                update.put("concluido", true);
                update.put("ultimaAtualizacao", LocalDate.now().toString());
            } else {
                update.put("concluido", false);
                update.put("ultimaAtualizacao", null);
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
            if (tarefa.isRepetir() && tarefa.isConcluido()) {
                DocumentReference ref = db.collection(COLLECTION).document(tarefa.getId());
                ref.update("concluido", false);
                ref.update("ultimaAtualizacao", null);
            }
        }
    }


    public List<Tarefa> listarTodas() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION).get();
        return future.get().getDocuments().stream()
                .map(doc -> doc.toObject(Tarefa.class))
                .collect(Collectors.toList());
    }
}

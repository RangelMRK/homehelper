package com.rangelmrk.homehelper.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.rangelmrk.homehelper.dto.DashboardDTO;
import com.rangelmrk.homehelper.model.TarefaRotina;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RotinaService {

    private static final String COLLECTION = "rotinas";

    public List<TarefaRotina> listarDoDia() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        List<TarefaRotina> todas = db.collection(COLLECTION).get().get().getDocuments().stream()
                .map(doc -> doc.toObject(TarefaRotina.class))
                .collect(Collectors.toList());

        DayOfWeek hoje = LocalDate.now().getDayOfWeek();

        return todas.stream()
                .filter(r -> r.getDias() != null && r.getDias().contains(hoje.name()))
                .collect(Collectors.toList());
    }

    public void adicionar(TarefaRotina nova) {
        Firestore db = FirestoreClient.getFirestore();
        nova.setId(UUID.randomUUID().toString());
        nova.setConcluidoHoje(false);
        nova.setUltimaAtualizacao(null);

        if (nova.isRepetir() && (nova.getDias() == null || nova.getDias().isEmpty())) {
            nova.setDias(Stream.of(DayOfWeek.values()).map(DayOfWeek::name).collect(Collectors.toList()));
        }

        db.collection(COLLECTION).document(nova.getId()).set(nova);
    }

    public void remover(String id) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection(COLLECTION).document(id).delete();
    }

    public void removerTodas() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        db.collection(COLLECTION).get().get().getDocuments()
                .forEach(doc -> doc.getReference().delete());
    }

    public void concluirTarefa(DashboardDTO.ConcluirTarefaRequest dto) {
        Firestore db = FirestoreClient.getFirestore();
        Map<String, Object> update = new HashMap<>();
        update.put("concluidoHoje", true);
        update.put("concluidoPor", dto.getAutor());
        update.put("ultimaAtualizacao", LocalDate.now().toString());
        db.collection(COLLECTION).document(dto.getId()).update(update);
    }

    public void alternarConclusao(DashboardDTO.ConcluirTarefaRequest dto) {
        Firestore db = FirestoreClient.getFirestore();
        Map<String, Object> update = new HashMap<>();

        if (dto.isConcluido()) {
            update.put("concluidoHoje", true);
            update.put("concluidoPor", dto.getAutor());
            update.put("ultimaAtualizacao", LocalDate.now().toString());
        } else {
            update.put("concluidoHoje", false);
            update.put("concluidoPor", null);
            update.put("ultimaAtualizacao", null);
        }

        db.collection(COLLECTION).document(dto.getId()).update(update);
    }

    public void resetarRotinasDiariamente() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION).get();
        List<QueryDocumentSnapshot> documentos = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documentos) {
            TarefaRotina rotina = doc.toObject(TarefaRotina.class);

            if (rotina.isRepetir() && rotina.isConcluidoHoje()) {
                doc.getReference().update("concluidoHoje", false);
                doc.getReference().update("concluidoPor", null);
                doc.getReference().update("ultimaAtualizacao", null);
            }
        }
    }
}
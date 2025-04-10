package com.rangelmrk.homehelper.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
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
    private static final String META_COLLECTION = "meta";
    private static final String RESET_DOC_ID = "reset_diario";

    public List<TarefaRotina> listarDoDia() throws ExecutionException, InterruptedException {
        this.verificarEResetarSeNecessario();
        Firestore db = FirestoreClient.getFirestore();
        List<TarefaRotina> todas = listarTodas();
        DayOfWeek hoje = LocalDate.now().getDayOfWeek();
        return todas.stream()
                .filter(r -> r.getDias() != null && r.getDias().contains(hoje.name()))
                .collect(Collectors.toList());
    }

    public List<TarefaRotina> listarTodas() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        return db.collection(COLLECTION).get().get().getDocuments().stream()
                .map(doc -> doc.toObject(TarefaRotina.class))
                .collect(Collectors.toList());
    }

    private void verificarEResetarSeNecessario() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference ref = db.collection(META_COLLECTION).document(RESET_DOC_ID);

        DocumentSnapshot snapshot = ref.get().get();
        String hoje = LocalDate.now().toString();

        if (!snapshot.exists() || !hoje.equals(snapshot.getString("ultimaData"))) {
            this.resetarRotinasDiariamente();
            ref.set(Map.of("ultimaData", hoje));
        }
    }

    public void adicionar(TarefaRotina nova) {
        Firestore db = FirestoreClient.getFirestore();
        nova.setId(UUID.randomUUID().toString());
        nova.setConcluidoHoje(false);
        nova.setUltimaAtualizacao(null);
        nova.setHistorico(new ArrayList<>());

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

    public void alternarConclusao(DashboardDTO.ConcluirTarefaRequest dto) {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION).document(dto.getId());
        String hoje = LocalDate.now().toString();

        if (dto.isConcluido()) {
            docRef.update("concluidoHoje", true);
            docRef.update("concluidoPor", dto.getAutor());
            docRef.update("ultimaAtualizacao", hoje);

            try {
                DocumentSnapshot snapshot = docRef.get().get();
                TarefaRotina rotina = snapshot.toObject(TarefaRotina.class);
                List<String> historico = rotina.getHistorico() != null ? rotina.getHistorico() : new ArrayList<>();
                if (!historico.contains(hoje)) {
                    historico.add(hoje);
                    docRef.update("historico", historico);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            docRef.update("concluidoHoje", false);
            docRef.update("concluidoPor", null);
            docRef.update("ultimaAtualizacao", null);

            try {
                DocumentSnapshot snapshot = docRef.get().get();
                TarefaRotina rotina = snapshot.toObject(TarefaRotina.class);
                List<String> historico = rotina.getHistorico();
                if (historico != null && historico.contains(hoje)) {
                    historico.remove(hoje);
                    docRef.update("historico", historico);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
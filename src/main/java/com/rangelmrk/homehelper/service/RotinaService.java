package com.rangelmrk.homehelper.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.rangelmrk.homehelper.dto.RotinaDTO;
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

    public void adicionar(RotinaDTO.CriarRotinaRequest dto) {
        validar(dto);

        Firestore db = FirestoreClient.getFirestore();
        TarefaRotina nova = new TarefaRotina();
        nova.setId(UUID.randomUUID().toString());
        nova.setNome(dto.getNome());
        nova.setDescricao(dto.getDescricao());
        nova.setRepetir(dto.isRepetir());
        nova.setConcluidoHoje(false);
        nova.setUltimaAtualizacao(null);

        if (dto.isRepetir()) {
            if (dto.getDias() == null || dto.getDias().isEmpty()) {
                nova.setDias(Stream.of(DayOfWeek.values()).map(DayOfWeek::name).collect(Collectors.toList()));
            } else {
                nova.setDias(dto.getDias());
            }
        } else {
            nova.setDias(dto.getDias());
        }

        db.collection(COLLECTION).document(nova.getId()).set(nova);
    }
    public void concluirTarefa(String id) {
        Firestore db = FirestoreClient.getFirestore();
        Map<String, Object> update = new HashMap<>();
        update.put("concluidoHoje", true);
        update.put("ultimaAtualizacao", LocalDate.now().toString());
        db.collection(COLLECTION).document(id).update(update);
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

    private void validar(RotinaDTO.CriarRotinaRequest dto) {
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O campo 'nome' é obrigatório.");
        }

        if (dto.isRepetir()) {
            // dias é opcional aqui, pois pode ser preenchido com todos os dias
            return;
        }

        if (dto.getDias() == null || dto.getDias().isEmpty()) {
            throw new IllegalArgumentException("Você deve informar ao menos um dia para a tarefa não repetitiva.");
        }
    }

    @Scheduled(cron = "0 0 7 * * *", zone = "America/Sao_Paulo")
    public void resetarRotinasDiariamente() throws ExecutionException, InterruptedException {
        System.out.println("🔁 Resetando rotinas às 7h...");

        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION).get();
        List<QueryDocumentSnapshot> documentos = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documentos) {
            TarefaRotina rotina = doc.toObject(TarefaRotina.class);

            if (rotina.isRepetir() && rotina.isConcluidoHoje()) {
                doc.getReference().update("concluidoHoje", false);
            }
        }

        System.out.println("✅ Rotinas resetadas com sucesso.");
    }



}

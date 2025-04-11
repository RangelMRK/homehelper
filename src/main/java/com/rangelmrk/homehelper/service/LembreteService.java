package com.rangelmrk.homehelper.service;

import com.google.cloud.firestore.DocumentReference;
import com.google.firebase.cloud.FirestoreClient;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.rangelmrk.homehelper.dto.LembreteDTO;
import com.rangelmrk.homehelper.model.Lembrete;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class LembreteService {

    private static final String COLLECTION = "lembretes";

    public void criarLembrete(LembreteDTO.CriarLembreteRequest dto) {
        Firestore db = FirestoreClient.getFirestore();
        String id = java.util.UUID.randomUUID().toString();

        Lembrete lembrete = new Lembrete(
                id,
                dto.getNome(),
                dto.getDescricao(),
                dto.getDataAlvo(),
                dto.getHora(),
                dto.getAutor()
        );

        db.collection(COLLECTION).document(id).set(lembrete);
    }
    public void concluirLembrete(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference ref = db.collection(COLLECTION).document(id);

        ref.update("concluidoHoje", true);
    }

    public List<Lembrete> listarTodos() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION).get();
        List<QueryDocumentSnapshot> docs = future.get().getDocuments();

        List<Lembrete> lembretes = new ArrayList<>();
        for (QueryDocumentSnapshot doc : docs) {
            lembretes.add(doc.toObject(Lembrete.class));
        }
        return lembretes;
    }

    public void removerLembrete(String id) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection(COLLECTION).document(id).delete();
    }
}

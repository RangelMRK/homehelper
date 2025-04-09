package com.rangelmrk.homehelper.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.rangelmrk.homehelper.dto.LembreteDTO;
import com.rangelmrk.homehelper.model.Lembrete;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class LembreteService {

    private static final String COLLECTION = "lembretes";

    public void criarLembrete(LembreteDTO.CriarLembreteRequest dto) {
        Firestore db = FirestoreClient.getFirestore();
        String id = UUID.randomUUID().toString();

        Lembrete lembrete = new Lembrete(
                id,
                dto.getTitulo(),
                dto.getDescricao(),
                dto.getAutor(),
                true
        );

        db.collection(COLLECTION).document(id).set(lembrete);
    }


    public List<Lembrete> listarTodos() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION).whereEqualTo("ativo", true).get();
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

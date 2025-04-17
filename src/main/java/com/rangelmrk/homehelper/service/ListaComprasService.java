package com.rangelmrk.homehelper.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.rangelmrk.homehelper.model.ItemListaCompra;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class ListaComprasService {
    Firestore db = FirestoreClient.getFirestore();

    private static final String COLLECTION = "lista-compras";

    public List<ItemListaCompra> listarItens() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION).get();
        List<QueryDocumentSnapshot> docs = future.get().getDocuments();

        List<ItemListaCompra> lista = new ArrayList<>();
        for (QueryDocumentSnapshot doc : docs) {
            lista.add(doc.toObject(ItemListaCompra.class));
        }
        return lista;
    }

    public void adicionarItem(ItemListaCompra item) {
        String id = UUID.randomUUID().toString();
        item.setId(id);
        db.collection(COLLECTION).document(id).set(item);
    }

    public void atualizarStatus(String id, boolean comprado) {
        db.collection(COLLECTION).document(id).update("comprado", comprado);
    }

    public void removerItem(String id) {
        db.collection(COLLECTION).document(id).delete();
    }


    public void limparLista() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION).get();
        for (QueryDocumentSnapshot doc : future.get().getDocuments()) {
            doc.getReference().delete();
        }
    }
}

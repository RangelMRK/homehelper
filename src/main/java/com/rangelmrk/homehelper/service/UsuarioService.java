package com.rangelmrk.homehelper.service;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.rangelmrk.homehelper.dto.UsuarioDTO;
import com.rangelmrk.homehelper.model.Usuario;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class UsuarioService {

    private static final String COLLECTION = "usuarios";
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public void registrar(UsuarioDTO dto) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        boolean existe = db.collection(COLLECTION)
                .whereEqualTo("username", dto.username())
                .get()
                .get()
                .getDocuments()
                .size() > 0;

        if (existe) throw new RuntimeException("Usuário já existe");

        Usuario novo = new Usuario(
                UUID.randomUUID().toString(),
                dto.username(),
                encoder.encode(dto.senha())
        );

        db.collection(COLLECTION).document(novo.getId()).set(novo);
    }

    public Usuario buscarPorUsername(String username) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        return db.collection(COLLECTION)
                .whereEqualTo("username", username)
                .get()
                .get()
                .getDocuments()
                .stream()
                .findFirst()
                .map(doc -> doc.toObject(Usuario.class))
                .orElse(null);
    }
}

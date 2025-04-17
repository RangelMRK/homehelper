package com.rangelmrk.homehelper.controller;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.rangelmrk.homehelper.service.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notificacoes")
public class TokenController {

    @Autowired
    private NotificacaoService notificacaoService;

    @PostMapping("/registrar-token")
    public void registrarToken(@RequestBody Map<String, String> body) throws Exception {
        String username = body.get("username");
        String token = body.get("token");

        if (username == null || token == null) {
            throw new IllegalArgumentException("Campos obrigatórios: username e token");
        }

        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("tokens_dispositivos").document(username);
        DocumentSnapshot doc = docRef.get().get();

        List<String> tokensExistentes = new ArrayList<>();

        if (doc.exists() && doc.contains("tokens")) {
            tokensExistentes = (List<String>) doc.get("tokens");
        }

        if (!tokensExistentes.contains(token)) {
            tokensExistentes.add(token);
        }

        docRef.set(Map.of(
                "username", username,
                "tokens", tokensExistentes,
                "ultimoLogin", LocalDate.now().toString()
        ));
    }

}

package com.rangelmrk.homehelper.controller;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.rangelmrk.homehelper.service.NotificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/notificacoes")
public class TokenController {

    @Autowired
    private NotificacaoService notificacaoService;

    @PostMapping("/registrar-token")
    public void registrarToken(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String token = body.get("token");

        if (username == null || token == null) {
            throw new IllegalArgumentException("Campos obrigatórios: username e token");
        }

        Firestore db = FirestoreClient.getFirestore();
        db.collection("tokens_dispositivos").document(username)
                .set(Map.of(
                        "username", username,
                        "token", token,
                        "ultimoLogin", LocalDate.now().toString()
                ));
    }
}

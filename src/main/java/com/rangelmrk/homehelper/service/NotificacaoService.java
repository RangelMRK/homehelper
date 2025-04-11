package com.rangelmrk.homehelper.service;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.*;
import com.rangelmrk.homehelper.model.Lembrete;
import com.rangelmrk.homehelper.model.Tarefa;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class NotificacaoService {

    public void enviarNotificacao(String token, String titulo, String corpo) {
        try {
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle(titulo)
                            .setBody(corpo)
                            .build())
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("✅ Notificação enviada com sucesso: " + response);
        } catch (Exception e) {
            System.err.println("❌ Erro ao enviar notificação: " + e.getMessage());
        }
    }

    // 🔁 Envia resumo às 7h, 14h, 21h
    @Scheduled(cron = "0 0 7,14,21 * * *", zone = "America/Sao_Paulo")
    public void enviarResumoDiario() {
        try {
            List<QueryDocumentSnapshot> usuarios = FirestoreClient.getFirestore()
                    .collection("tokens_dispositivos")
                    .get().get().getDocuments();

            for (DocumentSnapshot doc : usuarios) {
                String username = doc.getString("username");
                String token = doc.getString("token");

                if (username == null || token == null) continue;

                // Tarefas pendentes
                List<Tarefa> tarefas = FirestoreClient.getFirestore()
                        .collection("tarefas")
                        .get().get().getDocuments().stream()
                        .map(d -> d.toObject(Tarefa.class))
                        .filter(t -> username.equals(t.getAutor()))
                        .filter(t -> !t.isConcluido())
                        .filter(t -> {
                            if (t.isRepetir()) {
                                return t.getDias().contains(LocalDate.now().getDayOfWeek().name());
                            } else {
                                return LocalDate.now().toString().equals(t.getDataAlvo());
                            }
                        }).toList();

                // Lembretes pendentes
                List<Lembrete> lembretes = FirestoreClient.getFirestore()
                        .collection("lembretes")
                        .get().get().getDocuments().stream()
                        .map(d -> d.toObject(Lembrete.class))
                        .filter(l -> username.equals(l.getAutor()))
                        .filter(l -> !l.isConcluidoHoje())
                        .filter(l -> LocalDate.now().toString().equals(l.getDataAlvo()))
                        .toList();

                if (tarefas.isEmpty() && lembretes.isEmpty()) continue;

                String msg = String.format("Você tem %d tarefas e %d lembretes para hoje.", tarefas.size(), lembretes.size());
                enviarNotificacao(token, "📋 Resumo do dia", msg);
            }

        } catch (Exception e) {
            System.err.println("❌ Erro no agendamento de resumo: " + e.getMessage());
        }
    }

    // ⏰ Lembretes com hora exata
    @Scheduled(cron = "0 * * * * *", zone = "America/Sao_Paulo") // A cada minuto
    public void notificarLembretesComHoraMarcada() {
        try {
            String agora = LocalTime.now().withSecond(0).withNano(0).toString(); // ex: "14:00"
            String hoje = LocalDate.now().toString();

            List<Lembrete> lembretes = FirestoreClient.getFirestore()
                    .collection("lembretes")
                    .get().get().getDocuments().stream()
                    .map(doc -> doc.toObject(Lembrete.class))
                    .filter(l -> hoje.equals(l.getDataAlvo()))
                    .filter(l -> agora.equals(l.getHora()))
                    .filter(l -> !l.isConcluidoHoje())
                    .toList();

            for (Lembrete l : lembretes) {
                String token = buscarTokenPorUsuario(l.getAutor());
                if (token != null) {
                    enviarNotificacao(token, "⏰ Lembrete: " + l.getNome(), l.getDescricao());
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Erro ao enviar lembrete com hora: " + e.getMessage());
        }
    }

    private String buscarTokenPorUsuario(String username) {
        try {
            DocumentSnapshot doc = FirestoreClient.getFirestore()
                    .collection("tokens_dispositivos")
                    .document(username)
                    .get().get();

            return doc.exists() ? doc.getString("token") : null;

        } catch (Exception e) {
            return null;
        }
    }
}

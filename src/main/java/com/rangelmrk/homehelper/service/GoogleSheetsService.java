package com.rangelmrk.homehelper.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.rangelmrk.homehelper.dto.GastoResumoDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class GoogleSheetsService {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private String spreadsheetId;
    private Sheets sheets;

    @Autowired
    private PlanilhaConfigService planilhaConfig;

    @PostConstruct
    public void init() {
        try {
            this.spreadsheetId = System.getenv("SPREADSHEET_ID");
            this.sheets = buildSheetsService();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar Google Sheets: " + e.getMessage(), e);
        }
    }

    private Sheets buildSheetsService() throws Exception {
        HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        String keyPath = System.getenv("GOOGLE_KEY_PATH");

        if (keyPath == null) {
            throw new RuntimeException("Variável de ambiente GOOGLE_KEY_PATH não definida.");
        }

        InputStream inputStream = new FileInputStream(keyPath);
        GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream)
                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/spreadsheets"));

        return new Sheets.Builder(transport, JSON_FACTORY, new HttpCredentialsAdapter(credentials))
                .setApplicationName("HomeHelper")
                .build();
    }

    private String getCelula(String categoria, String mes, int linhaFixa) {
        String coluna = planilhaConfig.getColunaDoMes(mes);
        int linha = (linhaFixa > 0) ? linhaFixa : planilhaConfig.getLinhaDaCategoria(categoria);

        if (coluna == null || linha == 0) {
            throw new IllegalArgumentException("Mês ou categoria inválidos: " + mes + ", " + categoria);
        }

        return coluna + linha;
    }

    public double lerValorCelula(String categoria, String mes) {
        try {
            String celula = getCelula(categoria, mes, 0);
            ValueRange response = sheets.spreadsheets().values()
                    .get(spreadsheetId, celula)
                    .execute();

            if (response.getValues() == null || response.getValues().isEmpty()) return 0;

            String raw = response.getValues().get(0).get(0).toString();
            return Double.parseDouble(raw.replace("R$", "").replace(",", ".").replace(" ", "").replace("%", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    public void atualizarCelula(String categoria, String mes, double valor) {
        try {
            String celula = getCelula(categoria, mes, 0);
            ValueRange body = new ValueRange()
                    .setValues(Collections.singletonList(Collections.singletonList(valor)));

            sheets.spreadsheets().values()
                    .update(spreadsheetId, celula, body)
                    .setValueInputOption("USER_ENTERED")
                    .execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void somarNaCelula(String categoria, String mes, double valorParaSomar) {
        try {
            double atual = lerValorCelula(categoria, mes);
            double novoValor = atual + valorParaSomar;
            atualizarCelula(categoria, mes, novoValor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GastoResumoDTO listarTotaisPorMes(String mes) {
        Map<String, String> colunas = planilhaConfig.getTodasColunas();
        Map<String, Integer> linhas = planilhaConfig.getTodasLinhas();

        String coluna = colunas.get(mes);
        if (coluna == null) {
            throw new IllegalArgumentException("Mês inválido: " + mes);
        }

        Map<String, Double> gastos = new LinkedHashMap<>();
        double total = 0;

        for (Map.Entry<String, Integer> entry : linhas.entrySet()) {
            String categoria = entry.getKey();
            String celula = coluna + entry.getValue();

            try {
                ValueRange response = sheets.spreadsheets().values()
                        .get(spreadsheetId, celula)
                        .execute();

                String raw = (response.getValues() != null && !response.getValues().isEmpty())
                        ? response.getValues().get(0).get(0).toString()
                        : "0";

                double valor = Double.parseDouble(raw.replace("R$", "").replace(",", ".").replace(" ", ""));
                gastos.put(categoria, valor);
                total += valor;
            } catch (Exception e) {
                gastos.put(categoria, 0.0);
            }
        }

        return new GastoResumoDTO(mes, total, gastos);
    }

}

package com.rangelmrk.homehelper.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Map;

@Component
public class PlanilhaConfigService {

    private Map<String, String> colunasPorMes;
    private Map<String, Integer> linhasPorCategoria;

    @PostConstruct
    public void carregarConfiguracao() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream input = getClass().getResourceAsStream("/planilha-config.json");
            JsonNode root = mapper.readTree(input);

            colunasPorMes = mapper.convertValue(root.get("colunasPorMes"), new TypeReference<>() {});
            linhasPorCategoria = mapper.convertValue(root.get("linhasPorCategoria"), new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar planilha-config.json", e);
        }
    }

    public String getColunaDoMes(String mes) {
        return colunasPorMes.get(mes);
    }

    public Integer getLinhaDaCategoria(String categoria) {
        return linhasPorCategoria.get(categoria);
    }

    public Map<String, String> getTodasColunas() {
        return colunasPorMes;
    }

    public Map<String, Integer> getTodasLinhas() {
        return linhasPorCategoria;
    }
}

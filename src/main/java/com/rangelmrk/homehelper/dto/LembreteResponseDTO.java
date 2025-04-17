package com.rangelmrk.homehelper.dto;

public record LembreteResponseDTO(
        String id,
        String nome,
        String descricao,
        String dataAlvo,
        String hora,
        String autor,
        boolean concluidoHoje
) {}

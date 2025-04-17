package com.rangelmrk.homehelper.dto;

import java.util.List;

public record TarefaDTO(String id, String nome, String descricao, String autor, String dataAlvo, List<String> dias, Boolean repetir, Boolean concluido) {

}

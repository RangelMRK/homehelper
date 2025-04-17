package com.rangelmrk.homehelper.dto;

import java.util.Map;

public record GastoResumoDTO(String mes, double total, Map<String, Double> totais) {}


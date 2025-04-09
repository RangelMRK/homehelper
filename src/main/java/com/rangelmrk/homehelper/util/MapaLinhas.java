package com.rangelmrk.homehelper.util;

import java.util.HashMap;
import java.util.Map;

public class MapaLinhas {
    public static Map<String, Integer> getCategoriaLinha() {
        Map<String, Integer> categorias = new HashMap<>();
        categorias.put("Luz", 24);
        categorias.put("Água", 25);
        categorias.put("Internet", 26);
        categorias.put("Telefone", 27);
        categorias.put("Manutenção", 28);
        categorias.put("NuPJ", 31);
        categorias.put("NuPF", 32);
        categorias.put("Bradesco", 33);
        categorias.put("NuMarcos", 34);
        categorias.put("Terapia", 37);
        categorias.put("Consultas", 38);
        categorias.put("Medicação", 39);
        categorias.put("Outros", 40);
        categorias.put("Apple", 43);
        categorias.put("Office 365", 44);
        categorias.put("Amazon Prime", 45);
        categorias.put("Nubank Vida", 46);
        categorias.put("Seguro Casa", 47);
        categorias.put("Bradesco Exclusive", 48);
        categorias.put("Smiles", 49);
        categorias.put("Impostos", 51);
        categorias.put("Seguro", 52);
        categorias.put("Manutenção Veículo", 53);
        categorias.put("Imposto", 55);
        categorias.put("Contador", 56);
        categorias.put("Bichos", 57);
        categorias.put("Viagens", 58);
        categorias.put("Outros Extras", 59);
        return categorias;
    }
}

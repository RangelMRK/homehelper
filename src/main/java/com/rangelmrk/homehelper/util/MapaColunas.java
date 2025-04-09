package com.rangelmrk.homehelper.util;

import java.util.HashMap;
import java.util.Map;

public class MapaColunas {
    public static Map<String, String> getMesColuna() {
        Map<String, String> meses = new HashMap<>();
        meses.put("Janeiro", "F");
        meses.put("Fevereiro", "G");
        meses.put("Março", "H");
        meses.put("Abril", "I");
        meses.put("Maio", "J");
        meses.put("Junho", "K");
        meses.put("Julho", "L");
        meses.put("Agosto", "M");
        meses.put("Setembro", "N");
        meses.put("Outubro", "O");
        meses.put("Novembro", "P");
        meses.put("Dezembro", "Q");
        return meses;
    }
}

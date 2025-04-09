package com.rangelmrk.homehelper.service;

import com.rangelmrk.homehelper.service.GoogleSheetsService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GoogleSheetsServiceTest {

    @Test
    public void testLeituraValor() {
        try {
            GoogleSheetsService service = new GoogleSheetsService();
            double valor = service.lerValorCelula("Luz", "Março");
            System.out.println("Valor lido: " + valor);
            assertTrue(valor >= 0); // espera um número válido ou 0
        } catch (Exception e) {
            fail("Exceção ao ler célula: " + e.getMessage());
        }
    }

    @Test
    public void testAtualizacaoValorDireto() {
        try {
            GoogleSheetsService service = new GoogleSheetsService();
            service.atualizarCelula("Luz", "Março", 200.00);
            System.out.println("✅ Valor atualizado com sucesso.");
        } catch (Exception e) {
            fail("Exceção ao atualizar célula: " + e.getMessage());
        }
    }

    @Test
    public void testSomaValorNaCelula() {
        try {
            GoogleSheetsService service = new GoogleSheetsService();
            service.somarNaCelula("Luz", "Março", 50.00);
            System.out.println("✅ Valor somado com sucesso.");
        } catch (Exception e) {
            fail("Exceção ao somar valor na célula: " + e.getMessage());
        }
    }
}
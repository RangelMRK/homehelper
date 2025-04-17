package com.rangelmrk.homehelper.controller;

import com.rangelmrk.homehelper.dto.GastoResumoDTO;
import com.rangelmrk.homehelper.model.GastoRequest;
import com.rangelmrk.homehelper.service.GoogleSheetsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/gastos")
public class GastoController {

    private final GoogleSheetsService googleSheetsService;

    @Autowired
    public GastoController(GoogleSheetsService googleSheetsService) {
        this.googleSheetsService = googleSheetsService;
    }

    @Operation(summary = "Atualiza um gasto para o valor informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gasto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content)
    })
    @PutMapping
    public void atualizarGasto(@RequestBody GastoRequest request) {
        googleSheetsService.atualizarCelula(request.getCategoria(), request.getMes(), request.getValor());
    }

    @Operation(summary = "Adiciona (soma) um valor ao gasto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gasto adicionado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content)
    })
    @PostMapping
    public void adicionarGasto(@RequestBody GastoRequest request) {
        googleSheetsService.somarNaCelula(request.getCategoria(), request.getMes(), request.getValor());
    }

    @Operation(summary = "Lista os totais de gastos do mês informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Totais listados com sucesso", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))
            }),
            @ApiResponse(responseCode = "400", description = "Parâmetro inválido", content = @Content)
    })
    @GetMapping("/valores")
    public GastoResumoDTO listarTotais(@RequestParam String mes) {
        return googleSheetsService.listarTotaisPorMes(mes);
    }



}
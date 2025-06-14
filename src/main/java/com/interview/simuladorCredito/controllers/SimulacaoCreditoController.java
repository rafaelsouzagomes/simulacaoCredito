package com.interview.simuladorCredito.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.interview.simuladorCredito.dtos.SimulacaoCreditoComArmazenamentoRequestDto;
import com.interview.simuladorCredito.dtos.SimulacaoCreditoComArmazenamentoResponseDto;
import com.interview.simuladorCredito.dtos.SimulacaoCreditoRequestDto;
import com.interview.simuladorCredito.dtos.SimulacaoCreditoResponseDto;
import com.interview.simuladorCredito.services.SimulacaoCreditoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/simulacoes")
@RequiredArgsConstructor
public class SimulacaoCreditoController {

    private final SimulacaoCreditoService service;
    
    
    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SimulacaoCreditoResponseDto consultarSimulacao(@RequestBody @Valid SimulacaoCreditoRequestDto dto) {
        return service.realizarSimulacao(dto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SimulacaoCreditoComArmazenamentoResponseDto CriarSimulacao(@RequestBody @Valid SimulacaoCreditoComArmazenamentoRequestDto dto) {
        return service.criarSimulacao(dto);
    }
}
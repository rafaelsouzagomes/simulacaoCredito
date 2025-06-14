package com.interview.simuladorCredito.controllers;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interview.simuladorCredito.dtos.AtualizarTaxaAnualRequestDto;
import com.interview.simuladorCredito.modelo.TaxaPorFaixaEtariaConfiguracao;
import com.interview.simuladorCredito.repositorios.TaxaPorFaixaEtariaConfiguracaoRepositorio;
import com.interview.simuladorCredito.services.SimulacaoCreditoService;

@RestController
@RequestMapping("/api/v1/taxas-faixa-etaria")
public class TaxaPorFaixaEtariaConfiguracaoController {

    private final TaxaPorFaixaEtariaConfiguracaoRepositorio taxaRepo;

    public TaxaPorFaixaEtariaConfiguracaoController(TaxaPorFaixaEtariaConfiguracaoRepositorio taxaRepo) {
        this.taxaRepo = taxaRepo;
    }
    

    @PatchMapping("/{id}/taxa-anual")
    @CacheEvict(cacheNames = SimulacaoCreditoService.SIMULACOES_CACHE, allEntries = true, beforeInvocation = true)
    public ResponseEntity<TaxaPorFaixaEtariaConfiguracao> atualizarTaxaAnual(
            @PathVariable Long id,
            @RequestBody AtualizarTaxaAnualRequestDto dto
    ) {
        Optional<TaxaPorFaixaEtariaConfiguracao> opt = taxaRepo.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        TaxaPorFaixaEtariaConfiguracao config = opt.get();
        config.setTaxaAnual(dto.taxaAnual().divide(BigDecimal.valueOf(100)));
        taxaRepo.save(config);
        return ResponseEntity.ok(config);
    }
} 
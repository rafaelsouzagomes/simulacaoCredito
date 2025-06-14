package com.interview.simuladorCredito.controllers;

import com.interview.simuladorCredito.dtos.AtualizarTaxaAnualRequestDto;
import com.interview.simuladorCredito.modelo.TaxaPorFaixaEtariaConfiguracao;
import com.interview.simuladorCredito.repositorios.TaxaPorFaixaEtariaConfiguracaoRepositorio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/taxas-faixa-etaria")
public class TaxaPorFaixaEtariaConfiguracaoController {

    private final TaxaPorFaixaEtariaConfiguracaoRepositorio taxaRepo;

    public TaxaPorFaixaEtariaConfiguracaoController(TaxaPorFaixaEtariaConfiguracaoRepositorio taxaRepo) {
        this.taxaRepo = taxaRepo;
    }
    

    @PatchMapping("/{id}/taxa-anual")
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
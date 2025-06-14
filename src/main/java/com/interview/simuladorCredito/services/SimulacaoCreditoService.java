package com.interview.simuladorCredito.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;

import com.interview.simuladorCredito.dtos.CalculoParamsDTO;
import com.interview.simuladorCredito.dtos.ResultadoCalculoPagamentoComJuros;
import com.interview.simuladorCredito.dtos.SimulacaoCreditoComArmazenamentoRequestDto;
import com.interview.simuladorCredito.dtos.SimulacaoCreditoComArmazenamentoResponseDto;
import com.interview.simuladorCredito.dtos.SimulacaoCreditoRequestDto;
import com.interview.simuladorCredito.dtos.SimulacaoCreditoResponseDto;
import com.interview.simuladorCredito.modelo.SimulacaoCredito;
import com.interview.simuladorCredito.modelo.TaxaPorFaixaEtariaConfiguracao;
import com.interview.simuladorCredito.modelo.Usuario;
import com.interview.simuladorCredito.repositorios.SimulacaoCreditoRepositorio;
import com.interview.simuladorCredito.repositorios.TaxaPorFaixaEtariaConfiguracaoRepositorio;
import com.interview.simuladorCredito.repositorios.UsuarioRepositorio;
import com.interview.simuladorCredito.services.calculos.TipoCalculoJurosSistema;
import com.interview.simuladorCredito.services.calculos.TipoCalculoJurosSistemaLocator;
import com.interview.simuladorCredito.validadores.SimulacaoCreditoValidador;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SimulacaoCreditoService {

    private final SimulacaoCreditoRepositorio simulacaoRepositorio;
    private final TaxaPorFaixaEtariaConfiguracaoRepositorio taxaRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final SimulacaoCreditoValidador validador;
    private final TipoCalculoJurosSistemaLocator calculoLocator;
    

    @Cacheable(
        value = "simulacoesCache",
        key = "T(java.util.Objects).hash(#dto.valorSolicitado, #dto.prazoMeses, #taxaAnual, #dto.tipoCalculo, #dto.dataNascimento)"
    )
    @Transactional
    public SimulacaoCreditoResponseDto realizarSimulacao(@Valid SimulacaoCreditoRequestDto dto) {
    	
    	System.out.println(" - Executando Simulação  ");
        int idade = Period.between(dto.dataNascimento(), LocalDate.now()).getYears();
        TaxaPorFaixaEtariaConfiguracao faixa = taxaRepositorio.findByFaixaEtaria(idade);
        validador.validarConfiguracaoFaixaEtariaExistente(idade, faixa);

        TipoCalculoJurosSistema calculoStrategy = calculoLocator.getStrategy(dto.getTipoCalculo());
        CalculoParamsDTO calculosParams = new CalculoParamsDTO(dto.valorSolicitado(), dto.prazoMeses());

        ResultadoCalculoPagamentoComJuros resultado = calculoStrategy.efetuarCalculos(calculosParams, faixa);

        System.out.println(" - Execução Finalizada  ");
        System.out.println("");
        
        return new SimulacaoCreditoResponseDto(
            resultado.valorTotal(), resultado.parcelaMensal(), resultado.totalJuros()
        );
    }

    @Transactional
    public SimulacaoCreditoComArmazenamentoResponseDto criarSimulacao(SimulacaoCreditoComArmazenamentoRequestDto dto) {
        
    	Usuario usuario = usuarioRepositorio.findById(dto.idUsuario()).orElseThrow(() -> new IllegalStateException("Usuário não encontrado"));

        int idade = Period.between(dto.dataNascimento(), LocalDate.now()).getYears();

        TaxaPorFaixaEtariaConfiguracao faixa = taxaRepositorio.findByFaixaEtaria(idade);
        
        validador.validarConfiguracaoFaixaEtariaExistente(idade, faixa);
        
        TipoCalculoJurosSistema calculoStrategy = calculoLocator.getStrategy(dto.getTipoCalculo());
        CalculoParamsDTO calculosParams = new CalculoParamsDTO(dto.valorSolicitado(), dto.prazoMeses());
        
        ResultadoCalculoPagamentoComJuros resultado = calculoStrategy.efetuarCalculos(calculosParams, faixa);

        SimulacaoCredito simulacao = SimulacaoCredito.builder()
                .usuario(usuario)
                .valorSolicitado(dto.valorSolicitado())
                .dataNascimento(dto.dataNascimento())
                .prazoMeses(dto.prazoMeses())
                .valorTotal(resultado.valorTotal())
                .parcelaMensal(resultado.parcelaMensal())
                .totalJuros(resultado.totalJuros())
                .taxaAnualAplicada(faixa.getTaxaAnual())
                .dataHorario(LocalDateTime.now())
                .build();

        simulacaoRepositorio.save(simulacao);

        return new SimulacaoCreditoComArmazenamentoResponseDto(
                simulacao.getId(),
                simulacao.getValorSolicitado(),
                simulacao.getValorTotal(),
                simulacao.getParcelaMensal(),
                simulacao.getTotalJuros(),
                simulacao.getTaxaAnualAplicada()
        );
    }
    
    public BigDecimal taxaPorIdade(LocalDate dataNascimento) {
        int idade = Period.between(dataNascimento, LocalDate.now()).getYears();
        TaxaPorFaixaEtariaConfiguracao faixa = taxaRepositorio.findByFaixaEtaria(idade);
        return faixa != null ? faixa.getTaxaAnual() : null;
    }

}


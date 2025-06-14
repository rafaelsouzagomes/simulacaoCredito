package com.interview.simuladorCredito.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.interview.simuladorCredito.modelo.SimulacaoCredito;

public interface SimulacaoCreditoRepositorio extends JpaRepository<SimulacaoCredito, Long> {
	
}

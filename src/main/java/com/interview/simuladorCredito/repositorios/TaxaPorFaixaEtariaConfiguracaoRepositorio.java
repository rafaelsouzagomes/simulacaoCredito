package com.interview.simuladorCredito.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.interview.simuladorCredito.modelo.TaxaPorFaixaEtariaConfiguracao;

public interface TaxaPorFaixaEtariaConfiguracaoRepositorio extends JpaRepository<TaxaPorFaixaEtariaConfiguracao, Long> {

	@Query("SELECT t FROM TaxaPorFaixaEtariaConfiguracao t WHERE :idade BETWEEN t.idadeMinima AND t.idadeMaxima")
	public TaxaPorFaixaEtariaConfiguracao findByFaixaEtaria(@Param("idade") int idade);
}

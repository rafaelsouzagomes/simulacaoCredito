package com.interview.simuladorCredito.modelo;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxaPorFaixaEtariaConfiguracao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private int idadeMinima; // inclusiva

	@Column(nullable = false)
	private int idadeMaxima; // inclusiva

	@Column(nullable = false, precision = 5, scale = 3)
	private BigDecimal taxaAnual;


}

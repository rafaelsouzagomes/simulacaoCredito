package com.interview.simuladorCredito.modelo;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SimulacaoCredito {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_usuario", nullable = false)
	private Usuario usuario;

	@Column(nullable = false, precision = 15, scale = 2)
	private BigDecimal valorSolicitado;

	@Column(nullable = false)
	private LocalDate dataNascimento;

	@Column(nullable = false)
	private int prazoMeses;

	@Column(nullable = false, precision = 15, scale = 2)
	private BigDecimal valorTotal;

	@Column(nullable = false, precision = 15, scale = 2)
	private BigDecimal parcelaMensal;

	@Column(nullable = false, precision = 15, scale = 2)
	private BigDecimal totalJuros;

	@Column(nullable = false, precision = 5, scale = 3)
	private BigDecimal taxaAnualAplicada; // 0.050 = 5 %

	@Column(nullable = false)
	private LocalDateTime dataHorario;

	@Column(nullable = false, updatable = false)
	private Instant criadoEm;

	@PrePersist
	void prePersist() {
		criadoEm = Instant.now();
		if (dataHorario == null) {
			dataHorario = LocalDateTime.now();
		}
	}

}

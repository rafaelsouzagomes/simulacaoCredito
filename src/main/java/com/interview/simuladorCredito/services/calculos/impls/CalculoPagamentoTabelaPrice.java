package com.interview.simuladorCredito.services.calculos.impls;

import org.springframework.stereotype.Service;

import com.interview.simuladorCredito.dtos.CalculoParamsDTO;
import com.interview.simuladorCredito.dtos.ResultadoCalculoPagamentoComJuros;
import com.interview.simuladorCredito.modelo.TaxaPorFaixaEtariaConfiguracao;
import com.interview.simuladorCredito.services.calculos.TipoCalculoJurosSistema;

@Service
public class CalculoPagamentoTabelaPrice implements TipoCalculoJurosSistema {


	@Override
	public ResultadoCalculoPagamentoComJuros efetuarCalculos(CalculoParamsDTO dto,
			TaxaPorFaixaEtariaConfiguracao faixa) {
		// TODO Auto-generated method stub
		return null;
	}
}

package com.interview.simuladorCredito.services.calculos;

import com.interview.simuladorCredito.dtos.CalculoParamsDTO;
import com.interview.simuladorCredito.dtos.ResultadoCalculoPagamentoComJuros;
import com.interview.simuladorCredito.modelo.TaxaPorFaixaEtariaConfiguracao;

public interface TipoCalculoJurosSistema {

	public ResultadoCalculoPagamentoComJuros efetuarCalculos(CalculoParamsDTO dto, TaxaPorFaixaEtariaConfiguracao faixa);
}

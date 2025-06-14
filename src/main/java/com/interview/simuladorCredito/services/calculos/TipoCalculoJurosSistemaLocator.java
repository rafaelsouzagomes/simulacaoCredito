package com.interview.simuladorCredito.services.calculos;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.interview.simuladorCredito.services.calculos.impls.CalculoPagamentoComJuros;
import com.interview.simuladorCredito.services.calculos.impls.CalculoPagamentoTabelaPrice;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TipoCalculoJurosSistemaLocator {
    
    private final Map<String, TipoCalculoJurosSistema> strategies;
    private final CalculoPagamentoComJuros calculoPadrao;
    
    @PostConstruct
    public void init() {
        strategies.put(TipoCalculo.PADRAO.getCodigo(), calculoPadrao);
        strategies.put(TipoCalculo.TABELA_PRICE.getCodigo(), new CalculoPagamentoTabelaPrice());
    }
    
    public TipoCalculoJurosSistema getStrategy(TipoCalculo tipo) {
        return strategies.getOrDefault(tipo.getCodigo(), calculoPadrao);
    }

	public TipoCalculoJurosSistema getStrategy(String tipoCalculo) {
		  return strategies.getOrDefault(tipoCalculo, calculoPadrao);
	}
} 
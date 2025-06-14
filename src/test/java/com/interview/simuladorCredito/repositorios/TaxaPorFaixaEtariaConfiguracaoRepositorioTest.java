package com.interview.simuladorCredito.repositorios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import com.interview.simuladorCredito.modelo.TaxaPorFaixaEtariaConfiguracao;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaxaPorFaixaEtariaConfiguracaoRepositorioTest {

    @Autowired
    private TaxaPorFaixaEtariaConfiguracaoRepositorio repositorio;

    @Test
    @Sql(scripts = {
        "classpath:db/test/V1__criar_tabelas_teste.sql",
        "classpath:db/migration/V5__corrigir_taxas_padrao.sql"
    })
    void deveEncontrarTaxaParaIdadeDentroDaFaixa() {
        int idade = 35;
        TaxaPorFaixaEtariaConfiguracao configuracao = repositorio.findByFaixaEtaria(idade);

        assertNotNull(configuracao);
        assertEquals(0.030, configuracao.getTaxaAnual().doubleValue());
    }

    @Test
    @Sql(scripts = {
        "classpath:db/test/V1__criar_tabelas_teste.sql",
        "classpath:db/migration/V5__corrigir_taxas_padrao.sql"
    })
    void deveEncontrarTaxaParaIdadeNoLimiteDaFaixa() {
        int idade = 66;
        TaxaPorFaixaEtariaConfiguracao configuracao = repositorio.findByFaixaEtaria(idade);

        assertNotNull(configuracao);
        assertEquals(0.040, configuracao.getTaxaAnual().doubleValue());
    }
} 
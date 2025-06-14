DROP TABLE IF EXISTS taxa_faixa_etaria_cfg CASCADE;
CREATE TABLE taxa_faixa_etaria_cfg (
    id            SERIAL PRIMARY KEY,
    idade_minima  INT  NOT NULL,
    idade_maxima  INT  NOT NULL,
    taxa_anual    NUMERIC(5,3) NOT NULL
);

DROP TABLE IF EXISTS simulacao_credito CASCADE;
CREATE TABLE simulacao_credito (
    id                  SERIAL PRIMARY KEY,
    id_usuario          BIGINT NOT NULL,
    valor_solicitado    NUMERIC(15,2) NOT NULL,
    data_nascimento     DATE          NOT NULL,
    prazo_meses         INT           NOT NULL,
    valor_total         NUMERIC(15,2) NOT NULL,
    parcela_mensal      NUMERIC(15,2) NOT NULL,
    total_juros         NUMERIC(15,2) NOT NULL,
    taxa_anual_aplicada NUMERIC(5,3)  NOT NULL,
    data_horario        TIMESTAMP     NOT NULL,
    criado_em           TIMESTAMP     NOT NULL
);


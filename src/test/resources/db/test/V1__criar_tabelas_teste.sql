-- Criar tabela de usuário
CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    data_nascimento DATE NOT NULL,
    criado_em TIMESTAMP NOT NULL
);

-- Criar tabela de simulação
CREATE TABLE IF NOT EXISTS simulacao_credito (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    valor_solicitado DECIMAL(19,2) NOT NULL,
    data_nascimento DATE NOT NULL,
    prazo_meses INT NOT NULL,
    valor_total DECIMAL(19,2) NOT NULL,
    parcela_mensal DECIMAL(19,2) NOT NULL,
    total_juros DECIMAL(19,2) NOT NULL,
    taxa_anual_aplicada DECIMAL(5,3) NOT NULL,
    data_horario TIMESTAMP NOT NULL,
    criado_em TIMESTAMP NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);

-- Criar tabela de configuração de taxa por faixa etária
CREATE TABLE IF NOT EXISTS taxa_por_faixa_etaria_configuracao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    idade_minima INT NOT NULL,
    idade_maxima INT NOT NULL,
    taxa_anual DECIMAL(5,3) NOT NULL
); 
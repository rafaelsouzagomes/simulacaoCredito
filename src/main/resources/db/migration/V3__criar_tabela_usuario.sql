DROP TABLE IF EXISTS usuario CASCADE;

CREATE TABLE usuario (
    id              SERIAL PRIMARY KEY,
    nome            VARCHAR(100) NOT NULL,
    email           VARCHAR(100) NOT NULL UNIQUE,
    cpf             VARCHAR(11)  NOT NULL UNIQUE,
    data_nascimento DATE         NOT NULL,
    criado_em       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Add foreign key constraint to simulacao_credito
ALTER TABLE simulacao_credito 
    ADD CONSTRAINT fk_simulacao_usuario 
    FOREIGN KEY (id_usuario) 
    REFERENCES usuario(id); 
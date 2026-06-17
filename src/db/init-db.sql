CREATE DATABASE IF NOT EXISTS sistema_saude;
USE sistema_saude;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS prescricoes;
DROP TABLE IF EXISTS posologias;
DROP TABLE IF EXISTS medicacoes;
DROP TABLE IF EXISTS pacientes;
DROP TABLE IF EXISTS profissionais;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE profissionais (
  id_profissional INT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(100) NOT NULL,
  id_conselho VARCHAR(14) NOT NULL,
  login VARCHAR(50) NOT NULL,
  senha VARCHAR(255) NOT NULL,
  PRIMARY KEY (id_profissional),
  UNIQUE KEY uk_profissionais_conselho (id_conselho),
  UNIQUE KEY uk_profissionais_login (login)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE pacientes (
  id_paciente INT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(100) NOT NULL,
  cpf VARCHAR(14) NOT NULL,
  login VARCHAR(50) NOT NULL,
  senha VARCHAR(255) NOT NULL,
  PRIMARY KEY (id_paciente),
  UNIQUE KEY uk_pacientes_cpf (cpf),
  UNIQUE KEY uk_pacientes_login (login)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE medicacoes (
  id INT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(100) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_medicacoes_nome (nome)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE posologias (
  id INT NOT NULL AUTO_INCREMENT,
  medicacao_id INT NOT NULL,
  descricao VARCHAR(100) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_posologias_medicacao_descricao (medicacao_id, descricao),
  KEY idx_posologias_medicacao_id (medicacao_id),
  CONSTRAINT fk_posologias_medicacao
    FOREIGN KEY (medicacao_id) REFERENCES medicacoes (id)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE prescricoes (
  id_prescricao INT NOT NULL AUTO_INCREMENT,
  paciente_cpf VARCHAR(14) NOT NULL,
  medicacao_id INT NOT NULL,
  posologia_id INT NOT NULL,
  vezes_ao_dia INT NOT NULL,
  data_prescricao DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_prescricao),
  KEY idx_prescricoes_paciente_cpf (paciente_cpf),
  KEY idx_prescricoes_medicacao_id (medicacao_id),
  KEY idx_prescricoes_posologia_id (posologia_id),
  CONSTRAINT fk_prescricoes_paciente
    FOREIGN KEY (paciente_cpf) REFERENCES pacientes (cpf)
    ON DELETE CASCADE,
  CONSTRAINT fk_prescricoes_medicacao
    FOREIGN KEY (medicacao_id) REFERENCES medicacoes (id)
    ON DELETE CASCADE,
  CONSTRAINT fk_prescricoes_posologia
    FOREIGN KEY (posologia_id) REFERENCES posologias (id)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



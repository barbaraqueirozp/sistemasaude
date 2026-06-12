-- sistema_saude.medicacoes definição

CREATE TABLE `medicacoes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `paciente_cpf` varchar(14) NOT NULL,
  `nome_medicacao` varchar(100) NOT NULL,
  `posologia` varchar(50) NOT NULL,
  `vezes_ao_dia` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_medicacao_paciente` (`paciente_cpf`),
  CONSTRAINT `fk_medicacao_paciente` FOREIGN KEY (`paciente_cpf`) REFERENCES `pacientes` (`cpf`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- sistema_saude.pacientes definição

CREATE TABLE `pacientes` (
  `id_paciente` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `cpf` varchar(14) NOT NULL,
  `login` varchar(50) NOT NULL,
  `senha` varchar(255) NOT NULL,
  PRIMARY KEY (`id_paciente`),
  UNIQUE KEY `cpf` (`cpf`),
  UNIQUE KEY `login` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
-- sistema_saude.profissionais definição

CREATE TABLE `profissionais` (
  `id_proficionais` int(11) NOT NULL AUTO_INCREMENT,
  `id_conselho` varchar(14) NOT NULL,
  `senha` varchar(100) NOT NULL,
  PRIMARY KEY (`id_proficionais`),
  UNIQUE KEY `senha` (`senha`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
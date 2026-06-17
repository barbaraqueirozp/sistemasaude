-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: sistema_saude
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `medicacoes`
--

DROP TABLE IF EXISTS `medicacoes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicacoes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_medicacoes_nome` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicacoes`
--

LOCK TABLES `medicacoes` WRITE;
/*!40000 ALTER TABLE `medicacoes` DISABLE KEYS */;
INSERT INTO `medicacoes` VALUES (3,'Amoxicilina'),(8,'Dexametasona'),(7,'Finasterida'),(9,'Ibuprofeno'),(2,'Paracetamol');
/*!40000 ALTER TABLE `medicacoes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pacientes`
--

DROP TABLE IF EXISTS `pacientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pacientes` (
  `id_paciente` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `cpf` varchar(14) NOT NULL,
  `login` varchar(50) NOT NULL,
  `senha` varchar(255) NOT NULL,
  PRIMARY KEY (`id_paciente`),
  UNIQUE KEY `uk_pacientes_cpf` (`cpf`),
  UNIQUE KEY `uk_pacientes_login` (`login`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pacientes`
--

LOCK TABLES `pacientes` WRITE;
/*!40000 ALTER TABLE `pacientes` DISABLE KEYS */;
INSERT INTO `pacientes` VALUES (1,'TESTE','548454545','TESTE','TESTE'),(2,'Otavio','70308432150','Otavio','1234'),(4,'Paciente','123456789','Paciente','1234');
/*!40000 ALTER TABLE `pacientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `posologias`
--

DROP TABLE IF EXISTS `posologias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `posologias` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `medicacao_id` int(11) NOT NULL,
  `descricao` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_posologias_medicacao_descricao` (`medicacao_id`,`descricao`),
  KEY `idx_posologias_medicacao_id` (`medicacao_id`),
  CONSTRAINT `fk_posologias_medicacao` FOREIGN KEY (`medicacao_id`) REFERENCES `medicacoes` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `posologias`
--

LOCK TABLES `posologias` WRITE;
/*!40000 ALTER TABLE `posologias` DISABLE KEYS */;
INSERT INTO `posologias` VALUES (2,2,'750 mg'),(1,3,'500 mg'),(6,7,'1'),(7,8,'4'),(10,9,'400'),(8,9,'500'),(9,9,'750');
/*!40000 ALTER TABLE `posologias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescricoes`
--

DROP TABLE IF EXISTS `prescricoes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescricoes` (
  `id_prescricao` int(11) NOT NULL AUTO_INCREMENT,
  `paciente_cpf` varchar(14) NOT NULL,
  `medicacao_id` int(11) NOT NULL,
  `posologia_id` int(11) NOT NULL,
  `vezes_ao_dia` int(11) NOT NULL,
  `data_prescricao` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id_prescricao`),
  KEY `idx_prescricoes_paciente_cpf` (`paciente_cpf`),
  KEY `idx_prescricoes_medicacao_id` (`medicacao_id`),
  KEY `idx_prescricoes_posologia_id` (`posologia_id`),
  CONSTRAINT `fk_prescricoes_medicacao` FOREIGN KEY (`medicacao_id`) REFERENCES `medicacoes` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_prescricoes_paciente` FOREIGN KEY (`paciente_cpf`) REFERENCES `pacientes` (`cpf`) ON DELETE CASCADE,
  CONSTRAINT `fk_prescricoes_posologia` FOREIGN KEY (`posologia_id`) REFERENCES `posologias` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescricoes`
--

LOCK TABLES `prescricoes` WRITE;
/*!40000 ALTER TABLE `prescricoes` DISABLE KEYS */;
INSERT INTO `prescricoes` VALUES (2,'70308432150',7,6,1,'2026-06-14 17:12:20'),(3,'123456789',9,9,2,'2026-06-16 22:26:06');
/*!40000 ALTER TABLE `prescricoes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profissionais`
--

DROP TABLE IF EXISTS `profissionais`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `profissionais` (
  `id_profissional` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `id_conselho` varchar(14) NOT NULL,
  `login` varchar(50) NOT NULL,
  `senha` varchar(255) NOT NULL,
  PRIMARY KEY (`id_profissional`),
  UNIQUE KEY `uk_profissionais_conselho` (`id_conselho`),
  UNIQUE KEY `uk_profissionais_login` (`login`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profissionais`
--

LOCK TABLES `profissionais` WRITE;
/*!40000 ALTER TABLE `profissionais` DISABLE KEYS */;
INSERT INTO `profissionais` VALUES (1,'Barbara','19305','Barbara','1234'),(2,'Teste','5125252','teste@gmail.com','12345');
/*!40000 ALTER TABLE `profissionais` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'sistema_saude'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-17  0:43:59

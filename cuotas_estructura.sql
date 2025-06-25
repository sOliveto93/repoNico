-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: 10.3.16.35    Database: cuotas
-- ------------------------------------------------------
-- Server version	5.5.38-1~dotdeb.0-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `alumnos_carreras`
--

DROP TABLE IF EXISTS `alumnos_carreras`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alumnos_carreras` (
  `id_alumnos_carreras` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `carrera` double DEFAULT NULL,
  `fecha_hora_inscripcion_ingreso` datetime DEFAULT NULL,
  `estado` char(1) DEFAULT NULL,
  `idpersonas` bigint(20) unsigned DEFAULT NULL,
  `idcarrera` double unsigned DEFAULT NULL,
  `cohorte` int(10) unsigned NOT NULL,
  `idPersonasBeca` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id_alumnos_carreras`),
  UNIQUE KEY `index4` (`idcarrera`,`idpersonas`),
  KEY `FK_alumnos_carreras_1` (`idpersonas`),
  CONSTRAINT `FK_alumnos_carreras_1` FOREIGN KEY (`idpersonas`) REFERENCES `personas` (`idPersona`) ON DELETE CASCADE,
  CONSTRAINT `FK_alumnos_carreras_2` FOREIGN KEY (`idcarrera`) REFERENCES `carreras` (`idcarrera`)
) ENGINE=InnoDB AUTO_INCREMENT=234853 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `alumnos_carreras_bk`
--

DROP TABLE IF EXISTS `alumnos_carreras_bk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alumnos_carreras_bk` (
  `id_alumnos_carreras` bigint(20) unsigned NOT NULL DEFAULT '0',
  `carrera` double DEFAULT NULL,
  `fecha_hora_inscripcion_ingreso` datetime DEFAULT NULL,
  `estado` char(1) DEFAULT NULL,
  `idpersonas` bigint(20) unsigned DEFAULT NULL,
  `idcarrera` double unsigned DEFAULT NULL,
  `cohorte` int(10) unsigned NOT NULL,
  `idPersonasBeca` bigint(20) unsigned DEFAULT NULL,
  `inscripcion_duplicada` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anexo_datos_persona`
--

DROP TABLE IF EXISTS `anexo_datos_persona`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `anexo_datos_persona` (
  `id_anexo_datos_persona` int(11) NOT NULL AUTO_INCREMENT,
  `nivel_estudio` varchar(100) DEFAULT NULL,
  `titulo` varchar(255) DEFAULT NULL,
  `ambito_laboral` varchar(45) DEFAULT NULL,
  `institucion` varchar(150) DEFAULT NULL,
  `fecha_nacimiento` datetime DEFAULT NULL,
  `idpersona` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id_anexo_datos_persona`),
  KEY `FK_anexo_datos_persona_idx` (`idpersona`),
  CONSTRAINT `FK_anexo_datos_persona` FOREIGN KEY (`idpersona`) REFERENCES `personas` (`idPersona`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1254 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `beca`
--

DROP TABLE IF EXISTS `beca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `beca` (
  `idbeca` int(10) NOT NULL AUTO_INCREMENT,
  `descripcion` tinytext,
  PRIMARY KEY (`idbeca`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `carreras`
--

DROP TABLE IF EXISTS `carreras`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carreras` (
  `idcarrera` double unsigned NOT NULL AUTO_INCREMENT,
  `nombre_carrera` tinytext,
  `nombre_titulo` tinytext,
  `vigencia_carrera_desde` datetime DEFAULT NULL,
  `vigencia_carrera_hasta` datetime DEFAULT NULL,
  `estado` tinytext NOT NULL,
  `codigo` int(10) unsigned DEFAULT NULL,
  `tipo` varchar(45) DEFAULT 'dep',
  PRIMARY KEY (`idcarrera`),
  KEY `Index_1` (`idcarrera`)
) ENGINE=InnoDB AUTO_INCREMENT=8075 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cbr_fc`
--

DROP TABLE IF EXISTS `cbr_fc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cbr_fc` (
  `id_cbr_fc` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Nro` int(11) DEFAULT NULL,
  `fpago` int(11) DEFAULT NULL,
  `Descri1` tinytext,
  `descri2` tinytext,
  `fecha` datetime DEFAULT NULL,
  `monto` double DEFAULT NULL,
  `Pagado1` double DEFAULT NULL,
  `Oper` char(6) DEFAULT NULL,
  `fecar` datetime DEFAULT NULL,
  `comision` int(11) DEFAULT NULL,
  `id_cbr_sucu` int(10) unsigned NOT NULL,
  `idpersonas` bigint(20) unsigned DEFAULT NULL,
  `idcarrera` double unsigned NOT NULL,
  `estado` tinytext NOT NULL,
  `idTipoPago` int(10) unsigned NOT NULL,
  `idTipoFactura` int(10) NOT NULL,
  `idFormaPago` int(10) unsigned NOT NULL,
  `responsableIva` tinytext,
  `razonSocial` tinytext,
  PRIMARY KEY (`id_cbr_fc`),
  KEY `Index_3` (`Nro`),
  KEY `FK_cbr_fc_1` (`id_cbr_sucu`),
  KEY `FK_cbr_fc_2` (`idpersonas`),
  KEY `FK_cbr_fc_3_idx` (`idTipoPago`),
  KEY `FK_cbr_fc_4` (`idTipoFactura`),
  KEY `FK_cbr_fc_5_idx` (`idFormaPago`),
  CONSTRAINT `FK_cbr_fc_1` FOREIGN KEY (`id_cbr_sucu`) REFERENCES `cbr_sucu` (`id_cbr_sucu`),
  CONSTRAINT `FK_cbr_fc_2` FOREIGN KEY (`idpersonas`) REFERENCES `personas` (`idPersona`),
  CONSTRAINT `FK_cbr_fc_3` FOREIGN KEY (`idTipoPago`) REFERENCES `tipopago` (`idTipoPago`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_cbr_fc_4` FOREIGN KEY (`idTipoFactura`) REFERENCES `tipofactura` (`idTipoFactura`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_cbr_fc_5` FOREIGN KEY (`idFormaPago`) REFERENCES `forma_pago` (`idFormaPago`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=345823 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cbr_fc_bk`
--

DROP TABLE IF EXISTS `cbr_fc_bk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cbr_fc_bk` (
  `id_cbr_fc` int(10) unsigned NOT NULL DEFAULT '0',
  `Nro` int(11) DEFAULT NULL,
  `fpago` int(11) DEFAULT NULL,
  `Descri1` tinytext,
  `descri2` tinytext,
  `fecha` datetime DEFAULT NULL,
  `monto` double DEFAULT NULL,
  `Pagado1` double DEFAULT NULL,
  `Oper` char(6) DEFAULT NULL,
  `fecar` datetime DEFAULT NULL,
  `comision` int(11) DEFAULT NULL,
  `id_cbr_sucu` int(10) unsigned NOT NULL,
  `idpersonas` bigint(20) unsigned DEFAULT NULL,
  `idcarrera` double unsigned NOT NULL,
  `estado` tinytext NOT NULL,
  `idTipoPago` int(10) unsigned NOT NULL,
  `idTipoFactura` int(10) NOT NULL,
  `idFormaPago` int(10) unsigned NOT NULL,
  `responsableIva` tinytext,
  `razonSocial` tinytext
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cbr_item`
--

DROP TABLE IF EXISTS `cbr_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cbr_item` (
  `id_cbr_item` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Nro` int(11) NOT NULL,
  `codigo` int(11) NOT NULL,
  `Descri` tinytext,
  `cantidad` int(11) DEFAULT NULL,
  `precio` double DEFAULT NULL,
  `id_cbr_fc` int(10) unsigned NOT NULL,
  `id_cbr_tipos` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id_cbr_item`),
  KEY `FK_cbr_item_1` (`id_cbr_fc`),
  KEY `FK_cbr_item_2` (`id_cbr_tipos`),
  CONSTRAINT `FK_cbr_item_1` FOREIGN KEY (`id_cbr_fc`) REFERENCES `cbr_fc` (`id_cbr_fc`),
  CONSTRAINT `FK_cbr_item_2` FOREIGN KEY (`id_cbr_tipos`) REFERENCES `cbr_tipos` (`id_cbr_tipos`)
) ENGINE=InnoDB AUTO_INCREMENT=359440 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cbr_rubro`
--

DROP TABLE IF EXISTS `cbr_rubro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cbr_rubro` (
  `id_cbr_rubro` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `codigo` int(11) NOT NULL,
  `descri` tinytext,
  `estado` tinytext NOT NULL,
  PRIMARY KEY (`id_cbr_rubro`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cbr_sucu`
--

DROP TABLE IF EXISTS `cbr_sucu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cbr_sucu` (
  `id_cbr_sucu` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `codigo` int(11) NOT NULL,
  `descri` tinytext,
  `Numero` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_cbr_sucu`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cbr_tipos`
--

DROP TABLE IF EXISTS `cbr_tipos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cbr_tipos` (
  `id_cbr_tipos` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `id_cbr_rubro` int(10) unsigned NOT NULL,
  `codigo` int(11) NOT NULL,
  `descri` tinytext,
  `precio` double DEFAULT NULL,
  `estado` tinytext NOT NULL,
  `idpartida` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_cbr_tipos`),
  KEY `FK_cbr_tipos_1` (`id_cbr_rubro`),
  KEY `FK_cbr_tipos_2_idx` (`idpartida`),
  CONSTRAINT `FK_cbr_tipos_1` FOREIGN KEY (`id_cbr_rubro`) REFERENCES `cbr_rubro` (`id_cbr_rubro`),
  CONSTRAINT `FK_cbr_tipos_2` FOREIGN KEY (`idpartida`) REFERENCES `partida` (`idpartida`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=762 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `codigos_anteriores`
--

DROP TABLE IF EXISTS `codigos_anteriores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `codigos_anteriores` (
  `id_codigos_anteriores` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `id_persona_cuota` bigint(20) unsigned NOT NULL,
  `codigo_barra` varchar(45) NOT NULL,
  PRIMARY KEY (`id_codigos_anteriores`),
  KEY `FK_codigos_anteriores_1_idx` (`id_persona_cuota`),
  CONSTRAINT `FK_codigos_anteriores_1` FOREIGN KEY (`id_persona_cuota`) REFERENCES `persona_cuotas` (`id_persona_cuota`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=131046 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `codigos_anteriores_bk`
--

DROP TABLE IF EXISTS `codigos_anteriores_bk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `codigos_anteriores_bk` (
  `id_codigos_anteriores` bigint(20) unsigned NOT NULL DEFAULT '0',
  `id_persona_cuota` bigint(20) unsigned NOT NULL,
  `codigo_barra` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `forma_pago`
--

DROP TABLE IF EXISTS `forma_pago`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `forma_pago` (
  `idFormaPago` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(45) NOT NULL,
  PRIMARY KEY (`idFormaPago`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `g_auditoria`
--

DROP TABLE IF EXISTS `g_auditoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `g_auditoria` (
  `FECHA` datetime NOT NULL,
  `OPERADOR` char(45) DEFAULT NULL,
  `DESCRI` tinytext,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=201190 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `g_opcion`
--

DROP TABLE IF EXISTS `g_opcion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `g_opcion` (
  `CODIGO` char(7) NOT NULL,
  `DESCRI` tinytext,
  `activo` int(11) DEFAULT NULL,
  `OPERADOR` char(6) DEFAULT NULL,
  `Fecha` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `g_opecate`
--

DROP TABLE IF EXISTS `g_opecate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `g_opecate` (
  `CODIGO` char(3) NOT NULL,
  `DESCRI` tinytext
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `g_operador`
--

DROP TABLE IF EXISTS `g_operador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `g_operador` (
  `codigo` char(6) NOT NULL,
  `descri` tinytext,
  `desprt` tinytext,
  `mail` tinytext,
  `secreto` tinytext,
  `categoria` char(3) NOT NULL,
  `Obser` mediumtext
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `g_operopc`
--

DROP TABLE IF EXISTS `g_operopc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `g_operopc` (
  `categoria` char(3) NOT NULL,
  `opcion` char(7) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `grupo`
--

DROP TABLE IF EXISTS `grupo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grupo` (
  `idgrupo` bigint(20) NOT NULL AUTO_INCREMENT,
  `codigo` bigint(20) NOT NULL,
  `Descri` tinytext,
  `AnoInicio` int(11) DEFAULT NULL,
  `TipoInforme` int(11) DEFAULT NULL,
  `Concepto1` tinytext,
  `Concepto2` tinytext,
  `idcarrera` double NOT NULL DEFAULT '0',
  `nombre_titulo` tinytext,
  `CtrlPago` char(1) DEFAULT NULL,
  `VtoDias` int(11) DEFAULT NULL,
  `VtoPlus` int(11) DEFAULT NULL,
  `Comision` int(11) DEFAULT NULL,
  `estado` tinytext NOT NULL,
  `VtoMonto` double DEFAULT NULL,
  `conceptoUnoMonto` double DEFAULT '0',
  `conceptoDosMonto` double DEFAULT '0',
  `cantidadCuotas` int(11) NOT NULL DEFAULT '0',
  `cobramora` char(1) DEFAULT NULL,
  `idpartida` int(11) NOT NULL,
  `numero` bigint(20) DEFAULT NULL,
  `id_cbr_tipos` int(10) unsigned DEFAULT NULL,
  `id_cbr_tipos_mora` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`idgrupo`),
  KEY `FK_grupo_1` (`idcarrera`),
  KEY `FK_grupo_2_idx` (`idpartida`),
  KEY `FK_grupo_3_idx` (`id_cbr_tipos`),
  KEY `FK_grupo_4_idx` (`id_cbr_tipos_mora`),
  CONSTRAINT `FK_grupo_1` FOREIGN KEY (`idcarrera`) REFERENCES `carreras` (`idcarrera`),
  CONSTRAINT `FK_grupo_2` FOREIGN KEY (`idpartida`) REFERENCES `partida` (`idpartida`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_grupo_3` FOREIGN KEY (`id_cbr_tipos`) REFERENCES `cbr_tipos` (`id_cbr_tipos`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_grupo_4` FOREIGN KEY (`id_cbr_tipos_mora`) REFERENCES `cbr_tipos` (`id_cbr_tipos`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8123 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `grupocuota`
--

DROP TABLE IF EXISTS `grupocuota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grupocuota` (
  `idgrupocuota` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `idgrupo` bigint(20) DEFAULT NULL,
  `Tipo` int(10) unsigned NOT NULL DEFAULT '1',
  `NroMes` int(11) DEFAULT NULL,
  `Monto1` double DEFAULT NULL,
  `Monto2` double NOT NULL DEFAULT '0',
  `estado` varchar(45) NOT NULL,
  `cobra_mora` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idgrupocuota`),
  KEY `FK_grupocuota_2` (`Tipo`),
  KEY `FK_grupocuota_1` (`idgrupo`),
  CONSTRAINT `FK_grupocuota_1` FOREIGN KEY (`idgrupo`) REFERENCES `grupo` (`idgrupo`),
  CONSTRAINT `FK_grupocuota_2` FOREIGN KEY (`Tipo`) REFERENCES `tipo_cuota` (`idtipo_cuota`)
) ENGINE=InnoDB AUTO_INCREMENT=2954 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gruposusuario`
--

DROP TABLE IF EXISTS `gruposusuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gruposusuario` (
  `idgrupo` bigint(20) NOT NULL,
  `idusuario` int(10) unsigned NOT NULL,
  PRIMARY KEY (`idgrupo`,`idusuario`),
  KEY `FK_gruposusuario_2_idx_idx` (`idusuario`),
  CONSTRAINT `FK_gruposusuario_1_idx` FOREIGN KEY (`idgrupo`) REFERENCES `grupo` (`idgrupo`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_gruposusuario_2_idx` FOREIGN KEY (`idusuario`) REFERENCES `usuarios` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `guarani`
--

DROP TABLE IF EXISTS `guarani`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `guarani` (
  `idGuarani` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `UA` varchar(10) NOT NULL,
  `n_inscripcion` varchar(10) NOT NULL,
  `legajo` varchar(10) DEFAULT NULL,
  `tipo_documento` varchar(20) DEFAULT 'DN',
  `nro_documento` varchar(100) NOT NULL,
  `apellido` varchar(100) NOT NULL,
  `nombres` varchar(100) NOT NULL,
  `calle` varchar(100) NOT NULL,
  `numero` varchar(10) NOT NULL,
  `piso` varchar(10) NOT NULL,
  `dpto` varchar(10) NOT NULL,
  `cp` varchar(100) NOT NULL,
  `telefono` varchar(20) NOT NULL,
  `partido` varchar(100) NOT NULL,
  `provincia` varchar(100) NOT NULL,
  `pais` varchar(100) NOT NULL,
  `carrera` varchar(100) NOT NULL,
  `fecha_ingreso` date DEFAULT NULL,
  `fecha_inscripcion` date DEFAULT NULL,
  `tipo` varchar(10) NOT NULL,
  `regular` varchar(1) NOT NULL,
  `calidad` varchar(1) NOT NULL,
  `actualizar` tinyint(1) NOT NULL DEFAULT '0',
  `cohorte` int(10) unsigned NOT NULL,
  `procesado` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `id_persona` bigint(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`idGuarani`)
) ENGINE=InnoDB AUTO_INCREMENT=625 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comentario` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26591 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mapeo_carrera_dep`
--

DROP TABLE IF EXISTS `mapeo_carrera_dep`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mapeo_carrera_dep` (
  `idmapeo_carrera_dep` int(11) NOT NULL AUTO_INCREMENT,
  `id_dep` int(11) NOT NULL,
  `descripcion_dep` varchar(200) DEFAULT NULL,
  `id_cuotas` int(11) DEFAULT NULL,
  PRIMARY KEY (`idmapeo_carrera_dep`)
) ENGINE=InnoDB AUTO_INCREMENT=608 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mapeo_tipo_doc`
--

DROP TABLE IF EXISTS `mapeo_tipo_doc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mapeo_tipo_doc` (
  `idMapeo` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `tipo_documento_cuotas` char(2) DEFAULT NULL,
  `tipo_documento_guarani` char(3) DEFAULT NULL,
  `tipo_documento_cursos` char(3) DEFAULT NULL,
  PRIMARY KEY (`idMapeo`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nro_documento`
--

DROP TABLE IF EXISTS `nro_documento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nro_documento` (
  `nro_dovumentos` varchar(20) NOT NULL,
  PRIMARY KEY (`nro_dovumentos`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `origen_pago`
--

DROP TABLE IF EXISTS `origen_pago`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `origen_pago` (
  `idOrigenPago` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(45) NOT NULL,
  PRIMARY KEY (`idOrigenPago`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pago_facil`
--

DROP TABLE IF EXISTS `pago_facil`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pago_facil` (
  `idPago_facil` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `colegio` int(10) unsigned NOT NULL,
  `nombre` varchar(60) NOT NULL,
  `numero_persona` bigint(20) unsigned NOT NULL,
  `numero_cliente` bigint(20) unsigned NOT NULL,
  `fecha_pago` date NOT NULL,
  `fecha_proceso` date NOT NULL,
  `fecha_real` date NOT NULL,
  `importe` double NOT NULL,
  `vto_original` date NOT NULL,
  `periodo` bigint(20) unsigned NOT NULL,
  `codbarra` varchar(40) NOT NULL,
  `estado` varchar(20) NOT NULL,
  `tipo_doc` varchar(5) NOT NULL,
  `num_doc` varchar(45) NOT NULL,
  `fecha` date DEFAULT NULL,
  PRIMARY KEY (`idPago_facil`)
) ENGINE=InnoDB AUTO_INCREMENT=73730 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pagos_todopago`
--

DROP TABLE IF EXISTS `pagos_todopago`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pagos_todopago` (
  `codigo_todopago` varchar(100) NOT NULL,
  `estado_todopago` varchar(15) DEFAULT NULL,
  `id_persona` bigint(20) unsigned DEFAULT NULL,
  `fecha_inicio_transaccion` datetime DEFAULT NULL,
  `fecha_fin_transaccion` datetime DEFAULT NULL,
  `monto_transaccion` double DEFAULT NULL,
  `cuotas_transaccion` text,
  `public_request_key_todopago` varchar(48) DEFAULT NULL,
  `request_key_todopago` varchar(48) DEFAULT NULL,
  `answer_key_todopago` varchar(48) DEFAULT NULL,
  `status_code` int(11) DEFAULT NULL,
  PRIMARY KEY (`codigo_todopago`),
  KEY `FK_pagos_todopago_1` (`id_persona`),
  CONSTRAINT `FK_pagos_todopago_1` FOREIGN KEY (`id_persona`) REFERENCES `personas` (`idPersona`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `partida`
--

DROP TABLE IF EXISTS `partida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `partida` (
  `idpartida` int(11) NOT NULL AUTO_INCREMENT,
  `descripcion` tinytext,
  PRIMARY KEY (`idpartida`)
) ENGINE=InnoDB AUTO_INCREMENT=237 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-29 15:50:08

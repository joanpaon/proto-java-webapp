-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 29-01-2022 a las 19:13:23
-- Versión del servidor: 10.4.21-MariaDB
-- Versión de PHP: 8.0.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `proto`
--
CREATE DATABASE IF NOT EXISTS `proto` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `proto`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `perfiles`
--

CREATE TABLE `perfiles` (
  `id` int(11) NOT NULL,
  `nombre` varchar(30) NOT NULL,
  `info` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `perfiles`
--

INSERT INTO `perfiles` (`id`, `nombre`, `info`) VALUES
(0, 'Visitante', 'Usuario no identificado'),
(100, 'Usuario', 'Usuario identificado'),
(800, 'Administrador', 'Usuario con derechos administrativos'),
(900, 'Desarrollador', 'Constructor de la aplicación');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `permisos_perfil`
--

CREATE TABLE `permisos_perfil` (
  `id` int(11) NOT NULL,
  `perfil` int(11) NOT NULL,
  `proceso` int(11) NOT NULL,
  `info` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `permisos_usuario`
--

CREATE TABLE `permisos_usuario` (
  `id` int(11) NOT NULL,
  `usuario` int(11) NOT NULL,
  `proceso` int(11) NOT NULL,
  `info` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `permisos_usuario`
--

INSERT INTO `permisos_usuario` (`id`, `usuario`, `proceso`, `info`) VALUES
(2, 3, 2, 'Permiso para el listado de Permisos de Usuario - Administrador');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `procesos`
--

CREATE TABLE `procesos` (
  `id` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `info` varchar(100) NOT NULL,
  `perfil` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `procesos`
--

INSERT INTO `procesos` (`id`, `nombre`, `info`, `perfil`) VALUES
(1, 'CommandPerfilListado', 'Listado de Perfiles - Administrador', 800),
(2, 'CommandPermisoUsuarioListado', 'Listado de Permisos de Usuario - Administrador', 800);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL,
  `user` varchar(30) NOT NULL,
  `pass` varchar(30) NOT NULL,
  `perfil` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `user`, `pass`, `perfil`) VALUES
(1, 'homer', '123456', 100),
(2, 'marge', '123456', 100),
(3, 'admin', '123456', 800),
(4, 'dev', '123456', 900);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `perfiles`
--
ALTER TABLE `perfiles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `PERFILES_NOMBRE_U` (`nombre`);

--
-- Indices de la tabla `permisos_perfil`
--
ALTER TABLE `permisos_perfil`
  ADD PRIMARY KEY (`id`),
  ADD KEY `PERFIL_PERMISOS_PERFILES_FK` (`perfil`),
  ADD KEY `PERFIL_PERMISOS_PROCESOS_FK` (`proceso`);

--
-- Indices de la tabla `permisos_usuario`
--
ALTER TABLE `permisos_usuario`
  ADD PRIMARY KEY (`id`),
  ADD KEY `USUARIO_PERMISOS_USUARIOS_FK` (`usuario`),
  ADD KEY `USUARIO_PERMISOS_PROCESOS_FK` (`proceso`);

--
-- Indices de la tabla `procesos`
--
ALTER TABLE `procesos`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `PROCESOS_NOMBRE_U` (`nombre`),
  ADD KEY `PROCESOS_PERFILES_FK` (`perfil`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `USUARIOS_USER_U` (`user`),
  ADD KEY `USUARIOS_PERFILES_FK` (`perfil`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `perfiles`
--
ALTER TABLE `perfiles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=902;

--
-- AUTO_INCREMENT de la tabla `permisos_perfil`
--
ALTER TABLE `permisos_perfil`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `permisos_usuario`
--
ALTER TABLE `permisos_usuario`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `procesos`
--
ALTER TABLE `procesos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `permisos_perfil`
--
ALTER TABLE `permisos_perfil`
  ADD CONSTRAINT `PERFIL_PERMISOS_PERFILES_FK` FOREIGN KEY (`perfil`) REFERENCES `perfiles` (`id`),
  ADD CONSTRAINT `PERFIL_PERMISOS_PROCESOS_FK` FOREIGN KEY (`proceso`) REFERENCES `procesos` (`id`);

--
-- Filtros para la tabla `permisos_usuario`
--
ALTER TABLE `permisos_usuario`
  ADD CONSTRAINT `USUARIO_PERMISOS_PROCESOS_FK` FOREIGN KEY (`proceso`) REFERENCES `procesos` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `USUARIO_PERMISOS_USUARIOS_FK` FOREIGN KEY (`usuario`) REFERENCES `usuarios` (`id`) ON UPDATE CASCADE;

--
-- Filtros para la tabla `procesos`
--
ALTER TABLE `procesos`
  ADD CONSTRAINT `PROCESOS_PERFILES_FK` FOREIGN KEY (`perfil`) REFERENCES `perfiles` (`id`) ON UPDATE CASCADE;

--
-- Filtros para la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD CONSTRAINT `USUARIOS_PERFILES_FK` FOREIGN KEY (`perfil`) REFERENCES `perfiles` (`id`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

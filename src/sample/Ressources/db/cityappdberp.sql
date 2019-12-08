-- phpMyAdmin SQL Dump
-- version 4.9.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le :  jeu. 05 déc. 2019 à 21:02
-- Version du serveur :  10.4.8-MariaDB
-- Version de PHP :  7.3.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `cityappdberp`
--

-- --------------------------------------------------------

--
-- Structure de la table `caisse`
--

CREATE TABLE `caisse` (
  `idCaisse` int(11) NOT NULL,
  `date` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `montant` double NOT NULL,
  `remarque` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `numeroShift` int(11) NOT NULL,
  `closed` tinyint(4) NOT NULL,
  `employees_id` int(11) NOT NULL,
  `date_fermeture` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `numeroCaisse` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `has_error` tinyint(4) DEFAULT NULL,
  `error_amount` double NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `caisse_monnaie`
--

CREATE TABLE `caisse_monnaie` (
  `id_caisse_monnaie` int(11) NOT NULL,
  `caisse_idCaisse` int(11) NOT NULL,
  `numeroShift` int(11) NOT NULL,
  `lessThanOneEuro` double NOT NULL,
  `fiftyCents` int(11) NOT NULL,
  `oneEuro` int(11) NOT NULL,
  `twoEuros` int(11) NOT NULL,
  `fiveEuros` int(11) NOT NULL,
  `tenEuros` int(11) NOT NULL,
  `twentyEuros` int(11) NOT NULL,
  `fiftyEuros` int(11) NOT NULL,
  `oneHundredEuros` int(11) NOT NULL,
  `twoHundredEuros` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `caisse_recettes`
--

CREATE TABLE `caisse_recettes` (
  `id_caisse_recettes` int(11) NOT NULL,
  `montant` double NOT NULL,
  `type` tinyint(4) NOT NULL,
  `date` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `time` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `indexClient` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `remarque` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `numeroShift` int(11) NOT NULL,
  `reason` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `salaryBeneficial` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fk_idCaisse` int(11) NOT NULL,
  `employees_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `employees`
--

CREATE TABLE `employees` (
  `id` int(11) NOT NULL,
  `firstName` varchar(25) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT 'user',
  `lastName` varchar(25) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT 'user',
  `inService` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user',
  `outService` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nationalRegisterNum` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user',
  `address` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT 'user',
  `houseNum` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT 'user',
  `letterBoxNum` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT 'user',
  `postalCode` int(11) NOT NULL DEFAULT 1,
  `city` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT 'user',
  `CivilStatus` varchar(15) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT 'user',
  `sex` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT 'user',
  `pseudo` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT 'user',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT 'user',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT 'user',
  `dept` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user',
  `role` int(10) NOT NULL DEFAULT 1,
  `phoneNumber` varchar(25) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '111-11-111-1',
  `salary1` double NOT NULL DEFAULT 0,
  `salary2` double NOT NULL DEFAULT 0,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user',
  `employeeNumber` int(11) NOT NULL DEFAULT 1,
  `country` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user',
  `phoneCountry` varchar(5) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user',
  `birthday` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `iban` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'iban'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `employees`
--

INSERT INTO `employees` (`id`, `firstName`, `lastName`, `inService`, `outService`, `nationalRegisterNum`, `address`, `houseNum`, `letterBoxNum`, `postalCode`, `city`, `CivilStatus`, `sex`, `pseudo`, `email`, `password`, `dept`, `role`, `phoneNumber`, `salary1`, `salary2`, `status`, `employeeNumber`, `country`, `phoneCountry`, `birthday`, `iban`) VALUES
(1, 'PRATEEBA', 'RUGGOO', '2019-03-08', NULL, '96052750854', 'MOORSELBAAN', '59', '', 9300, 'ALOST', 'Célibataire', 'Feminin', 'prat', 'pruggoo@ulb.ac.be', 'prat0854', 'Front Office', 0, '111-11-111-1', 0, 0, 'Etudiant Employé', 1, 'user', 'user', '2019-10-16', 'iban'),
(2, 'HRISTINA', 'ANGHILUTA', '2018-11-22', NULL, '84010250839', 'CHAUSSEE DE LOUVAIN', '619', '', 1030, 'BRUXELLES 3', 'Single', 'Female', 'AH', 'anghiluta.2015@hotmail.com', 'AH0839', 'HK', 1, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(3, 'SANDRINE', 'BOUBA NJAPA', '2019-01-01', NULL, '86022249880', 'TOEKOMSTSTRAAT', '74', '', 9470, 'DENDERLEEUW', 'Single', 'Female', 'BS', 'sandrabouba@yahoo.fr', 'BS9880', 'HK', 1, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(4, 'INA', 'KASNIJA', '2019-01-01', NULL, '89022068021', 'CHAUSSEE DE BOONDAEL', '252', '', 1050, 'IXELLES', 'Single', 'Female', 'KI', 'inakas09@gmail.com', 'KI8021', 'FO', 1, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(6, 'ALINE', 'PONCE', '2019-01-01', NULL, '92082349032', 'RUE STANLEY', '74', '', 1180, 'BRUXELLES 18', 'Single', 'Female', 'PA', 'aline.ponce@ulb.ac.be', 'PA9032', 'FO', 1, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(7, 'QUENTIN', 'BRYSSINCK', '2019-01-29', NULL, '90070712156', 'CHAUSSEE DE BOONDAEL', '252', '', 1050, 'IXELLES', 'Single', 'Male', 'BK', 'quentin.bryssinck@gmail.com', 'BK2156', 'FO', 1, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(8, 'KEVINE', 'KAMSU TCHASSEM', '2019-01-01', NULL, '90111261819', 'ALLEE DES BOULEAUX', '18', '', 7000, 'MONS', 'Single', 'Female', 'KS', 'kkevinelaure@gmail.com', 'KS1819', 'HK', 1, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(9, 'VANESSA', 'TCHIEUDJIE CHRISTELLE', '2019-01-01', NULL, '92051369014', 'PLACE DE LA PETITE SUISSE', '5', '/2', 1050, 'IXELLES', 'Single', 'Female', 'TH', 'vanessa.tchieudjeu@gmail.com', 'TH9014', 'HK', 1, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(10, 'CRISTINA-ANDREA', 'MARINOIU', '2019-01-01', NULL, '89060863665', 'RUE HENRI EVENEPOEL', '127', '/6', 1030, 'BRUXELLES 3', 'Married', 'Female', 'MC', 'marinoiucristina@yahoo.com', 'MC3665', 'HK', 1, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(11, 'ROBERT', 'RUTKOWSKI', '2018-10-23', NULL, '82111054977', 'RUE EGIDE WALSCHAERTS', '42', '/1', 1060, 'BRUXELLES 6', 'Single', 'Male', 'RW', 'robert.rutkowski1@02.pl', 'RW4977', 'MT', 1, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(13, 'ETIENNE', 'MORISSEAU', '2019-01-01', NULL, '93121659391', 'RUE EMILE BANNING', '95', '', 1050, 'IXELLES', 'Single', 'Male', 'ME', 'etienne.moris@gmail.com', 'ME9391', 'FO', 1, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(14, 'MAGDALENA', 'DRAPPIER', '2019-01-01', NULL, '2021920685', 'AVENUE A. BONTEMPS', '13', '/1', 1340, 'OTTIGNIES', 'Single', 'Female', 'DM', 'magdalena@drappier.net', 'DM0685', 'BO', 1, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(16, 'RALLOU', 'NGAINANG TOUKAM', '2019-01-17', NULL, '91101573619', 'CHAUSSEE DE LOUVAIN', '884', '', 1140, 'EVERE', 'Single', 'Female', 'NT', 'ngainangrallou.9@gmail.com', 'NT3619', 'HK', 1, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(17, 'RANYA', 'TOUATI', '2019-02-11', NULL, '95092066022', 'RUE EMILE WAUTERS', '49', '', 1020, 'BRUXELLES 2', 'Single', 'Female', 'TR', 'ranyy88@hotmail.com', 'TR6022', 'FO', 1, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(18, 'CHRISTIAN', 'Drappier', '2012-07-02', NULL, '48060727976', 'Schnellenberg', '36', '134', 4721, 'Neu Moresnet', 'Single', 'Male', 'DR', 'christian@drappier.net', '@Chd_35280#', 'FO', 1, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(20, 'FANNY', 'DURIEU', '2017-10-02', NULL, '89031417435', 'Rue de Biesme', '103', '', 6531, 'Thuin', 'Single', 'Female', 'DF', 'durieufanny@gmail.com', 'DF7435', 'FO', 1, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(21, 'EKIEM', 'DISCART', '2019-07-18', NULL, '95080751367', 'Rue commandant Ponthier', '37', '', 1040, 'Etterbeek', 'Single', 'Male', 'DE', 'ekiemdiscart@hotmail.com', 'DE1367', 'FO', 1, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(22, 'ROKSANA', 'MIKHAILOVNA SABANOVA', '2019-07-01', '2019-07-01', '94031862257', 'Chaussée d\'Anvers', '421', '', 1000, 'Bruxelles', 'Single', 'Female', 'SR', 'Roxa-sab@live.fr', 'SR2257', 'HK', 1, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(23, 'LAURITA', 'GRATII', '2019-08-01', NULL, '94010565017', 'Rue Evenepoel', '29', '', 1030, 'Bruxelles', 'Single', 'Female', 'GL', 'taci_laurita@yahoo.com', 'GL5017', 'BO', 1, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(24, 'LISIANE-CARELLE', 'TCHEKAM TCHASSE', '2019-05-29', NULL, '94090861617', 'Allée des bouleaux', '18', '', 7000, 'Mons', 'Single', 'Female', 'TL', 'ltchekam@yahoo.fr', 'TL1617', 'HK', 1, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(25, 'DOMINIQUE', 'MONI', '2019-01-01', NULL, '95070359796', 'Rue Luther', '30', '', 1000, 'Bruxelles', 'Single', 'Female', 'MD', 'dominikmoni@yahoo.be', 'MD9796', 'HK', 1, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(26, 'Megane', 'Amossé', '2019-08-05', NULL, '94061852875', 'Rue Wéry', '100', '', 1050, 'Bruxelles', 'Single', 'Female', 'MA', 'megane.amosse@hotmail.fr', 'MA2875', 'HK', 5, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(27, 'loic', 'keming', '2019-08-07', NULL, '94112856960', 'frans cloetens', '9', '1', 1950, 'bruxelles', 'Single', 'Male', '1230', 'keming.loic@yahoo.com', '1230', 'DR', 1, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(29, 'Nancy', 'Janssens', '2019-09-23', NULL, '80022219860', 'Rue Sainte-Thérèse', '35', '', 1000, 'Bruxelles', 'Married', 'Female', 'JN', 'janssensnancy@icloud.com', 'Nancy9860', 'HK', 1, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(30, 'Laurita', 'Gratii', '2019-08-01', NULL, '94010565017', 'Rue Henri Evenpoel', '29', '5', 1030, 'Bruxelles', 'Married', 'Female', 'LG', 'gratii.ion@outlook.fr', 'LG5017', 'BO', 1, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(31, 'Loïc', 'Keming', '2019-08-19', '2019-12-05', '94112856960', 'Porte d\'Anderlecht', '172', '1', 1000, 'Bruxelles', 'Single', 'Male', 'LK', 'keming.loic@yahoo.com', 'LK6960', 'BO', 5, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(32, 'Cristina', 'Riveiro McClelland', '2019-10-19', NULL, '9409091234', 'Rue Louis Hap', '167', '', 1040, 'Bruxelles', 'Single', 'Female', 'CR', 'cristinariveiromc@gmail.com', 'Cristina1234', 'FO', 1, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban'),
(33, 'Daniel', 'Precz', '2019-10-01', NULL, '123456789', 'Rue de Rollebacq', '41', '', 1000, 'Bruxelles', 'Single', 'Male', 'PZ', 'danielpg92@gmail.com', 'PZ1234', 'FO', 1, '111-11-111-1', 0, 0, 'user', 1, 'user', 'user', NULL, 'iban');

-- --------------------------------------------------------

--
-- Structure de la table `planning`
--

CREATE TABLE `planning` (
  `id_planning` int(11) NOT NULL,
  `id_user` int(11) NOT NULL DEFAULT 1,
  `week` varchar(20) NOT NULL DEFAULT 'week',
  `date` varchar(20) NOT NULL DEFAULT 'date',
  `startTime` varchar(20) NOT NULL DEFAULT 'str',
  `endTime` varchar(20) NOT NULL DEFAULT 'end',
  `status` varchar(20) NOT NULL DEFAULT 'waiting',
  `callRedirect` varchar(25) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `caisse`
--
ALTER TABLE `caisse`
  ADD PRIMARY KEY (`idCaisse`);

--
-- Index pour la table `caisse_monnaie`
--
ALTER TABLE `caisse_monnaie`
  ADD PRIMARY KEY (`id_caisse_monnaie`);

--
-- Index pour la table `caisse_recettes`
--
ALTER TABLE `caisse_recettes`
  ADD PRIMARY KEY (`id_caisse_recettes`);

--
-- Index pour la table `employees`
--
ALTER TABLE `employees`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `planning`
--
ALTER TABLE `planning`
  ADD UNIQUE KEY `id_planning_UNIQUE` (`id_planning`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `caisse`
--
ALTER TABLE `caisse`
  MODIFY `idCaisse` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `caisse_monnaie`
--
ALTER TABLE `caisse_monnaie`
  MODIFY `id_caisse_monnaie` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `caisse_recettes`
--
ALTER TABLE `caisse_recettes`
  MODIFY `id_caisse_recettes` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `employees`
--
ALTER TABLE `employees`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- AUTO_INCREMENT pour la table `planning`
--
ALTER TABLE `planning`
  MODIFY `id_planning` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

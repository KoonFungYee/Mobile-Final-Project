-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jun 09, 2019 at 07:48 AM
-- Server version: 10.1.38-MariaDB
-- PHP Version: 7.2.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `socstude_findfood`
--

-- --------------------------------------------------------

--
-- Table structure for table `comment`
--

CREATE TABLE `comment` (
  `id` int(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `rating` varchar(255) NOT NULL,
  `comment` varchar(255) NOT NULL,
  `date` varchar(255) NOT NULL,
  `foodname` varchar(255) NOT NULL,
  `restname` varchar(255) NOT NULL,
  `state` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `comment`
--

INSERT INTO `comment` (`id`, `name`, `rating`, `comment`, `date`, `foodname`, `restname`, `state`) VALUES
(1, 'testing', '3.00', 'good!', '1-6-2019', 'k', 'k', 'Johor'),
(6, 'hi', '2.00', 'ok loh', '27/5/2019', 'k', 'k', 'Johor'),
(9, 'Koon Fung Yee', '3.00', 'Hi, nice to meet you', '2019/05/27 18:26:52', 'k', 'k', 'Johor'),
(10, 'Koon Fung Yee', '4.00', 'Hi, lol', '2019/05/27 20:54:30', 'k', 'k', 'Johor'),
(11, 'Koon Fung Yee', '2.00', 'still okay', '2019/05/27 21:47:23', 'k', 'k', 'Johor'),
(12, 'Koon Fung Yee', '5.00', 'ok', '2019/05/27 22:02:45', 'k', 'k', 'Johor'),
(13, 'Koon Fung Yee', '5.00', 'goodðŸ˜€', '2019/05/27 22:04:23', 'k', 'k', 'Johor'),
(14, 'Koon Fung Yee', '5.00', 'sv', '2019/05/27 22:05:59', 'k', 'k', 'Johor'),
(15, 'Koon Fung Yee', '5.00', 'gh', '2019/05/27 22:08:59', 'k', 'k', 'Johor'),
(16, 'Koon Fung Yee', '3.00', 'jk', '2019/05/27 22:12:17', 'k', 'k', 'Johor'),
(17, 'Koon Fung Yee', '4.00', 'll', '2019/05/27 22:15:22', 'k', 'k', 'Johor'),
(18, 'Koon Fung Yee', '2.00', 'oo', '2019/05/27 22:16:42', 'k', 'k', 'Johor'),
(19, 'Koon Fung Yee', '4.00', 'hju', '2019/05/27 22:22:54', 'k', 'k', 'Johor'),
(20, 'Koon Fung Yee', '5', 'good', '2019/05/29', 'k', 'k', 'Johor'),
(21, 'AAA', '3.00', 'okay', '2019/06/01 01:31:44', 'k', 'k', 'Johor'),
(23, 'Kelvin Koon', '3.00', 'Good!', '2019/06/01 02:48:56', 'Buffet', 'Pasar Baru', 'Kuala Lumpur'),
(24, 'Kelvin Koon', '4.00', 'good', '2019/06/01 03:32:21', 'Caesar Salad with Tamago', 'Fish & Co', 'Selangor'),
(25, 'Kelvin Koon', '5.00', 'ok', '2019/06/01 04:09:36', 'Caesar Salad with Tamago', 'Fish & Co', 'Selangor'),
(26, 'Kelvin Koon', '5.00', 'good', '2019/06/01 04:10:12', 'Fried Calamari', 'Fish & Co', 'Selangor'),
(27, 'Kelvin Koon', '3.00', 'still okay', '2019/06/01 04:21:22', 'Caesar Salad with Grilled Chicken', 'Fish & Co', 'Selangor'),
(28, 'Koon Fung Yee', '5.00', 'Good', '2019/06/07 17:27:44', 'Caesar Salad with Tamago', 'Fish & Co', 'Selangor'),
(29, 'Koon Fung Yee', '3.00', 'okay', '2019/06/07 17:45:52', 'Nasi Lemak', 'long', 'Selangor');

-- --------------------------------------------------------

--
-- Table structure for table `food`
--

CREATE TABLE `food` (
  `id` int(255) NOT NULL,
  `foodname` varchar(255) NOT NULL,
  `price` varchar(255) NOT NULL,
  `period` varchar(255) NOT NULL,
  `remarks` varchar(255) NOT NULL,
  `rating` varchar(255) NOT NULL,
  `restname` varchar(255) NOT NULL,
  `state` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `food`
--

INSERT INTO `food` (`id`, `foodname`, `price`, `period`, `remarks`, `rating`, `restname`, `state`, `status`) VALUES
(3, 'k', '8.90', 'Breakfast', '', '3.67', 'k', 'Johor', 'available'),
(4, 'fg', '2.60', 'Dinner', '', '5.00', 'k', 'Johor', 'available'),
(8, 'kk', '2.30', 'Breakfast', '', '0.00', 'bbq', 'Johor', 'available'),
(9, 'Buffet', '45.00', 'Dinner', 'From 5.30pm - 12.30pm', '3.00', 'Pasar Baru', 'Kuala Lumpur', 'available'),
(10, 'Fried Calamari', '17.90', 'All Day', '', '5.00', 'Fish & Co', 'Selangor', 'available'),
(11, 'Caesar Salad with Tamago', '13.15', 'All Day', 'Romaine with Caeser dressing and croutons served with bright cheery tamogo.', '4.67', 'Fish & Co', 'Selangor', 'available'),
(12, 'Caesar Salad with Grilled Chicken', '16.00', 'All Day', '', '3.00', 'Fish & Co', 'Selangor', 'available'),
(15, 'Nasi Lemak', '2.00', 'Breakfast', '', '3.00', 'long', 'Selangor', 'available');

-- --------------------------------------------------------

--
-- Table structure for table `restaurant`
--

CREATE TABLE `restaurant` (
  `id` int(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `building` varchar(255) NOT NULL,
  `state` varchar(255) NOT NULL,
  `starttime` varchar(255) NOT NULL,
  `endtime` varchar(255) NOT NULL,
  `telnumber` varchar(255) NOT NULL,
  `fbpage` varchar(255) NOT NULL,
  `category` varchar(255) NOT NULL,
  `longtitude` varchar(255) NOT NULL,
  `latitude` varchar(255) NOT NULL,
  `rating` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `restaurant`
--

INSERT INTO `restaurant` (`id`, `name`, `building`, `state`, `starttime`, `endtime`, `telnumber`, `fbpage`, `category`, `longtitude`, `latitude`, `rating`, `status`) VALUES
(5, 'k', 'kl', 'Johor', '2pm', '2am', '0109560095', 'kelvin.koon0121', 'bbq', '103.664222', '1.572760', '4.00', 'available'),
(10, 'bbq', 'k', 'Johor', 'k', 'k', 'k', 'k', 'bbq', '103.891016', '1.485892', '0.00', 'available'),
(11, 'Pasar Baru', 'New World Petaling', 'Kuala Lumpur', '10.00am', '10.00pm', '03-76820000', 'NewWorldPetalingJaya', 'trending', '100.528', '8.4396', '3.00', 'available'),
(12, 'Fish & Co', 'The Mines', 'Selangor', '10.00am', '10.00pm', '0389417002', 'fishncomy', 'trending', '101.71770833333333', '3.02985', '4.22', 'available'),
(13, 'long', 'One City2', 'Selangor', '10.00am', '10.00pm', '0389561645', 'kelvin.koon0121', 'bbq', '101.72318166666668', '3.03026', '3.00', 'available');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `gender` varchar(255) NOT NULL,
  `location` varchar(255) NOT NULL,
  `longtitude` varchar(255) NOT NULL,
  `latitude` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `email`, `password`, `name`, `phone`, `gender`, `location`, `longtitude`, `latitude`) VALUES
(1, 'kelvin2274@hotmail.com', '123456', 'Kelvin Koon', '12', 'Male', 'Johor', '101.72299833333334', '3.0288099999999996'),
(2, 'koon950121@gmail.com', '123456', 'Koon Fung Yee', '0109560095', 'Male', 'Johor', '101.72318166666668', '3.03026'),
(25, 'koon19950121@gmail.com', '123', 'Koon Fung Yee', '0123456789', 'Male', 'Johor', '101.72318166666668', '3.03026');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `comment`
--
ALTER TABLE `comment`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `food`
--
ALTER TABLE `food`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `restaurant`
--
ALTER TABLE `restaurant`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `comment`
--
ALTER TABLE `comment`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT for table `food`
--
ALTER TABLE `food`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `restaurant`
--
ALTER TABLE `restaurant`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

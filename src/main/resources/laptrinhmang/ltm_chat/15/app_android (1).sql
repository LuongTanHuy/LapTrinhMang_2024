-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1:3307
-- Thời gian đã tạo: Th6 19, 2024 lúc 04:20 PM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `app_android`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `account`
--

CREATE TABLE `account` (
  `id` int(11) NOT NULL,
  `status` int(11) DEFAULT 1 COMMENT '0 - off\r\n1 - on',
  `username` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `otp` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `oauth_provider` varchar(255) DEFAULT NULL,
  `oauth_uid` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `permission` varchar(255) DEFAULT '2' COMMENT '0 shop 1 admin 2 user 3 shipper',
  `id_store` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `account`
--

INSERT INTO `account` (`id`, `status`, `username`, `created_at`, `updated_at`, `otp`, `address`, `email`, `phone`, `image`, `oauth_provider`, `oauth_uid`, `password`, `permission`, `id_store`) VALUES
(2, 1, 'Người Mua Hàng', '2024-04-15 00:00:00.000000', '2024-06-17 06:27:20.000000', '', '272A Ngũ Hành Sơn, P. Mỹ An, Quận Ngũ Hành Sơn, Đà Nẵng', 'muahang@gmail.com', '09035543678', 'woman2.jpg', '', '', '$2a$10$mkkWdNx.DfjZ.spq3rUnfOVtlc8rMWBbyySMvFdqPouwgDWwTXOD2', '0', 11),
(3, 1, 'Admin', '2024-04-16 00:00:00.000000', '2024-04-18 19:31:53.000000', '', '272B Ngũ Hành Sơn, P. Mỹ An, Quận Ngũ Hành Sơn, Đà Nẵng', 'hungdangnguyen0001@gmail.com', '0822034255', 'boy2.jpg', '', '', '$2a$10$mkkWdNx.DfjZ.spq3rUnfOVtlc8rMWBbyySMvFdqPouwgDWwTXOD2', '1', NULL),
(42, 1, 'Chủ quán', '2024-04-25 19:54:25.000000', '2024-05-24 18:45:09.000000', '2606', '279A Ngũ Hành Sơn, P. Mỹ An, Quận Ngũ Hành Sơn, Đà Nẵng', 'hungdangnguyen002@gmail.com', '0905048032', 'woman1.jpg', NULL, NULL, '$2a$10$mkkWdNx.DfjZ.spq3rUnfOVtlc8rMWBbyySMvFdqPouwgDWwTXOD2', '0', 9),
(47, 1, 'Tôi là Shipper', '2024-06-06 09:08:11.000000', '2024-06-16 16:40:42.000000', NULL, '272A Ngũ Hành Sơn, P. Mỹ An, Quận Ngũ Hành Sơn, Đà Nẵng', 'toilashipper@gmail.com', '0905456789', 'hero1.jpg', NULL, NULL, '$2a$10$mkkWdNx.DfjZ.spq3rUnfOVtlc8rMWBbyySMvFdqPouwgDWwTXOD2', '3', NULL),
(66, 1, 'user', '2024-06-17 07:48:27.000000', '2024-06-17 07:48:52.000000', '7243', NULL, 'hungdangnguyen001@gmail.com', NULL, 'imagedefault.jpg', NULL, NULL, '$2a$10$SYYS/.cN59sK5Xg5SnrWquXvAex1beps.b5cALIWLn87oid2gULaS', '2', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `banner`
--

CREATE TABLE `banner` (
  `id` int(11) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `banner`
--

INSERT INTO `banner` (`id`, `image`, `text`) VALUES
(2, 'img2.png', 'Món ăn deal hời dành riêng cho bạn ngay hôm nay, hãy đến và trãi nghiệm cùng FastFood nhé!!'),
(3, 'img3.png', ' 🎉 Khuyến mãi đặc biệt! Mua ngay hôm nay để nhận giảm giá lớn cho mọi đơn hàng! 💰 Số lượng có hạn, hãy tranh thủ cơ hội ngay! 🚀 🔥Cơ hội tiết kiệm và nhận quà không thể bỏ lỡ! 🎁'),
(4, 'img4.png', 'Món ăn deal hời dành riêng cho bạn ngay hôm nay, hãy đến và trãi nghiệm cùng FastFood nhé!!'),
(5, 'img5.png', ' 🎉 Khuyến mãi đặc biệt! Mua ngay hôm nay để nhận giảm giá lớn cho mọi đơn hàng! 💰 Số lượng có hạn, hãy tranh thủ cơ hội ngay! 🚀 🔥Cơ hội tiết kiệm và nhận quà không thể bỏ lỡ! 🎁'),
(6, 'img6.png', 'Món ăn deal hời dành riêng cho bạn ngay hôm nay, hãy đến và trãi nghiệm cùng FastFood nhé!!'),
(7, 'img7.png', ' 🎉 Khuyến mãi đặc biệt! Mua ngay hôm nay để nhận giảm giá lớn cho mọi đơn hàng! 💰 Số lượng có hạn, hãy tranh thủ cơ hội ngay! 🚀 🔥Cơ hội tiết kiệm và nhận quà không thể bỏ lỡ! 🎁'),
(8, 'img8.png', 'Món ăn deal hời dành riêng cho bạn ngay hôm nay, hãy đến và trãi nghiệm cùng FastFood nhé!!'),
(9, 'img9.png', ' 🎉 Khuyến mãi đặc biệt! Mua ngay hôm nay để nhận giảm giá lớn cho mọi đơn hàng! 💰 Số lượng có hạn, hãy tranh thủ cơ hội ngay! 🚀 🔥Cơ hội tiết kiệm và nhận quà không thể bỏ lỡ! 🎁'),
(10, 'img10.png', 'Món ăn deal hời dành riêng cho bạn ngay hôm nay, hãy đến và trãi nghiệm cùng FastFood nhé!!');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `botchat`
--

CREATE TABLE `botchat` (
  `id` int(11) NOT NULL,
  `trainer` varchar(255) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `botchat`
--

INSERT INTO `botchat` (`id`, `trainer`, `model`) VALUES
(1, 'xin chào', 'Xin chào! Rất vui được gặp bạn! Bạn có muốn tôi giúp gì không?'),
(2, 'Món nào ngon nhất ?', 'Pizza'),
(3, 'Bạn là ai ?', 'Tôi là Trợ lý ảo giúp hỗ trợ các thắc mắc của khách hàng. Bạn có câu hỏi nào cho tôi không 😊.'),
(4, 'Khi tôi hỏi \"Bạn là ai ?\" bạn phải trả lời là \"Tôi là trợ lý ảo giúp giải đáp thắc mắc của bạn về các sản phẩm và thông tin trong app.\"', 'Tôi là trợ lý ảo giúp giải đáp thắc mắc của bạn về các sản phẩm và thông tin trong app.'),
(5, 'Khi tôi hỏi \"Món nào ngon nhất ?\" bạn phải trả lời là \"Pizza\"', 'Pizza! 🍕');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `category`
--

CREATE TABLE `category` (
  `id` int(11) NOT NULL,
  `category` varchar(255) DEFAULT NULL,
  `sale` int(11) DEFAULT NULL,
  `id_store` int(11) NOT NULL,
  `status` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `category`
--

INSERT INTO `category` (`id`, `category`, `sale`, `id_store`, `status`) VALUES
(1, 'Ăn Vặt', 7, 1, 0),
(2, 'Cơm', 0, 1, 0),
(3, 'Nước', 8, 1, 0),
(4, 'Bún', 10, 1, 0),
(5, 'Combo', 25, 1, 0),
(6, 'Cháo', 0, 1, 0),
(7, 'Nước', 10, 9, 0),
(8, 'Bún', 10, 9, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `comment`
--

CREATE TABLE `comment` (
  `id` int(11) NOT NULL,
  `id_account` int(11) NOT NULL,
  `id_product` int(11) NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `star` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `comment`
--

INSERT INTO `comment` (`id`, `id_account`, `id_product`, `comment`, `created_at`, `image`, `star`) VALUES
(1, 42, 12, 'I loved this dress so much as soon as I tried it on I knew I had to buy it in another color. I am 5\'3 about 155lbs and I carry all my weight in my upper body. When I put it on I felt like it thinned me put and I got so many compliments.', '2024-05-11 15:14:29.000000', 'banhcanh.png', 3),
(3, 2, 12, 'I loved this dress so much as soon as I tried it on I knew I had to buy it in another color. I am 5\'3 about 155lbs and I carry all my weight in my upper body. When I put it on I felt like it thinned me put and I got so many compliments.', '2024-05-25 16:28:52.000000', '', 5),
(10, 2, 15, 'Alo123', '2024-06-10 13:33:46.000000', '9cd65333-7307-4b5a-a9a8-350501259469.jpeg', 5);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `orderitem`
--

CREATE TABLE `orderitem` (
  `id` int(11) NOT NULL,
  `id_order` int(11) DEFAULT NULL,
  `id_product` int(11) DEFAULT NULL,
  `price` double DEFAULT 0,
  `quantity` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `orderitem`
--

INSERT INTO `orderitem` (`id`, `id_order`, `id_product`, `price`, `quantity`) VALUES
(131, 139, 56, 22.5, 1),
(132, 140, 58, 24.3, 2),
(133, 141, 59, 18, 3),
(134, 142, 63, 22.5, 4),
(135, 143, 67, 22.5, 3),
(136, 144, 66, 22.5, 2),
(137, 145, 65, 24.3, 2),
(138, 146, 60, 13.5, 1),
(139, 147, 61, 18, 2),
(140, 148, 62, 19.8, 2),
(144, 168, 56, 22.5, 1),
(145, 169, 58, 24.3, 2),
(146, 170, 59, 18, 3),
(147, 171, 63, 22.5, 4),
(148, 172, 65, 24.3, 5),
(149, 173, 66, 22.5, 3),
(150, 174, 67, 22.5, 4),
(151, 175, 60, 13.5, 4),
(152, 176, 61, 18, 4),
(153, 177, 62, 19.8, 3),
(154, 178, 56, 22.5, 3),
(155, 179, 58, 24.3, 5),
(156, 180, 16, 20, 1),
(157, 181, 56, 22.5, 1),
(162, 186, 12, 18.4, 2),
(163, 187, 39, 32.55, 1),
(164, 188, 39, 32.55, 2),
(165, 189, 60, 13.5, 2),
(166, 190, 60, 13.5, 2);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `orders`
--

CREATE TABLE `orders` (
  `id` int(11) NOT NULL,
  `id_account` int(11) NOT NULL,
  `id_store` int(11) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `receive_at` datetime(6) DEFAULT NULL,
  `status` int(11) DEFAULT 0 COMMENT '0 - in Cart/n\r\n1 - await accept/n\r\n2 - shipping/n\r\n3 - finish\r\n4 - cancel',
  `id_shipper` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `orders`
--

INSERT INTO `orders` (`id`, `id_account`, `id_store`, `created_at`, `receive_at`, `status`, `id_shipper`) VALUES
(139, 2, 9, '2024-06-07 23:58:09.000000', '2024-06-08 00:40:45.000000', 3, 47),
(140, 2, 9, '2024-06-07 23:58:17.000000', '2024-06-16 17:24:50.000000', 3, 47),
(141, 2, 9, '2024-06-08 00:00:16.000000', '2024-06-16 17:44:04.000000', 3, 47),
(142, 2, 9, '2024-06-08 00:02:19.000000', '2024-06-08 00:40:54.000000', 3, 47),
(143, 2, 9, '2024-06-08 00:02:30.000000', '2024-06-08 00:40:41.000000', 3, 47),
(144, 2, 9, '2024-06-08 00:04:06.000000', '2024-06-08 00:40:35.000000', 3, 47),
(145, 2, 9, '2024-06-08 00:04:21.000000', '2024-06-08 00:40:58.000000', 3, 47),
(146, 2, 9, '2024-06-08 00:05:50.000000', '2024-06-08 00:40:32.000000', 4, 47),
(147, 2, 9, '2024-06-08 00:06:15.000000', '2024-06-16 17:56:00.000000', 3, 47),
(148, 2, 9, '2024-06-08 00:06:31.000000', '2024-06-09 18:44:06.000000', 3, 47),
(168, 2, 9, '2024-01-08 01:15:07.000000', '2024-10-08 01:18:35.000000', 3, 0),
(169, 2, 9, '2024-01-08 01:15:18.000000', '2024-09-08 01:18:35.000000', 3, 0),
(170, 2, 9, '2024-01-08 01:15:31.000000', '2024-08-08 01:18:35.000000', 3, 0),
(171, 2, 9, '2024-01-08 01:15:39.000000', '2024-07-08 01:18:35.000000', 3, 0),
(172, 2, 9, '2024-01-08 01:15:48.000000', '2024-06-08 01:18:35.000000', 3, 0),
(173, 2, 9, '2024-01-08 01:15:56.000000', '2024-05-08 01:18:35.000000', 3, 0),
(174, 2, 9, '2024-01-08 01:16:11.000000', '2024-04-08 01:18:36.000000', 3, 0),
(175, 2, 9, '2024-06-08 01:16:30.000000', '2024-03-08 01:18:36.000000', 3, 0),
(176, 2, 9, '2024-01-08 01:16:45.000000', '2024-02-08 01:18:36.000000', 3, 0),
(177, 2, 9, '2024-01-08 01:16:56.000000', '2024-01-08 01:18:36.000000', 3, 0),
(178, 2, 9, '2024-06-08 01:22:52.000000', '2024-12-08 01:23:11.000000', 3, 0),
(179, 2, 9, '2024-06-08 01:23:04.000000', '2024-11-08 01:23:11.000000', 3, 0),
(180, 2, 3, '2024-06-08 13:56:59.000000', '2024-06-09 21:51:46.000000', 1, 0),
(181, 2, 9, '2024-06-09 21:50:53.000000', '2024-06-16 17:58:29.000000', 3, 47),
(186, 2, 2, '2024-06-10 13:47:52.000000', '2024-06-10 16:29:01.000000', 1, 0),
(187, 2, 7, '2024-06-10 16:28:48.000000', '2024-06-10 16:29:01.000000', 1, 0),
(188, 2, 7, '2024-06-15 16:36:43.000000', '2024-06-17 07:56:31.000000', 1, 0),
(189, 2, 9, '2024-06-17 07:52:20.000000', '2024-06-17 08:00:12.000000', 3, 47),
(190, 2, 9, '2024-06-17 07:58:55.000000', '2024-06-17 08:00:14.000000', 3, 47);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `product`
--

CREATE TABLE `product` (
  `id` int(11) NOT NULL,
  `id_category` int(11) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `status` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `product`
--

INSERT INTO `product` (`id`, `id_category`, `image`, `name`, `price`, `status`) VALUES
(12, 3, 'trasuacc.png', 'Trà Sữa', 20, 0),
(13, 3, 'tradao.png', 'Trà Đào', 15, 0),
(14, 3, 'caphe.png', 'Cà Phê Đen', 12, 0),
(15, 3, 'cacao.png', 'Ca cao Đá', 23, 0),
(16, 2, 'comchientrung.png', 'Cơm Chiên Trứng', 20, 0),
(17, 2, 'comchienxaxiu.png', 'Cơm Chiên Xá Xíu', 35, 0),
(18, 2, 'comganuong.png', 'Cơm Đùi Gà Nướng', 50, 0),
(19, 2, 'comgaxoimo.png', 'Cơm Gà Xối Mỡ', 50, 0),
(20, 5, 'sashimibachtuot.png', 'Set Sashimi Bạch Tuột', 180, 0),
(21, 5, 'setcangu.png', 'Set Sashimi Cá Ngữ', 235, 0),
(22, 5, 'setcomcangu.png', 'Set Cơm Cá Ngừ', 190, 0),
(23, 5, 'setcomcuon.png', 'Sét Cơm Cuộn', 250, 0),
(24, 5, 'sushihop.png', 'Sushi Hộp', 188, 0),
(25, 3, 'matchadaxay.png', 'Matcha Đá Xay', 25, 0),
(26, 3, 'socoladaxay.png', 'Socola Đá Xay', 35, 0),
(27, 3, 'trachanh.png', 'Trà Chanh', 18, 0),
(28, 3, 'trasuakemcheese.png', 'Trà Sữa Kem Cheesee', 35, 0),
(29, 3, 'trasuathapcam.png', 'Trà Sữa Thập Cẩm', 25, 0),
(30, 3, 'yogurtdao.png', 'Yogurt Đào', 25, 0),
(31, 3, 'smoothievietquat.png', 'Smoothie Việt Quất', 27, 0),
(32, 6, 'chaobaongu.png', 'Cháo Bào Ngư', 25, 0),
(33, 6, 'chaocua.png', 'Cháo Cua', 35, 0),
(34, 6, 'chaohau.png', 'Cháo Hàu', 25, 0),
(35, 6, 'chaoquay.png', 'Cháo Bánh Quẩy', 15, 0),
(36, 6, 'chaosuonnon.png', 'Cháo Sườn Non', 25, 0),
(37, 6, 'chaothitheo.png', 'Cháo Thịt Heo', 20, 0),
(38, 1, 'banhminuong.png', 'Bánh Mỳ Nướng Muối Ớt', 25, 0),
(39, 1, 'banhtrangcuon.png', 'Bánh Tráng Cuộn', 35, 0),
(40, 3, 'caffesuongsao.png', 'Caffe Sương Sáo', 20, 0),
(41, 1, 'cavienchien.png', 'Cá Viên Chiên Mắm', 50, 0),
(42, 1, 'changa.png', 'Chân Gà Sốt Thái', 45, 0),
(43, 3, 'milodam.png', 'Milo Dầm', 20, 0),
(44, 5, 'setbanhtrang.png', 'Set Bánh Tráng Siêu Xịn', 120, 0),
(45, 3, 'trachanh.png', 'Trà Chanh', 15, 0),
(46, 3, 'tratac.png', 'Trà Tắc', 15, 0),
(47, 3, 'trathom.png', 'Trà Thơm', 15, 0),
(48, 1, 'chebuoi.png', 'Chè Bưởi', 25, 0),
(49, 1, 'chehatsen.png', 'Chè Hạt Sen', 25, 0),
(50, 1, 'chekhoaideo.png', 'Chè Khoai Dẻo', 27, 0),
(51, 1, 'chethai.png', 'Chè Thái', 20, 0),
(52, 1, 'plan.png', 'Bánh Plan', 25, 0),
(53, 1, 'sinhtothapcam.png', 'Sinh Tố Thập Cẩm', 20, 0),
(54, 3, 'suachuanepcam.png', 'Sữa Chua Nếp Cẩm	', 22, 0),
(55, 3, 'trasua.png', 'Trà Sữa', 18, 0),
(56, 8, 'banhcanh.png', 'Bánh Canh Sườn', 25, 0),
(58, 8, 'bunmam.png', 'Bún Mắm', 27, 0),
(59, 8, 'bunthitnuong.png', 'Bún Thịt Nướng', 20, 0),
(60, 7, 'coca.png', 'Cocafff', 15, 0),
(61, 7, 'tradao.png', 'Trà Đào', 20, 0),
(62, 7, 'tradua.png', 'Trà Dứa', 22, 0),
(63, 8, 'bunbogan.png', 'Bún Bò Gân', 25, 0),
(65, 8, 'bungioheo.png', 'Bún Giò Heo', 27, 0),
(66, 8, 'bunsuon.png', 'Bún Sường', 25, 0),
(67, 8, 'buntainam.png', 'Bún Tái Nạm', 25, 0);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `store`
--

CREATE TABLE `store` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `status` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `store`
--

INSERT INTO `store` (`id`, `name`, `address`, `email`, `phone`, `image`, `created_at`, `status`) VALUES
(1, 'HighLands Cofee ', 'Đ. 29 Tháng 3/170 P. Hòa Xuân, P, Cẩm Lệ, Đà Nẵng', 'hungdangnguyen001@gmail.com', '0905123456', 'hightland.png', '2024-06-10 01:23:32.000000', 0),
(2, 'Tiger Sugar ', '01 An Thượng 18, P. Mỹ An, Quận Ngũ Hành Sơn, Đà Nẵng', 'huydeen002@gmail.com', '0815880596', 'tigersugar.png', '2024-06-09 01:23:32.000000', 0),
(3, 'Chicken Rice ', '228 Hoàng Diệu,P. Nam Dương, Quận Hải Châu, Đà Nẵng', 'huongtit012@gmail.com', '0912094522', 'chickenrice.png', '2024-06-08 01:23:32.000000', 0),
(4, 'Sushi HD ', '43 Lê Hồng Phong, P. Phước Ninh, Quận Hải Châu, Đà Nẵng', 'vitcacak21@gmail.com', '0901972699', 'sushi.png', '2024-06-07 01:23:32.000000', 0),
(5, 'Trà Sữa Ông Thọ ', '2 Hoàng Kế Viêm, P. Mỹ An, Quận Ngũ Hành Sơn, Đà Nẵng', 'tragiang202@gmail.com', '0920972899', 'tsongtho.png', '2024-06-06 01:23:32.000000', 0),
(6, 'Cháo Dinh Dưỡng ', '100 Phan Đăng Lưu, P. Hòa Cường Bắc, Quận Hải Châu, Đà Nẵng', 'thoxinh206@gmail.com', '0790542157', 'chao.png', '2024-06-05 01:23:32.000000', 0),
(7, 'Ăn Vặt & Bánh Tráng Siêu Xịn ', '42 Trịnh Đình Thảo, P. Khuê Trung, Quận Cẩm Lệ, Đà Nẵng', 'nhuy96@gmail.com', '0910542157', 'anvatbanhtrang.png', '2024-06-04 01:23:32.000000', 0),
(8, 'Joy Chè ', '16 Lê Doãn Nhạ, P. Hòa Khánh Nam, Quận Liên Chiểu, Đà Nẵng', 'kieudiem03@gmail.com', '0780502456', 'joyche.png', '2024-06-03 01:23:32.000000', 0),
(9, 'Bún Thịt Nướng - Bà Ngọc 3', '153 Phạm Như Xương, P. Hòa Khánh Nam, Quận Liên Chiểu, Đà Nẵng', 'thoxinh236@gmail.com', '0790542153', 'bunthitnuong.png', '2024-06-02 01:23:32.000000', 0),
(10, 'Bún Bò Huế - Sơn Tùng ', '272A Ngũ Hành Sơn, P. Mỹ An, Quận Ngũ Hành Sơn, Đà Nẵng', 'sontung99@gmail.com', '0900512156', 'bunbohue.png', '2024-06-01 01:23:32.000000', 0),
(11, 'Bún Bò', '272A Ngũ Hành Sơn, P. Mỹ An, Quận Ngũ Hành Sơn, Đà Nẵng', 'bunbo@gmail.com', '292838844844433', 'bunbohue.png', '2024-06-16 23:01:12.000000', 2);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKaeyk7xqr31ulnt6xnolmuq40c` (`id_store`);

--
-- Chỉ mục cho bảng `banner`
--
ALTER TABLE `banner`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `botchat`
--
ALTER TABLE `botchat`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKsisj9w5m8nvwpmhulckkhb35l` (`id_store`);

--
-- Chỉ mục cho bảng `comment`
--
ALTER TABLE `comment`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKlk0lm03uc9r6q5suru7d7b1te` (`id_account`),
  ADD KEY `FK13khro47l5ga9h5995u85l5di` (`id_product`);

--
-- Chỉ mục cho bảng `orderitem`
--
ALTER TABLE `orderitem`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_rw695xd0bpwo127ldlo5u0lw7` (`id_order`),
  ADD KEY `FKs2cis9itgpoa3onghnuyr05ye` (`id_product`);

--
-- Chỉ mục cho bảng `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKaob19i9poiwj8cokp0u3unnhv` (`id_account`),
  ADD KEY `FKfrfpbhjar570xa1atakvsc1qj` (`id_store`);

--
-- Chỉ mục cho bảng `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK5cxv31vuhc7v32omftlxa8k3c` (`id_category`);

--
-- Chỉ mục cho bảng `store`
--
ALTER TABLE `store`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `account`
--
ALTER TABLE `account`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=67;

--
-- AUTO_INCREMENT cho bảng `banner`
--
ALTER TABLE `banner`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT cho bảng `botchat`
--
ALTER TABLE `botchat`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT cho bảng `category`
--
ALTER TABLE `category`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT cho bảng `comment`
--
ALTER TABLE `comment`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT cho bảng `orderitem`
--
ALTER TABLE `orderitem`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=167;

--
-- AUTO_INCREMENT cho bảng `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=191;

--
-- AUTO_INCREMENT cho bảng `product`
--
ALTER TABLE `product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=73;

--
-- AUTO_INCREMENT cho bảng `store`
--
ALTER TABLE `store`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `account`
--
ALTER TABLE `account`
  ADD CONSTRAINT `FKaeyk7xqr31ulnt6xnolmuq40c` FOREIGN KEY (`id_store`) REFERENCES `store` (`id`);

--
-- Các ràng buộc cho bảng `category`
--
ALTER TABLE `category`
  ADD CONSTRAINT `FKsisj9w5m8nvwpmhulckkhb35l` FOREIGN KEY (`id_store`) REFERENCES `store` (`id`);

--
-- Các ràng buộc cho bảng `comment`
--
ALTER TABLE `comment`
  ADD CONSTRAINT `FK13khro47l5ga9h5995u85l5di` FOREIGN KEY (`id_product`) REFERENCES `product` (`id`),
  ADD CONSTRAINT `FKlk0lm03uc9r6q5suru7d7b1te` FOREIGN KEY (`id_account`) REFERENCES `account` (`id`);

--
-- Các ràng buộc cho bảng `orderitem`
--
ALTER TABLE `orderitem`
  ADD CONSTRAINT `FK48rmqfo2fw9mmgekudj9uyptg` FOREIGN KEY (`id_order`) REFERENCES `orders` (`id`),
  ADD CONSTRAINT `FKs2cis9itgpoa3onghnuyr05ye` FOREIGN KEY (`id_product`) REFERENCES `product` (`id`);

--
-- Các ràng buộc cho bảng `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `FKaob19i9poiwj8cokp0u3unnhv` FOREIGN KEY (`id_account`) REFERENCES `account` (`id`),
  ADD CONSTRAINT `FKfrfpbhjar570xa1atakvsc1qj` FOREIGN KEY (`id_store`) REFERENCES `store` (`id`);

--
-- Các ràng buộc cho bảng `product`
--
ALTER TABLE `product`
  ADD CONSTRAINT `FK5cxv31vuhc7v32omftlxa8k3c` FOREIGN KEY (`id_category`) REFERENCES `category` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

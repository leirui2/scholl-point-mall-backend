/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50744
 Source Host           : localhost:3306
 Source Schema         : scholl point mall

 Target Server Type    : MySQL
 Target Server Version : 50744
 File Encoding         : 65001

 Date: 10/12/2025 20:30:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类别名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '类别描述',
  `isDelete` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除标志：0-未删除，1-已删除',
  `createTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_isDeleted`(`isDelete`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1994378398924345352 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品类别表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1994378398924315350, '个护美妆', '护肤品、化妆品等个人护理用品', 1, '2025-11-28 20:00:00', '2025-12-07 18:15:02');
INSERT INTO `category` VALUES (1994378398924345345, '毛绒玩具', '各种可爱的毛绒玩具，适合儿童玩耍和收藏', 0, '2025-11-28 20:00:00', '2025-11-28 20:00:00');
INSERT INTO `category` VALUES (1994378398924345346, '文具用品', '各种学习和办公文具，包括笔、本子、尺子等', 0, '2025-11-28 20:00:00', '2025-11-28 20:00:00');
INSERT INTO `category` VALUES (1994378398924345347, '运动器材', '适合学生的各种运动器材，如球类、跳绳等', 0, '2025-11-28 20:00:00', '2025-11-28 20:00:00');
INSERT INTO `category` VALUES (1994378398924345348, '电子产品', '学生适用的电子设备配件，如计算器、耳机等', 0, '2025-11-28 20:00:00', '2025-11-28 20:00:00');
INSERT INTO `category` VALUES (1994378398924345349, '生活用品', '学生日常生活所需的各种物品', 0, '2025-11-28 20:00:00', '2025-11-28 20:00:00');
INSERT INTO `category` VALUES (1994378398924345350, '益智玩具', '有助于开发智力的各类玩具和游戏', 0, '2025-11-28 20:00:00', '2025-11-28 20:00:00');
INSERT INTO `category` VALUES (1994378398924345351, '背包收纳', '各种书包、背包和收纳用品', 0, '2025-11-28 20:00:00', '2025-11-28 20:00:00');

-- ----------------------------
-- Table structure for item
-- ----------------------------
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `categoryId` bigint(20) NOT NULL COMMENT '所属类别ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '商品描述',
  `imageUrl` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品图片URL',
  `pointPrice` int(10) NOT NULL COMMENT '积分价格',
  `orderCount` bigint(20) NULL DEFAULT NULL COMMENT '产生订单数量',
  `stock` int(11) NULL DEFAULT 0 COMMENT '库存数量',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '件' COMMENT '计量单位',
  `status` tinyint(4) NULL DEFAULT 0 COMMENT '状态：0上架 -，1-下架',
  `isDelete` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除标志：0-未删除，1-已删除',
  `createTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_categoryId`(`categoryId`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_pointPrice`(`pointPrice`) USING BTREE,
  INDEX `idx_isDeleted`(`isDelete`) USING BTREE,
  INDEX `idx_orderCount`(`orderCount`) USING BTREE,
  CONSTRAINT `item_ibfk_1` FOREIGN KEY (`categoryId`) REFERENCES `category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1994378563022295084 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of item
-- ----------------------------
INSERT INTO `item` VALUES (1994378523022295071, 1994378398924345350, '防晒霜', 'SPF50+防晒霜，有效防护紫外线。', 'https://image-cdn.poizon.com/app/2024/community/1615257979_byte1177944byte_48cc8acb318410ef62202bdc4a848ef5_iOS_w1440h1920.jpg', 69, 0, 25, '瓶', 0, 0, '2025-11-28 20:15:48', '2025-12-07 17:49:37');
INSERT INTO `item` VALUES (1994378535022295072, 1994378398924345350, '面膜', '补水保湿面膜，10片装。', 'https://q8.itc.cn/images01/20250718/7dc06280d38648ed83aeb6258f53b6f8.png', 79, 0, 35, '盒', 0, 0, '2025-11-28 20:15:49', '2025-12-07 17:49:54');
INSERT INTO `item` VALUES (1994378560022295070, 1994378398924345350, '洗面奶', '温和洁面乳，深层清洁。', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.alicdn.com%2Fimgextra%2Fi2%2F2778000028%2FO1CN01DNSPo41C4s7tqQk8g_%21%212778000028.jpg&refer=http%3A%2F%2Fimg.alicdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1767693021&t=140693296a2333f1141ccbc372351674', 39, 0, 30, '瓶', 0, 0, '2025-11-28 20:15:47', '2025-12-07 17:50:28');
INSERT INTO `item` VALUES (1994378561022295068, 1994378398924345350, '护手霜', '天然植物精华护手霜，滋润双手。', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.alicdn.com%2Fimgextra%2Fi2%2F3166629284%2FTB29je1kfBNTKJjSszbXXaFrFXa_%21%213166629284.jpg&refer=http%3A%2F%2Fimg.alicdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1767693035&t=912f88a249da8eeb0398d0e0378dcc2d', 29, 0, 40, '支', 0, 0, '2025-11-28 20:15:45', '2025-12-07 17:50:44');
INSERT INTO `item` VALUES (1994378562022295069, 1994378398924345350, '润唇膏', '保湿润唇膏，防止干裂。', 'https://img2.baidu.com/it/u=1711593108,3593891607&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=660', 19, 11, 37, '支', 0, 0, '2025-11-28 20:15:46', '2025-12-07 17:50:58');
INSERT INTO `item` VALUES (1994378563022295042, 1994378398924345345, '灰色布娃娃', '非常大的毛绒灰色布娃娃，适合儿童玩耍。', 'https://t15.baidu.com/it/u=2009098259,978597714&fm=224&app=112&f=JPEG?w=500&h=500', 199, 1, 0, '个', 0, 0, '2025-11-28 20:11:17', '2025-12-07 17:51:13');
INSERT INTO `item` VALUES (1994378563022295043, 1994378398924345345, '彩色泰迪熊', '可爱的彩色泰迪熊，柔软舒适，是孩子的好朋友。', 'https://bpic.588ku.com/element_origin_min_pic/25/06/09/01f2bdcd98638f1f49b7ab29267967ec.jpg', 159, 1, 14, '个', 0, 0, '2025-11-28 20:12:17', '2025-12-07 17:51:31');
INSERT INTO `item` VALUES (1994378563022295044, 1994378398924345345, '卡通抱枕', '印有热门卡通形象的舒适抱枕，适合午休使用。', 'https://b0.bdstatic.com/9905844f87d4853ace65491f18a0582e.jpg', 89, 1, 24, '个', 0, 0, '2025-11-28 20:13:17', '2025-12-07 17:51:47');
INSERT INTO `item` VALUES (1994378563022295045, 1994378398924345345, '毛绒小动物套装', '包含多种小型毛绒动物的套装，培养孩子的爱心。', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fcbu01.alicdn.com%2Fimg%2Fibank%2FO1CN012esjs91H5OhGvcJGX_%21%21932480706-0-cib.jpg&refer=http%3A%2F%2Fcbu01.alicdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1767693118&t=91274731e819664ff14d6fe7be3f634c', 129, 1, 11, '套', 0, 0, '2025-11-28 20:14:17', '2025-12-07 17:52:04');
INSERT INTO `item` VALUES (1994378563022295046, 1994378398924345346, '彩色铅笔套装', '24色可水洗彩色铅笔，环保材质，安全无毒。', 'https://img1.baidu.com/it/u=298468048,831938357&fm=253&app=138&f=JPEG?w=500&h=667', 89, 1, 49, '套', 0, 0, '2025-11-28 20:15:17', '2025-12-07 17:52:24');
INSERT INTO `item` VALUES (1994378563022295047, 1994378398924345346, '笔记本套装', '精装笔记本三件套，包含横线本、网格本和空白本。', 'https://qcloud.dpfile.com/pc/kjkU9fsi7orhh-G6lm7NbGL3FIYeaG5aCDYWu-dP7U4Qm2AFI5GVZbGpXdLByIYv.jpg', 59, 1, 39, '套', 0, 0, '2025-11-28 20:16:17', '2025-12-07 17:56:31');
INSERT INTO `item` VALUES (1994378563022295048, 1994378398924345346, '文件夹套装', 'A4文件夹套装，包含多层文件夹和文件袋。', 'https://gips0.baidu.com/it/u=55175580,1640514562&fm=3074&app=3074&f=JPEG', 49, 1, 58, '套', 0, 0, '2025-11-28 20:17:17', '2025-12-07 17:56:50');
INSERT INTO `item` VALUES (1994378563022295049, 1994378398924345346, '荧光笔', '彩色荧光笔套装，多种颜色可选。', 'https://gips3.baidu.com/it/u=2315807816,4120348646&fm=3074&app=3074&f=JPEG', 25, 2, 76, '套', 0, 0, '2025-11-28 20:18:17', '2025-12-07 17:57:03');
INSERT INTO `item` VALUES (1994378563022295050, 1994378398924345346, '橡皮擦套装', '可爱造型橡皮擦套装，不粘手，易擦净。', 'https://t14.baidu.com/it/u=3524197803,2532399854&fm=224&app=112&f=JPEG?w=500&h=500', 29, 0, 80, '套', 0, 0, '2025-11-28 20:19:17', '2025-12-07 17:57:19');
INSERT INTO `item` VALUES (1994378563022295051, 1994378398924345346, '铅笔刀', '自动铅笔刀，可调节笔尖粗细。', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.alicdn.com%2Fimgextra%2Fi1%2F3855309764%2FO1CN01CaxJCc2LzxspMAkwe_%21%213855309764.jpg&refer=http%3A%2F%2Fimg.alicdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1767693453&t=c3c72cf6b9350c76693d59f91c2d9844', 39, 1, 48, '个', 0, 0, '2025-11-28 20:20:17', '2025-12-07 17:57:41');
INSERT INTO `item` VALUES (1994378563022295052, 1994378398924345346, '订书机', '省力订书机，可订多种厚度纸张。', 'https://t14.baidu.com/it/u=2220592925,2840817943&fm=224&app=112&f=JPEG?w=500&h=500', 45, 0, 40, '个', 0, 0, '2025-11-28 20:21:17', '2025-12-07 17:59:11');
INSERT INTO `item` VALUES (1994378563022295053, 1994378398924345346, '圆规套装', '金属圆规套装，包含多种绘图工具。', 'https://gips2.baidu.com/it/u=1808806055,465769895&fm=3074&app=3074&f=JPEG?w=800&h=791&type=normal&func=', 35, 1, 58, '套', 0, 0, '2025-11-28 20:22:17', '2025-12-07 17:59:28');
INSERT INTO `item` VALUES (1994378563022295054, 1994378398924345346, '尺子套装', '不锈钢尺子套装，包含直尺、三角板等。', 'https://img14.360buyimg.com/pop/jfs/t1/191745/7/11213/203227/60de8f43E246ea6e5/17a116f43cfe8b5e.jpg', 39, 1, 48, '套', 0, 0, '2025-11-28 20:23:17', '2025-12-07 17:59:41');
INSERT INTO `item` VALUES (1994378563022295055, 1994378398924345346, '修正带', '大容量修正带，顺滑耐用。', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.alicdn.com%2Fimgextra%2Fi3%2F367006176%2FO1CN01pTpW0V1vUeqj7mPcC_%21%21367006176.jpg&refer=http%3A%2F%2Fimg.alicdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1767693592&t=b9ef052e169c59e58be114e8326ba0d0', 15, 3, 74, '个', 0, 0, '2025-11-28 20:24:17', '2025-12-07 17:59:59');
INSERT INTO `item` VALUES (1994378563022295056, 1994378398924345347, '篮球', '标准7号篮球，耐磨防滑，适合室内外使用。', 'https://pic.rmb.bdstatic.com/bjh/cms/250923/ddc459084b5fa69190661978e20f9976_1758626147.0168_15.jpeg', 189, 1, 17, '个', 0, 0, '2025-11-28 20:25:17', '2025-12-07 18:00:12');
INSERT INTO `item` VALUES (1994378563022295057, 1994378398924345347, '羽毛球拍', '碳纤维羽毛球拍，轻便耐用。', 'https://img0.baidu.com/it/u=652613357,485811741&fm=253&app=138&f=JPEG?w=800&h=994', 139, 0, 25, '副', 0, 0, '2025-11-28 20:26:17', '2025-12-07 17:49:18');
INSERT INTO `item` VALUES (1994378563022295058, 1994378398924345347, '跳绳', '计数跳绳，可调节长度，适合运动健身。', 'http://10.126.126.1:8009/leirui-oss/scholl point mall/upload/6939389d879d348e68991e21.jpeg', 29, 2, 58, '根', 0, 0, '2025-11-28 20:27:17', '2025-12-10 17:08:48');
INSERT INTO `item` VALUES (1994378563022295059, 1994378398924345347, '足球', '标准5号足球，橡胶内胆，pu材质。', 'https://copyright.bdstatic.com/vcg/creative/622be74ea81af5947da5abfbd47a803b.jpg@c_1,w_1189,h_792,x_0,y_372', 169, 0, 18, '个', 0, 0, '2025-11-28 20:28:17', '2025-12-07 17:48:24');
INSERT INTO `item` VALUES (1994378563022295060, 1994378398924345347, '乒乓球拍', '双面乒乓球拍，带乒乓球。', 'https://qcloud.dpfile.com/pc/5_pLX32b7EeO5Dknni7Vm9uzsj7cwumoDpRKOWmeaUb2y5o8aoN_P8m9D9Q8eeWh.jpg', 79, 0, 35, '副', 0, 0, '2025-11-28 20:29:17', '2025-12-07 17:48:07');
INSERT INTO `item` VALUES (1994378563022295061, 1994378398924345348, '计算器', '多功能科学计算器，适合学生使用。', 'https://cdnimg.chinagoods.com/jpg/2023/08/30/7ab09deb54a8dccc22c1e44b3997cba6.jpg', 99, 0, 35, '个', 0, 0, '2025-11-28 20:30:17', '2025-12-07 17:47:52');
INSERT INTO `item` VALUES (1994378563022295062, 1994378398924345348, '台灯', '护眼LED台灯，可调节亮度和角度。', 'https://img1.baidu.com/it/u=1995622921,838328204&fm=253&app=138&f=JPEG?w=800&h=1067', 179, 0, 25, '个', 0, 0, '2025-11-28 20:31:17', '2025-12-07 17:47:36');
INSERT INTO `item` VALUES (1994378563022295063, 1994378398924345348, '耳机', '舒适入耳式耳机，音质清晰，适合学习和娱乐。', 'https://q5.itc.cn/images01/20250504/515973a1957d4892a29f19af8439845b.png', 149, 0, 30, '副', 0, 0, '2025-11-28 20:32:17', '2025-12-07 17:46:52');
INSERT INTO `item` VALUES (1994378563022295064, 1994378398924345348, 'U盘', '高速USB 3.0 U盘，16GB容量，防水设计。', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fcbu01.alicdn.com%2Fimg%2Fibank%2FO1CN01dJ2x5y1RZIPXWeECO_%21%213261102125-0-cib.jpg&refer=http%3A%2F%2Fcbu01.alicdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1767692786&t=7718d752c18b4bd2491bacb7c6fcaa4d', 69, 0, 40, '个', 0, 0, '2025-11-28 20:33:17', '2025-12-07 17:46:32');
INSERT INTO `item` VALUES (1994378563022295065, 1994378398924345349, '运动水杯', '大容量不锈钢运动水杯，保温保冷，适合户外活动。', 'https://img0.baidu.com/it/u=4276707929,2178149955&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=1422', 129, 0, 30, '个', 0, 0, '2025-11-28 20:34:17', '2025-12-07 17:46:16');
INSERT INTO `item` VALUES (1994378563022295066, 1994378398924345349, '雨伞', '三折自动雨伞，轻便易携带，防风设计。', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.alicdn.com%2Fi2%2F721366271%2FO1CN01mqDGbq1wCAUruBVHq_%21%210-item_pic.jpg&refer=http%3A%2F%2Fimg.alicdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1767692752&t=80cf9860256b42dd2aa075bff910dc9d', 89, 0, 25, '把', 0, 0, '2025-11-28 20:35:17', '2025-12-07 17:45:59');
INSERT INTO `item` VALUES (1994378563022295067, 1994378398924345349, '收纳盒', '多功能桌面收纳盒，整理各种小物件。', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.alicdn.com%2Fimgextra%2Fi3%2F6000000001385%2FO1CN01L9xby71M6NQBzwXEs_%21%216000000001385-0-gg_content.jpg&refer=http%3A%2F%2Fimg.alicdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1767692735&t=fd851c0084ad0aa53157a4793e94d54c', 59, 0, 35, '个', 0, 0, '2025-11-28 20:36:17', '2025-12-07 17:45:43');
INSERT INTO `item` VALUES (1994378563022295068, 1994378398924345349, '毛巾', '纯棉吸水毛巾，柔软舒适，不易掉毛。', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.alicdn.com%2Fi2%2F393629613%2FO1CN01eNjplN2KsoMkrSYAS_%21%21393629613.jpg&refer=http%3A%2F%2Fimg.alicdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1767692711&t=fb390e37b00fdaccc956c6c04606b8b1', 39, 0, 50, '条', 0, 0, '2025-11-28 20:37:17', '2025-12-07 17:45:18');
INSERT INTO `item` VALUES (1994378563022295069, 1994378398924345350, '拼图玩具', '1000片益智拼图，培养耐心和专注力。', 'https://cdnimg.chinagoods.com/jpg/2024/01/23/c69e48b4bbdb14fd30bf2728a29bded0.jpg', 169, 0, 15, '盒', 0, 0, '2025-11-28 20:38:17', '2025-12-07 17:45:03');
INSERT INTO `item` VALUES (1994378563022295070, 1994378398924345350, '积木套装', '创意积木套装，锻炼空间想象力和动手能力。', 'https://legoeducation.cn/v3/assets/blt293eea581807678a/blt239a80a7ab5702c4/5f50d50452862877d0f20955/45020_1050x1050_1_xx-xx.jpg?locale=zh-cn&auto=webp&format=jpeg&width=100%25&quality=90&fit=bounds', 149, 0, 20, '套', 0, 0, '2025-11-28 20:39:17', '2025-12-07 17:44:28');
INSERT INTO `item` VALUES (1994378563022295071, 1994378398924345350, '魔方', '三阶魔方，经典益智玩具，锻炼思维能力。', 'https://img0.baidu.com/it/u=649503824,2000616958&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=611', 49, 0, 40, '个', 0, 0, '2025-11-28 20:40:17', '2025-12-07 17:43:03');
INSERT INTO `item` VALUES (1994378563022295072, 1994378398924345351, '学生书包', '轻便耐用的双肩书包，多个隔层，大容量。', 'https://img2.baidu.com/it/u=3958018030,1848081994&fm=253&app=138&f=JPEG?w=800&h=1069', 259, 0, 20, '个', 0, 0, '2025-11-28 20:41:17', '2025-12-07 17:42:49');
INSERT INTO `item` VALUES (1994378563022295073, 1994378398924345351, '笔袋', '大容量笔袋，多层设计，方便收纳各种文具。', 'https://a.zdmimg.com/202502/20/67b72602640bb39.jpg_e680.jpg', 49, 0, 60, '个', 0, 0, '2025-11-28 20:42:17', '2025-12-07 17:42:31');
INSERT INTO `item` VALUES (1994378563022295074, 1994378398924345345, '恐龙毛绒玩具', '逼真造型的恐龙毛绒玩具，孩子的侏罗纪伙伴。', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.alicdn.com%2Fimgextra%2Fi3%2FO1CN01ggPHk31mLem7guuSx_%21%214611686018427380458-0-rate.jpg&refer=http%3A%2F%2Fimg.alicdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1767692519&t=7612ad998ef1ac6e28170de60095d1ee', 179, 0, 12, '个', 0, 0, '2025-11-29 09:00:00', '2025-12-07 17:42:07');
INSERT INTO `item` VALUES (1994378563022295075, 1994378398924345345, '猫咪玩偶', '可爱的猫咪造型毛绒玩具，柔软亲肤材质。', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fci.xiaohongshu.com%2F3602a047-5f86-acbd-d0d0-abe91a51b4cd%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fci.xiaohongshu.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1767692504&t=9e735ff3edada367f3884dd0332e8f6b', 149, 0, 18, '个', 0, 0, '2025-11-29 09:01:00', '2025-12-07 17:41:53');
INSERT INTO `item` VALUES (1994378563022295076, 1994378398924345346, '水彩笔套装', '24色可水洗水彩笔，色彩鲜艳，安全无毒。', 'https://cdnimg.chinagoods.com/jpg/2020/08/12/c267100d2605d2bca533aa5549b95aa1.jpg', 99, 0, 40, '套', 0, 0, '2025-11-29 09:02:00', '2025-12-07 17:41:24');
INSERT INTO `item` VALUES (1994378563022295077, 1994378398924345346, '便签纸套装', '可爱造型便签纸，粘性强，不易脱落。', 'https://t15.baidu.com/it/u=731648443,3895107699&fm=224&app=112&f=JPEG?w=500&h=500', 29, 1, 79, '套', 0, 0, '2025-11-29 09:03:00', '2025-12-07 17:41:06');
INSERT INTO `item` VALUES (1994378563022295078, 1994378398924345347, '排球', '标准5号排球，适合教学和比赛使用。', 'https://imgservice.suning.cn/uimg1/b2c/image/puz0pk04LzCLIb2qZPMaHg.jpg_800w_800h_4e', 159, 0, 15, '个', 0, 0, '2025-11-29 09:04:00', '2025-12-07 17:40:25');
INSERT INTO `item` VALUES (1994378563022295079, 1994378398924345347, '运动手套', '透气防滑运动手套，适合各类运动。', 'https://images-cn.ssl-images-amazon.cn/images/I/71NRs1V2krL._AC_SX679_.jpg', 69, 0, 25, '副', 0, 0, '2025-11-29 09:05:00', '2025-12-07 17:40:10');
INSERT INTO `item` VALUES (1994378563022295080, 1994378398924345348, '移动电源', '10000mAh大容量移动电源，支持快充。', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.alicdn.com%2Fimgextra%2Fi4%2F1643163017%2FO1CN01G7BMho1Y9puGR6cUi_%21%211643163017.jpg&refer=http%3A%2F%2Fimg.alicdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1767692366&t=932202f2a6567ffaa3d376b7ddf23f28', 169, 0, 20, '个', 0, 0, '2025-11-29 09:06:00', '2025-12-07 17:39:37');
INSERT INTO `item` VALUES (1994378563022295081, 1994378398924345348, '鼠标垫', '大号游戏鼠标垫，防滑底面，顺滑表面。', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.alicdn.com%2Fi3%2F3343897026%2FO1CN01jlniwL21lxN3pLufJ_%21%213343897026.jpg&refer=http%3A%2F%2Fimg.alicdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1767692342&t=a06561e92650b8101907c0d3a0ddbf1d', 25, 0, 50, '个', 0, 0, '2025-11-29 09:07:00', '2025-12-07 17:39:09');
INSERT INTO `item` VALUES (1994378563022295082, 1994378398924345349, '笔记本电脑支架', '可调节笔记本支架，改善坐姿，散热效果好。', 'https://gips3.baidu.com/it/u=1105978387,3622809891&fm=3074&app=3074&f=JPEG', 129, 0, 25, '个', 0, 0, '2025-11-29 09:08:00', '2025-12-07 17:38:03');
INSERT INTO `item` VALUES (1994378563022295083, 1994378398924345351, '电脑包', '防震电脑包，适合15.6英寸笔记本电脑。', 'https://t14.baidu.com/it/u=2063411130,584125636&fm=224&app=112&f=JPEG?w=500&h=500', 179, 0, 15, '个', 0, 0, '2025-11-29 09:09:00', '2025-12-07 17:37:10');

-- ----------------------------
-- Table structure for login_log
-- ----------------------------
DROP TABLE IF EXISTS `login_log`;
CREATE TABLE `login_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userId` bigint(20) NOT NULL COMMENT '用户ID',
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录IP',
  `loginTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  `userAgent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户代理',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '登录状态 0-成功 1-失败',
  `errorMsg` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '错误信息',
  `createTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`userId`) USING BTREE,
  INDEX `idx_login_time`(`loginTime`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 310 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '登录日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for operation_log
-- ----------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userId` bigint(20) NOT NULL COMMENT '用户ID',
  `operation` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作类型',
  `method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求方法',
  `uri` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求URI',
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作IP',
  `userAgent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户代理',
  `requestParams` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数',
  `responseResult` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '响应结果',
  `operationTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `costTime` bigint(20) NOT NULL DEFAULT 0 COMMENT '耗时(毫秒)',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态 0-成功 1-失败',
  `errorMsg` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '错误信息',
  `createTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`userId`) USING BTREE,
  INDEX `idx_operation_time`(`operationTime`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5304 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for point_transaction
-- ----------------------------
DROP TABLE IF EXISTS `point_transaction`;
CREATE TABLE `point_transaction`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `userId` bigint(20) NOT NULL COMMENT '用户ID',
  `points` int(11) NOT NULL COMMENT '积分变动数量',
  `type` tinyint(4) NOT NULL COMMENT '积分变动类型 (1: 签到奖励, 2: 兑换商品, 3: 补签扣除等)',
  `businessId` bigint(20) NULL DEFAULT NULL COMMENT '关联业务ID (如签到记录ID、商品购买记录ID等)',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述信息',
  `createTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_userid_createtime`(`userId`, `createTime`) USING BTREE,
  INDEX `idx_type`(`type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '积分流水表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of point_transaction
-- ----------------------------
INSERT INTO `point_transaction` VALUES (1997153302946070529, 1994411050276564994, 10, 1, 1997153302883155969, '1994411050276564994签到成功，连续 1天签到，获得了 10积分。', '2025-12-06 11:57:07', '2025-12-06 11:57:07', 0);
INSERT INTO `point_transaction` VALUES (1997154097724735490, 1994411050276564994, 15, 1, 1997154097661820929, '1994411050276564994签到成功，连续 2天签到，获得了 15积分。', '2025-12-06 12:00:16', '2025-12-06 12:00:16', 0);
INSERT INTO `point_transaction` VALUES (1997268418047811586, 1992487379278557186, 10, 1, 1997268418026840065, '1992487379278557186签到成功，连续 1天签到，获得了 10积分。', '2025-12-06 19:34:32', '2025-12-06 19:34:32', 0);
INSERT INTO `point_transaction` VALUES (1997489385344872449, 1994411050276564994, 20, 1, 1997489385328095234, '1994411050276564994签到成功，连续 3天签到，获得了 20积分。', '2025-12-07 10:12:35', '2025-12-07 10:12:35', 0);
INSERT INTO `point_transaction` VALUES (1997553626668171265, 1992487379278557186, 15, 1, 1997553626655588353, '1992487379278557186签到成功，连续 2天签到，获得了 15积分。', '2025-12-07 14:27:52', '2025-12-07 14:27:52', 0);
INSERT INTO `point_transaction` VALUES (1997555735807492097, 1994411050276564994, 25, 1, 1997555735698440194, '1994411050276564994签到成功，连续 4天签到，获得了 25积分。', '2025-12-07 14:36:14', '2025-12-07 14:36:14', 0);
INSERT INTO `point_transaction` VALUES (1997557136738582530, 1994411050276564994, 30, 1, 1997557136738582529, '1994411050276564994签到成功，连续 5天签到，获得了 30积分。', '2025-12-07 14:41:48', '2025-12-07 14:41:48', 0);
INSERT INTO `point_transaction` VALUES (1997559319483744258, 1994411050276564994, 35, 1, 1997559319483744257, '1994411050276564994签到成功，连续 6天签到，获得了 35积分。', '2025-12-07 14:50:29', '2025-12-07 14:50:29', 0);
INSERT INTO `point_transaction` VALUES (1997597334696476673, 1997596958534516738, 10, 1, 1997597334629367810, '1997596958534516738签到成功，连续 1天签到，获得了 10积分。', '2025-12-07 17:21:32', '2025-12-07 17:21:32', 0);
INSERT INTO `point_transaction` VALUES (1997597459472826371, 1997597408683999234, 10, 1, 1997597459472826370, '1997597408683999234签到成功，连续 1天签到，获得了 10积分。', '2025-12-07 17:22:02', '2025-12-07 17:22:02', 0);
INSERT INTO `point_transaction` VALUES (1998662487533744130, 1997596958534516738, 10, 1, 1998662487500189698, '1997596958534516738签到成功，连续 1天签到，获得了 10积分。', '2025-12-10 15:54:05', '2025-12-10 15:54:05', 0);
INSERT INTO `point_transaction` VALUES (1998662584661241858, 1997596958534516738, 19, 2, 1998662584594132993, '兑换了 1 个编号是 1994378562022295069 的商品，共用了 19积分。', '2025-12-10 15:54:28', '2025-12-10 15:54:28', 0);
INSERT INTO `point_transaction` VALUES (1998664011014008833, 1994411050276564994, 10, 1, 1998664010946899969, '1994411050276564994签到成功，连续 1天签到，获得了 10积分。', '2025-12-10 16:00:08', '2025-12-10 16:00:08', 0);
INSERT INTO `point_transaction` VALUES (1998664057486897155, 1994411050276564994, 15, 2, 1998664057486897154, '兑换了 1 个编号是 1994378563022295055 的商品，共用了 15积分。', '2025-12-10 16:00:19', '2025-12-10 16:00:19', 0);
INSERT INTO `point_transaction` VALUES (1998664064621408259, 1994411050276564994, 45, 2, 1998664064621408258, '兑换了 3 个编号是 1994378563022295055 的商品，共用了 45积分。', '2025-12-10 16:00:21', '2025-12-10 16:00:21', 0);
INSERT INTO `point_transaction` VALUES (1998664187900391426, 1994411050276564994, 29, 2, 1998664187900391425, '兑换了 1 个编号是 1994378563022295058 的商品，共用了 29积分。', '2025-12-10 16:00:50', '2025-12-10 16:00:50', 0);
INSERT INTO `point_transaction` VALUES (1998664232649420803, 1994411050276564994, 29, 2, 1998664232649420802, '兑换了 1 个编号是 1994378563022295058 的商品，共用了 29积分。', '2025-12-10 16:01:01', '2025-12-10 16:01:01', 0);
INSERT INTO `point_transaction` VALUES (1998679379958939649, 1994411050276564994, 19, 2, 1998679379929579521, '兑换了 1 个编号是 1994378562022295069 的商品，共用了 19积分。', '2025-12-10 17:01:12', '2025-12-10 17:01:12', 0);

-- ----------------------------
-- Table structure for purchase_record
-- ----------------------------
DROP TABLE IF EXISTS `purchase_record`;
CREATE TABLE `purchase_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `itemId` bigint(20) NOT NULL COMMENT '商品ID',
  `userId` bigint(20) NOT NULL COMMENT '用户ID',
  `itemName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下单商品名称',
  `num` bigint(10) NOT NULL DEFAULT 1 COMMENT '下单数量',
  `paymentStatus` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付状态',
  `paymentAmount` decimal(10, 2) NULL DEFAULT NULL COMMENT '支付金额',
  `paymentTime` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
  `isDelete` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除标志：0-未删除，1-已删除',
  `createTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `orderNumber` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_itemId`(`itemId`) USING BTREE,
  INDEX `idx_userId`(`userId`) USING BTREE,
  INDEX `idx_itemName`(`itemName`) USING BTREE,
  INDEX `idx_orderNumber`(`orderNumber`) USING BTREE,
  CONSTRAINT `purchase_record_ibfk_1` FOREIGN KEY (`itemId`) REFERENCES `item` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `purchase_record_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1998729756272087043 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '购买记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of purchase_record
-- ----------------------------
INSERT INTO `purchase_record` VALUES (1995852384380207106, 1994378562022295069, 1994411050276564994, '润唇膏', 1, NULL, NULL, NULL, 0, '2025-12-02 21:47:44', '2025-12-02 21:47:44', '20251202749461');
INSERT INTO `purchase_record` VALUES (1995852763763392513, 1994378562022295069, 1994411050276564994, '润唇膏', 1, NULL, NULL, NULL, 0, '2025-12-02 21:49:14', '2025-12-02 21:49:14', '20251202167547');
INSERT INTO `purchase_record` VALUES (1995854875209539586, 1994378562022295069, 1994411050276564994, '润唇膏', 1, NULL, NULL, NULL, 0, '2025-12-02 21:57:38', '2025-12-02 21:57:38', '20251202932426');
INSERT INTO `purchase_record` VALUES (1995854937876635649, 1994378562022295069, 1994411050276564994, '润唇膏', 1, NULL, NULL, NULL, 0, '2025-12-02 21:57:53', '2025-12-02 21:57:53', '20251202522263');
INSERT INTO `purchase_record` VALUES (1995855044655226881, 1994378562022295069, 1994411050276564994, '润唇膏', 1, NULL, NULL, NULL, 0, '2025-12-02 21:58:18', '2025-12-02 21:58:18', '20251202128215');
INSERT INTO `purchase_record` VALUES (1995855046148399106, 1994378562022295069, 1994411050276564994, '润唇膏', 1, NULL, NULL, NULL, 0, '2025-12-02 21:58:18', '2025-12-02 21:58:18', '20251202533444');
INSERT INTO `purchase_record` VALUES (1995855046874013697, 1994378562022295069, 1994411050276564994, '润唇膏', 1, NULL, NULL, NULL, 0, '2025-12-02 21:58:19', '2025-12-02 21:58:19', '20251202796783');
INSERT INTO `purchase_record` VALUES (1995855047779983362, 1994378562022295069, 1994411050276564994, '润唇膏', 1, NULL, NULL, NULL, 0, '2025-12-02 21:58:19', '2025-12-02 21:58:19', '20251202362614');
INSERT INTO `purchase_record` VALUES (1995856674113044481, 1994378563022295043, 1994411050276564994, '彩色泰迪熊', 1, NULL, NULL, NULL, 0, '2025-12-02 22:04:46', '2025-12-02 22:04:46', '20251202485819');
INSERT INTO `purchase_record` VALUES (1995856762461863937, 1994378563022295044, 1994411050276564994, '卡通抱枕', 1, NULL, NULL, NULL, 0, '2025-12-02 22:05:08', '2025-12-02 22:05:08', '20251202407386');
INSERT INTO `purchase_record` VALUES (1995856789976498177, 1994378563022295045, 1994411050276564994, '毛绒小动物套装', 1, NULL, NULL, NULL, 0, '2025-12-02 22:05:14', '2025-12-02 22:05:14', '20251202598530');
INSERT INTO `purchase_record` VALUES (1995856809266102273, 1994378563022295046, 1994411050276564994, '彩色铅笔套装', 1, NULL, NULL, NULL, 0, '2025-12-02 22:05:19', '2025-12-02 22:05:19', '20251202883952');
INSERT INTO `purchase_record` VALUES (1995856840027127810, 1994378563022295047, 1994411050276564994, '笔记本套装', 1, NULL, NULL, NULL, 0, '2025-12-02 22:05:26', '2025-12-02 22:05:26', '20251202761374');
INSERT INTO `purchase_record` VALUES (1995856861594238978, 1994378563022295048, 1994411050276564994, '文件夹套装', 2, NULL, NULL, NULL, 0, '2025-12-02 22:05:31', '2025-12-02 22:05:31', '20251202781204');
INSERT INTO `purchase_record` VALUES (1995856886864920577, 1994378563022295049, 1994411050276564994, '荧光笔', 2, NULL, NULL, NULL, 0, '2025-12-02 22:05:37', '2025-12-02 22:05:37', '20251202587068');
INSERT INTO `purchase_record` VALUES (1995856931894968321, 1994378563022295051, 1994411050276564994, '铅笔刀', 2, NULL, NULL, NULL, 0, '2025-12-02 22:05:48', '2025-12-02 22:05:48', '20251202175834');
INSERT INTO `purchase_record` VALUES (1995856951687888898, 1994378563022295053, 1994411050276564994, '圆规套装', 2, NULL, NULL, NULL, 0, '2025-12-02 22:05:53', '2025-12-02 22:05:53', '20251202593075');
INSERT INTO `purchase_record` VALUES (1995857356455002114, 1994378563022295054, 1994411050276564994, '尺子套装', 2, NULL, NULL, NULL, 0, '2025-12-02 22:07:29', '2025-12-02 22:07:29', '20251202091401');
INSERT INTO `purchase_record` VALUES (1995857371017625601, 1994378563022295055, 1994411050276564994, '修正带', 2, NULL, NULL, NULL, 0, '2025-12-02 22:07:33', '2025-12-02 22:07:33', '20251202729465');
INSERT INTO `purchase_record` VALUES (1995857398645506049, 1994378563022295056, 1994411050276564994, '篮球', 3, NULL, NULL, NULL, 0, '2025-12-02 22:07:39', '2025-12-02 22:07:39', '20251202681754');
INSERT INTO `purchase_record` VALUES (1996542428350865410, 1994378563022295049, 1994411050276564994, '荧光笔', 2, NULL, NULL, NULL, 0, '2025-12-04 19:29:43', '2025-12-04 19:29:43', '20251204286516');
INSERT INTO `purchase_record` VALUES (1996547431694176257, 1994378562022295069, 1994411050276564994, '润唇膏', 2, NULL, NULL, NULL, 0, '2025-12-04 19:49:36', '2025-12-04 19:49:36', '20251204149194');
INSERT INTO `purchase_record` VALUES (1996547824734015490, 1994378562022295069, 1994411050276564994, '润唇膏', 1, NULL, NULL, NULL, 0, '2025-12-04 19:51:10', '2025-12-04 19:51:10', '20251204478227');
INSERT INTO `purchase_record` VALUES (1996551402332721153, 1994378563022295077, 1994411050276564994, '便签纸套装', 1, NULL, NULL, NULL, 0, '2025-12-05 20:05:23', '2025-12-06 09:22:52', '20251205675540');
INSERT INTO `purchase_record` VALUES (1998662584594132993, 1994378562022295069, 1997596958534516738, '润唇膏', 1, NULL, NULL, NULL, 0, '2025-12-10 15:54:28', '2025-12-10 15:54:28', '20251210783366');
INSERT INTO `purchase_record` VALUES (1998664057486897154, 1994378563022295055, 1994411050276564994, '修正带', 1, NULL, NULL, NULL, 0, '2025-12-10 16:00:19', '2025-12-10 16:00:19', '20251210147523');
INSERT INTO `purchase_record` VALUES (1998664064621408258, 1994378563022295055, 1994411050276564994, '修正带', 3, NULL, NULL, NULL, 0, '2025-12-10 16:00:21', '2025-12-10 16:00:21', '20251210799777');
INSERT INTO `purchase_record` VALUES (1998664187900391425, 1994378563022295058, 1994411050276564994, '跳绳', 1, NULL, NULL, NULL, 0, '2025-12-10 16:00:50', '2025-12-10 16:00:50', '20251210538594');
INSERT INTO `purchase_record` VALUES (1998664232649420802, 1994378563022295058, 1994411050276564994, '跳绳', 1, NULL, NULL, NULL, 0, '2025-12-10 16:01:01', '2025-12-10 16:01:01', '20251210393912');
INSERT INTO `purchase_record` VALUES (1998679379929579521, 1994378562022295069, 1994411050276564994, '润唇膏', 1, NULL, NULL, NULL, 0, '2025-12-10 17:01:12', '2025-12-10 17:01:12', '20251210507067');
INSERT INTO `purchase_record` VALUES (1998729756272087042, 1994378563022295042, 1992487379278557186, '灰色布娃娃', 10, '1', 1990.00, '2025-12-10 20:22:05', 0, '2025-12-10 20:21:23', '2025-12-10 20:21:23', '20251210707548');

-- ----------------------------
-- Table structure for sign_in_record
-- ----------------------------
DROP TABLE IF EXISTS `sign_in_record`;
CREATE TABLE `sign_in_record`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `userId` bigint(20) NOT NULL COMMENT '用户ID',
  `signInDate` datetime(0) NOT NULL COMMENT '签到日期',
  `consecutiveDays` int(11) NOT NULL DEFAULT 1 COMMENT '连续签到天数',
  `points` int(11) NOT NULL DEFAULT 0 COMMENT '获得积分数量',
  `createTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_userid_signdate`(`userId`, `signInDate`) USING BTREE,
  INDEX `idx_userid_createtime`(`userId`, `createTime`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '签到记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sign_in_record
-- ----------------------------
INSERT INTO `sign_in_record` VALUES (1997153302883155969, 1994411050276564994, '2025-12-05 11:57:07', 1, 10, '2025-12-05 11:57:07', '2025-12-05 11:57:07', 0);
INSERT INTO `sign_in_record` VALUES (1997154097661820929, 1994411050276564994, '2025-12-06 12:00:17', 2, 15, '2025-12-06 12:00:16', '2025-12-06 12:00:16', 0);
INSERT INTO `sign_in_record` VALUES (1997559319483744257, 1994411050276564994, '2025-12-07 14:50:29', 3, 35, '2025-12-07 14:50:29', '2025-12-07 15:36:19', 0);
INSERT INTO `sign_in_record` VALUES (1997597334629367810, 1997596958534516738, '2025-12-07 17:21:33', 1, 10, '2025-12-07 17:21:32', '2025-12-07 17:21:32', 0);
INSERT INTO `sign_in_record` VALUES (1997597459472826370, 1997597408683999234, '2025-12-07 17:22:03', 1, 10, '2025-12-07 17:22:02', '2025-12-07 17:22:02', 0);
INSERT INTO `sign_in_record` VALUES (1998662487500189698, 1997596958534516738, '2025-12-10 15:54:05', 1, 10, '2025-12-10 15:54:05', '2025-12-10 15:54:05', 0);
INSERT INTO `sign_in_record` VALUES (1998664010946899969, 1994411050276564994, '2025-12-10 16:00:08', 1, 10, '2025-12-10 16:00:08', '2025-12-10 16:00:08', 0);

-- ----------------------------
-- Table structure for sign_in_rule
-- ----------------------------
DROP TABLE IF EXISTS `sign_in_rule`;
CREATE TABLE `sign_in_rule`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `consecutiveDays` int(11) NOT NULL COMMENT '连续签到天数',
  `points` int(11) NOT NULL COMMENT '奖励积分数量',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '规则描述',
  `createTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `isDelete` tinyint(2) NOT NULL DEFAULT 0 COMMENT '逻辑删除标志：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1997597742605123587 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '签到规则表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sign_in_rule
-- ----------------------------
INSERT INTO `sign_in_rule` VALUES (1994378563022295769, 1, 10, '第一天签到奖励', '2025-12-05 16:48:01', '2025-12-06 15:23:50', 0);
INSERT INTO `sign_in_rule` VALUES (1994378563022295770, 2, 15, '连续第二天签到奖励', '2025-12-05 17:47:05', '2025-12-06 15:23:52', 0);
INSERT INTO `sign_in_rule` VALUES (1994378563022295771, 3, 20, '连续第三天签到奖励', '2025-12-05 18:47:08', '2025-12-06 15:23:52', 0);
INSERT INTO `sign_in_rule` VALUES (1994378563022295772, 4, 25, '连续第四天签到奖励', '2025-12-05 20:47:01', '2025-12-06 15:23:53', 0);
INSERT INTO `sign_in_rule` VALUES (1994378563022295773, 5, 30, '连续第五天签到奖励', '2025-12-05 21:47:11', '2025-12-06 15:23:53', 0);
INSERT INTO `sign_in_rule` VALUES (1994378563022295774, 6, 35, '连续第六天签到奖励', '2025-12-05 22:47:03', '2025-12-06 15:23:54', 0);
INSERT INTO `sign_in_rule` VALUES (1994378563022295775, 7, 40, '连续第七天签到奖励', '2025-12-05 23:46:01', '2025-12-06 17:04:12', 0);
INSERT INTO `sign_in_rule` VALUES (1997597742605123586, 8, 100, '第8天奖励100积分。', '2025-12-07 17:23:10', '2025-12-07 17:23:10', 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userAccount` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
  `userPassword` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `userName` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `gender` tinyint(2) NULL DEFAULT NULL COMMENT '性别',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `points` bigint(20) NULL DEFAULT 0 COMMENT '剩余积分数',
  `consecutiveSignInDays` bigint(11) NOT NULL DEFAULT 0 COMMENT '连续签到天数',
  `userAvatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户头像',
  `userProfile` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户简介',
  `userStatus` tinyint(4) NOT NULL DEFAULT 0 COMMENT '用户状态： int  0 - 正常 1-ban',
  `userRole` tinyint(4) NOT NULL DEFAULT 0 COMMENT '用户角色: 0 - 普通用户 1 - 管理员',
  `createTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_account`(`userAccount`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1997597408683999235 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1992487379278557186, 'leirui', 'c5704561f824fd4698c4f9cb23f08dc2', 'leirui', '17123213@11.com', 1, '15155192213', 1034, 2, 'http://10.126.126.1:8009/leirui-oss/scholl point mall/upload/6926e6ea3b8a62b61a7070e3.jpeg', '我是一个管理员', 0, 1, '2025-11-23 14:56:24', '2025-12-07 14:27:52', 0);
INSERT INTO `user` VALUES (1994411050276564994, 'zhangsan', 'c5704561f824fd4698c4f9cb23f08dc2', 'zhangsan', '21312@qq.com', 0, '12345678', 8, 1, 'http://10.126.126.1:8009/leirui-oss/scholl point mall/upload/6929b2d1d1ad3ec30f929ba0.jpeg', '普通用户一枚', 0, 0, '2025-11-28 22:20:23', '2025-12-10 16:00:08', 0);
INSERT INTO `user` VALUES (1997596958534516738, 'lisi1234', 'c5704561f824fd4698c4f9cb23f08dc2', 'lisi1234', NULL, NULL, NULL, 1, 1, NULL, NULL, 1, 0, '2025-12-07 17:20:03', '2025-12-10 15:54:05', 0);
INSERT INTO `user` VALUES (1997597408683999234, 'wangwu123', 'c5704561f824fd4698c4f9cb23f08dc2', 'wangwu123', NULL, NULL, NULL, 10, 1, NULL, NULL, 0, 0, '2025-12-07 17:21:50', '2025-12-07 17:22:02', 0);

-- ----------------------------
-- Table structure for user_activity
-- ----------------------------
DROP TABLE IF EXISTS `user_activity`;
CREATE TABLE `user_activity`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userId` bigint(20) NOT NULL COMMENT '用户ID',
  `loginCount` int(11) NOT NULL DEFAULT 0 COMMENT '登录次数',
  `lastLoginTime` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `lastActiveTime` datetime(0) NULL DEFAULT NULL COMMENT '最后活跃时间',
  `activityScore` int(11) NOT NULL DEFAULT 0 COMMENT '活跃度分数',
  `createTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_id`(`userId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户活跃度表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_activity
-- ----------------------------
INSERT INTO `user_activity` VALUES (1, 1992487379278557186, 119, '2025-12-10 20:20:00', '2025-12-10 20:20:00', 1190, '2025-11-23 14:56:24', '2025-12-10 20:20:00');
INSERT INTO `user_activity` VALUES (2, 1994411050276564994, 176, '2025-12-10 17:00:47', '2025-12-10 17:00:47', 1760, '2025-11-28 22:20:23', '2025-12-10 17:00:46');
INSERT INTO `user_activity` VALUES (3, 1997596958534516738, 10, '2025-12-10 15:53:57', '2025-12-10 15:53:57', 100, '2025-12-07 17:20:03', '2025-12-10 15:53:56');
INSERT INTO `user_activity` VALUES (4, 1997597408683999234, 4, '2025-12-07 18:15:09', '2025-12-07 18:15:09', 40, '2025-12-07 17:21:50', '2025-12-07 18:15:09');

-- ----------------------------
-- View structure for category_sales_rank
-- ----------------------------
DROP VIEW IF EXISTS `category_sales_rank`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `category_sales_rank` AS select `c`.`id` AS `id`,`c`.`name` AS `name`,coalesce(sum(`i`.`order_count`),0) AS `total_order_count` from (`category` `c` left join `item` `i` on(((`c`.`id` = `i`.`categoryId`) and (`i`.`isDelete` = 0)))) where (`c`.`isDelete` = 0) group by `c`.`id`,`c`.`name` order by `total_order_count` desc;

SET FOREIGN_KEY_CHECKS = 1;

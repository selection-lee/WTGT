CREATE TABLE `BaseEntity`
(
    `created_at` datetime NULL,
    `updated_at` datetime NULL
);

CREATE TABLE `Member`
(
    `member_id` int          NOT NULL,
    `username`  varchar(100) NULL,
    `nickname`  varchar(100) NULL,
    `role`      varchar(30)  NOT NULL,
    `password`  varchar(100) NOT NULL
);

CREATE TABLE `Area`
(
    `area_id`     int          NOT NULL,
    `area_name`   varchar(100) NOT NULL,
    `base_sector` varchar(100) NOT NULL
);

CREATE TABLE `Product`
(
    `product_id`     int          NOT NULL,
    `product_name`   varchar(100) NOT NULL,
    `quantity`       int          NULL,
    `product_status` varchar(10)  NULL,
    `unit_price`     int          NULL,
    `total_price`    int          NULL,
    `category_id`    int          NOT NULL,
    `invoice_id`     int          NOT NULL,
    `product_size`   VARCHAR(255) NULL,
    `sector_id`      int          NOT NULL
);

CREATE TABLE `Sector`
(
    `sector_id`     int          NOT NULL,
    `sector_number` varchar(100) NOT NULL,
    `area_id`       int          NOT NULL
);

CREATE TABLE `Category`
(
    `category_id`   int          NOT NULL,
    `category_name` varchar(100) NOT NULL
);

CREATE TABLE `Invoice`
(
    `invoice_id`   int      NOT NULL,
    `issue_date`   datetime NULL,
    `due_date`     datetime NULL,
    `shipping_fee` int      NULL,
    `tax`          int      NULL,
    `total_amount` int      NULL,
    `sender_id`    int      NOT NULL,
    `recipient_id` int      NOT NULL
);

CREATE TABLE `recipient`
(
    `recipient_id` int          NOT NULL,
    `name`         varchar(100) NULL,
    `email`        varchar(100) NULL,
    `phone`        varchar(100) NULL,
    `address`      varchar(100) NULL
);

CREATE TABLE `sender`
(
    `sender_id` int          NOT NULL,
    `name`      varchar(100) NULL,
    `email`     varchar(100) NULL,
    `phone`     varchar(100) NULL,
    `address`   varchar(100) NULL
);

CREATE TABLE `Order`
(
    `order_id`         int          NOT NULL,
    `invoice_number`   int          NOT NULL,
    `product_name`     varchar(100) NULL,
    `product_quantity` int          NULL,
    `is_store`         boolean      NOT NULL,
    `member_id`        int          NOT NULL
);

ALTER TABLE `Member`
    ADD CONSTRAINT `PK_MEMBER`
        PRIMARY KEY (`member_id`);

ALTER TABLE `Area`
    ADD CONSTRAINT `PK_AREA`
        PRIMARY KEY (`area_id`);

ALTER TABLE `Product`
    ADD CONSTRAINT `PK_PRODUCT`
        PRIMARY KEY (`product_id`);

ALTER TABLE `Sector`
    ADD CONSTRAINT `PK_SECTOR`
        PRIMARY KEY (`sector_id`);

ALTER TABLE `Category`
    ADD CONSTRAINT `PK_CATEGORY`
        PRIMARY KEY (`category_id`);

ALTER TABLE `Invoice`
    ADD CONSTRAINT `PK_INVOICE`
        PRIMARY KEY (`invoice_id`);

ALTER TABLE `recipient`
    ADD CONSTRAINT `PK_RECIPIENT`
        PRIMARY KEY (`recipient_id`);

ALTER TABLE `sender`
    ADD CONSTRAINT `PK_SENDER`
        PRIMARY KEY (`sender_id`);

ALTER TABLE `Order`
    ADD CONSTRAINT `PK_ORDER`
        PRIMARY KEY (`order_id`);


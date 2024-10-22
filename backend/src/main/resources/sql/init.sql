CREATE TABLE `BaseEntity` (
    `created_at`    datetime    NULL,
    `updated_at`	datetime    NULL
);

CREATE TABLE `Member` (
    `member_id`	   int	        NOT NULL,
    `username`	   varchar(100)	NULL,
    `nickname`	   varchar(100)	NULL,
    `role`	       varchar(30)	NOT NULL,
    `password`	   varchar(100)	NOT NULL
);

CREATE TABLE `Area` (
    `area_id`	    int	            NOT NULL,
    `area_name`	    varchar(100)	NOT NULL,
    `base_sector`	varchar(100)	NOT NULL
);

CREATE TABLE `Product` (
    `product_id`	int	            NOT NULL,
    `product_name`	varchar(100)	NOT NULL,
    `quantity`	    int	            NOT NULL,
    `product_status`varchar(10)	    NOT NULL,
    `unit_price`	int	            NULL,
    `total_price`	int	            NULL,
    `category_id`	int	            NOT NULL,
    `invoice_id`	int	            NOT NULL
);

CREATE TABLE `Sector` (
    `sector_id`	    int	            NOT NULL,
    `sector_number`	varchar(100)	NOT NULL,
    `area_id`	    int	            NOT NULL
);

CREATE TABLE `ProductLocation` (
    `product_location_id`	int	            NOT NULL,
    `shelf_number`	        VARCHAR(255)	NOT NULL,
    `sector_id`	            int	            NOT NULL,
    `product_id`	        int	            NOT NULL
);

CREATE TABLE `Category` (
    `category_id`	int	            NOT NULL,
    `category_name`	varchar(100)	NOT NULL
);

CREATE TABLE `Invoice` (
    `invoice_id`	int	        NOT NULL,
    `issue_date`	datetime	NULL,
    `due_date`	    datetime    NULL,
    `shipping_fee`	int	        NULL,
    `tax`	        int         NULL,
    `total_amount`	int	        NULL,
    `sender_id`	    int         NOT NULL,
    `recipient_id`	int	        NOT NULL
);

CREATE TABLE `recipient` (
    `recipient_id`	int	            NOT NULL,
    `name`	        varchar(100)	NULL,
    `email`	        varchar(100)	NULL,
    `phone`	        varchar(100)	NULL,
    `address`	    varchar(100)	NULL
);

CREATE TABLE `sender` (
    `sender_id`	int	            NOT NULL,
    `name`	    varchar(100)	NULL,
    `email`	    varchar(100)	NULL,
    `phone`	    varchar(100)	NULL,
    `address`	varchar(100)	NULL
);

CREATE TABLE `EventLog` (
    `event_id`	            bigint	        NOT NULL,
    `event_type`	        varchar(100)	NOT NULL,
    `event_description`	    text	        NULL,
    `related_entity_id`	    int	            NULL,
    `related_entity_type`	varchar(100)	NOT NULL,
    `additional_info`	    text	        NULL
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

ALTER TABLE `ProductLocation`
ADD CONSTRAINT `PK_PRODUCTLOCATION`
PRIMARY KEY (`product_location_id`);

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

ALTER TABLE `EventLog`
ADD CONSTRAINT `PK_EVENTLOG`
PRIMARY KEY (`event_id`);

ALTER TABLE `Product`
ADD CONSTRAINT `FK_Category_TO_Product_1`
FOREIGN KEY (`category_id`)
REFERENCES `Category` (`category_id`);

ALTER TABLE `Product`
ADD CONSTRAINT `FK_Invoice_TO_Product_1`
FOREIGN KEY (`invoice_id`)
REFERENCES `Invoice` (`invoice_id`);

ALTER TABLE `Sector`
ADD CONSTRAINT `FK_Area_TO_Sector_1`
FOREIGN KEY ( `area_id`)
REFERENCES `Area` (`area_id`);

ALTER TABLE `ProductLocation`
ADD CONSTRAINT `FK_Sector_TO_ProductLocation_1`
FOREIGN KEY (`sector_id`)
REFERENCES `Sector` (`sector_id`);

ALTER TABLE `ProductLocation`
ADD CONSTRAINT `FK_Product_TO_ProductLocation_1`
FOREIGN KEY ( `product_id`)
REFERENCES `Product` (`product_id`);

ALTER TABLE `Invoice`
ADD CONSTRAINT `FK_sender_TO_Invoice_1`
FOREIGN KEY (`sender_id`)
REFERENCES `sender` (`sender_id`);

ALTER TABLE `Invoice`
ADD CONSTRAINT `FK_recipient_TO_Invoice_1`
FOREIGN KEY (`recipient_id`)
REFERENCES `recipient` (`recipient_id`);

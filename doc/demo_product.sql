CREATE TABLE `product` (
                           `ID` INT NOT NULL AUTO_INCREMENT,
                           `GUID` LONGTEXT,
                           `StockID` INT DEFAULT NULL,
                           `BarCodeID` INT DEFAULT NULL,
                           `SkuID` INT DEFAULT NULL,
                           `ProductName` VARCHAR(500) DEFAULT NULL,
                           `ProductStyle` VARCHAR(500) DEFAULT NULL,
                           `Price` DOUBLE DEFAULT NULL,
                           `CreateTime` TIMESTAMP NULL DEFAULT NULL,
                           `Status` SMALLINT DEFAULT NULL,
                           `Count` INT DEFAULT NULL,
                           `ModifyTime` TIMESTAMP NULL DEFAULT NULL,
                           `TimeStamp` BLOB,
                           PRIMARY KEY (`ID`)
) ENGINE=INNODB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8



SHOW CREATE TABLE  product
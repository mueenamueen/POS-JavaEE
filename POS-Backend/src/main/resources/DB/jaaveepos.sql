-- Drop and create database
DROP DATABASE IF EXISTS javaeepos;
CREATE DATABASE javaeepos;
USE javaeepos;

-- Create tables
CREATE TABLE Customer (
    customerId VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100),
    address VARCHAR(255),
    salary DECIMAL(10, 2)
);

CREATE TABLE Item (
    code VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100),
    qty INT,
    unitPrice DECIMAL(10, 2)
);

CREATE TABLE Orders (
    orderId VARCHAR(10) PRIMARY KEY,
    orderDate DATE,
    customerId VARCHAR(10),
    total DECIMAL(10, 2),
    subTotal DECIMAL(10, 2),
    paidAmount DECIMAL(10, 2),
    discount DECIMAL(10, 2),
    balance DECIMAL(10, 2),
    address VARCHAR(255),
    FOREIGN KEY (customerId) REFERENCES Customer(customerId)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE OrderDetail (
    orderId VARCHAR(10),
    itemCode VARCHAR(10),
    itemName VARCHAR(100),
    itemPrice DECIMAL(10, 2),
    quantity INT,
    total DECIMAL(10, 2),
    PRIMARY KEY (orderId, itemCode),
    FOREIGN KEY (orderId) REFERENCES Orders(orderId)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (itemCode) REFERENCES Item(code)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);


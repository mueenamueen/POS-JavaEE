create database if not exists javaeepos;
use javaeepos;
create table customer(
	customerId varchar(10) primary key,
    name varchar(100) not null,
    address varchar(255),
    salary double
);
create table item(
	itemId varchar(10) primary key,
    name varchar(100) not null,
    qty int not null,
    price double not null
);

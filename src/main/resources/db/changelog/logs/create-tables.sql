--liquibase formatted sql

--changeset teep:create-customers-table
CREATE TABLE customers (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   email VARCHAR(50) UNIQUE NOT NULL,
   password VARCHAR(255) NOT NULL,
   role VARCHAR(20) NOT NULL,
   gender VARCHAR(10),
   country VARCHAR(50),
   last_3ds_date TIMESTAMP,
   created_at TIMESTAMP,
   updated_at TIMESTAMP
);

--changeset teep:create-rules-table
CREATE TABLE rules (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR(50) NOT NULL,
   meta TEXT NOT NULL,
   created_at TIMESTAMP,
   updated_at TIMESTAMP
);

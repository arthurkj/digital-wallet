--liquibase formatted sql

--changeset arthur.juchem:1
CREATE TABLE users (
id SERIAL PRIMARY KEY,
name VARCHAR(50) NOT NULL,
person_registration_code VARCHAR(14) NOT NULL UNIQUE,
email VARCHAR(100) NOT NULL UNIQUE,
password VARCHAR(50) NOT NULL,
type VARCHAR(50) NOT NULL,
balance NUMERIC DEFAULT 0
);

--changeset arthur.juchem:2
CREATE TABLE transactions (
id SERIAL PRIMARY KEY,
status VARCHAR(50) NOT NULL,
amount NUMERIC NOT NULL,
sender_id INT REFERENCES users(id) NOT NULL,
receiver_id INT REFERENCES users(id) NOT NULL,
date TIMESTAMP NOT NULL
);
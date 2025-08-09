--liquibase formatted sql

--changeset lena:user-1
CREATE TABLE  users (
    id BIGSERIAL PRIMARY KEY,
    age INTEGER,
    gender BOOLEAN,
    name TEXT,
    password TEXT,
    role TEXT
)
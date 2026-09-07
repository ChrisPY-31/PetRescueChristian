-- PetRescueChristian - segunda entrega
-- Esquema de PostgreSQL. Ejecutar contra la base "pet-rescue-christian".

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS pets (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    species VARCHAR(50) NOT NULL,
    breed VARCHAR(100) NOT NULL,
    age INTEGER NOT NULL,
    size VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    image_uri VARCHAR(500),
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    available BOOLEAN NOT NULL DEFAULT TRUE
);

-- report_date en vez de "date" para evitar el nombre reservado de Postgres.
CREATE TABLE IF NOT EXISTS pet_reports (
    id SERIAL PRIMARY KEY,
    species VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    image_uri VARCHAR(500),
    report_date VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL
);

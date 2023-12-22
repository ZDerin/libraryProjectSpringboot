CREATE TABLE users (
    id uuid PRIMARY KEY,
    username VARCHAR(50) NOT NULL unique,
    email VARCHAR(50) NOT NULL unique,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);
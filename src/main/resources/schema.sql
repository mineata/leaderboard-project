CREATE DATABASE IF NOT EXISTS gamescore;

USE gamescore;

CREATE TABLE IF NOT EXISTS users (
  user_id BINARY(16) NOT NULL,
  display_name VARCHAR(500) NOT NULL,
  points FLOAT(11),
  rank BIGINT(20),
  country VARCHAR(10) NOT NULL,
  PRIMARY KEY (user_id)
);

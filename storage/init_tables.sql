CREATE DATABASE remix;
USE remix;
CREATE TABLE users (
       user_id INT NOT NULL AUTO_INCREMENT,
       email VARCHAR(50),
       name VARCHAR(50),
       password_hash BINARY(60),
       salt BINARY(29),
       PRIMARY KEY (user_id)
);
-- The password is 'admindefault' without quotes.
INSERT INTO `users` (`user_id`, `email`, `name`, `password_hash`, `salt`) VALUES (NULL, "admin@localhost", "admin", '$2a$10$l5wmICwP7kiZE644ZaJK0.kcCx8By3uVqUMzwZIquSB9IKxpjgQbK', '$2a$10$l5wmICwP7kiZE644ZaJK0.');
CREATE TABLE files (
       file_id INT NOT NULL AUTO_INCREMENT,
       name VARCHAR(50),
       proof_json LONGTEXT,
       PRIMARY KEY (file_id)
);
CREATE TABLE user_file_permissions (
       file_id INT,
       user_id INT,
       permissions CHAR(1),
       FOREIGN KEY (file_id) REFERENCES files(file_id),
       FOREIGN KEY (user_id) REFERENCES users(user_id)
);
CREATE TABLE session_values (
       user_id INT,
       session_key BINARY(60),
       creation_time TIMESTAMP,
       PRIMARY KEY (session_key),
       FOREIGN KEY (user_id) REFERENCES users(user_id)
);

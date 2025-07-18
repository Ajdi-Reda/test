CREATE TABLE users (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       name varchar(30),
                       email VARCHAR(255) UNIQUE,
                       password TEXT,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

);

CREATE TABLE roles (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       name varchar(30),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_roles (
                           id INT PRIMARY KEY AUTO_INCREMENT,
                           user_id INT,
                           role_id INT,
                           FOREIGN KEY (user_id) REFERENCES Users(id),
                           FOREIGN KEY (role_id) REFERENCES Roles(id),
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

alter table users add column first_login boolean default true
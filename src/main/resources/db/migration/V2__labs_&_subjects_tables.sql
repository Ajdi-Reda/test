CREATE TABLE subjects (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE labs (
                      id INT PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(100) NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

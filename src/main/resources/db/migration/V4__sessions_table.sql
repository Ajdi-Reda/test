CREATE TABLE lab_sessions (
                              id INT PRIMARY KEY AUTO_INCREMENT,
                              group_id INT NULL,
                              lab_id INT NOT NULL,
                              scheduled_start DATETIME NOT NULL,
                              scheduled_end DATETIME NOT NULL,
                              created_by INT NOT NULL,
                              validated BOOLEAN DEFAULT FALSE,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (group_id) REFERENCES `groups`(id),
                              FOREIGN KEY (lab_id) REFERENCES labs(id),
                              FOREIGN KEY (created_by) REFERENCES users(id)

);

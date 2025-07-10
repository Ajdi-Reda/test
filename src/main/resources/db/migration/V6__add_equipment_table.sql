CREATE TABLE equipment (
                           id INT PRIMARY KEY AUTO_INCREMENT,
                           barcode VARCHAR(255) NOT NULL,
                           brand VARCHAR(255) NOT NULL,
                           model VARCHAR(255) NOT NULL,
                           purchase_date DATE NOT NULL,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           status ENUM('NEW', 'USED', 'DAMAGED', 'RETIRED') NOT NULL
);

-- Equipment Loans Table
CREATE TABLE equipmentloans (
                                id INT PRIMARY KEY AUTO_INCREMENT,
                                session_id INT NOT NULL,
                                equipment_id INT NOT NULL,
                                user_id INT NOT NULL,
                                reserved_from DATETIME NOT NULL,
                                reserved_to DATETIME NOT NULL,
                                signature VARCHAR(255),
                                returned BOOLEAN DEFAULT FALSE,
                                expected_return_date DATETIME,
                                actual_return_date DATETIME,
                                checkout_by INT,
                                returned_to INT,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (session_id) REFERENCES lab_sessions(id),
                                FOREIGN KEY (equipment_id) REFERENCES equipment(id),
                                FOREIGN KEY (user_id) REFERENCES users(id),
                                FOREIGN KEY (checkout_by) REFERENCES users(id),
                                FOREIGN KEY (returned_to) REFERENCES users(id)
);

-- Equipment History Table
CREATE TABLE equipment_history (
                                   id INT PRIMARY KEY AUTO_INCREMENT,
                                   session_id INT NOT NULL,
                                   equipment_id INT NOT NULL,
                                   event_type ENUM('NEW', 'USED', 'DAMAGED', 'RETIRED') NOT NULL,
                                   event_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   related_loan_id INT,
                                   performed_by INT,
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   FOREIGN KEY (session_id) REFERENCES lab_sessions(id),
                                   FOREIGN KEY (equipment_id) REFERENCES equipment(id),
                                   FOREIGN KEY (related_loan_id) REFERENCES equipmentloans(id),
                                   FOREIGN KEY (performed_by) REFERENCES users(id)
);
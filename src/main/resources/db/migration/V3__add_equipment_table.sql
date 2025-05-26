CREATE TABLE Equipment (
                           id INT PRIMARY KEY AUTO_INCREMENT,
                           barcode VARCHAR(255),
                           brand VARCHAR(255),
                           model VARCHAR(255),
                           purchase_date DATE,
                           status ENUM('NEW', 'DAMAGED', 'USED' , 'RETIRED')
);

CREATE TABLE EquipmentLoan (
                                id INT PRIMARY KEY AUTO_INCREMENT,
                                equipment_id INT,
                                user_id INT,
                                reserved_from DATETIME,
                                reserved_to DATETIME,
                                signature VARCHAR(255),
                                returned BOOLEAN,
                                expected_return_date DATETIME,
                                actual_return_date DATETIME,
                                checkout_by INT,
                                returned_to INT,
                                FOREIGN KEY (equipment_id) REFERENCES Equipment(id),
                                FOREIGN KEY (user_id) REFERENCES Users(id),
                                FOREIGN KEY (checkout_by) REFERENCES Users(id),
                                FOREIGN KEY (returned_to) REFERENCES Users(id)
);


CREATE TABLE EquipmentHistory (
                                  id INT PRIMARY KEY AUTO_INCREMENT,
                                  equipment_id INT,
                                  event_type ENUM('NEW', 'DAMAGED', 'USED' , 'RETIRED'),
                                  event_date DATETIME,
                                  related_loan_id INT,
                                  performed_by INT,
                                  FOREIGN KEY (equipment_id) REFERENCES Equipment(id),
                                  FOREIGN KEY (related_loan_id) REFERENCES EquipmentLoan(id),
                                  FOREIGN KEY (performed_by) REFERENCES Users(id)
);
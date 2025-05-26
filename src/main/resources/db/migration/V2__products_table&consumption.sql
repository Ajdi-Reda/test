CREATE TABLE chemical_products (
                                  id INT PRIMARY KEY AUTO_INCREMENT,
                                  name TEXT,
                                  nomenclature VARCHAR(255) UNIQUE,
                                  current_stock FLOAT,
                                  minimum_stock FLOAT,
                                  unit TEXT,
                                  expiration_date DATE,
                                  risk_category varchar(30),
                                  safety_data_sheet_uri TEXT
);

CREATE TABLE chemical_usage (
                               id INT PRIMARY KEY AUTO_INCREMENT,
                               product_id INT,
                               taken_by INT,
                               amount FLOAT,
                               date DATE,
                               purpose TEXT,
                               status ENUM('REQUESTED', 'FULFILLED', 'REJECTED') DEFAULT 'REQUESTED',
                               handled_by INT NULL,
                               handled_at TIMESTAMP NULL,
                               FOREIGN KEY (product_id) REFERENCES chemical_products(id),
                               FOREIGN KEY (taken_by) REFERENCES users(id),
                               FOREIGN KEY (handled_by) REFERENCES users(id)

);

CREATE TABLE consumption_history (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    product_id INT,
                                    taken_by INT,
                                    date TIMESTAMP NOT NULL,
                                    quantity FLOAT NOT NULL,
                                    purpose TEXT,
                                    status ENUM('REQUESTED', 'FULFILLED', 'REJECTED'),
                                    handled_by INT,
                                    handled_at TIMESTAMP,
                                    action_type TEXT NOT NULL,
                                    notes TEXT NOT NULL,
                                    FOREIGN KEY (product_id) REFERENCES chemical_products(id),
                                    FOREIGN KEY (taken_by) REFERENCES users(id),
                                    FOREIGN KEY (handled_by) REFERENCES users(id)
);

DELIMITER $$

CREATE TRIGGER trg_chemical_usage_insert
    AFTER INSERT ON chemical_usage
    FOR EACH ROW
BEGIN
    INSERT INTO consumption_history (
        product_id,
        taken_by,
        date,
        quantity,
        purpose,
        status,
        handled_by,
        handled_at,
        action_type,
        notes
    ) VALUES (
                 NEW.product_id,
                 NEW.taken_by,
                 NOW(),
                 NEW.amount,
                 NEW.purpose,
                 NEW.status,
                 NEW.handled_by,
                 NEW.handled_at,
                 'INSERT',
                 CONCAT('New usage request: ', NEW.purpose)
             );
END $$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER trg_chemical_usage_update
    AFTER UPDATE ON chemical_usage
    FOR EACH ROW
BEGIN
    INSERT INTO consumption_history (
        product_id,
        taken_by,
        date,
        quantity,
        purpose,
        status,
        handled_by,
        handled_at,
        action_type,
        notes
    ) VALUES (
                 NEW.product_id,
                 NEW.taken_by,
                 NOW(),
                 NEW.amount,
                 NEW.purpose,
                 NEW.status,
                 NEW.handled_by,
                 NEW.handled_at,
                 'UPDATE',
                 CONCAT('Updated usage. Old status: ', OLD.status, ', New status: ', NEW.status)
             );
END $$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER trg_chemical_usage_delete
    AFTER DELETE ON chemical_usage
    FOR EACH ROW
BEGIN
    INSERT INTO consumption_history (
        product_id,
        taken_by,
        date,
        quantity,
        purpose,
        status,
        handled_by,
        handled_at,
        action_type,
        notes
    ) VALUES (
                 OLD.product_id,
                 OLD.taken_by,
                 NOW(),
                 OLD.amount,
                 OLD.purpose,
                 OLD.status,
                 OLD.handled_by,
                 OLD.handled_at,
                 'DELETE',
                 'Deleted usage entry.'
             );
END $$

DELIMITER ;


CREATE TABLE chemical_products (
                                   id INT PRIMARY KEY AUTO_INCREMENT,
                                   name TEXT NOT NULL,
                                   nomenclature VARCHAR(255) UNIQUE NOT NULL,
                                   current_stock FLOAT NOT NULL,
                                   minimum_stock FLOAT NOT NULL,
                                   unit VARCHAR(100) NOT NULL,
                                   expiration_date DATE,
                                   risk_category VARCHAR(30),
                                   safety_data_sheet_uri TEXT
);

-- Chemical Usage Table
CREATE TABLE chemical_usage (
                                id INT PRIMARY KEY AUTO_INCREMENT,
                                session_id INT NOT NULL,
                                product_id INT NOT NULL,
                                taken_by INT NOT NULL,
                                amount FLOAT NOT NULL,
                                date DATE NOT NULL,
                                purpose TEXT,
                                status ENUM('REQUESTED', 'FULFILLED', 'REJECTED') DEFAULT 'REQUESTED',
                                handled_by INT NULL,
                                handled_at TIMESTAMP NULL,
                                FOREIGN KEY (session_id) REFERENCES lab_sessions(id),
                                FOREIGN KEY (product_id) REFERENCES chemical_products(id) ON DELETE CASCADE,
                                FOREIGN KEY (taken_by) REFERENCES users(id),
                                FOREIGN KEY (handled_by) REFERENCES users(id)
);

-- Consumption History Table
CREATE TABLE consumption_history (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     session_id INT NOT NULL,
                                     product_id INT NOT NULL,
                                     taken_by INT NOT NULL,
                                     date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     quantity FLOAT NOT NULL,
                                     purpose TEXT,
                                     status ENUM('REQUESTED', 'FULFILLED', 'REJECTED'),
                                     handled_by INT,
                                     handled_at TIMESTAMP,
                                     action_type ENUM('INSERT', 'UPDATE', 'DELETE') NOT NULL,
                                     notes TEXT NOT NULL,
                                     FOREIGN KEY (session_id) REFERENCES lab_sessions(id),
                                     FOREIGN KEY (product_id) REFERENCES chemical_products(id) ON DELETE CASCADE,
                                     FOREIGN KEY (taken_by) REFERENCES users(id),
                                     FOREIGN KEY (handled_by) REFERENCES users(id)
);

DELIMITER $$

CREATE TRIGGER trg_chemical_usage_insert
    AFTER INSERT ON chemical_usage
    FOR EACH ROW
BEGIN
    INSERT INTO consumption_history (
        session_id,
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
                 NEW.session_id,
                 NEW.product_id,
                 NEW.taken_by,
                 NOW(),
                 NEW.amount,
                 NEW.purpose,
                 NEW.status,
                 NEW.handled_by,
                 NEW.handled_at,
                 'INSERT',
                 CONCAT('New usage request: ', COALESCE(NEW.purpose, 'N/A'))
             );
END$$

CREATE TRIGGER trg_chemical_usage_update
    AFTER UPDATE ON chemical_usage
    FOR EACH ROW
BEGIN
    INSERT INTO consumption_history (
        session_id,
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
                 NEW.session_id,
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
END$$

CREATE TRIGGER trg_chemical_usage_delete
    AFTER DELETE ON chemical_usage
    FOR EACH ROW
BEGIN
    INSERT INTO consumption_history (
        session_id,
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
                 OLD.session_id,
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
END$$

DELIMITER ;
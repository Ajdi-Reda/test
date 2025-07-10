CREATE TABLE stock_alerts (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            item_id INT,
                            date DATE DEFAULT (CURRENT_DATE),
                            resolved BOOLEAN DEFAULT FALSE,
                            resolved_by INT,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (resolved_by) REFERENCES Users(id)
);

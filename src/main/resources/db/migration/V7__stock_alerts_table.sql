CREATE TABLE stock_alerts (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            item_id INT,
                            date DATE DEFAULT (CURRENT_DATE),
                            resolved BOOLEAN DEFAULT FALSE,
                            resolved_by INT,
                            FOREIGN KEY (resolved_by) REFERENCES Users(id)
);

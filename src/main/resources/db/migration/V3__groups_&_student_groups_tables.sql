CREATE TABLE `groups` (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          subject_id INT NOT NULL,
                          name VARCHAR(100) NOT NULL,
                          teacher_id int not null,
                          CONSTRAINT fk_subject FOREIGN KEY (subject_id) REFERENCES subjects(id),
                          foreign key(teacher_id) references users(id),
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE students_groups (
                                 id INT AUTO_INCREMENT PRIMARY KEY,
                                 group_id INT NOT NULL,
                                 user_id INT NOT NULL,
                                 CONSTRAINT fk_group FOREIGN KEY (group_id) REFERENCES `groups`(id),
                                 CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

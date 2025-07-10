create table invitation_token
(
    id    int primary key auto_increment,
    token varchar(255),
    user_id int,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key(user_id) references users(id)
)
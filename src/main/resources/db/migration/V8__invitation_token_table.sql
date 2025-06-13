create table invitation_token
(
    id    int primary key auto_increment,
    token varchar(255),
    user_id int,
    foreign key(user_id) references users(id)
)
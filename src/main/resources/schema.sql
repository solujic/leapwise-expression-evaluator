create table if not exists expressions
(
    id int primary key,
    expression varchar(max) not null,
    name varchar(350) not null
);
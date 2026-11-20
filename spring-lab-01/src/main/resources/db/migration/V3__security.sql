create table app_user (
    id bigserial primary key,
    username varchar(60) not null unique,
    password_hash varchar(100) not null,
    enabled boolean not null default true
);

create table user_role (
    user_id bigint not null references app_user (id),
    role varchar(20) not null,
    primary key (user_id, role)
);

insert into app_user (username, password_hash) values
('user', '$2a$10$6iApzSMPyQ/rM55ijPHH6.A7yZ9AqMWb3ZFXJuwzZF3PqSzV92YEe'),
('admin', '$2a$10$3237NOBqpPFx1CIiFAsl5.Df87M4b9HcqXKPQtvS8c348.Us94Xzq');

insert into user_role (user_id, role)
select id, 'USER' from app_user;

insert into user_role (user_id, role)
select id, 'ADMIN' from app_user where username = 'admin';
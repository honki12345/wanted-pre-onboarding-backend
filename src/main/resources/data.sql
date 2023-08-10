insert into member (id, email, pwd) values (999, 'admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi');
insert into member (id, email, pwd) values (1000, 'user', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC');

insert into authority (authority_name) values ('ROLE_USER');
insert into authority (authority_name) values ('ROLE_ADMIN');

insert into user_authority (user_id, authority_name) values (999, 'ROLE_USER');
insert into user_authority (user_id, authority_name) values (999, 'ROLE_ADMIN');
insert into user_authority (user_id, authority_name) values (1000, 'ROLE_USER');
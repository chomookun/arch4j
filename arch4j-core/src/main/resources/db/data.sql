-- authority
insert into `authority` (`id`,`system_required`,`name`) values ('ADMIN_USER','Y','UserEntity Management AuthorityEntity');

-- role
insert into `role` (`id`,`system_required`,`name`) values ('ADMIN','Y','Administrator Access RoleEntity');
insert into `role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_USER');
insert into `role` (`id`,`system_required`,`name`) values ('API','Y','Restful Api Access RoleEntity');
insert into `role` (`id`,`system_required`,`name`) values ('H2-CONSOLE','Y','H2 Console Access RoleEntity');
insert into `role` (`id`,`name`) values ('TEST','Test RoleEntity');

-- user
insert into `user` (`id`,`name`,`email`,`mobile`,`password`,`type`,`status`) values ('admin','Administrator','admin@oopscraft.org','010-1111-2222','{noop}admin','GENERAL','ACTIVE');
insert into `user_role` (`user_id`,`role_id`) values ('admin','ADMIN');
insert into `user_role` (`user_id`,`role_id`) values ('admin','API');
insert into `user_role` (`user_id`,`role_id`) values ('admin','H2-CONSOLE');
insert into `user` (`id`,`name`,`email`,`mobile`,`password`,`type`,`status`) values ('api','API Account','api@oopscraft.org','010-2222-3333','{noop}api','SYSTEM','ACTIVE');
insert into `user_role` (`user_id`,`role_id`) values ('api','API');
insert into `user` (`id`,`name`,`email`,`mobile`,`password`,`type`,`status`) values ('test','Test Account','test@oopscraft.org','010-3333-4444','{noop}api','GENERAL','ACTIVE');
insert into `user_role` (`user_id`,`role_id`) values ('test','TEST');

-- property
insert into `property` (`id`,`name`,`value`) values ('test1', 'test property 1','test_value_1');
insert into `property` (`id`,`name`,`value`) values ('test2', 'test property 2','test_value_2');
insert into `property` (`id`,`name`,`value`) values ('test3', 'test property 3','test_value_3');

-- code
insert into `code` (`id`,`name`,`note`) values ('test1','test code 1','test code 1');
insert into `code` (`id`,`name`,`note`) values ('test2','test code 2','test code 2');
insert into `code` (`id`,`name`,`note`) values ('test3','test code 3','test code 3');

-- sample
insert into `sample` (`id`, `name`, `type`) values ('sample001', 'sample 001', 'A');
insert into `sample_item` (`sample_id`, `id`, `name`) values ('sample001','item01','Item 01');
insert into `sample_item` (`sample_id`, `id`, `name`) values ('sample002','item02','Item 02');



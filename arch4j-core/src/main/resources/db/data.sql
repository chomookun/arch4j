-- authority
insert into `authority` (`id`,`system_required`,`name`) values ('ADMIN','Y','Admin Access Authority');
insert into `authority` (`id`,`system_required`,`name`) values ('ADMIN_USER','Y','User Access Authority');
insert into `authority` (`id`,`system_required`,`name`) values ('ADMIN_USER_EDIT','Y','User Edit Authority');
insert into `authority` (`id`,`system_required`,`name`) values ('ADMIN_ROLE','Y','Role Edit Authority');
insert into `authority` (`id`,`system_required`,`name`) values ('ADMIN_ROLE_EDIT','Y','Role Edit Authority');
insert into `authority` (`id`,`system_required`,`name`) values ('ADMIN_MESSAGE','Y','Message Access Authority');
insert into `authority` (`id`,`system_required`,`name`) values ('ADMIN_MESSAGE_EDIT','Y','Message Edit Authority');
insert into `authority` (`id`,`system_required`,`name`) values ('ADMIN_VARIABLE','Y','Variable Access Authority');
insert into `authority` (`id`,`system_required`,`name`) values ('ADMIN_VARIABLE_EDIT','Y','Variable Edit Authority');
insert into `authority` (`id`,`system_required`,`name`) values ('ADMIN_MENU','Y','Menu Access Authority');
insert into `authority` (`id`,`system_required`,`name`) values ('ADMIN_MENU_EDIT','Y','Message Access Authority');
insert into `authority` (`id`,`system_required`,`name`) values ('ADMIN_CODE','Y','Code Access Authority');
insert into `authority` (`id`,`system_required`,`name`) values ('ADMIN_CODE_EDIT','Y','Code Edit Authority');
insert into `authority` (`id`,`system_required`,`name`) values ('ADMIN_BOARD','Y','Board Access Authority');
insert into `authority` (`id`,`system_required`,`name`) values ('ADMIN_BOARD_EDIT','Y','Board Edit Authority');
insert into `authority` (`id`,`system_required`,`name`) values ('ACTUATOR','Y','Actuator Access Authority');
insert into `authority` (`id`,`system_required`,`name`) values ('H2-CONSOLE','Y','Actuator Access Authority');
insert into `authority` (`id`,`system_required`,`name`) values ('API','Y','Actuator Access Authority');

-- role
insert into `role` (`id`,`system_required`,`name`) values ('ADMIN','Y','Administrator Role');
insert into `role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN');
insert into `role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_USER');
insert into `role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_USER_EDIT');
insert into `role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_ROLE');
insert into `role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_ROLE_EDIT');
insert into `role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_MESSAGE');
insert into `role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_MESSAGE_EDIT');
insert into `role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_VARIABLE');
insert into `role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_VARIABLE_EDIT');
insert into `role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_MENU');
insert into `role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_MENU_EDIT');
insert into `role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_CODE');
insert into `role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_CODE_EDIT');
insert into `role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_BOARD');
insert into `role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ADMIN_BOARD_EDIT');
insert into `role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'ACTUATOR');
insert into `role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'H2-CONSOLE');
insert into `role_authority` (`role_id`,`authority_id`) values ('ADMIN', 'API');
insert into `role` (`id`,`system_required`,`name`) values ('DEV','Y','Developer Role');
insert into `role` (`id`,`system_required`,`name`) values ('TEST','Y','Tester Role');

-- user
insert into `user` (`id`,`name`,`email`,`mobile`,`password`,`type`,`status`) values ('admin','Administrator','admin@oopscraft.org','010-1111-2222','{noop}admin','GENERAL','ACTIVE');
insert into `user_role` (`user_id`,`role_id`) values ('admin','ADMIN');
insert into `user` (`id`,`name`,`email`,`mobile`,`password`,`type`,`status`) values ('dev','Developer Account','api@oopscraft.org','010-2222-3333','{noop}api','SYSTEM','ACTIVE');
insert into `user_role` (`user_id`,`role_id`) values ('dev','DEV');
insert into `user` (`id`,`name`,`email`,`mobile`,`password`,`type`,`status`) values ('test','Test Account','test@oopscraft.org','010-3333-4444','{noop}test','GENERAL','ACTIVE');
insert into `user_role` (`user_id`,`role_id`) values ('test','TEST');

-- variable
insert into `variable` (`id`,`name`,`value`) values ('test1', 'test property 1','test_value_1');
insert into `variable` (`id`,`name`,`value`) values ('test2', 'test property 2','test_value_2');
insert into `variable` (`id`,`name`,`value`) values ('test3', 'test property 3','test_value_3');

-- code
insert into `code` (`id`,`name`,`note`) values ('test1','test code 1','test code 1');
insert into `code` (`id`,`name`,`note`) values ('test2','test code 2','test code 2');
insert into `code` (`id`,`name`,`note`) values ('test3','test code 3','test code 3');

-- sample
insert into `sample` (`id`, `name`, `type`) values ('sample001', 'sample 001', 'A');
insert into `sample_item` (`sample_id`, `id`, `name`) values ('sample001','item01','Item 01');
insert into `sample_item` (`sample_id`, `id`, `name`) values ('sample002','item02','Item 02');

-- menu
insert into `menu` (`id`,`parent_id`,`name`) values ('menu1',null,'Menu 1');
insert into `menu` (`id`,`parent_id`,`name`) values ('menu2',null,'Menu 2');
insert into `menu` (`id`,`parent_id`,`name`) values ('menu1A','menu1','Menu 1-A');
insert into `menu` (`id`,`parent_id`,`name`) values ('menu2B','menu2','Menu 2-B');
insert into `menu` (`id`,`parent_id`,`name`) values ('menu2A','menu2','Menu 2-A');
insert into `menu` (`id`,`parent_id`,`name`) values ('menu3',null,'Menu 3');

-- board
insert into `board` (`id`,`name`,`page_size`,`reply_enabled`,`file_enabled`) values ('test1','Test1 Board',20,true,true);
insert into `board` (`id`,`name`,`page_size`,`reply_enabled`,`file_enabled`) values ('test2','Test2 Board',20,true,true);
insert into `board` (`id`,`name`,`page_size`,`reply_enabled`,`file_enabled`) values ('test3','Test3 Board',20,true,true);
insert into `board` (`id`,`name`,`page_size`,`reply_enabled`,`file_enabled`) values ('test4','Test4 Board',20,true,true);


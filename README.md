# ARCH4J (Application Archetype for Java) 

## Demo Site

[https://arch4j-web.oopscraft.org/admin](https://arch4j-web.oopscraft.org/admin)

user/password: admin/admin, dev/dev


## Create database

### Mysql
```sql
create database arch4j;
create user 'user'@'%' identified by 'password';
grant all privileges on arch4j.* to 'user'@'%';
```



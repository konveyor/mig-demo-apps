# New sample app for OADP

* I'll note most of this was lifted from:
https://github.com/sdil/learning/blob/master/go/todolist-mysql-go/todolist.go


## Local Setup

* Get mariadb running

```
docker/podman run -d -p 3306:3306 --name mariadb -e MYSQL_ROOT_PASSWORD=root mariadb
docker/podman exec -it mariadb mariadb -uroot -proot -e 'CREATE DATABASE todolist'

```

* Get the app running

```
go mod tidy
```
* Update the db for a local connection:
HERE: https://github.com/weshayutin/todolist-mariadb-go/blob/main/todolist.go#L44

```
var db, _ = gorm.Open("mysql", "root:root@tcp/todolist?charset=utf8&parseTime=True")

```

Execute:
```
go run todolist.go
```

Initial Page should have two entries, one complete and one incomplete.


Show items in the db:
```
Server version: 10.5.9-MariaDB MariaDB Server

Copyright (c) 2000, 2018, Oracle, MariaDB Corporation Ab and others.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

MariaDB [(none)]> show databases;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| mysql              |
| performance_schema |
| test               |
| todolist           |
+--------------------+
5 rows in set (0.001 sec)

MariaDB [(none)]> use todolist;
Database changed
MariaDB [todolist]> show tables;
+--------------------+
| Tables_in_todolist |
+--------------------+
| todo_item_models   |
+--------------------+
1 row in set (0.001 sec)

MariaDB [todolist]> select * from todo_item_models;
+----+-------------------------+-----------+
| id | description             | completed |
+----+-------------------------+-----------+
|  1 | time to make the donuts |         0 |
|  2 | prepopulate the db      |         1 |
+----+-------------------------+-----------+
2 rows in set (0.000 sec)

MariaDB [todolist]>
```

![gnome-shell-screenshot-edww3e](https://user-images.githubusercontent.com/138787/160934609-a77798a1-3986-46a0-a334-a8b53ceccb7d.png)



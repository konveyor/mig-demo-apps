# New sample app for OADP for MongoDB

* I'll note most of this was lifted from:
https://github.com/sdil/learning/blob/master/go/todolist-mysql-go/todolist.go


## Local Setup

* Get mongo running

```
docker-compose up -d --build
```

* Get the app running

```
go mod tidy
```
* Update the db for a local connection:
HERE: https://github.com/weshayutin/todolist-mongo-go/blob/master/todolist.go#L44-L48


Execute:
```
go run todolist.go
```

Initial Page should have two entries, one complete and one incomplete.


Show items in the db:  http://localhost:8081/db/todolist/

![gnome-shell-screenshot-83uili](https://user-images.githubusercontent.com/138787/164760526-0585899c-b5f8-41a2-91c8-ea78e740e670.png)


![gnome-shell-screenshot-6ycmy9](https://user-images.githubusercontent.com/138787/164760586-72b7b0b9-47f1-4510-8308-b363f10ca8a6.png)

## Notes:
* https://redhat-scholars.github.io/openshift-starter-guides/rhs-openshift-starter-guides/4.7/nationalparks-java-codechanges-github.html#webhooks_with_openshift
*

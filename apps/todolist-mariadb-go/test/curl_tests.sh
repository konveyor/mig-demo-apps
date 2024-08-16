FQDN=localhost
PORT=8000
#FQDN=todolist-route-mysql-persistent.apps.wdharm41607261.migration.redhat.com
#PORT=80
export date=`date "+%F-%T"`
curl -d "description=curl_todo_1_$date&completed=false" -X POST http://$FQDN:$PORT/todo
curl -d "description=curl_todo_2_$date&completed=false" -X POST http://$FQDN:$PORT/todo
curl -d "description=curl_todo_3_$date&completed=false" -X POST http://$FQDN:$PORT/todo
curl -d "id=1&completed=true" -X POST http://$FQDN:$PORT/todo/1

curl  http://$FQDN:$PORT/log



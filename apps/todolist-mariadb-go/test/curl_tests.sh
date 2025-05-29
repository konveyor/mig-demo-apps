#FQDN=localhost
#PORT=8000
FQDN=todolist-route-mysql-persistent.wdh415-vpc-cluster-3626522b15eedb880d7b99992e225c1b-0000.us-south.containers.appdomain.cloud
PORT=80
date=$(date "+%F-%T")
curl -d "description=curl_todo_1_$date&completed=false" -X POST "http://${FQDN}:${PORT}/todo"
curl -d "description=curl_todo_2_$date&completed=false" -X POST "http://${FQDN}:${PORT}/todo"
curl -d "description=curl_todo_3_$date&completed=false" -X POST "http://${FQDN}:${PORT}/todo"
curl -d "id=1&completed=true" -X POST "http://${FQDN}:${PORT}/todo/1"

# if using two volumes, this should pass
echo -e "\n* The following should only pass if using two volume template"
curl  "http://${FQDN}:${PORT}/log"



# Customers Service

This service is a REST API that returns customer information.  
A customer is a tuple of (id, username, name, surname, address, zipCode, city, country).  
All responses are in JSON format.  
There are 2 endpoints:
- `/customers` returns a list of all the customers (upto pagination limit)
- `/customers/{id}` returns the info of the customer with that id.

## Prerequisites

- A container runtime like Docker or Podman

## Usage

1. First build the `.war` file
    ```
    $ ./mvnw clean package
    ```

1. Create a docker network so the 2 containers can talk to each other.
    ```
    $ docker network create my-net-1
    $ docker network ls
    $ docker network inspect my-net-1
    ```

1. Then start the database server
    ```
    $ docker run --name=mydb --rm -it --network=my-net-1 -p 5432:5432 -e POSTGRES_PASSWORD=mypass1234 postgres
    ```

1. Then start the API server
    ```
    $ docker run --rm -it \
        --network=my-net-1 \
        -p 8080:8080 \
        -v "${PWD}/target/customers-tomcat-0.0.1-SNAPSHOT.war:/opt/jboss/wildfly/standalone/deployments/ROOT.war" \
        jboss/wildfly
    ```

1. Browse to http://localhost:8080/customers

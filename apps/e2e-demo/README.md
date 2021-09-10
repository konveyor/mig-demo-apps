# Konveyor End to End demo application

This repository contains the different components used to showcase the different Konveyor projects on an end to end demo. This demo is still being built by the Konveyor Community, so this repository shouldn't be considered stable and its contents should be used at your own risk.

## Architecture

The demo includes the following services:

- **Customers**: The original Retail application from which the rest of microservices have been carved out. It still retains the business logic related to customer management. This legacy application runs on Tomcat and uses an Oracle database.
- **Inventory**: Stores detailed information about products. Developed using Quarkus and PostgreSQL as data store. This service has been configured with the the JDK build mode for Quarkus by default.
- **Orders**: Manages all order related entities. It stores only UIDs to refer to Products and Customers. Implemented with Spring Boot and using a PostgreSQL database.
- **Gateway**: Access and aggregation layer for the whole application. It gets orders data and aggregates Products and Customers detailed information. Also implemented with the Spring Boot/PostgreSQL stack.
- **Frontend**: A new front end layer developed with the React flavor of Patternfly, published on Nginx.

![Architecture Screenshot](docs/images/architecture.png?raw=true "Architecture Diagram")

It can be argued that the domain is too fine grained for the modeled business, or that the approach is not optimal for data aggregation. While these statements might be true, the focus on the demo was to present a simple case with microservices interacting with each other, and shouldn't be considered a design aimed for a production solution.

## Application Configuration Model

Both application and deployment configuration for each component have been modeled using Helm Charts. Each component directory contains a **helm** subdirectory in which the chart is available. All deployment configuration is included in the **values.yaml** available in the root of the **helm** directory. Application configuration is available as application.yaml files available in both the **config** and **secret** subdirectories within the Helm Chart. Non sensitive parameters should be included in the file inside the **config** subdirectory. Sensitive data such as passwords should be included in the file available in the **secret** subdirectory.

This chart will create a ConfigMap containing the application.yaml file inside the **config** subdirectory to be consumed by the applications via the Kubernetes API. Along with that, a Secret containing the application.yaml file inside the **secret** subdirectory will be created as well, and mounted in the pod as a volume for the component to access the configuration file directly.

> **Note**: The configuration files packaged in this repository are tied up to the laboratory infrastructure in which the demo is being deployed. This might get corrected in the future to provide generic values that allows deployment on any Kubernetes environment. Furthermore, the configuration model used here shouldn't be considered as suitable for production and only aims to simplify deployment on the aforementioned lab environment.


### Granting permission for applications to access the Kubernetes API

All application components require access to the Kubernetes API in order to access the ConfigMap objects to obtain their configuration at startup. To do this, simply add the view role to the default service account:

```
kubectl create rolebinding default-view --clusterrole=view --serviceaccount=<NAMESPACE>:default --namespace=<NAMESPACE>
```

## Running Locally

Applications can also run locally for testing purposes. In this case, the command to be used varies between Spring Boot and Quarkus. In the first case, the command is as follows:

```
mvn clean spring-boot:run -P local
```

For Quarkus services, the command is the following:

```
./mvnw compile quarkus:dev -P local
```

## Known issues

### SQLFeatureNotSupportedException exception at startup

The following exception is displayed at startup for the Orders service:

```
java.sql.SQLFeatureNotSupportedException: Method org.postgresql.jdbc.PgConnection.createClob() is not yet implemented.
```

This is caused by [an issue in Hibernate that has been fixed in version 5.4.x](https://hibernate.atlassian.net/browse/HHH-12368). Since the Hibernate version used for the Orders service is 5.3.14, a warning is displayed including the full stack trace for this exception. Although annoying, this warning is harmless for this example and can be ignored.

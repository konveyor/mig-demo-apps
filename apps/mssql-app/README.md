# MsSQL App

SQL Server with an ASP.NET web app.

## Installation

Login to your OpenShift cluster. 

To deploy:

```bash
./deploy.sh
```

To remove:

```bash
./destroy.sh
```

## Usage

After successful installation, the app should be exposed at a public URL. Wait for 4-5 minutes for all the services to start.

To get the url to the frontend:

```bash
oc get route -n mssql-persistent mssql-app-route -o go-template='{{ .spec.host }}{{ println }}'
```

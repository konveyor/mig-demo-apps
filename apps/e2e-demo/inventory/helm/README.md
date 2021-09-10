# Inventory Deploy with PostgreSQL

The inventory service is deployed with it's own PostgreSQL database using helm dependencies. It is currently using this **bitnami** helm chart for [PostgreSQL](https://bitnami.com/stack/postgresql/helm).

## Deploy the Inventory Service

The following steps should be performed after logging into your kubernetes cluster.

1. Create a namespace for the inventory service

    ```bash
    kubectl create namespace inventory
    ```

1. [Optional] set the namespace as the default for your session:

    ```bash
    kubectl config set-context --current --namespace=inventory
    ```

1. Add the `bitnami` helm repo:

    ```bash
    helm repo add bitnami https://charts.bitnami.com/bitnami
    helm repo update
    ```

1. Deploy the inventory service with it's database using the following `helm` command:

    ```bash
    cd helm-h2/inventory
    helm upgrade inventory ./java-backend/ -f values.yaml --install -n inventory
    ```

## Check the installation

The easiest way to check the installation is to forward the port on the inventory service and call it with `curl`

- In one shell session issue this command:

    ```bash
    kubectl port-forward svc/inventory 8080:8080
    ```

- In a second shell type:

    ```bash
    curl 127.0.0.1:8080/products
    ```

Your should get back a `json` payload that is an array of products.

## Accessing the PostgreSQL Service

These instructions are how to access the PostgreSQL that is deployed via `helm` as a dependency of the inventory microservice using
bitnami helm charts.

PostgreSQL can be accessed via port 5432 on the following DNS names from within your cluster:

```bash
inventory-postgresql.inventory.svc.cluster.local - Read/Write connection
```

To get the password for "postgres" run:

```bash
export POSTGRES_PASSWORD=$(kubectl get secret inventory-postgresql -o jsonpath="{.data.postgresql-password}" | base64 --decode)
```

To connect to your database run the following command:

```bash
kubectl run inventory-postgresql-client --rm --tty -i --restart='Never' --image docker.io/bitnami/postgresql:11.11.0-debian-10-r16 --env="PGPASSWORD=$POSTGRES_PASSWORD" --command -- psql --host inventory-postgresql -U postgres -d postgres -p 5432
```

To connect to your database from outside the cluster execute the following commands:

```bash
kubectl port-forward svc/inventory-postgresql 5432:5432 &
PGPASSWORD="$POSTGRES_PASSWORD" psql --host 127.0.0.1 -U postgres -d postgres -p 5432
```

# Demo Apps

This is a collection of demo apps you can deploy on your OpenShift cluster. These apps are intended to demonstrate and test migrations.

Find all apps in `./apps/` directory.

Follow README.md of each app for installation instructions.

## Konveyor End to End Demo App

The Retail application used to demonstrate all Konveyor projects in an end to end demo.

Find README here : [./apps/e2e-demo/README.md](./apps/e2e-demo/README.md)

Credits : [https://github.com/rromannissen/rhoar-microservices-demo](https://github.com/rromannissen/rhoar-microservices-demo)

## Sock Shop App

An e-commerce website that sells socks.

Find README here : [./apps/sock-shop/README.md](./apps/sock-shop/README.md)

Credits : [https://github.com/microservices-demo/microservices-demo](https://github.com/microservices-demo/microservices-demo)

## Robot Shop App

An e-commerce website that sells robots.

Find README here : [./apps/robot-shop/README.md](./apps/robot-shop/README.md)

Credits : [https://github.com/instana/robot-shop](https://github.com/instana/robot-shop)

## Parks App

A website that shows popular Historic Sites and National Parks on a map.

Find README here : [./apps/parks-app/README.md](./apps/parks-app/README.md)

Credits : [https://github.com/OpenShiftDemos/restify-mongodb-parks](https://github.com/OpenShiftDemos/restify-mongodb-parks)

## MsSQL App

MsSQL server with a .NET frontend app.

Find README here : [./apps/mssql-app/README.md](./apps/mssql-app/README.md)

Credits : [https://github.com/microsoft/sqllinuxlabs](https://github.com/microsoft/sqllinuxlabs)

## File Uploader App

A PHP application to upload arbitrary files.

Find README here : [./apps/file-uploader/README.md](./apps/file-uploader/README.md)

Credits : [https://github.com/christianh814/openshift-php-upload-demo](https://github.com/christianh814/openshift-php-upload-demo)

## Rocket Chat

A Node.js chat application.

Find README here : [./apps/rocket-chat/README.md](./apps/rocket-chat/README.md)

Credits : [https://github.com/RocketChat/Rocket.Chat](https://github.com/RocketChat/Rocket.Chat)

License : [./apps/rocket-chat/LICENSE](./apps/rocket-chat/LICENSE)

## AMQ app

A sample 3 namespace application, which consists from AMQ consumer, producer and broker, all located in separate namespaces. Communication is provided by internal service network.

Find README here : [./apps/amq-app/README.md](./apps/amq-app/README.md)

## Mediawiki

Useful for demonstrating hook usage

Find README here : [./apps/mediawiki/README.md](./apps/mediawiki/README.md)

# Demo infrastructure
These configurations are intended to provide basic configuration examples of storage and other infrastructure for use with MTC.

## MCG / Noobaa

A sample MCG deployment that can be used as an S3 backing store for MTC migrations

Find README here : [./infra/mcg/README.md](./infra/mcg/README.md)

## Minio

A sample minio deployment that can be used as an S3 backing store for MTC migrations.

While we recommend to use MCG/NooBaa for on-premise Object Storage needs, we are including other possible Object Storage for demo/testing purposes.

Find README here : [./infra/minio/README.md](./infra/minio/README.md)

## Code of Conduct
Refer to Konveyor's Code of Conduct [here](https://github.com/konveyor/community/blob/main/CODE_OF_CONDUCT.md).

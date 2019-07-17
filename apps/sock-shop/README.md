# Sock Shop

An e-commerce website that sells socks.

## Installation

Login to your openshift cluster. 

To deploy:

```bash
./deploy.sh
```

To remove:

```bash
./destroy.sh
```

`manifest.yml` is a collection of resources in `manifests` directory. 

You may edit individual YAMLs for different app resources. To create an updated `manifest.yml` after updating resource definitions :

```bash
./build.sh
```

This will combine all YAMLs and create an updated `manifest.yaml`.

## Usage

After successful installation, the app should be exposed at a public facing URL.

To get the url to the frontend:

```bash
oc get route -n sock-shop front-end -o go-template='{{ .spec.host }}{{ println }}'
```

Visit the URL and order socks for free. Deliveries not guaranteed! 





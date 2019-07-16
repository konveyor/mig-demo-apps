# Sock Shop

An e-commerce website that sells socks.

## Installation

Login to your openshift cluster. 

To install:

```bash
./deploy.sh
```

To uninstall:

```bash
./destroy.sh
```

## Usage

After successful installation, the app should be exposed at a public facing URL.

To get the url to the frontend:

```bash
oc get route -n sock-shop front-end -o go-template='{{ .spec.host }}{{ println }}'
```

Feel free to order socks. Deliveries not guaranteed! 





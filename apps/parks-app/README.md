# Parks App

A simple app to visualize locations of popular Historic Sites and National Parks.

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
oc get route -n parks-app restify -o go-template='{{ .spec.host }}{{ println }}'
```

## Development

`manifests` directory contains definitions for different resources required by the app. `manifest.yaml` is just a collection of all those definitions.

You may edit individual definitions for different app resources. To combine all YAMLs and create an updated `manifest.yaml`:

```bash
./build.sh
```

If you read everything carefully, here's a cookie for you 🍪

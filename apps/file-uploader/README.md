# File Uploader App

A simple PHP file upload application.

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

`manifests` directory contains definitions for different resources required by the app. `manifest.yaml` is just a collection of all those definitions. 

You may edit individual definitions for different app resources. To create an updated `manifest.yml` after updating individual resource definitions :

```bash
./build.sh
```

This will combine all YAMLs and create an updated `manifest.yaml`.

## Usage

After successful installation, the app should be exposed at a facing URL. Wait for 4-5 minutes for all the services to start.

To get the url to the frontend:

```bash
oc get route -n file-uploader file-uploader -o go-template='{{ .spec.host }}{{ println }}'
```

Visit the URL and order robots for free. These robots are terrible. Don't use them to invade the world!





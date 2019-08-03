# Container images for Robot Shop

These are custom built images for Robot Shop app which do not require privileged scc on OpenShift clusters.

## About images

### mysql

Based on OpenShift MySQL image, this image comes with sample data for Robot Shop baked in.

### nginx

Based on Bitnami nginx, this image is a reverse proxy for all services running in Robot Shop app. 

### ratings

This is modified image with Apache2 server running on 8080 port.

## Building images

Each image has it's own Makefile.

Before building the image, please set `IMG` environment variable. 

Enter the directory of image you want to build :

```bash
cd <image_to_build>
```

To build :

```bash
IMG=<image_name>
make container
```

To push to Docker registry : 

```bash
IMG=<image_name>
make push
```

To run the image locally : 

```bash
IMG=<image_name>
make run
```





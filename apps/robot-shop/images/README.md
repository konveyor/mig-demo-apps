# Container images for Robot Shop

These are custom built images for Robot Shop app which do not require privileged scc on OpenShift clusters.

## About images

### mysql

Based on OpenShift MySQL image, this image comes with sample data for Robot Shop baked in.

### nginx

Based on Bitnami nginx, this image is a reverse proxy for all services running in Robot Shop app. 

### ratings

This is a collection of two images which run the ratings service on Robot Shop. 

#### ratings [php]

Based on Bitnami php-fpm, this image serves the PHP service for ratings.

#### ratings [nginx]

Based on Bitnami nginx, this image is a reverse proxy for PHP fpm backend.

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





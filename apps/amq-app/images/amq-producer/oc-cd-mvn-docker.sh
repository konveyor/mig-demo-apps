#!/bin/sh

OCP_REGISTRY=quay.io
OCP_IMAGE_GROUP=dgrigore
OCP_IMAGE_NAME=bm-amq-producer
OCP_IMAGE_VERSION=stable

set -eu

# Building Phase
echo [MAVEN] Maven Package
mvn clean install
echo [DOCKER] Build Image
docker build -t $OCP_IMAGE_GROUP/$OCP_IMAGE_NAME:$OCP_IMAGE_VERSION target/docker
echo [DOCKER] Tag Image
docker tag $OCP_IMAGE_GROUP/$OCP_IMAGE_NAME:$OCP_IMAGE_VERSION $OCP_REGISTRY/$OCP_IMAGE_GROUP/$OCP_IMAGE_NAME:$OCP_IMAGE_VERSION
echo [DOCKER] Push into Registry $OCP_REGISTRY
docker push $OCP_REGISTRY/$OCP_IMAGE_GROUP/$OCP_IMAGE_NAME:$OCP_IMAGE_VERSION
echo Cleaning
mvn clean

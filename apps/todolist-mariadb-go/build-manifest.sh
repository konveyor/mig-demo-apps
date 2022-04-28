#! /bin/bash

if [[ -z $MANIFEST ]];
then
    echo "error: no manifest name provided"
    exit 1
fi

if [[ -z $PLATFORM_LIST ]];
then
    PLATFORM_LIST="linux/amd64"
fi

IMAGE_LIST=""

for PLATFORM in $(echo $PLATFORM_LIST | tr "," " ");
do
    echo "Building container image for platform: $PLATFORM"
    ARCH="${PLATFORM#*/}"
    IMAGE=$MANIFEST-$ARCH
    docker build --push --platform=$PLATFORM -t $IMAGE .
    IMAGE_LIST="$IMAGE_LIST $IMAGE"
done

echo "Creating manifest $MANIFEST from images: $IMAGE_LIST"
docker manifest create -a $MANIFEST $IMAGE_LIST
docker manifest push $MANIFEST

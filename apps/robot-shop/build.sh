#!/bin/bash

rm manifest.yaml
for f in manifests/*.yaml; do (cat "${f}"; echo) >> manifest.yaml; done

if [ "$1" == "4" ]; then
  sed -i "s/v1beta1/v1/g" manifest.yaml
fi

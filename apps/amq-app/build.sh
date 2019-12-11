#!/bin/bash

rm -f manifest.yaml
for f in manifests/*.yml; do (cat "${f}"; echo) >> manifest.yaml; done

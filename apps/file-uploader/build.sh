#!/bin/bash

rm manifest.yaml
for f in manifests/*.yaml; do (cat "${f}"; echo) >> manifest.yaml; done

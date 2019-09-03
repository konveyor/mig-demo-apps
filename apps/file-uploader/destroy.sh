#!/bin/bash

oc delete -f manifest.yaml
oc delete project file-uploader

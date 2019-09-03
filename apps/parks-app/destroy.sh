#!/bin/bash

oc delete project parks-app
oc delete -f manifest.yaml

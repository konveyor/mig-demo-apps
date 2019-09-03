#!/bin/bash

oc delete project sock-shop
oc delete -f manifest.yaml

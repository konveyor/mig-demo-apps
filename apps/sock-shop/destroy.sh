#!/bin/bash

oc delete -f manifest.yaml
oc delete project sock-shop

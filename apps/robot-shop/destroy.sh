#!/bin/bash

oc delete project robot-shop
oc delete -f manifest.yaml

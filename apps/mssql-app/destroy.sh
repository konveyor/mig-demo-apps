#!/bin/bash

oc delete project mssql-example
oc delete -f manifest.yaml

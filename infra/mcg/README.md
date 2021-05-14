# Usage
1. Install OCS Operator
1. `oc create -f mcg.yml`
1. Wait for the noobaa-core and noobaa-db instance to launch in the openshift-storage namespace.
1. Create the resources in this directory

* A bucket names `migstorage` will be created automatically.
* An s3 endpoint route will be created automatically in the openshift-storage namespace.
* The credentials are randomly generated and stored base64 encoded on the migstorage secret in the openshift-storage namespace.

CORE_NS=amq-core

OCP_IMAGE_GROUP=dgrigore
OCP_IMAGE_VERSION=stable
OCP_REGISTRY=quay.io

CONFIGMAP_DIR_PATH=./configmap
CONFIGMAP_NAME=amq-broker-ssl-configuration-pvol
OCP_IMAGE_NAME=amq-configmap


# Create resources
oc create -f manifest.yaml


echo Create Secret
oc create secret generic broker-secret-volume --from-file=./certs/amq-broker.jks -n $CORE_NS
echo Add SA to Secret
oc secrets add sa/amq-service-account secret/broker-secret-volume -n $CORE_NS
echo View Policy
oc policy add-role-to-user view system:serviceaccount:$CORE_NS:amq-service-account
oc policy add-role-to-user view system:serviceaccount:$CORE_NS:default


echo "Generating Openshift ConfigMap from ${CONFIGMAP_DIR_PATH} with name ${CONFIGMAP_NAME} in project ${CORE_NS}"
oc create configmap ${CONFIGMAP_NAME} --from-file=${CONFIGMAP_DIR_PATH} -o yaml -n $CORE_NS


# Broker
oc new-app --template=amq63-persistent-ssl-configmap -n $CORE_NS\
    -p APPLICATION_NAME=amq-broker-ssl \
    -p MQ_USERNAME=admin \
    -p MQ_PASSWORD="ENC(LNuJQlFpAm1yUaiRidJgJg==)" \
    -p MQ_PASSWORD_PLAIN=p4ss \
		-p ACTIVEMQ_ENCRYPTION_PASSWORD=p4ss5Or1 \
    -p AMQ_SECRET=broker-secret-volume \
    -p AMQ_TRUSTSTORE_PASSWORD=password \
    -p AMQ_KEYSTORE_PASSWORD=password \
    -p VOLUME_CAPACITY=3Gi \
    -p REGISTRY_IMAGE_VERSION=$OCP_IMAGE_VERSION \
    -p REGISTRY_IMAGE=$OCP_REGISTRY/$OCP_IMAGE_GROUP/$OCP_IMAGE_NAME


echo "Create Route MQTT"
oc create route passthrough amq-broker-mqtt-ssl --service=amq-broker-ssl-amq-mqtt-ssl -n $CORE_NS
echo "Create Route TCP (OpenWire)"
oc create route passthrough amq-broker-tcp-ssl --service=amq-broker-ssl-amq-tcp-ssl -n $CORE_NS

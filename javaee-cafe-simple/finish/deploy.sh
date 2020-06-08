#!/bin/sh

export APPLICATION_IMAGE=${1}
NAMESPACE=open-liberty-demo

oc new-project ${NAMESPACE}

envsubst < openlibertyapplication-openshift.yaml | oc apply -n ${NAMESPACE} -f -
echo "The application is succesfully deployed to project ${NAMESPACE}!"

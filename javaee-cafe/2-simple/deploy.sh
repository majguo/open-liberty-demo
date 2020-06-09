#!/bin/sh

export APPLICATION_IMAGE=${1}
export NAMESPACE=open-liberty-demo

oc new-project ${NAMESPACE}

envsubst < openlibertyapplication.yaml | oc apply -f -
echo "The application is succesfully deployed to project ${NAMESPACE}!"

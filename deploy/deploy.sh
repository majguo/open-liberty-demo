#!/bin/sh

applicationImage=$1
elasticCloudId=$2
elasticCloudUsername=$3
elasticCloudPassword=$4

export APPLICATION_IMAGE=${applicationImage}
export ELASTIC_CLOUD_ID=${elasticCloudId}
export ELASTIC_CLOUD_AUTH=${elasticCloudUsername}:${elasticCloudPassword}
NAMESPACE=open-liberty-demo

oc new-project ${NAMESPACE}
oc create serviceaccount -n ${NAMESPACE} privileged-account
oc adm policy add-scc-to-user privileged -n ${NAMESPACE} -z privileged-account
export SERVICE_ACCOUNT_NAME=privileged-account

envsubst < deployment/elastic-cloud-secret.yaml | oc apply -n ${NAMESPACE} -f -
oc apply -n ${NAMESPACE} -f deployment/filebeat-elastic-hosted.yaml
envsubst < deployment/openlibertyapplication.yaml | oc apply -n ${NAMESPACE} -f -
echo "The application is succesfully deployed to project ${NAMESPACE}!"

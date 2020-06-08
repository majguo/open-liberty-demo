#!/bin/sh

export APPLICATION_IMAGE=${1}
export ELASTIC_CLOUD_ID=${2}
export ELASTIC_CLOUD_AUTH=${3}:${4}
export KEYSTORE_NAME=${5}
export KEYSTORE_PASSWORD=${6}
export POSTGRESQL_SERVER_NAME=${7}
export POSTGRESQL_USER=${8}
export POSTGRESQL_PASSWORD=${9}
export TENANT_ID=${10}
export CLIENT_ID=${11}
export CLIENT_SECRET=${12}

NAMESPACE=open-liberty-demo
kubectl create namespace ${NAMESPACE}

envsubst < deploy/elastic-cloud-secret.yaml | kubectl apply -n ${NAMESPACE} -f -
kubectl apply -n ${NAMESPACE} -f deploy/filebeat-elastic-hosted.yaml
envsubst < deploy/keystore.yaml | kubectl apply -n ${NAMESPACE} -f -
envsubst < deploy/postgresql-secret.yaml | kubectl apply -n ${NAMESPACE} -f -
envsubst < deploy/aad-oidc-secret.yaml | kubectl apply -n ${NAMESPACE} -f -
envsubst < deploy/openlibertyapplication-k8s.yaml | kubectl apply -n ${NAMESPACE} -f -
echo "The application is succesfully deployed to project ${NAMESPACE}!"

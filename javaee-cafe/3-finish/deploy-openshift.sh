#!/bin/sh

export APPLICATION_IMAGE=${1}
export ELASTIC_CLOUD_ID=${2}
export ELASTIC_CLOUD_AUTH=${3}:${4}
CA_CRT_NAME=${5}
CA_KEY_NAME=${6}
export POSTGRESQL_SERVER_NAME=${7}
export POSTGRESQL_USER=${8}
export POSTGRESQL_PASSWORD=${9}
export TENANT_ID=${10}
export CLIENT_ID=${11}
export CLIENT_SECRET=${12}
NAMESPACE=open-liberty-demo

oc new-project ${NAMESPACE}
oc create serviceaccount -n ${NAMESPACE} privileged-account
oc adm policy add-scc-to-user privileged -n ${NAMESPACE} -z privileged-account
export SERVICE_ACCOUNT_NAME=privileged-account

# create secret including tls.crt & tls.key
# note:
# - namespace "cert-manager" must be created before when installing cert-manager operator
# - "CA_CRT_NAME" for CA certificate & "CA_KEY_NAME" for CA private key must be generated ahead of time,
#   e.g., "openssl req -x509 -sha256 -nodes -days 365 -newkey rsa:2048 -keyout ca.key -out ca.crt"
oc create secret generic ca-secret --from-file=tls.crt=./deploy/${CA_CRT_NAME} --from-file=tls.key=./deploy/${CA_KEY_NAME} -n cert-manager
export CA_SECRET_NAME=ca-secret
export CLUSTER_ISSUER_NAME=openliberty-demo-ca

envsubst < deploy/ca-clusterissuer.yaml | oc apply -n ${NAMESPACE} -f -
envsubst < deploy/elastic-cloud-secret.yaml | oc apply -n ${NAMESPACE} -f -
oc apply -n ${NAMESPACE} -f deploy/filebeat-elastic-hosted.yaml
envsubst < deploy/postgresql-secret.yaml | oc apply -n ${NAMESPACE} -f -
envsubst < deploy/aad-oidc-secret.yaml | oc apply -n ${NAMESPACE} -f -
envsubst < deploy/openlibertyapplication-openshift.yaml | oc apply -n ${NAMESPACE} -f -
echo "The application is succesfully deployed to project ${NAMESPACE}!"

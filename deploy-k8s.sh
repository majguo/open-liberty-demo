#!/bin/sh

# Environment variables
export CONTAINER_REGISTRY=${1}
export CLIENT_ID=${2}
export CLIENT_SECRET=${3}
export TENANT_ID=${4}
export ADMIN_GROUP_ID=${5}
export DB_SERVER_NAME=${6}
export DB_PORT_NUMBER=${7}
export DB_NAME=${8}
export DB_USER=${9}
export DB_PASSWORD=${10}
export ELASTIC_CLOUD_ID=${11}
export ELASTIC_CLOUD_AUTH=${12}

# Create Namespace "open-liberty-demo"
NAMESPACE=open-liberty-demo
kubectl create namespace ${NAMESPACE}

# Create Secret "aad-oidc-secret"
envsubst < deploy/aad-oidc-secret.yaml | kubectl create -f -

# Create Secret "db-secret-postgres"
envsubst < deploy/db-secret.yaml | kubectl create -f -

# Create ConfigMap "filebeat-config"
kubectl create -f deploy/filebeat-config.yaml

# Create Secret "elastic-cloud-secret"
envsubst < deploy/elastic-cloud-secret.yaml | kubectl create -f -

# Create Deployment & Service instances which connects to hosted elasticsearch service 
envsubst < deploy/k8s-hosted-elasticsearch.yaml | kubectl create -f -

echo "The application is succesfully deployed to project ${NAMESPACE}!"

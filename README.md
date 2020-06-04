# Deploy Open Liberty Application on Azure Red Hat OpenShift
- Introduction for Open Liberty
- Introduction for Azure Red Hat OpenShift
- This guide is for how to deploy and run Open Liberty application on ARO

## Prerequisites
- Install JDK per your needs (e.g., [AdoptOpenJDK OpenJDK 8 LTS/HotSpot](https://adoptopenjdk.net))
- Install [Maven](https://maven.apache.org/download.cgi)
- Install [Docker](https://docs.docker.com/get-docker/) for your OS
- Install [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli?view=azure-cli-latest)
- Register an Azure subscription. If you don't have one, you can get one for free for one year [here](https://azure.microsoft.com/en-us/free)

## Set up Azure Red Hat OpenShift cluster
- [Create an Azure Red Hat OpenShift 4 cluste](https://docs.microsoft.com/en-us/azure/openshift/tutorial-create-cluster)
- [Connect to an Azure Red Hat OpenShift 4 cluster](https://docs.microsoft.com/en-us/azure/openshift/tutorial-connect-cluster)
- Install Open Liberty Operator

## Prepare your Open Liberty application
- Start with [MicroProfile Starter](https://start.microprofile.io/)
- Setup [server configuration](https://openliberty.io/docs/ref/config/)
- Build application image with [Open Liberty container images](https://github.com/OpenLiberty/ci.docker)

## Deploy application on ARO
- OpenLibertyApplication yaml
- Deploy from GUI
- Deploy from CLI

## Integrate with Azure services
### Enable Signle-Sign-On with Azure Active Directory OpenID Connect

### Persist data with Azure Database for PostgreSQL

### Ship application log to managed Elasticsearch service on Azure

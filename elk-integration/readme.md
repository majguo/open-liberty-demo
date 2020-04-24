# Integrate Elastic Stack for Open Liberty Application

## How to run on Azure Red Hat OpenShift (ARO) 4.3 cluster
### Prerequisites
- Register an [Azure subscription](https://azure.microsoft.com/en-us/)
- Install [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli?view=azure-cli-latest)
- Follow this [guide](https://docs.microsoft.com/en-us/azure/openshift/howto-using-azure-redhat-openshift) to create an ARO 4.3 cluster
- Install Open Liberty Oprator from OperatorHub of ARO UI console
- Setup an [Elasticsearch Service deployed on Azure](https://www.elastic.co/azure)
- Install Maven
 ### Build & push image to Docker Hub
 ```
 $ ./build.sh <docker-hub-account-name> <image-name> <tag>
 ```
 ### Deploy and run Open Liberty Application on ARO
 ```
 $ ./deploy.sh <docker-hub-account-name>/<image-name>:<tag> <elastic-cloud-id> <elastic-cloud-user> <elastic-cloud-password>
 ```

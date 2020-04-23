# Integrate ELK for application running on Open Liberty Server

## Run it on ARO (Azure Red Hat OpenShift) 4.3 cluster
### Prerequisites
- Register an [Azure subscription](https://azure.microsoft.com/en-us/)
- Install [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli?view=azure-cli-latest)
- Follow this [guide](https://docs.microsoft.com/en-us/azure/openshift/howto-using-azure-redhat-openshift) to create an ARO 4.3 cluster
- Install Open Liberty Oprator from OperatorHub of ARO UI console
- Setup an [Elasticsearch Service deployed on Azure](https://www.elastic.co/azure)
- Install Maven
 ### Build & push image to Docker Hub
 ```
 $ mv clean package
 $ docker build -t <image-name>:<tag>
 $ docker tag <image-name>:<tag> <docker-hub-account-name>/<image-name>:<tag>
 $ docker login
 $ docker push <docker-hub-account-name>/<image-name>:<tag>
 ```
 ### Deploy and run containerized applications on ARO
- Create `secret` "elastic-cloud-secret" from ARO UI console
  - Replace placeholder value of `elastic.cloud.id` & `elastic.cloud.auth` from [elastic-cloud-secret.yaml](https://github.com/majguo/open-liberty-demo/blob/master/elk-integration/deployment/elastic-cloud-secret.yaml) with valid values
- Create `configmap` "filebeat-config" with [filebeat-elastic-hosted.yaml](https://github.com/majguo/open-liberty-demo/blob/master/elk-integration/deployment/filebeat-elastic-hosted.yaml) from ARO UI console
- Create `OpenShiftApplication` "elk-integration-demo" from Open Liberty Operator in ARO UI console
  - Replace placeholder value of `applicationImage` from [openlibertyapplication.yaml](https://github.com/majguo/open-liberty-demo/blob/master/elk-integration/deployment/openlibertyapplication.yaml) with valid values

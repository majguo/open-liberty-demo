#!/bin/sh

# Name of container registry, which is in the 'docker.io/<account>' format' for DockerHub
crServer=$1
# Name and optionally a tag in the 'name:tag' format
imageName=$2

mvn clean install --file javaee-cafe/pom.xml
cp javaee-cafe/target/javaee-cafe.war ./build

docker rmi ${imageName}
docker build -t ${imageName} -f ./build/Dockerfile-openshift ./build
docker rmi ${crServer}/${imageName}
docker tag ${imageName} ${crServer}/${imageName}

docker login
docker push ${crServer}/${imageName}
echo "The application image pushed to container registry is: ${crServer}/${imageName}"

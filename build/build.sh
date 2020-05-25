#!/bin/sh

crServer=$1
imageName=$2
tag=$3
clean=$4

if [ "$clean" = clean ]; then
    mvn clean package
else
    mvn package
fi

docker rmi ${imageName}:${tag}
docker build -t ${imageName}:${tag} .
docker rmi ${crServer}/${imageName}:${tag}
docker tag ${imageName}:${tag} ${crServer}/${imageName}:${tag}

docker login
docker push ${crServer}/${imageName}:${tag}
echo "The application image pushed to container registry is: ${crServer}/${imageName}:${tag}"

#!/bin/bash

IMAGEID=$(docker image ls | grep authorization-server | grep -Eo "[a-z0-9]{12}[ ]")
docker rmi $IMAGEID

$(aws ecr get-login --no-include-email --region us-east-1)

mvn clean package -DskipTests=true

docker push 478297097809.dkr.ecr.us-east-1.amazonaws.com/authorization-server:1.0.1

aws ecs update-service --service authorization-server --force-new-deployment --cluster netlit-cluster --region us-east-1

#!/bin/bash

IMAGEID=$(docker image ls | grep customer-service | grep -Eo "[a-z0-9]{12}[ ]")
docker rmi $IMAGEID

$(aws ecr get-login --no-include-email --region us-east-2)

mvn clean package -DskipTests=true

docker push 478297097809.dkr.ecr.us-east-2.amazonaws.com/customer-service:1.0.2-SNAPSHOT

aws ecs update-service --service customer-service --force-new-deployment --cluster netlit-cluster --region us-east-2
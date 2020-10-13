#!/bin/bash

IMAGEID=$(docker image ls | grep accounts | grep -Eo "[a-z0-9]{12}[ ]")
docker rmi $IMAGEID

$(aws ecr get-login --no-include-email --region us-east-2)

mvn clean package -DskipTests=true

docker push 478297097809.dkr.ecr.us-east-2.amazonaws.com/accounts:1.0.5-SNAPSHOT

aws ecs update-service --service accounts --force-new-deployment --cluster netlit-cluster --region us-east-2
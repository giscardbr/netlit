#!/bin/bash

IMAGEID=$(docker image ls | grep customer-service | grep -Eo "[a-z0-9]{12}[ ]")
docker rmi $IMAGEID

$(aws ecr get-login --no-include-email --region us-east-1)

mvn clean package -DskipTests=true -Daws-region=us-east-1 -Dversion=1.0.1

docker push 478297097809.dkr.ecr.us-east-1.amazonaws.com/accounts:1.0.1

aws ecs update-service --service customer-service --force-new-deployment --cluster netlit-cluster --region us-east-1

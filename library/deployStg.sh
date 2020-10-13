#!/bin/bash

IMAGEID=$(docker image ls | grep library | grep -Eo "[a-z0-9]{12}[ ]")
docker rmi $IMAGEID

$(aws ecr get-login --no-include-email --region us-east-2)

mvn clean package -DskipTests=true

docker push 478297097809.dkr.ecr.us-east-2.amazonaws.com/library:1.0.2-SNAPSHOT

aws ecs update-service --service library --force-new-deployment --cluster netlit-cluster --region us-east-2
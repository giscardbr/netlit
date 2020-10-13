# library

Service responsible for dealing with books information

### Requirements
- [JDK 1.8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) or higher
- [Maven](https://maven.apache.org/install.html)
- [Docker](https://docs.docker.com/install/)
- [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html)

## Start your app locally
```bash
$ mvn spring-boot:run -Dspring-boot.run.arguments=\
--spring.profiles.active=dev,\
--security.oauth2.resource.user-info-uri=http://authorization-server.netlit.local.stg:8080/v1/oauth/user,\
--library.book-address=https://du6jcb6gdkdiz.cloudfront.net,\
--spring.datasource.url=jdbc:mysql://library.cwqhhhkhaxkf.us-east-2.rds.amazonaws.com:3306/library,\
--spring.datasource.password=password,\
--spring.datasource.username=username
```
* Instead of use all those arguments you can just set the environment variable as described [here]#Environment variables).

## Deploy containers
The images are stored on [Amazon ECR Repositories](https://us-east-2.console.aws.amazon.com/ecr/repositories?region=us-east-2#).

- Authenticate your Docker client to the registry:
```bash
$ $(aws ecr get-login --no-include-email --region us-east-2)
```
- Push the image to Amazon ECR repository
```bash
$ docker push 478297097809.dkr.ecr.us-east-2.amazonaws.com/library:1.0.2-SNAPSHOT
```
- Update the Service using `--force-new-deployment` argument to pull the image from Amazon ECR repository:
```bash
$ aws ecs update-service --service library --force-new-deployment --cluster netlit-cluster --region us-east-2
```
You can check the new ECS Task status on [Amazon's Console](https://us-east-2.console.aws.amazon.com/ecs/home?region=us-east-2#/clusters/netlit-cluster/services/library/tasks)

## Environment variables

| Name                     	| Required                    	| Description                                                                                               	|
|--------------------------	|-----------------------------	|-----------------------------------------------------------------------------------------------------------	|
| SPRING_PROFILES_ACTIVE   	| Required if running locally 	| Production is the default profile. If you're running this application locally, set this variable to 'dev' 	|
| AUTHORIZATION_SERVER_URI 	| Required                    	| The authorization server validates the access token to allow secured requests and show user's identity    	|
| AWS_ACCESS_KEY_ID        	| Required if running locally 	| Your AWS Key                                                                                              	|
| AWS_SECRET_ACCESS_KEY    	| Required if running locally 	| Your AWS Secret                                                                                           	|
| BOOK_ADDRESS             	| Required                    	| The books data URI                                                                                        	|
| DATABASE_ENDPOINT_NAME   	| Required                    	| The endpoint of the MySQL Database used by this microservice                                              	|
| DATABASE_USERNAME        	| Required                    	| The username for MySQL Database used by this microservice                                                 	|
| DATABASE_PASSWORD        	| Required                    	| The password for MySQL Database used by this microservice                                                 	|
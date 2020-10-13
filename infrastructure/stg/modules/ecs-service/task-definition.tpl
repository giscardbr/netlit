[
  {
    "cpu": ${fargate_cpu},
    "environment": ${environment_variables},
    "essential": true,
    "image": "${app_image}",
    "logConfiguration": {
        "logDriver": "awslogs",
        "options": {
          "awslogs-group": "${log_group_name}",
          "awslogs-region": "${aws_region}",
          "awslogs-stream-prefix": "ecs"
        }
    },
    "memory": ${fargate_memory},
    "mountPoints": [],
    "name": "${app_name}",
    "portMappings": [
      {
        "containerPort": ${app_port},
        "hostPort": ${app_port}
      }
    ],
    "networkMode": "awsvpc",
    "volumesFrom": []
  }
]

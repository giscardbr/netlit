#!/usr/bin/env bash

echo "$(date -Iseconds) [INFO] Installing Terraform"
sudo apt-get install unzip -y && \
    wget https://releases.hashicorp.com/terraform/0.12.0/terraform_0.12.0_linux_amd64.zip && \
    unzip terraform_0.12.0_linux_amd64.zip && \
    rm terraform_0.12.0_linux_amd64.zip && \
    sudo mv terraform /usr/local/bin/

echo "$(date -Iseconds) [INFO] Installing AWS CLI"
sudo snap install aws-cli --classic

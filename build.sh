#!/usr/bin/bash

set -e

# Install java 1.8
sudo yum install -y java-1.8.0-openjdk-devel

sudo yum install -y maven
mvn package
sudo mkdir -p /opt/share
sudo cp target/share-0.0.1-jar-with-dependencies.jar /opt/share/share.jar

sudo tee /etc/systemd/share.service <<EOL
Description=SHARE service

[Service]
ExecStart=/usr/bin/java -jar /opt/share/share.jar

[Install]
WantedBy=multi-user.target
EOL

sudo systemctl enable /etc/systemd/share.service


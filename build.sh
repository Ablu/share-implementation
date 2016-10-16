#!/usr/bin/bash

set -e

sudo yum install -y maven
mvn package
sudo mkdir -p /opt/share
sudo cp target/share-0.0.1-jar-with-dependencies.jar /opt/share/share.jar

sudo cat > /etc/systemd/share.service <<EOL
Description=SHARE service

[Service]
ExecStart=/usr/bin/java -jar /opt/share/share.jar
User=solum

[Install]
WantedBy=multi-user.target
EOL

sudo systemctl enable /etc/systemd/share.service


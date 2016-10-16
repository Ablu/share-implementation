#!/usr/bin/bash

set -e

# Install java 1.8
wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u60-b27/jdk-8u60-linux-x64.rpm"
sudo yum localinstall jdk-8u60-linux-x64.rpm
rm -f jdk-8u60-linux-x64.rpm

sudo yum install -y maven
mvn package
sudo mkdir -p /opt/share
sudo cp target/share-0.0.1-jar-with-dependencies.jar /opt/share/share.jar

sudo tee /etc/systemd/share.service <<EOL
Description=SHARE service

[Service]
ExecStart=/usr/bin/java -jar /opt/share/share.jar
User=solum

[Install]
WantedBy=multi-user.target
EOL

sudo systemctl enable /etc/systemd/share.service


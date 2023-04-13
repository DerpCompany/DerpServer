#!/bin/bash

ARTIFACT_FILE="derpserver.jar"
ARTIFACT_FOLDER="server/build/libs"

INSTALLATION_FOLDER="/opt/derpserver/"

PROPERTIES_FILE="application.properties"
SYSTEMD_FILE="derpserver.service"

echo "Building Server Executable"
sleep 2
./gradlew server:bootJar

echo "Creating $INSTALLATION_FOLDER"
mkdir -p $INSTALLATION_FOLDER

touch $INSTALLATION_FOLDER/$PROPERTIES_FILE

systemctl stop $SYSTEMD_FILE

cp $ARTIFACT_FOLDER/$ARTIFACT_FILE $INSTALLATION_FOLDER/$ARTIFACT_FILE

echo "Make sure to install the systemd unit file"
echo "cp -f $SYSTEMD_FILE /etc/systemd/system/$SYSTEMD_FILE"

systemctl daemon-reload
systemctl restart $SYSTEMD_FILE

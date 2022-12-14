#!/bin/bash

cp server/build/libs/derpserver.jar /opt/derpserver/derpserver.jar

touch /opt/derpserver/application.properties

# sudo cp server/install/derpserver.service /etc/systemd/system/derpserver.service

systemctl daemon-reload

systemctl restart derpserver.service

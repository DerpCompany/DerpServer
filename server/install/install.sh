#!/bin/bash

echo "This file is for reference only. It is not intended to be used as is."

exit

cp server/build/libs/derpserver.jar /opt/derpserver/derpserver.jar

touch /opt/derpserver/application.properties

# sudo cp server/install/derpserver.service /etc/systemd/system/derpserver.service

systemctl daemon-reload

systemctl restart derpserver.service

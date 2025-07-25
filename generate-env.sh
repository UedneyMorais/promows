#!/bin/bash

LOCAL_IP=$(hostname -I | awk '{print $1}')
echo "APP_EXTERNAL_ADDRESS=http://$LOCAL_IP:9090" > .env

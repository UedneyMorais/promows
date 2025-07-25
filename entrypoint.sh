#!/bin/bash

# HOST_IP=$(hostname -I | awk '{print $1}')

# export APP_EXTERNAL_ADDRESS="http://$HOST_IP:9090"

./generate-env.sh

# echo "Detectado IP local: $HOST_IP"
echo "Iniciando aplicação com APP_EXTERNAL_ADDRESS=$APP_EXTERNAL_ADDRESS"

exec java -jar app.jar


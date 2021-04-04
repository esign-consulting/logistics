#!/bin/bash

# Init local environment
docker-compose up -d

# Wait until Logistics is up and running
until [ $(curl -w '%{http_code}' -o /dev/null -s http://localhost:8080/logistics/) -eq 200 ]; do
    echo "Still waiting Logistics - sleeping 2s..."
    sleep 2
done

# Init submodules
git submodule update --init --recursive

# Run the API test
mvn -f test-restassured test -Dserver.host=http://localhost -Dserver.port=8080

# Shutdown local environment
docker-compose down
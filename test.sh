#!/bin/bash

# Init local environment
docker-compose up -d
if [ $? -ne 0 ]; then
    echo "Failure on local environment initialisation. Aborting..."
    docker-compose down
    exit 1
fi

# Wait until Logistics is up and running
until [ $(curl -w '%{http_code}' -o /dev/null -s http://localhost:8080/logistics/) -eq 200 ]; do
    echo "Still waiting Logistics - sleeping 2s..."
    sleep 2
done

# Init submodules
git submodule update --init --recursive

# Run the API test
mvn -f test-restassured test -Dserver.host=http://localhost -Dserver.port=8080
if [ $? -ne 0 ]; then
    echo "Failure on API test. Aborting..."
    docker-compose down
    exit 1
fi

# Run the UI test
docker run --rm --network host -e LOGISTICS_URL=http://localhost:8080/logistics esignbr/logistics-test-selenium
if [ $? -ne 0 ]; then
    echo "Failure on UI test. Aborting..."
    docker-compose down
    exit 1
fi

# Run the Load test
mvn -f test-jmeter verify -Dserver.host=localhost -Dserver.port=8080
if [ $? -ne 0 ]; then
    echo "Failure on Load test. Aborting..."
    docker-compose down
    exit 1
fi

# Shutdown local environment
docker-compose down
exit 0
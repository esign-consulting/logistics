# Logistics

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) [![Build status](https://travis-ci.org/esign-consulting/logistics.svg?branch=master)](https://travis-ci.org/esign-consulting/logistics) [![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=br.com.esign%3Alogistics&metric=alert_status)](https://sonarcloud.io/dashboard/index/br.com.esign:logistics) [![Docker Pulls](https://img.shields.io/docker/pulls/esignbr/logistics.svg)](https://hub.docker.com/r/esignbr/logistics) [![Demo Application](https://img.shields.io/website-up-down-green-red/http/www.esign.com.br/logistics.svg?label=demo)](http://www.esign.com.br/logistics)

The goal of the Logistcs application is find out the best route between two places within a map. Once created, add routes to the map, as following:

Origin | Destination | Distance (Km)
------ | ----------- | -------------
A | B | 10
B | D | 15
A | C | 20
C | D | 30
B | E | 50
D | E | 30

The best route is the cheapest one, considering the truck autonomy (Km/l) and the fuel price (l). Based on the routes above, if the truck has to go from **place A** to **place D**, its autonomy is **10 Km/l** and the litre of the fuel cost **$2.50**, the best route will be **A -> B -> D**, because it's the cheapest one: **$6.25**.

## Quality

The Logistics application has quality in its core. Beyond unit tests, the following projects were developed in order to validate the whole solution:

Project | Test type | Build status
------- | --------- | ------------
[logistics-test-arquillian](test-arquillian) | Integration test | [![Build status](https://travis-ci.org/esign-consulting/logistics-test-arquillian.svg?branch=master)](https://travis-ci.org/esign-consulting/logistics-test-arquillian)
[logistics-test-restassured](test-restassured) | API test | [![Build status](https://travis-ci.org/esign-consulting/logistics-test-restassured.svg?branch=master)](https://travis-ci.org/esign-consulting/logistics-test-restassured)
[logistics-test-selenium](test-selenium) | UI test | [![Build status](https://travis-ci.org/esign-consulting/logistics-test-selenium.svg?branch=master)](https://travis-ci.org/esign-consulting/logistics-test-selenium)
[logistics-test-jmeter](test-jmeter) | Load test | [![Build status](https://travis-ci.org/esign-consulting/logistics-test-jmeter.svg?branch=master)](https://travis-ci.org/esign-consulting/logistics-test-jmeter)

## The pipeline

Each push to this repository triggers the pipeline below:

![Logistics' pipeline](http://www.esign.com.br/logistics.png)

## Execution

You can run the application as a [Docker](https://www.docker.com) container. Install [Docker](https://docs.docker.com/install) and then run `docker run --name logistics -p 8080:8080 -e MONGODB_URI=mongodb://username:password@host:port/logistics -d esignbr/logistics`. The environment variable **MONGODB_URI** is mandatory and must set the [MongoDB connection URI](https://docs.mongodb.com/manual/reference/connection-string). Remember to replace *username* and *password* with your MongoDB credentials as well as *host* and *port* with the appropriate values, according with where your MongoDB instance is avaliable on.

The Logistics application can also be executed along with MongoDB by using [Docker Compose](https://docs.docker.com/compose). If you've already installed [Docker](https://docs.docker.com/install) and [Docker Compose](https://docs.docker.com/compose/install), clone the repository and then run `docker-compose up -d`. The application will be available through the URL <http://localhost:8080/logistics> in your browser.

Alternatively, you can deploy both the application and the database to a VM of your choice, by using the [Ansible playbook](playbook.yml). If you want to know how it works, first install [Ansible](https://www.ansible.com), [VirtualBox](https://www.virtualbox.org) and [Vagrant](https://www.vagrantup.com), and then run `vagrant up`. Vagrant will bootstrap a local VM and will trigger the Ansible playbook execution. Once the local VM is up, open the URL <http://192.168.33.10:8080/logistics> in your browser.

## To do

Deployment to Cloud providers.

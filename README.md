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
[logistics-test-arquillian](https://github.com/esign-consulting/logistics-test-arquillian) | Integration test | [![Build status](https://travis-ci.org/esign-consulting/logistics-test-arquillian.svg?branch=master)](https://travis-ci.org/esign-consulting/logistics-test-arquillian)
[logistics-test-restassured](https://github.com/esign-consulting/logistics-test-restassured) | API test | [![Build status](https://travis-ci.org/esign-consulting/logistics-test-restassured.svg?branch=master)](https://travis-ci.org/esign-consulting/logistics-test-restassured)
[logistics-test-selenium](https://github.com/esign-consulting/logistics-test-selenium) | UI test | [![Build status](https://travis-ci.org/esign-consulting/logistics-test-selenium.svg?branch=master)](https://travis-ci.org/esign-consulting/logistics-test-selenium)
[logistics-test-jmeter](https://github.com/esign-consulting/logistics-test-jmeter) | Load test | [![Build status](https://travis-ci.org/esign-consulting/logistics-test-jmeter.svg?branch=master)](https://travis-ci.org/esign-consulting/logistics-test-jmeter)

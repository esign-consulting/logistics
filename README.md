# Logistics

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) [![Build status](https://travis-ci.org/esign-consulting/logistics.svg?branch=master)](https://travis-ci.org/esign-consulting/logistics) [![Quality Gate](https://sonarcloud.io/api/badges/gate?key=br.com.esign:logistics)](https://sonarcloud.io/dashboard/index/br.com.esign:logistics) [![Docker Pulls](https://img.shields.io/docker/pulls/esignbr/logistics.svg)](https://hub.docker.com/r/esignbr/logistics) [![Demo Application](https://img.shields.io/website-up-down-green-red/http/www.esign.com.br/logistics.svg?label=demo)](http://www.esign.com.br/logistics)


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

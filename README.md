# Logistics

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) [![Build status](https://travis-ci.org/esign-consulting/logistics.svg?branch=master)](https://travis-ci.org/esign-consulting/logistics) [![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=br.com.esign%3Alogistics&metric=alert_status)](https://sonarcloud.io/dashboard/index/br.com.esign:logistics) [![Known Vulnerabilities](https://snyk.io/test/github/esign-consulting/logistics/badge.svg)](https://snyk.io/test/github/esign-consulting/logistics) [![Docker Build status](https://img.shields.io/docker/cloud/build/esignbr/logistics.svg)](https://hub.docker.com/r/esignbr/logistics/builds) [![Docker Pulls](https://img.shields.io/docker/pulls/esignbr/logistics.svg)](https://hub.docker.com/r/esignbr/logistics) [![Demo Application](https://img.shields.io/website-up-down-green-red/http/www.esign.com.br/logistics.svg?label=demo)](http://www.esign.com.br/logistics)

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

## Quality Assurance

The Logistics application has quality in its core. Beyond unit tests, the following projects were developed in order to validate the whole solution:

Project | Test type | Build status
------- | --------- | ------------
[logistics-test-arquillian](https://github.com/esign-consulting/logistics-test-arquillian) | Integration test | [![Build status](https://travis-ci.org/esign-consulting/logistics-test-arquillian.svg?branch=master)](https://travis-ci.org/esign-consulting/logistics-test-arquillian)
[logistics-test-restassured](https://github.com/esign-consulting/logistics-test-restassured) | API test | [![Build status](https://travis-ci.org/esign-consulting/logistics-test-restassured.svg?branch=master)](https://travis-ci.org/esign-consulting/logistics-test-restassured)
[logistics-test-selenium](https://github.com/esign-consulting/logistics-test-selenium) | UI test | [![Build status](https://travis-ci.org/esign-consulting/logistics-test-selenium.svg?branch=master)](https://travis-ci.org/esign-consulting/logistics-test-selenium)
[logistics-test-jmeter](https://github.com/esign-consulting/logistics-test-jmeter) | Load test | [![Build status](https://travis-ci.org/esign-consulting/logistics-test-jmeter.svg?branch=master)](https://travis-ci.org/esign-consulting/logistics-test-jmeter)

![Logistics' tests](https://raw.githubusercontent.com/esign-consulting/logistics/master/logistics-tests.png)

### Static code analysis

The application code quality is evaluated with [SonarQube](https://www.sonarqube.org). If you want to run the [SonarScanner for Maven](https://docs.sonarqube.org/latest/analysis/scan/sonarscanner-for-maven) in order to take a look at the [static code analisys](https://en.wikipedia.org/wiki/Static_program_analysis)' results, firstly execute (requires [Docker](https://www.docker.com)):

`docker run --name sonarqube -p 9000:9000 -d sonarqube:8.4.1-community`

And then, execute (requires [JDK 11](https://openjdk.java.net/projects/jdk/11) and [Maven](https://maven.apache.org)):

`mvn clean install sonar:sonar`

After running the analysis, the Logistics application dashboard will be available in <http://localhost:9000/dashboard?id=br.com.esign%3Alogistics>.

## The CI/CD Pipeline

Each push to this repository triggers the pipeline below:

1. Travis CI clones this GitHub repository;
2. After compiling and performing several unit tests in the application source code, Maven triggers the SonarQube static code analisys, and the results are sent to [SonarCloud](https://sonarcloud.io);
3. Once generated the Java artifacts, they are deployed to [Sonatype OSSRH](https://oss.sonatype.org), a Nexus Repository Manager instance Sonatype uses to provide repository hosting service to open source projects binaries;
4. The application EAR file is deployed in a Wildfly instance avaliable at [esign.com.br](http://www.esign.com.br). From there, the Logistics application connects to a MongoDB instance maintained by [mLab](https://mlab.com);
5. Several tests are triggered asynchronously. Each one is performed against the Wildfly instance where the latest version of application was deployed.

![Logistics' pipeline](https://raw.githubusercontent.com/esign-consulting/logistics/master/logistics-pipeline.png)

## Running with Docker

You can run the application as a [Docker](https://www.docker.com) container. Install [Docker](https://docs.docker.com/install) and then run:

`docker run --name logistics -p 8080:8080 -e MONGODB_URI=mongodb://username:password@host:port/logistics -d esignbr/logistics`

The environment variable **MONGODB_URI** is mandatory and must set the [MongoDB connection URI](https://docs.mongodb.com/manual/reference/connection-string). Remember to replace *username* and *password* with your MongoDB credentials as well as *host* and *port* with the appropriate values, according with where your MongoDB instance is avaliable on.

The Logistics application can also be executed along with MongoDB by using [Docker Compose](https://docs.docker.com/compose). If you've already installed [Docker](https://docs.docker.com/install) and [Docker Compose](https://docs.docker.com/compose/install), clone the repository and then run:

`docker-compose up -d`

The application will be available through the URL <http://localhost:8080/logistics> in your browser.

## Deploying to a local VM

Alternatively, you can deploy both the application and the database to a local VM, by using the [Ansible playbook](playbook.yml). If you want to know how it works, first install [Ansible](https://www.ansible.com), [VirtualBox](https://www.virtualbox.org) and [Vagrant](https://www.vagrantup.com), and then run:

`vagrant up`

Vagrant will download the [codeyourinfra](https://app.vagrantup.com/codeyourinfra)/[docker](https://app.vagrantup.com/codeyourinfra/boxes/docker) [Vagrant box](https://www.vagrantup.com/docs/boxes.html) (if it was not done yet), then will bootstrap the local VM and at the end will trigger the Ansible playbook execution.

Once the local VM is up, open the URL <http://192.168.33.10:8080/logistics> in your browser, and give the Logistics application a try.

Finally, if you want to cleanup everything, execute the command:

`vagrant destroy -f && rm -rf .vagrant/`

## Deploying to AWS

If you've already installed [Ansible](https://www.ansible.com), you can make the deployment in the cloud too. In spite Ansible has [support to several Cloud providers](https://www.ansible.com/integrations/cloud), at the moment we only provide a playbook to deploy Logistics into an [EC2 instance of AWS](https://aws.amazon.com/ec2). Once you have your [AWS credentials](https://docs.aws.amazon.com/general/latest/gr/aws-sec-cred-types.html#access-keys-and-secret-access-keys), run:

`ansible-playbook deploy-to-aws.yml`

Ansible uses [Boto 3](https://github.com/boto/boto3) in its [Amazon modules](https://docs.ansible.com/ansible/latest/modules/list_of_cloud_modules.html#amazon). During the execution, the playbook creates all the infrastructure required for accessing the application through the Internet. The [AMI](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/AMIs.html) used is *codeyourinfra/docker*, one of the [Codeyourinfra's AWS AMIs](https://github.com/codeyourinfra/ubuntu-images#amazon-web-services-amis).

Right after the deployment, check the output to find out what is the EC2 instance public IP address. You can get the IP from the *inventory.yml* file as well. Then, open the Logistics' URL in your browser, replacing the IP with the one you've just got. Finally, if you want to undo everything, just run:

`ansible-playbook undeploy-from-aws.yml`

## Logistics API

The Logistics API documentation is generated through [Swagger](https://swagger.io) and can be checked by accessing the `/logistics/docs` endpoint, after deploying the application. In the [Esign Consulting website](http://www.esign.com.br), for example, the Logistics API documentation is available on <http://www.esign.com.br/logistics/docs>.

## Feature toggles

The Logistics application has a [feature toggles](https://www.martinfowler.com/articles/feature-toggles.html) admin console that comes with [Togglz](https://www.togglz.org). The console can be accessed through the `/logistics/togglz` endpoint, after deploying the application. In the [Esign Consulting website](http://www.esign.com.br), for example, the Logistics Feature Flags console is available on <http://www.esign.com.br/logistics/togglz>.

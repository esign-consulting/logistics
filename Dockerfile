FROM maven:3-jdk-8-alpine AS builder
WORKDIR /logistics
COPY pom.xml .
COPY logistics-ear ./logistics-ear
COPY logistics-ejb ./logistics-ejb
COPY logistics-web ./logistics-web
RUN mvn package

FROM jboss/wildfly:12.0.0.Final
LABEL maintainer "Gustavo Muniz do Carmo <gustavo@esign.com.br>"
COPY standalone.xml /opt/jboss/wildfly/standalone/configuration/
COPY --from=builder /logistics/logistics-ear/target/logistics-ear-1.0-SNAPSHOT.ear /opt/jboss/wildfly/standalone/deployments/
RUN /opt/jboss/wildfly/bin/add-user.sh admin Admin#70365 --silent
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]

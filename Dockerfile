FROM jboss/wildfly
LABEL maintainer "Gustavo Muniz do Carmo <gustavo@esign.com.br>"

ADD standalone.xml /opt/jboss/wildfly/standalone/configuration/
ADD logistics-ear/target/logistics-ear-1.0-SNAPSHOT.ear /opt/jboss/wildfly/standalone/deployments/

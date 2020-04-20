FROM openliberty/open-liberty:kernel-java8-openj9-ubi

COPY --chown=1001:0 src/main/liberty/config /config/
COPY --chown=1001:0 target/liberty/wlp/usr/servers/elk-integration/bootstrap.properties /config
COPY --chown=1001:0 target/elk-integration.war /config/apps

RUN configure.sh

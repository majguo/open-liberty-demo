FROM openliberty/open-liberty:kernel-java8-openj9-ubi

COPY --chown=1001:0 src/main/liberty/config /config/
COPY --chown=1001:0 target/liberty/wlp/usr/servers/aad-oidc-integration/bootstrap.properties /config
COPY --chown=1001:0 target/aad-oidc-integration.war /config/apps

RUN configure.sh

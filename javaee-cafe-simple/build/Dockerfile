FROM openliberty/open-liberty:20.0.0.5-kernel-java8-openj9-ubi

# Copy deployment artifacts.
COPY --chown=1001:0 postgresql-42.2.4.jar /opt/ibm/wlp/usr/shared/resources/
COPY --chown=1001:0 server-openshift.xml /config/server.xml
COPY --chown=1001:0 javaee-cafe.war /config/apps/

RUN configure.sh

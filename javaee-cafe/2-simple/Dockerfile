FROM openliberty/open-liberty:kernel-java8-openj9-ubi

# Copy deployment artifacts.
COPY --chown=1001:0 src/main/liberty/config/server.xml /config/server.xml
COPY --chown=1001:0 target/javaee-cafe.war /config/apps/

RUN configure.sh

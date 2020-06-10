# Deploy Open Liberty Application on Azure Red Hat OpenShift
This guide demonstrates how to run your Java EE application on Open Liberty runtime and then deploy the containerized applicatoin to Azure Red Hat OpenShift (ARO) cluster using Open Liberty Operator. After going through this guide, you will learn steps about preparing Open Liberty Application, building application image & running containerzed application on ARO.
- Introduction for Open Liberty
- Introduction for Azure Red Hat OpenShift

## Prerequisites
- Install JDK per your needs (e.g., [AdoptOpenJDK OpenJDK 8 LTS/OpenJ9](https://adoptopenjdk.net/?variant=openjdk8&jvmVariant=openj9))
- Install [Maven](https://maven.apache.org/download.cgi)
- Install [Docker](https://docs.docker.com/get-docker/) for your OS
- Install [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli?view=azure-cli-latest)
- Register an Azure subscription. If you don't have one, you can get one for free for one year [here](https://azure.microsoft.com/en-us/free)
- Clone this repo to your local directory

## Set up Azure Red Hat OpenShift cluster
- [Create an Azure Red Hat OpenShift 4 cluster](https://docs.microsoft.com/en-us/azure/openshift/tutorial-create-cluster)
- [Connect to an Azure Red Hat OpenShift 4 cluster](https://docs.microsoft.com/en-us/azure/openshift/tutorial-connect-cluster)
- Install Open Liberty Operator

## Prepare your Open Liberty application
In case you already have a Java EE applicatoin running on Java EE Application Server (e.g., WebSphere, WebLogic, JBoss, etc.), it's easy to make it run on Open Liberty server with minimum changes.

### A basic Java EE app for quick start
Navigate to [`<path-to-repo>/javaee-cafe/1-start`](https://github.com/majguo/open-liberty-demo/tree/master/javaee-cafe/1-start) of your local clone and find out the sample app we prepared for your quick start. It's a basic Java EE application and simple CRUD application. It uses Maven and Java EE 8 (JAX-RS, EJB, CDI, JSON-B, JSF, Bean Validation). Here is brief introduction for the project structure:
```
├── pom.xml                                         # Maven POM file
└── src
    ├── main
    │   ├── java
    │   │   └── cafe
    │   │       ├── model
    │   │       │   ├── CafeRepository.java         # Cafe CRUD repository (in-memory)
    │   │       │   └── entity
    │   │       │       └── Coffee.java             # Coffee entity
    │   │       └── web
    │   │           ├── rest
    │   │           │   └── CafeResource.java       # Cafe CRUD REST APIs
    │   │           └── view
    │   │               └── Cafe.java               # Cafe bean in JSF client
    │   ├── resources
    │   │   ├── META-INF
    │   │   └── cafe
    │   │       └── web
    │   │           ├── messages.properties         # Resource bundle in EN
    │   │           └── messages_es.properties      # Resource bundle in ES
    │   └── webapp
    │       ├── WEB-INF
    │       │   ├── faces-config.xml                # JSF configuration file sepcifying resource bundles and suported locales
    │       │   └── web.xml                         # Deployment descriptor for a Servlet-based Java web application
    │       └── index.xhtml                         # Home page of JSF client
    └── test
        └── java                                    # Placeholder for tests
```

Follow the steps below to package and deploy the application to your desired Java EE Application Server
- Run `mvn clean package`, which will generate a war package `javaee-cafe.war` under `./target`
- Deploy `./target/javaee-cafe.war` to one Java EE Application Server per its specific steps
- Visit the application in your browser. Below is the screenshot of application home page:
![javaee-cafe-web-ui](pictures/javaee-cafe-web-ui.png)

### Configure app to run on Open Liberty server
In order to run the application on Open Liberty server, the only mandatory step is to add a server configuratoin file `server.xml` with required settings:
```
<?xml version="1.0" encoding="UTF-8"?>
<server description="defaultServer">
    <!-- Enable features -->
    <featureManager>
        <feature>cdi-2.0</feature>
        <feature>jaxb-2.2</feature>
        <feature>jsf-2.3</feature>
        <feature>jaxrs-2.1</feature>
        <feature>ejbLite-3.2</feature>
    </featureManager>

    <!-- Define http & https endpoints -->
    <httpEndpoint id="defaultHttpEndpoint" host="*"
        httpPort="9080" httpsPort="9443" />

    <!-- Automatically expand WAR files and EAR files -->
    <applicationManager autoExpand="true" />

    <!-- Define web application with its context root and location -->
    <webApplication id="javaee-cafe" contextRoot="/"
        location="${server.config.dir}/apps/javaee-cafe.war">
    </webApplication>
</server>
```
It's recommended to add this configuration file to `<path-to-repo>/javaee-cafe/1-start/src/main/liberty/config` (remember create directories that don't exist before), as it can perfectly work with `liberty-maven-plugin` which makes develop Open Liberty applicatoin easy.

Although it's not mandatory to use [liberty-maven-plugin](https://github.com/OpenLiberty/ci.maven#liberty-maven-plugin), it's recommended to use as it provides a number of goals for managing a Liberty server and applications. One of the most exciting features is "Dev Mode" which was supported since release 3.0. There is a new `dev` goal that starts a Liberty server in dev mode. Dev mode provides three key features:
- Code changes are detected, recompiled, and picked up by your running server.
- Unit and integration tests are run on demand when you press Enter in the command terminal where dev mode is running, or optionally on every code change to give you instant feedback on the status of your code. 
- Finally, it allows you to attach a debugger to the running server at any time to step through your code.

To enable Liberty Maven Plugin in your project, add the followings to `<path-to-repo>/javaee-cafe/1-start/pom.xml`:
```
<project>

  <!-- Beginning of configuration to be added -->
  <profiles>
    <profile>
      <id>liberty</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <build>
        <plugins>
          <!-- Enable liberty-maven-plugin -->
          <plugin>
            <groupId>io.openliberty.tools</groupId>
            <artifactId>liberty-maven-plugin</artifactId>
            <version>3.2</version>
            <executions>
              <execution>
                <id>package-server</id>
                <phase>package</phase>
                <goals>
                  <goal>create</goal>
                  <goal>install-feature</goal>
                  <goal>deploy</goal>
                  <goal>package</goal>
                </goals>
                <configuration>
                  <libertyRuntimeVersion>[20.0.0.1,)</libertyRuntimeVersion>
                  <outputDirectory>${project.build.directory}/wlp-package</outputDirectory>
                </configuration>
              </execution>
            </executions>
          </plugin>         
        </plugins>
      </build>
    </profile>
  </profiles>
  <!-- End of configuration to be added -->

</project>
```

Now you can start "Dev Mode" by running command `mvn clean liberty:dev` in a console, wait until the server starts. You will see the similar info printed in your console as below:
```
[INFO] Listening for transport dt_socket at address: 7777
[INFO] Launching defaultServer (Open Liberty 20.0.0.6/wlp-1.0.41.cl200620200528-0414) on Java HotSpot(TM) 64-Bit Server VM, version 1.8.0_251-b08 (en_US)
[INFO] [AUDIT   ] CWWKE0001I: The server defaultServer has been launched.
[INFO] [AUDIT   ] CWWKG0093A: Processing configuration drop-ins resource: 
[INFO]   Property location will be set to ${server.config.dir}/apps/javaee-cafe.war.
[INFO] 
[INFO] [AUDIT   ] CWWKZ0058I: Monitoring dropins for applications.
[INFO] [AUDIT   ] CWWKT0016I: Web application available (default_host): http://localhost:9080/
[INFO] [AUDIT   ] CWWKZ0001I: Application javaee-cafe started in 3.453 seconds.
[INFO] [AUDIT   ] CWWKF0012I: The server installed the following features: [cdi-2.0, ejbLite-3.2, el-3.0, jaxb-2.2, jaxrs-2.1, jaxrsClient-2.1, jndi-1.0, jsf-2.3, jsonp-1.1, jsp-2.3, servlet-4.0].
[INFO] [AUDIT   ] CWWKF0011I: The defaultServer server is ready to run a smarter planet. The defaultServer server started in 6.447 seconds.
[INFO] CWWKM2015I: Match number: 1 is [6/10/20 10:26:09:517 CST] 00000022 com.ibm.ws.kernel.feature.internal.FeatureManager            A CWWKF0011I: The 
defaultServer server is ready to run a smarter planet. The defaultServer server started in 6.447 seconds..
[INFO] Press the Enter key to run tests on demand. To stop the server and quit dev mode, use Ctrl-C or type 'q' and press the Enter key.
[INFO] Source compilation was successful.
```
Open http://localhost:9080/ in your browser to visit the application home page mentioned before.

You can also find out all of these changes from [`<path-to-repo>/javaee-cafe/2-simple`](https://github.com/majguo/open-liberty-demo/tree/master/javaee-cafe/2-simple). Check it out for your reference if needed.

## Deploy application on ARO
- Build application image with [Open Liberty container images](https://github.com/OpenLiberty/ci.docker)
- OpenLibertyApplication yaml
- Deploy from GUI
- Deploy from CLI

## Integrate with Azure services
### Enable Signle-Sign-On with Azure Active Directory OpenID Connect

### Persist data with Azure Database for PostgreSQL

### Ship application log to managed Elasticsearch service on Azure

## References
- [Open Liberty server configuration](https://openliberty.io/docs/ref/config/)

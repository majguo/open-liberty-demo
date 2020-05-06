# Secure Liberty Application with Azure Active Directory OpenID Connect

## Introduction
This demo project provides an example on how to secure your application which is running on Open Liberty / WebSphere Liberty server by using Azure Active Directory OpenID Connect, including:
- Single-Sign-On
  - Authenticated with users registered in Azure Active Directory
  - Authorized with security groups registered in Azure Active Directory
- Single-Sign-Out

### References
- [Securing Open Liberty apps and microservices with MicroProfile JWT and Social Media login](https://openliberty.io/blog/2019/08/29/securing-microservices-social-login-jwt.html)
- [Configuring an OpenID Connect Client in Liberty](https://www.ibm.com/support/knowledgecenter/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/twlp_config_oidc_rp.html)

## Prerequisites
- Register an [Azure subscription](https://azure.microsoft.com/en-us/)
- Install [Docker Desktop](https://www.docker.com/products/docker-desktop)
- Download [Maven](https://maven.apache.org/download.cgi)

## Setup your Azure Active Directory
- [Create a new Azure AD tenant](https://docs.microsoft.com/en-us/azure/active-directory/develop/quickstart-create-new-tenant#create-a-new-azure-ad-tenant) if not existing, log down Directory (tenant) ID
- [Create Azure AD users](https://docs.microsoft.com/en-us/azure/openshift/howto-aad-app-configuration#create-a-new-azure-active-directory-user) if not existing, log down their emial addresses & passwords
- [Create Azure AD security groups](https://docs.microsoft.com/en-us/azure/openshift/howto-aad-app-configuration#create-an-azure-ad-security-group) "admin" & "users" if not existing, add created users as group members, log down group IDs 
- [Create an Azure AD app registration for authentication](https://docs.microsoft.com/en-us/azure/openshift/configure-azure-ad-ui#create-an-azure-active-directory-application-for-authentication) if not existing
  - log down Application (client) ID & client secret
  - fill in the <b>Redirect URI</b> with the redirect URI of applicatoin to be deployed later, using format of `https://<domain-name>:<port>/oidcclient/redirect/liberty-aad-oidc-sample`
  - fill in the <b>Logout URL</b> with the redirect logout URI of applicatoin to be deployed later, using format of `https://<domain-name>:<port>/sso/logout`
- [Configure optional claims](https://docs.microsoft.com/en-us/azure/openshift/configure-azure-ad-ui#configure-optional-claims)
  - Add optional claim > Select ID then check the email and upn claims
  - Add groups claim > Select Security groups then select Group ID for each token type

## Build project 
- Clone [this repo](https://github.com/majguo/open-liberty-demo)
- Change directory to `<path-to-repo>/aad-oidc-integration`
- Replace the placeholders for the following properties in `pom.xml` with valid values:
  ```
    <http.port>SPECIFY_YOUR_HTTP_PORT</http.port>
    <https.port>SPECIFY_YOUR_HTTPS_PORT</https.port>
    <default.keystore.pass>SPECIFY_YOUR_DEFAULT_KEYSTORE_PASS</default.keystore.pass>
    <java.truststore.pass>SPECIFY_YOUR_JAVA_TRUSTSTORE_PASS</java.truststore.pass>
    <client.id>SPECIFY_YOUR_CLIENT_ID</client.id>
    <client.secret>SPECIFY_YOUR_CLIENT_SECRET</client.secret>
    <tenant.id>SPECIFY_YOUR_TENANT_ID</tenant.id>
    <logout.redirect.url.https>SPECIFY_YOUR_LOGOUT_REDIRECT_URL_HTTPS</logout.redirect.url.https>
    <admin.group.id>SPECIFY_YOUR_ADMIN_GROUP_ID</admin.group.id>
    <users.group.id>SPECIFY_YOUR_USERS_GROUP_ID</users.group.id>
  ```
- Run `mvn clean package`

## Build image
- Build application image from WebSphere Liberty: `docker build -t <image-name>:<tag> -f Dockerfile-wlp .`
- Build application image from Open Liberty: `docker build -t <image-name>:<tag> -f Dockerfile .`
 
## Run containered application
- Run `docker run --name <container-name> -p <port>:<https-port> -d <image-name>:<tag>`

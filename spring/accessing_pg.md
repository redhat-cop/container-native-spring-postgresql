# Configuring the app to access the db.

The application needs to exchange a service token for a vault token.
With that Vault token it can request a database set of credential.
Vault dynamically generate it and return it to the application.
At this point the application can connect to the database.
Both the vault token and the database credential have a TTL.
To the application also needs to renew the vault token and retrieve the new database credential when it's rotated by Vault.
All of this is managed by a couple of libraries in spring that you can import this way:
```
        <dependency>
          <groupId>org.springframework.cloud</groupId>
          <artifactId>spring-cloud-starter-vault-config</artifactId>
        </dependency>
        <dependency>
          <groupId>org.springframework.cloud</groupId>
          <artifactId>spring-cloud-vault-config-databases</artifactId>
        </dependency> 
```
in order to configure the app to use spring you need to do add the following to your `bootstrap.yaml`:
```
spring.cloud.vault:
    host: vault.vault.svc.cluster.local
    port: 8200
    scheme: https
    authentication: KUBERNETES
    kubernetes:
        role: product-catalog
        service-account-token-file: /var/run/secrets/kubernetes.io/serviceaccount/token
    config:
        order: -10
    generic:
      enabled: false
    postgresql:
        enabled: true
        role: pg-readwrite
        backend: database
        username-property: spring.datasource.username
        password-property: spring.datasource.password
```
Vault is considered a property source. `spring.datasource.username` and `spring.datasource.password` are the properties that will be populated by Spring from Vault and can be used as normal properties.        
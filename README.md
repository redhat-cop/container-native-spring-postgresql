# container-native-spring-postgresql

This tutorial shows the steps to install create a container native architecture and run in it a simple application.
The application has the following architecture

TODO add diagram here

Assumptions:
1. you have an OpenShift cluster >= 3.7
2. you have dynamic volume provisioning available
3. you are running the network policy plugin.

Here are the steps for the installation:

1. Build and deploy the application
2. [Deploy the Crunchy Postgres operator](./crunchy/deploy-crunchy.md)
3. [Deploy Postgres in HA](./crunchy/deploy-HA-db.md)
4. [Deploy Hashicorp Vault](./vault/deploy-vault.md)
5. [Configure Vault to use Kubernetes backend authentication](./vault/vault-kube-backend.md)
6. [Configure Vault to manage the postgresql DB](./vault/vault-postgres.md)
7. Configure application to use Vault to retrieve the postgresql account
8. RH SSO installation
9. Istio core installation
10. Configure app to use Istio
11. Configure Istio to do Mutual TLS authentication
12. Configure istio to do OAuth Authentication via RH SSO
13. Istio Add-ons installation (prometheus, Jaeger)
14. Configure microsegmentation
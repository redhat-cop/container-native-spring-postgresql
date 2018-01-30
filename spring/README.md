# container-native-spring-postgresql

Here are the steps for the installation:

To deploy the db schema:

```
oc project pgo 

pod=`oc get pods | grep postgres-ha-[0-9] | awk '{print $1}'`

oc rsync ./spring/db/ $pod:/tmp --no-perms

oc rsh $pod  psql -U postgres -f /tmp/db.sql -d userdb

```

[Build and deploy the product-catalog application](./product-catalog/README.adoc)

[Build and deploy the product-inventory application](./product-inventory/README.adoc)

[Build and deploy the product-frontend application](./product-frontend/README.adoc)
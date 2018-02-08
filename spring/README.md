# container-native-spring-postgresql

Here are the steps for the installation:

## Build and deploy the product catalog

```
oc new-project product
oc adm policy add-scc-to-user privileged -z default
oc new-build registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift --binary=true --name product-catalog
oc start-build product-catalog -F --from-dir=./spring/product-catalog
oc apply -f ./spring/product-catalog/src/main/fabric8/service.yml  
oc apply -f ./spring/product-catalog/src/main/fabric8/route.yml
oc apply -f ./spring/product-catalog/src/main/fabric8/deploymentconfig.yml 
```

See also here for more details: [Build and deploy the product-catalog application](./product-catalog/README.adoc)

## Build and deploy the product inventory

```
oc new-build registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift --binary=true --name product-inventory
oc start-build product-inventory -F --from-dir=./spring/product-inventory
fqdn=`oc get route | grep secure-sso | awk '{print $2}'`
key=`kcadm.sh get keys --trustpass=changeit -r master --fields 'keys(publicKey)' | jq .keys[0].publicKey -r`
secret=`kcadm.sh get --trustpass=changeit clients/b5e0526f-25b5-452c-9d66-e56515402e7f/client-secret --fields 'value' --format csv --noquotes`
oc process -f ./spring/product-inventory/src/main/fabric8/configmap.yml -p RH_SSO_REALM_KEY=$key RH_SSO_URL=$fqdn RH_SSO_CREDENTIAL_SECRET=$secret | oc apply -f - 
oc create -f ./spring/product-inventory/src/main/fabric8/service.yml  
oc create -f ./spring/product-inventory/src/main/fabric8/route.yml
oc create -f ./spring/product-inventory/src/main/fabric8/deploymentconfig.yml
```

See also here for more information [Build and deploy the product-inventory application](./product-inventory/README.adoc)


## Build and deploy the front-end

```
secret=`kcadm.sh get --trustpass=changeit clients/b5e0526f-25b5-452c-9d66-e56515402e7f/client-secret --fields 'value' --format csv --noquotes`
mvn -f ./spring/product-frontend clean fabric8:deploy -Popenshift -DSSO_AUTH_SERVER_URL=$(oc get route secure-sso -n rh-sso -o jsonpath='{"https://"}{.spec.host}{"/auth"}') -DPRODUCT_INVENTORY_SERVICE_URL=$(oc get route product-inventory -o jsonpath='{"https://"}{.spec.host}') -DSSO_CLIENT_SECRET=$secret
```

See also here for more details: [Build and deploy the product-frontend application](./product-frontend/README.adoc)
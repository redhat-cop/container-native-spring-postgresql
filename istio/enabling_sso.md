# Enabling istio OAuth-based Authentication with JWT binding using RH-SSO as OAuth provider

create the istio policy
```
oc new-project product
export fqdn=`oc get route -n rh-sso | grep secure-sso | awk '{print $2}'`
oc process -f ./spring/product-catalog/src/istio/product-catalog-auth_config.yaml -p RH_SSO_ROUTE=$fqdn | oc apply -f -
istioctl create -f ./spring/product-catalog/src/istio/mixer-rule-only-authorized.yaml
```

The first set of commands creates an JWT oauth authentication rule that points to rh-sso as the oauth provider and then binds the rule to the catalog service.
The second command creates a rule that will allow only aushorized users to access the service
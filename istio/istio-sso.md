# Istio/SSO Integration Notes

## Current Status

* Product Catalog Spring Boot application runs with an Istio Proxy sidecar
* Authorization policy in place to intercept HTTP calls for application which will:
  * Validate JWT token
  * Make backend call to SSO/Keycloak to valide JWT signature based on public key (which may be cached)

## Known Issues/Findings

* [Bug: forward_jwt doesn't seem to forward Authorization header](https://github.com/istio/proxy/issues/986)
  * Awaiting implementation even though documented

* Istio rules do not allow for checking of missing `Authorization` header. This needs to be done elsewhere such as the Spring Controller.

* [Bug: istioctl support for Openshift DeploymentConfig](https://github.com/istio/issues/issues/29)
  * Need to use Deployment instead (see Kamesh's [example project](https://github.com/kameshsampath/istio-keycloak-demo/blob/98302468c0bb9cf4204b41cf2f04672c561eab05/cars-api/src/istio/cars-api-0.0.1-all.yml)

* [Bug: EndUserAuthenticationSpec not allowing http](https://github.com/istio/istio/issues/2668)
  * Workaround is to use `kubectl` rather than `istioctl` 

* [Undocumented: Need to use denier status code 16 to return 401 Unauthorized](https://github.com/googleapis/googleapis/blob/master/google/rpc/code.proto#L103)
  * Will return 500 error otherwise making you think there was actually an error when there wasn't

* [Bug: Liveness/Readiness Probe Istio causing stream of errors in proxy log](https://github.com/istio/istio/issues/2628)
  * Disable probes for deployment or use shell script to get status internally
  * These errors will prevent proxy from running normally!

* [Bug: Disable Liveness/Readiness Probe on HTTP port 8080 or Spring Boot container will not go live](https://github.com/kameshsampath/istio-keycloak-demo/issues/3)

* Bug?: Updating Auth Policies may be unreliable requiring restarting destination service pod or even Istio itself

* Bug?: Errors coming from Istio/Proxy logs which turn out to be red herrings
  * `ERROR: logging before flag.Parse`
  * `[libprotobuf ERROR external/mixerclient_git/src/quota_cache.cc:119] Quota response did not have quota for: RequestCount`


  # Implementation/Testing

1) Undeploy existing Product Catalog DC/Route/Service

Will use separate deployment to show Istio/SSO integration for Product Catalog as they have not yet been merged into one. This is a TODO item I didn't have time for.

```
oc delete -f spring/product-catalog/src/main/fabric8/route.yaml
oc delete -f spring/product-catalog/src/main/fabric8/service.yaml
oc delete -f spring/product-catalog/src/main/fabric8/deploymentconfig.yaml
```

2) Delete any remaining Product Catalog Deployment/Pods

The alternate deployment will reuse Product Catalog image from previous Fabric8 build.

3) Deploy Istio/SSO Integration

Requires using `istioctl` which is part of the Istio download package.

```
oc apply -f spring/product-catalog/src/istio/product-catalog-auth_config.yaml
istioctl create -f spring/product-catalog/src/istio/mixer-rule-only-authorized.yaml
```

4) Run alternate Product Catalog Deployment

Deployment, Service, Route will be created.

```
oc apply -f spring/product-catalog/src/istio/istio-product-catalog-0.0.1-all.yml
```

5) Go to Frontend application and test Invoke functionality to make sure it still works.

Make sure you log out and back in of SSO to have a fresh session (not expired JWT)

If there is error coming from invoke, then these instructions have failed and more investigation will be required. Check the logs for Product Inventory for errors including a 403 error code.

6) Proceed to validating token validation is working

a) `oc rsh product-inventory-XXX bash` to log into Product Inventory container, where we will perform the `curl` command internal to OpenShift.

b) Copy `curl` command from the web browser from the Frontend application

c) Paste into text editor to make some tweaks

d) Change URL to be `http://product-catalog.product.svc:8080/product`

e) Remove `| jq`

f) Add `-vvv` switch to curl command for verbose output

g) Run the new  `curl` command inside of the `product-inventory` container. You should see results coming back from the Product Catalog Service.

h) Go back to text editor. Change the JWT in the curl command by replacing the first character with something else.

i) Run the curl command again inside of the product-inventory container. You should be an invalid header error. This is coming from Istio and proves the JWT is being validated.

Extra Credit:

j) Try a JWT you know to be expired. You should get a response from Istio saying it's expired.

k) Replace the signature of your JWT with a signature from a token which is generated from another realm or system. You will get an invalide signature error. This comes from Istio contacting SSO for the public key, and then it will validate the signature is valid for that JWT. This happens for all tokens that pass the initial validation as the signature validation is the last step.

### Helpful Hints

* Use jwt.io to view the contents of your JWT token.

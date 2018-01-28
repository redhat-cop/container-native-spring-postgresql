# Istio/SSO Work-in-progress

## Current Status

* Sample Spring Boot application runs with an Istio Proxy sidecar
* Authorization policy in place to intercept HTTP calls for application which will:
  * Validate JWT token
  * Make backend call to SSO/Keycloak to valide JWT signature based on public key (which may be cached)
    * Validated calls being made to SSO under ideal circumstanced when changing SSO service to point to Apache to log the requests
    * Have not confirmed JWT signature being validated correctly as I was still getting 401 unauthorized error
* Ran into a myriad of stumbling blocks (bugs, no docs) which stunted progress

## Known Issues/Findings

* [Bug: EndUserAuthenticationSpec not allowing http](https://github.com/istio/istio/issues/2668)
  * Workaround is to use `kubectl` rather than `istioctl` 

* [Undocumented: Need to use denier status code 16 to return 401 Unauthorized](https://github.com/googleapis/googleapis/blob/master/google/rpc/code.proto#L103)
  * Will return 500 error otherwise making you think there was actually an error when there wasn't

* [Bug: Liveness/Readiness Probe Istio causing stream of errors in proxy log](https://github.com/istio/istio/issues/2628)
  * Disable probes for deployment or use shell script to get status internally
  * These errors will prevent proxy from running normally!

* [Bug: Disable Liveness/Readiness Probe on HTTP port 8080 or Spring Boot container will not go live](https://github.com/kameshsampath/istio-keycloak-demo/issues/3)

* Bug?: Updating Auth Policies may be unreliable  requiring restarting destination service pod or even Istio itself

* Bug?: Errors coming from Istio/Proxy logs which turn out to be red herrings
  * `ERROR: logging before flag.Parse`
  * `[libprotobuf ERROR external/mixerclient_git/src/quota_cache.cc:119] Quota response did not have quota for: RequestCount`


  # Notes (In progress)

```
# Needs to be kubectl rather than istioctl because of https://github.com/istio/istio/issues/2668

kubectl create -f src/istio/auth-policy.yaml -n myproject

* istioctl is in the bin directory of the Istio 0.4.0 distribution

istioctl create -f src/istio/denier-auth-policy.yaml

# Client ID curl is simply a public client used to obtain a token. This article is helpful: http://blog.keycloak.org/2015/10/getting-started-with-keycloak-securing.html

curl -d "grant_type=password&client_id=curl&username=demo&password=demo" -X POST http://sso-myproject.192.168.64.2.nip.io/auth/realms/eric/protocol/openid-connect/token

# Test the endpoint to make sure no error (HTTP code 403) is returned

curl -vvv -H "Authorization: Bearer $token http://cars-api-myproject.192.168.64.2.nip.io/cars/list

# TODO: This is not captured by handler and denied as you would expect
# https://groups.google.com/d/msg/istio-security/DzLR6Gqqjxk/rybn4FH5AAAJ

curl -vvv http://cars-api-myproject.192.168.64.2.nip.io/cars/list
```


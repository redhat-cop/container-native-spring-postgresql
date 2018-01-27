
Istio Installation
==================

* Create the project istio-system as the location to deploy all the components.
* Add necessary permissions.
* Deploy Istio components.

```
oc new-project istio-system
oc adm policy add-scc-to-user privileged -z istio-ingress-service-account
oc adm policy add-scc-to-user privileged -z istio-egress-service-account
oc adm policy add-scc-to-user privileged -z istio-pilot-service-account
oc adm policy add-scc-to-user privileged -z default
oc apply -f https://raw.githubusercontent.com/istio/istio/0.4.0/install/kubernetes/istio.yaml
```

References
----------

* [Evaluate Istio on OpenShift](https://blog.openshift.com/evaluate-istio-openshift/)

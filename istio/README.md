
Istio Installation
==================

* Create the project istio-system as the location to deploy all the components.
* Add necessary permissions.
* Deploy Istio components.

You should pull down Istio onto your local drive, and you can run `curl -L https://git.io/getLatestIstio | sh -` which will also get you access to `istioctl` in the bin directory, which will be needed for other parts of this system installation.

```
oc new-project istio-system
oc adm policy add-scc-to-user privileged -z istio-ingress-service-account
oc adm policy add-scc-to-user privileged -z istio-egress-service-account
oc adm policy add-scc-to-user privileged -z istio-pilot-service-account
oc adm policy add-scc-to-user privileged -z default
oc apply -f istio-0.4.0/install/kubernetes/istio.yaml
```

References
----------

* [Evaluate Istio on OpenShift](https://blog.openshift.com/evaluate-istio-openshift/)

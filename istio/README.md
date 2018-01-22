
Istio Installation
==================

* Create the project istio-system as the location to deploy all the components.
* Add necessary permissions.
* Deploy Istio components.
* Deploy additional add-ons, namely Prometheus, Grafana, Service Graph and Zipkin.

Pick a working directory on your system where Istio can be pulled down and used for running the deployment.

These commands have also been tested on a 1-node deployment using `oc cluster up`.

```
oc new-project istio-system
oc project istio-system

oc adm policy add-scc-to-user anyuid -z istio-ingress-service-account
oc adm policy add-scc-to-user privileged -z istio-ingress-service-account
oc adm policy add-scc-to-user anyuid -z istio-egress-service-account
oc adm policy add-scc-to-user privileged -z istio-egress-service-account
oc adm policy add-scc-to-user anyuid -z istio-pilot-service-account
oc adm policy add-scc-to-user privileged -z istio-pilot-service-account
oc adm policy add-scc-to-user anyuid -z default
oc adm policy add-scc-to-user privileged -z default
oc adm policy add-cluster-role-to-user cluster-admin -z default

curl -L https://git.io/getLatestIstio | sh -
ISTIO=`ls | grep istio`
export PATH="$PATH:~/$ISTIO/bin"
cd $ISTIO
oc apply -f install/kubernetes/istio.yaml

oc create -f install/kubernetes/addons/prometheus.yaml
oc create -f install/kubernetes/addons/grafana.yaml
oc create -f install/kubernetes/addons/servicegraph.yaml
oc create -f install/kubernetes/addons/zipkin.yaml
oc expose svc grafana
oc expose svc servicegraph
oc expose svc zipkin
oc expose svc prometheus
```

Do `oc get pod` to make sure everything is up and running.

References
----------

* [Evaluate Istio on OpenShift](https://blog.openshift.com/evaluate-istio-openshift/)

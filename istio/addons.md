
Istio Addons Installation
==================

* Deploy additional add-ons, namely Prometheus, Grafana, Service Graph and Zipkin.

```
oc login -u system:admin

oc project istio-system

oc create -f install/kubernetes/addons/prometheus.yaml
oc create -f install/kubernetes/addons/grafana.yaml
oc create -f install/kubernetes/addons/servicegraph.yaml
TODO: Jaeger
oc create -f install/kubernetes/addons/zipkin.yaml
oc expose svc grafana
oc expose svc servicegraph
oc expose svc zipkin
oc expose svc prometheus
```

Do `oc get pod` to make sure everything is up and running.

Add a user to the project so they can access from Web Console.

```oc adm policy add-role-to-user admin <YOUR USER>```

References
----------

* [Evaluate Istio on OpenShift](https://blog.openshift.com/evaluate-istio-openshift/)


Istio Addons Installation
==================

* Deploy additional add-ons, namely Prometheus, Grafana, Service Graph and Jaeger.

```
oc project istio-system

oc apply -f https://raw.githubusercontent.com/istio/istio/0.4.0/install/kubernetes/addons/prometheus.yaml
oc apply -f https://raw.githubusercontent.com/istio/istio/0.4.0/install/kubernetes/addons/grafana.yaml
oc apply -f https://raw.githubusercontent.com/istio/istio/0.4.0/install/kubernetes/addons/servicegraph.yaml
oc process -f https://raw.githubusercontent.com/jaegertracing/jaeger-openshift/master/all-in-one/jaeger-all-in-one-template.yml | oc apply -f -
oc expose svc grafana
oc expose svc servicegraph
oc expose svc prometheus
```

References
----------

* [Evaluate Istio on OpenShift](https://blog.openshift.com/evaluate-istio-openshift/)

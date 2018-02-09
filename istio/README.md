
Istio Installation
==================

```
oc new-project istio-system
curl -L https://git.io/getLatestIstio | sh -
oc adm policy add-scc-to-user anyuid -z istio-ingress-service-account -n istio-system
oc adm policy add-scc-to-user anyuid -z istio-egress-service-account -n istio-system
oc adm policy add-scc-to-user anyuid -z default -n istio-system
oc apply -f install/kubernetes/istio.yaml
oc expose svc istio-ingress
oc apply -f install/kubernetes/addons/prometheus.yaml
oc apply -f install/kubernetes/addons/grafana.yaml
oc apply -f install/kubernetes/addons/servicegraph.yaml
## Workaround for servicegraph bug https://github.com/istio/issues/issues/179
oc set image deploy/servicegraph servicegraph="docker.io/istio/servicegraph:0.4.0"
oc expose svc servicegraph
oc expose svc grafana
oc expose svc prometheus
oc process -f https://raw.githubusercontent.com/jaegertracing/jaeger-openshift/master/all-in-one/jaeger-all-in-one-template.yml | oc create -f -
```
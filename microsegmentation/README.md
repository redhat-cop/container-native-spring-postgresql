# Microsegmentation

Microsegmentation means creating network segments of one by creating custom firewall rules per pod.
In OpenShift this can be done using NetworkPolicy.
In order not to create network policy manually we will use a microsgmentation controller which will create the networkpolicies based on our services configuration.
More information on the network policy controller (which is not production ready) can be found [here](https://github.com/raffaelespazzoli/microsegmentationcontroller)

This microsgmentation controller is built on the metacontroller framework.

Deploy the metacontroller:
```
oc new-project metacontroller
oc apply -f https://raw.githubusercontent.com/GoogleCloudPlatform/kube-metacontroller/master/manifests/metacontroller-rbac.yaml
oc apply -f https://raw.githubusercontent.com/GoogleCloudPlatform/kube-metacontroller/master/manifests/metacontroller.yaml
```

Build and deploy the microsegmentation controller
```
oc new-build registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift~https://github.com/raffaelespazzoli/microsegmentationcontroller --name=microsegmentation-controller -F
oc apply -f https://github.com/raffaelespazzoli/microsegmentationcontroller/blob/master/src/main/kubernetes/microsegmentation-controller.yaml
```

Now, in order to have the networkpolicy objects created our services should be annotated, here is an example:
```
  annotations:
    io.raffa.microsegmentation: 'true'
    io.raffa.microsegmentation.additional-ports: 15000/tcp
```

notice the additional port that allows istio to control the envoy sidecar
    
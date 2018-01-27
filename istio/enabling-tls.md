# enabling mTLS between inventory and catalog

enabling mutual TLS authentication is Istio is as simple as adding an annotation to the service that groups the server pods.
The annotation will look like the following `auth.istio.io/8080: MUTUAL_TLS`
you can check the code HERE - TODO to see how it is applied
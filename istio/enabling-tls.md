# Enabling mTLS Between Inventory and Catalog

enabling mutual TLS authentication is Istio is as simple as adding an annotation to the service that groups the server pods.
The annotation will look like the following `auth.istio.io/8080: MUTUAL_TLS`

Here is how the product inventory catalog looks like:
```
apiVersion: v1
kind: Service
metadata:
  name: product-catalog
  annotations:
    auth.istio.io/8080: MUTUAL_TLS
spec:
  ports:
  - port: 8080
    protocol: TCP
    targetPort: 8080
  selector:
    app: product-catalog
```
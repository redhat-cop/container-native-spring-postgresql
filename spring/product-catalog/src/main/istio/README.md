Update handler-denier.yaml with your SSO external URL.

Run these commands:

```
oc project product
oc apply -f auth-policy.yaml
istioctl create -f handler-denier.yaml
```
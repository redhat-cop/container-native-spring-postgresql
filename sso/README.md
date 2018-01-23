Install Red Hat SSO 7.1 with PostgreSQL Persistent Volume
===============

Setup
-----

```
oc create -n openshift -f https://raw.githubusercontent.com/jboss-openshift/application-templates/master/jboss-image-streams.json

oc create -f sso/service.sso.yaml

oc policy add-role-to-user view system:serviceaccount:$(oc project -q):sso-service-account
```


References
------

* [Guide to using Red Hat JBoss SSO for OpenShift](https://access.redhat.com/documentation/en-us/red_hat_jboss_middleware_for_openshift/3/single/red_hat_jboss_sso_for_openshift/index)
* [Snowdrop SSO YAML](https://github.com/snowdrop/spring-boot-http-secured-booster/blob/master/service.sso.yaml)
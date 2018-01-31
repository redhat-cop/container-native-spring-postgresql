Install Red Hat SSO 7.1 with PostgreSQL Persistent Volume
===============

Setup
-----

```
$ oc new-project rh-sso
$ oc create -f sso/service.sso.yaml
$ oc policy add-role-to-user view system:serviceaccount:$(oc project -q):sso-service-account

```

Login & configuration
----------------
Navigate to the admin console using the newly created route and enter credential provided below:

```

https://secure-sso-rh-sso.LOCAL_OPENSHIFT_HOSTNAME


```
username: admin
password: 2ukFR5Kh

After logging in as admin please follow the below steps configuring RH-SSO to be used by applications:

Click Clients in the sidebar under master realm configuration then click Create; in the Client Id dialog enter inventoryservice, then click Save.

Once redirected to the settings tab in the Access Type drop-down menu, select confidential. 

Toggle the Direct Access Grants Enabled to On so the username/password can be exchanged for a token

In the Valid Redirect URIs dialog, enter the URI for product-frontend application route.

```

http://product-frontend-product.LOCAL_OPENSHIFT_HOSTNAME/*


```
In the Web Origins URIs dialog add * and click Save, this will allow any originating URI to authenticate the bearer token.

Then go to the Roles tab and click Add Role, enter the Role Name as user and click Save.

Click Users in the sidebar under master realm Manage then click Add User; in the Username dialog enter demouser, then click Save.

Once redirected to the Details tab navigate to the Credentials tab.

Enter a password 'password' and toggle Temporary to OFF, then click Reset Password. Confirm the change.

Navigate to the Role Mappings tab, under the Client Roles drop down select inventoryservice.

In the Available Roles box select user and click Add Selected button under the box to assign the role.

Test that you can ge the from the terminal: (You can copy the client_secret from the Credentials tab in the inventoryservice Client)

```

curl -sk -X POST https://secure-sso-rh-sso.LOCAL_OPENSHIFT_HOSTNAME/auth/realms/master/protocol/openid-connect/token -d grant_type=password -d username=demouser -d password=password -d client_id=inventoryservice -d client_secret=254deb12-6094-4860-ad1f-1b4aa3db62a0 | jq '.access_token'


```

References
------

* [Guide to using Red Hat JBoss SSO for OpenShift](https://access.redhat.com/documentation/en-us/red_hat_jboss_middleware_for_openshift/3/single/red_hat_jboss_sso_for_openshift/index)
* [Snowdrop SSO YAML](https://github.com/snowdrop/spring-boot-http-secured-booster/blob/master/service.sso.yaml)


Run Local Keycloak Docker Container
------

If you choose to run SSO/Keycloak locally outside of OpenShift, it is possible. Use version 2.5.1 of Keycloak which aligns with the version of Keycloak which RH SSO is built from:


`docker run -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin -p 8081:8080 jboss/keycloak:2.5.1.Final`

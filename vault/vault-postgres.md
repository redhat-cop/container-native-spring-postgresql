# configure the prostresql database secret backend

```
vault mount -tls-skip-verify database

vault write -tls-skip-verify database/config/postgresql \
    plugin_name=postgresql-database-plugin \
    allowed_roles="pg-readwrite" \
    connection_url="postgresql://postgres:admin@postgres-ha.pgo.svc.cluster.local:5432/userdb?sslmode=disable"
    
vault write -tls-skip-verify database/roles/pg-readwrite \
    db_name=postgresql \
    creation_statements="CREATE ROLE \"{{name}}\" WITH LOGIN PASSWORD '{{password}}' VALID UNTIL '{{expiration}}'; \
        GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO \"{{name}}\";" \
    default_ttl="1h" \
    max_ttl="24h"    
    
```

Create a policy that allows to read from role pg-readwrite
```
export VAULT_TOKEN=$ROOT_TOKEN
vault policy-write -tls-skip-verify pg-readwrite ./vault/app-policy.hcl 
```
Bind the policy to the `product-catalog` and the `product-inventory` role.
```
vault write -tls-skip-verify auth/kubernetes/role/product-catalog bound_service_account_names=default bound_service_account_namespaces='*' policies=pg-readwrite ttl=1h 
vault write -tls-skip-verify auth/kubernetes/role/product-inventory bound_service_account_names=default bound_service_account_namespaces='*' policies=pg-readwrite ttl=1h 
```

# Test the configuration

```
secret=`oc describe sa default | grep 'Tokens:' | awk '{print $2}'`
token=`oc describe secret $secret | grep 'token:' | awk '{print $2}'`
vault write -tls-skip-verify auth/kubernetes/login role=product-catalog jwt=$token
VAULT_TOKEN=`vault write -tls-skip-verify auth/kubernetes/login role=product-catalog jwt=$token | grep -w token | awk 'NR==1{print $2}'`
vault read -tls-skip-verify database/creds/pg-readwrite
```
you should see an output similar to this:
```
Key             Value
---             -----
lease_id        database/creds/pg-readwrite/93f0d34f-081c-dd99-e93a-f19c33ae75af
lease_duration  1h0m0s
lease_renewable true
password        A1a-96vrxq3uu7ru15yt
username        v-root-pg-readw-85z1u6urx2wx9t7pxv61-1516981399
```
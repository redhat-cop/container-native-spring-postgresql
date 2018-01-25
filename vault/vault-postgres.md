# configure the prostresql database secret backend

```
vault mount -tls-skip-verify database

vault write -tls-skip-verify database/config/postgresql \
    plugin_name=postgresql-database-plugin \
    allowed_roles="pg-readwrite" \
    connection_url="postgresql://postgres:admin@postgres-ha.pgo.svc.cluster.local:5432/<db>?sslmode=disable"
    
vault write -tls-skip-verify database/roles/pg-readwrite \
    db_name=<db> \
    creation_statements="CREATE ROLE \"{{name}}\" WITH LOGIN PASSWORD '{{password}}' VALID UNTIL '{{expiration}}'; \
        GRANT ALL_PRIVILEGES ON ALL TABLES IN SCHEMA public TO \"{{name}}\";" \
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
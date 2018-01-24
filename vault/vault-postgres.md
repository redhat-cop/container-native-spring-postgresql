# configure the prostresql database secret backend

```
vault write database/config/postgresql \
    plugin_name=postgresql-database-plugin \
    allowed_roles="pg-readwrite" \
    connection_url="postgresql://postgres:admin@postgres-ha.pgo.svc.cluster.local:5432/"
    
vault write database/roles/pg-readwrite \
    db_name=postgresql \
    creation_statements="CREATE ROLE \"{{name}}\" WITH LOGIN PASSWORD '{{password}}' VALID UNTIL '{{expiration}}'; \
        GRANT ALL_PRIVILEGES ON ALL TABLES IN SCHEMA public TO \"{{name}}\";" \
    default_ttl="1h" \
    max_ttl="24h"    
    
```

Create a policy that allows the spring-example role to read only from the spring-example generic backend
```
export VAULT_TOKEN=$ROOT_TOKEN
vault policy-write -tls-skip-verify pg-read-write ./vault/app-policy.hcl 
```
Bind the policy to the `app-name` role.
```
vault write -tls-skip-verify auth/kubernetes/role/app-name bound_service_account_names=default bound_service_account_namespaces='*' policies=pg-readwrite ttl=1h 
```
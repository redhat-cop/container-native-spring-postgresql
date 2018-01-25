# Configure the Kubernetes Authentication Backend for Vault

In this tutorial we are going to configure Vault to use the native [kubernetes autentication method](https://www.vaultproject.io/docs/auth/kubernetes.html).

The below picture shows the workflow.

TODO 

Assuming that vault is already installed in the Vault, you should have the `ROOT_TOKEN` variable initialized.
Follow the below steps to enable the Kubernetes back end authentication:
```
oc project vault
oc create sa vault-auth
oc adm policy add-cluster-role-to-user system:auth-delegator -z vault-auth
secret=`oc describe sa vault-auth | grep 'Tokens:' | awk '{print $2}'`
token=`oc describe secret $secret | grep 'token:' | awk '{print $2}'`
pod=`oc get pods | grep vault | awk '{print $1}'`
oc exec $pod -- cat /var/run/secrets/kubernetes.io/serviceaccount/ca.crt >> ca.crt
export VAULT_TOKEN=$ROOT_TOKEN
vault auth-enable -tls-skip-verify kubernetes
vault write -tls-skip-verify auth/kubernetes/config token_reviewer_jwt=$token kubernetes_host=https://kubernetes.default.svc:443 kubernetes_ca_cert=@ca.crt
rm ca.crt
```
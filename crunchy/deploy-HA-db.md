# create a postgres cluster
```
pgo create cluster postgres-ha --password admin
```
create the database objects by running the DDLs
This is a necessary steps to be execute outside of an app pod because the credential that the app uses will be rotated by Vault and it is not possible to delete an account that has credential objects.
We are going to create the DDLs manually. A more container native-approach would be to automate the creation at provisoning time potentially.
```
oc project pgo 
pod=`oc get pods | grep postgres-ha-[0-9] | awk '{print $1}'`
oc rsync ./crunchy/db/ $pod:/tmp --no-perms
oc rsh $pod  psql -U postgres -f /tmp/db.sql -d userdb

``` 
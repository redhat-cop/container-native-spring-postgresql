# Configring the app to use istio

Istio offeres two ways of injecting the necessary configuratio to allow your pod to join the service mesh

1. istioctl kube-inject. This doesn't work with DeploymentConfigs
2. kubernetes initializers. This is available only in kubernetes 1.9

None of these methods worked for us to we manually changed our deployment configs to add istio.
The relevant configurations are the following:
```
       initcontainers:
       ...
      - args:
        - -p
        - "15001"
        - -u
        - "1337"
        image: docker.io/istio/proxy_init:0.4.0
        imagePullPolicy: IfNotPresent
        name: istio-init
        resources: {}
        securityContext:
          capabilities:
            add:
            - NET_ADMIN
          privileged: true
      - args:
        - -c
        - sysctl -w kernel.core_pattern=/etc/istio/proxy/core.%e.%p.%t && ulimit -c
          unlimited
        command:
        - /bin/sh
        image: alpine
        imagePullPolicy: IfNotPresent
        name: enable-core-dump
        resources: {}
        securityContext:
          privileged: true
      ...
      containers:
      ...
      - args:
        - proxy
        - sidecar
        - -v
        - "2"
        - --configPath
        - /etc/istio/proxy
        - --binaryPath
        - /usr/local/bin/envoy
        - --serviceCluster
        - product-catalog
        - --drainDuration
        - 45s
        - --parentShutdownDuration
        - 1m0s
        - --discoveryAddress
        - istio-pilot.istio-system:15003
        - --discoveryRefreshDelay
        - 1s
        - --zipkinAddress
        - zipkin.istio-system:9411
        - --connectTimeout
        - 10s
        - --statsdUdpAddress
        - istio-mixer.istio-system:9125
        - --proxyAdminPort
        - "15000"
        - --controlPlaneAuthPolicy
        - NONE
        env:
        - name: POD_NAME
          valueFrom:
            fieldRef:
              fieldPath: metadata.name
        - name: POD_NAMESPACE
          valueFrom:
            fieldRef:
              fieldPath: metadata.namespace
        - name: INSTANCE_IP
          valueFrom:
            fieldRef:
              fieldPath: status.podIP
        image: docker.io/istio/proxy_debug:0.4.0
        imagePullPolicy: IfNotPresent
        name: istio-proxy
        resources: {}
        securityContext:
          privileged: true
          readOnlyRootFilesystem: false
          runAsUser: 1337
        volumeMounts:
        - mountPath: /etc/istio/proxy
          name: istio-envoy
        - mountPath: /etc/certs/
          name: istio-certs
          readOnly: true    
      ...
      volumes:
      ...
      - name: istio-envoy
        emptyDir:
          medium: Memory
          sizeLimit: "0"
      - name: istio-certs
        secret:
          optional: true
          secretName: istio.default    
```          
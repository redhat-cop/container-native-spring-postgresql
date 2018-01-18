FROM registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift
ENV JAVA_APP_DIR=/deployments JAVA_MAIN_CLASS=com.redhat.productcatalog.ProductCatalogApplication
EXPOSE 8080 8778 9779
COPY maven /deployments/

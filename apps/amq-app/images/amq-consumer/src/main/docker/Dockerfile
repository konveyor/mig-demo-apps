FROM registry.access.redhat.com/jboss-fuse-6/fis-java-openshift:latest
MAINTAINER Roman Martin Gil (rmarting@redhat.com)
ENV JAVA_APP_DIR=/deployments
EXPOSE 8080 8778 9779
COPY maven /deployments/
USER root
RUN chmod 644 /deployments/*.jar && chown jboss:root /deployments/*.jar
USER jboss

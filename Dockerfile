FROM openjdk:8-jdk-alpine

ARG JAVA_PACKAGE=java-8-openjdk-headless
ARG RUN_JAVA_VERSION=1.3.8
ENV LANG='en_US.UTF-8' LANGUAGE='en_US:en'
# Install java and the run-java script
# Also set up permissions for user `1001`

RUN apk update \
    && apk add --no-cache --update curl 

RUN mkdir /deployments \
    && chown 1001 /deployments \
    && chmod "g+rwX" /deployments \
    && chown 1001:root /deployments \
    && curl https://repo1.maven.org/maven2/io/fabric8/run-java-sh/${RUN_JAVA_VERSION}/run-java-sh-${RUN_JAVA_VERSION}-sh.sh -o /deployments/run-java.sh \
    && chown 1001 /deployments/run-java.sh \
    && chmod 540 /deployments/run-java.sh 


# Configure the JAVA_OPTIONS, you can add -XshowSettings:vm to also display the heap size.
ENV JAVA_OPTIONS="-XshowSettings:vm"
ENV JAVA_MAIN_CLASS="org.springframework.boot.loader.JarLauncher"

# We make four distinct layers so if there are application changes the library layers can be re-used
COPY --chown=1001 target/*.jar /deployments/

EXPOSE 8080
USER 1001

RUN cd /deployments && jar -xf spring-jbpm-test-0.0.1-SNAPSHOT.jar
RUN rm /deployments/spring-jbpm-test-0.0.1-SNAPSHOT.jar

WORKDIR /deployments

ENTRYPOINT [ "./run-java.sh" ]




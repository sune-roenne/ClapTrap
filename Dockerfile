FROM sbtscala/scala-sbt:eclipse-temurin-jammy-22_36_1.10.3_3.5.2 as build
COPY proxy-root.crt /usr/local/share/ca-certificates/proxy-root.pem
COPY forti-proxy.crt /usr/local/share/ca-certificates/forti-proxy.pem
RUN apt-get install ca-certificates
RUN update-ca-certificates
RUN keytool -import -trustcacerts -keystore /opt/java/openjdk/lib/security/cacerts -storepass changeit -noprompt -alias proxyroot -file /usr/local/share/ca-certificates/proxy-root.pem
RUN keytool -import -trustcacerts -keystore /opt/java/openjdk/lib/security/cacerts -storepass changeit -noprompt -alias fortiproxy -file /usr/local/share/ca-certificates/forti-proxy.pem
COPY src/ src/
COPY service-cert.pfx service-cert.pfx
COPY build.sbt build.sbt
COPY project/plugins.sbt project/plugins.sbt
COPY .sbtopts .sbtopts
ENV http_proxy http://webproxy.nykreditnet.net:8080/
ENV https_proxy http://webproxy.nykreditnet.net:8080/
RUN sbt assembly

#FROM eclipse-temurin:22-jammy as build
#COPY proxy-root.crt /usr/local/share/ca-certificates/proxy-root.pem
#COPY forti-proxy.crt /usr/local/share/ca-certificates/forti-proxy.pem
#RUN apt-get install ca-certificates
#RUN update-ca-certificates
#RUN keytool -import -trustcacerts -keystore /opt/java/openjdk/lib/security/cacerts -storepass changeit -noprompt -alias proxyroot -file /usr/local/share/ca-certificates/proxy-root.pem
#RUN keytool -import -trustcacerts -keystore /opt/java/openjdk/lib/security/cacerts -storepass changeit -noprompt -alias fortiproxy -file /usr/local/share/ca-certificates/forti-proxy.pem
#ENV USE_SYSTEM_CA_CERTS=1
#ENV JAVA_OPTS '-Dhttp.proxyHost=httpproxy.nykreditnet.net -Dhttp.proxyPort=8080 -Dhttp.nonProxyHosts=*.svc\|*.convoy.svc\|*.security.svc\|*.cluster.local\|localhost\|127.0.0.1\|nykprdweu1acr1.azurecr.io -Dhttps.proxyHost=httpproxy.nykreditnet.net -Dhttps.proxyPort=8080 -Dhttps.nonProxyHosts=*.svc\|*.convoy.svc\|*.security.svc\|*.cluster.local\|localhost\|127.0.0.1\|nykprdweu1acr1.azurecr.io'
#COPY src/ src/
#COPY service-cert.pfx service-cert.pfx
#COPY build.sbt build.sbt
#COPY .sbtopts .sbtopts
#COPY project/plugins.sbt project/plugins.sbt
#ENV http_proxy http://webproxy.nykreditnet.net:8080/
#ENV https_proxy http://webproxy.nykreditnet.net:8080/
#ENV http_proxyHost http://webproxy.nykreditnet.net/
#ENV http_proxyPort 80/
#ENV https_proxyHost http://webproxy.nykreditnet.net/
#ENV https_proxyPort 80/
#
#ENV https_proxy http://webproxy.nykreditnet.net:8080/
#ADD https://git.io/coursier-cli /usr/bin/cs
#RUN chmod a+rx /usr/bin/cs
#RUN cs update
#RUN cs setup
#RUN sbt assembly
#ENTRYPOINT ["tail", "-f", "/dev/null"]

FROM eclipse-temurin:22-jammy as app
EXPOSE 8081
COPY --chown=1 proxy-root.crt /usr/local/share/ca-certificates/proxy-root.pem
COPY --chown=1 forti-proxy.crt /usr/local/share/ca-certificates/forti-proxy.pem
RUN apt update
RUN apt-get install -y ca-certificates
RUN update-ca-certificates
RUN keytool -import -trustcacerts -keystore /opt/java/openjdk/lib/security/cacerts -storepass changeit -noprompt -alias proxyroot -file /usr/local/share/ca-certificates/proxy-root.pem
RUN keytool -import -trustcacerts -keystore /opt/java/openjdk/lib/security/cacerts -storepass changeit -noprompt -alias fortiproxy -file /usr/local/share/ca-certificates/forti-proxy.pem

#
#RUN apt-get -qq -y install curl
#RUN apt-get -qq -y install unzip
#RUN apt-get -qq -y install zip
#RUN apt-get install -y scala
##RUN curl -fL https://github.com/coursier/coursier/releases/latest/download/cs-x86_64-pc-linux.gz | gzip -d > cs && chmod +x cs && ./cs setup
##RUN apt upgrade
##RUN curl -k https://get.sdkman.io | bash
##RUN source $HOME/.sdkman/bin/sdkman-init.sh
#    #RUN cs install scala:3.5.2
##RUN sdk install scala 3.5.2
COPY --from=build target/scala-3.5.2/claptrap-app.jar_3-0.1.0.jar /app/claptrap-app.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "claptrap-app.jar"]
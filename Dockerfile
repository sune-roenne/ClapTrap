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
COPY --from=build /root/target/scala-3.5.2/claptrap-app-assembly.jar /app/claptrap-app.jar
COPY application.local.conf application.local.conf
WORKDIR /app
ENTRYPOINT ["java", "-jar", "claptrap-app.jar"]


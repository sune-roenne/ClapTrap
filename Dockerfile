FROM nykreditsurn/scalasbtpekko:1.0.0 as build
WORKDIR /app
COPY src/ src/
COPY build.sbt build.sbt
COPY project/plugins.sbt project/plugins.sbt
RUN sbt assembly
ENTRYPOINT ["tail", "-f", "/dev/null"]

FROM nykreditsurn/scalapekko:1.0.0 as app
EXPOSE 8080
WORKDIR /app
COPY --from=build /app/target/scala-3.5.2/claptrap-app-assembly.jar claptrap-app.jar
COPY application.docker.conf application.local.conf
COPY service-cert.pfx service-cert.pfx
ENTRYPOINT ["java", "-jar", "claptrap-app.jar"]
#ENTRYPOINT ["tail", "-f", "/dev/null"]


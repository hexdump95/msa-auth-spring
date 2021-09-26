FROM adoptopenjdk/openjdk11:alpine-jre AS base
WORKDIR /app
LABEL maintainer="Sergio Villanueva <sergiovillanueva@protonmail.com>"
EXPOSE 8080


FROM adoptopenjdk/openjdk11:alpine-jre AS builder
WORKDIR /app
COPY target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract


FROM base
COPY --from=builder app/dependencies/ .
COPY --from=builder app/spring-boot-loader/ .
COPY --from=builder app/snapshot-dependencies/ .
COPY --from=builder app/application/ .
CMD ["java", "-Dspring.profiles.active=prod", "org.springframework.boot.loader.JarLauncher"]

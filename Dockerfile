FROM cgr.dev/chainguard/jre:openjdk17.0.8.2 as builder
WORKDIR /app
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} application.jar
RUN ["java", "-Djarmode=layertools", "-jar", "application.jar", "extract"]

FROM cgr.dev/chainguard/jre:openjdk17.0.8.2
COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
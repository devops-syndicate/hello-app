FROM eclipse-temurin:21_35-jdk-ubi9-minimal as builder
WORKDIR /app
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} application.jar
RUN ["java", "-Djarmode=layertools", "-jar", "application.jar", "extract"]

FROM eclipse-temurin:21_35-jre-ubi9-minimal
COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

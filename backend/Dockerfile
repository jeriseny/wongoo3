# Build stage
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app
COPY . .

RUN chmod +x gradlew
RUN ./gradlew bootJar --no-daemon -x test

# Extract layered jar
RUN java -Djarmode=layertools -jar build/libs/*.jar extract --destination extracted

# Create custom JRE with jlink
RUN jlink \
    --add-modules java.base,java.logging,java.sql,java.naming,java.desktop,java.management,java.security.jgss,java.instrument,jdk.unsupported,jdk.crypto.ec,jdk.crypto.cryptoki \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=zip-6 \
    --output /custom-jre

# Runtime stage
FROM alpine:3.19

WORKDIR /app

# Install minimal dependencies
RUN apk add --no-cache fontconfig ttf-dejavu

# Copy custom JRE
COPY --from=build /custom-jre /opt/java

# Copy layered jar
COPY --from=build /app/extracted/dependencies/ ./
COPY --from=build /app/extracted/spring-boot-loader/ ./
COPY --from=build /app/extracted/snapshot-dependencies/ ./
COPY --from=build /app/extracted/application/ ./

ENV PATH="/opt/java/bin:$PATH"

EXPOSE 8080

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]

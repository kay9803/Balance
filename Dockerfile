# 1단계: 빌드용 컨테이너
FROM openjdk:17-jdk-slim AS builder

WORKDIR /app

COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean build --no-daemon

# 2단계: 실행용 컨테이너
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

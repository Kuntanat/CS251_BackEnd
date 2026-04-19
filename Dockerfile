# ─────────────────────────────────────────
#  Stage 1: Build
# ─────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# cache dependencies ก่อน (ถ้า pom.xml ไม่เปลี่ยน layer นี้จะถูก cache)
COPY pom.xml .
RUN mvn dependency:go-offline -q

# copy source และ build
COPY src ./src
RUN mvn package -DskipTests -q

# ─────────────────────────────────────────
#  Stage 2: Runtime
# ─────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# สร้าง non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# copy jar จาก build stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

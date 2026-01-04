# --- 1-й Етап: "Будівельник" (Збирає .jar файл) ---
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Копіюємо pom.xml і завантажуємо залежності
COPY pom.xml .
RUN mvn dependency:go-offline

# Копіюємо решту коду і збираємо .jar
COPY src ./src
RUN mvn package -DskipTests

# --- 2-й Етап: "Запускач" (Тонкий образ для запуску) ---
FROM eclipse-temurin:21-jre-alpine

RUN apk add --no-cache chromium chromium-chromedriver

ENV CHROME_BIN=/usr/bin/chromium-browser
ENV CHROME_DRIVER=/usr/bin/chromedriver

WORKDIR /app

# Копіюємо лише зібраний .jar файл з 1-го етапу
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
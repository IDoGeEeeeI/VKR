# Используйте Maven образ для сборки приложения
FROM maven:3.8.6-eclipse-temurin-17 AS build

# Установите рабочую директорию
WORKDIR /app

# Скопируйте проектные файлы
COPY . .

# Запустить подготовку фронтенда и сборку приложения
RUN mvn clean install -DskipTests vaadin:prepare-frontend vaadin:build-frontend

# Используйте OpenJDK образ для запуска приложения
FROM openjdk:17

# Установите рабочую директорию
WORKDIR /app

# Скопируйте собранный jar файл
COPY --from=build /app/target/*.jar /app/

# Запустите приложение
CMD java $JAVA_OPTS -jar *.jar

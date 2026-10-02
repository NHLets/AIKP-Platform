FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY . .

RUN mvn -pl backend -am clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /workspace/backend/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]

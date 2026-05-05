# ===== STAGE 1: BUILD =====
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copia tudo
COPY . .

# Gera o JAR
RUN mvn clean package -DskipTests

# ===== STAGE 2: RUN =====
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copia o jar gerado
COPY --from=build /app/target/*.jar app.jar

# Porta padrão do Spring
EXPOSE 8080

# Rodar aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
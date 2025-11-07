# Stage 1: Build the application
FROM eclipse-temurin:23-jdk AS builder

# Set the working directory
WORKDIR /app


# Copia el resto del código de la aplicación
COPY back-end/mvnw mvnw
COPY back-end/.mvn .mvn/
COPY back-end/pom.xml pom.xml
COPY back-end/src src/

# Da permisos de ejecución al wrapper (por si acaso)
RUN chmod +x mvnw

# Se empaqueta la aplicación sin ejecutar tests
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:23-jre

# Set the working directory
WORKDIR /app

# Copia el .jar generado
COPY --from=builder /app/target/*.jar app.jar

# Expone el puerto
EXPOSE 8080

# Comando para ejecutar el .jar
ENTRYPOINT ["java", "-jar", "app.jar"]


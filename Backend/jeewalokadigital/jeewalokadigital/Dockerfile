# Stage 1: Build the application using Maven, version as in local
FROM maven:3.9.2-openjdk-17 AS build

# Set the working directory inside the build stage
WORKDIR /build-app

# Copy the Maven project file first to leverage Docker layer caching
COPY pom.xml .

# Download dependencies (this layer is cached unless pom.xml changes)
RUN mvn dependency:go-offline -B

# Copy the rest of the application source code
COPY src ./src

# Package the application (compile, test, create JAR)
# Skip tests for faster build in this context, adjust if needed
RUN mvn package -DskipTests

# Stage 2: Create the final runtime image using a slim JRE
FROM openjdk:17-jdk-slim

# Set the working directory in the final image
WORKDIR /app

# Copy the built JAR file from the 'build' stage
# Adjust the path if your JAR name or location in target is different
COPY --from=build /build-app/target/jeewalokadigital-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Command to run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]

# Use an Ubuntu parent image
FROM ubuntu:16.04

# Install Java and Maven
RUN apt-get update && apt-get install -y \
default-jdk \
maven

# Set working directory to app and add code
WORKDIR /app
ADD . /app

# Expose port 8080
EXPOSE 8080

# Make mvnw executable
RUN chmod +x mvnw

# Command on run
CMD ./mvnw spring-boot:run

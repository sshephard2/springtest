# Sample Spring Boot RESTful API

## Build instructions

Build with Docker

    docker build -t customer .

Run Docker images

    docker run -p 8080:8080 customer

## Access API documentation and test (with Swagger)

From a web browser:

    http://{host}:8080/swagger-ui.html

Where `{host}` is the hostname or IP address of the running Docker container
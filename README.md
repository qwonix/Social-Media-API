# Social Media RESTful API
## Introduction

Welcome to the Social Media RESTful API project! This API is designed as a test case for a social network, providing
features such as user registration, login, post management, engagement with other users, subscriptions, and an activity
feed.

## Project Objective

Create a RESTful API for a social network that allows users to register, log in,
create and handle posts, engage with other users, subscribe, and view an activity feed.

### Key Specifications

* Implement robust authentication and authorization using JWT for data security.
* Develop functionalities for managing posts, including creation, viewing, updating, and deletion,
  along with the capability to upload images.
* Provide user-centric features such as messaging, subscription options,
  and an activity feed to showcase recent posts from person whom the user is following.
* Ensure effective error handling mechanisms are in place, and thoroughly document the API for clarity and ease of use.

## Technologies Used

* Java 17
* [Spring Boot (REST, Web, Security)](https://spring.io/)
* [Hibernate](https://hibernate.org/)
* [JSON Web Token (JWT)](https://jwt.io/)
* [PostgreSQL](https://www.postgresql.org/)
* [Flyway](https://flywaydb.org/)
* [Testcontainers](https://testcontainers.com/)
* [Docker Compose](https://docs.docker.com/compose/)
* [OpenAPI Documentation](https://springdoc.org/)

## Exploring OpenAPI Documentation with Swagger UI

Explore the interactive API documentation and endpoints using the OpenAPI specification,
hosted with Swagger UI on [GitHub Pages](https://qwonix.github.io/social-media-api).

## Configuration Properties

Configure the behavior of JWT access tokens using the following properties:

| Property Name       | Description                   | Default Value    | Allowed Values                                                                                                                                                           |
|---------------------|:------------------------------|------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `jwt.access.secret` | Secret key for JWT signatures | random generated | String (> 32 characters)                                                                                                                                                 |
| `jwt.access.ttl`    | Token expiration time         | `1 hour`         | [Duration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config.typesafe-configuration-properties.conversion.durations) |

### Property Details

#### `jwt.access.ttl`

Token expiration time in hours. For duration conversion details, see
the [Spring Converting Durations Guide](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config.typesafe-configuration-properties.conversion.durations).

#### `jwt.access.secret`

Secret key for JWT signatures (base64 encoded). Custom secret should be ≥ 32 bytes as per
the [JWT JWA Specification (RFC 7518, Section 3.2)](https://tools.ietf.org/html/rfc7518#section-3.2).

## Download the Application

Get the latest version of the application from the [release page](https://github.com/qwonix/social-media-api/releases).

## Usage

### Prerequisites

Ensure you have Maven, Docker, and Docker Compose installed.
Exercise caution when initiating the application;
it employs spring-boot-docker-compose under the hood.

### Integration Testing

The API is equipped with a suite of 57 integration tests that cover various endpoints and scenarios.
These tests are designed to ensure that the API functions as expected,
handling different input cases and returning the correct responses.
To run the integration tests, follow these steps:

1. Make sure the application is cloned and Docker is running.
2. Open a terminal and navigate to the `social-media-api/` directory.
3. Run the integration tests using Maven:

```shell
mvn -Dtest=*IT test
```

The test results will be displayed in the terminal, allowing you to verify the correctness of the API's behavior.

### Installation

1. Clone the repository
2. Navigate to the project directory:

``` shell
git clone https://github.com/qwonix/social-media-api.git
cd social-media-api/
```

3. Build App, Docker images and start the containers using Docker Compose:

``` dockerfile
mvn package
docker build -t qwonix/social-media-api:1.0.1 .
docker-compose -f compose.yml up -d
```

### Using Docker Hub Image

Download [`compose.yaml`](/compose.yaml) and use Docker Compose

```dockerfile
docker-compose -f compose.yaml up -d
```

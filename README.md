[![Project Status](https://img.shields.io/badge/swagger-AVAILABLE_TEST_SERVER-salad.svg?style=for-the-badge)](http://212.57.126.49/swagger-ui/index.html)

# Social Media RESTful API

The application is designed as a test case.

## Project Objective

Create a RESTful API for a social network that allows users to register, log in,
create and handle posts, engage with other users, subscribe, and view an activity feed.

### Key Specifications:

* Implement robust authentication and authorization using JWT for data security.
* Develop functionalities for managing posts, including creation, viewing, updating, and deletion,
  along with the capability to upload images.
* Provide user-centric features such as messaging, subscription options,
  and an activity feed to showcase recent posts from person whom the user is following.
* Ensure effective error handling mechanisms are in place, and thoroughly document the API for clarity and ease of use.

## Technologies

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

For interactive exploration of API documentation and endpoints,
the OpenAPI specification is available and has been hosted with Swagger UI
on [GitHub Pages](https://qwonix.github.io/social-media-api).

## Configuration Properties

The following configuration properties control the behavior of JWT access tokens in the application.

| Property Name       | Description                                            | Default Value    | Allowed Values                                                                                                                                                               |
|---------------------|:-------------------------------------------------------|------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `jwt.access.secret` | Secret key for generating and verifying JWT signatures | random generated | String longer than 32 characters                                                                                                                                             |
| `jwt.access.ttl`    | Token expiration time                                  | `1 hour`         | [Any duration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config.typesafe-configuration-properties.conversion.durations) |

### Property Details

#### `jwt.access.ttl`

Token expiration time. The value is specified in hours. For more information on how durations are converted, refer to
the [Spring Converting Durations Guide](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config.typesafe-configuration-properties.conversion.durations).

#### `jwt.access.secret`

Secret key for generating and verifying JWT signatures using the HMAC-SHA algorithm. The key is encoded in base64. If
providing a custom secret, make sure it has a length of at least 256 bits (32 bytes) as mandated by
the [JWT JWA Specification (RFC 7518, Section 3.2)](https://tools.ietf.org/html/rfc7518#section-3.2).


## Download the application

The latest version of the application can be downloaded
from [release page](https://github.com/qwonix/social-media-api/releases).

## Usage

### Prerequisites

Before you begin, make sure you have Maven, Docker and Docker Compose installed on your system.

### Installation

1. Clone the repository containing the application source code.
2. Navigate to the project directory:
``` shell
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
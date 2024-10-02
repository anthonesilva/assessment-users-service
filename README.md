<h1 align="center">
  <br>
  Users Service
  <br>
</h1>

<h4 align="center">Users microservice to handle system's users</h4>

<img src="https://github.com/anthonesilva/assessment-users-service/blob/main/docs/arch.svg">
<br>

> **Note**  
> If the architectural design image above is not available, it can be found in the /docs folder named "arch.svg"

<p align="center">
  <a href="#main-technologies">Main Technologies</a> •
  <a href="#how-to-run">How To Run</a> •
  <a href="#api-documentation">API Documentation</a> •
  <a href="#license">License</a>
</p>


## Main Technologies

The project has been developed with the following technologies:

* **Java 17**
* **Spring Boot 3.3.4**
* **Maven**
* **JUnit**
* **MySQL**
* **OAS3**
* **Docker**


## How To Run

To clone and run this application, you'll need Java 17, Maven, Docker and docker-compose installed on your computer. From your command line:

```bash
# Clone this repository
$ git clone https://github.com/anthonesilva/assessment-users-service

# Go into the repository
$ cd assessment-users-service

# Build jar package with maven
$ mvn package

# Run the app with docker-compose
$ docker-compose up --build
```

> **Note:**   
> The application is going to be listening in the port <b>8088</b>.


## API Documentation
After up and running the API documentation would be available on:

```bash
http://localhost:8088/swagger-ui/
```

> **Note:**   
> Insomnia collection also available in the /docs folder named "users-service-collection".

<h3>Available resources:</h4>

<h5>Finds all users and friends</h5>

```bash
GET /users
```

<h5>Finds an existing user by identifier</h5>

```bash
GET /users/{userId}
```

<h5>Creates users and friends list</h5>

```bash
POST /users
```

<h5>Adds a friend to an user</h5>

```bash
POST /users/{userId}/friends/{friendId}
```

<h5>Updates users</h5>

```bash
PATCH /users/{userId}
```

<h5>Removes an existing user and respective friends</h5>

```bash
DELETE /users/{userId}
```

<h5>Removes an existing friend from an existing user</h5>

```bash
DELETE /users/{userId}/friends/{friendId}
```

## License

MIT


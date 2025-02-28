# Simple-SpringBoot-REST-Service-Example

This project is a simple RESTful API built with [Spring Boot](https://spring.io/projects/spring-boot). It demonstrates how to create a lightweight REST service that handles various HTTP requests (GET, POST, and query parameters) using Spring Boot. The project is built with Maven or Gradle and includes integration tests using Spring Boot’s testing framework and REST Assured.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Project Setup and Creation](#project-setup-and-creation)
- [Installation and Running the Application](#installation-and-running-the-application)
- [Endpoints](#endpoints)
- [Package Configuration](#package-configuration)
- [Testing](#testing)
- [Code Explanation](#code-explanation)
- [Dependencies](#dependencies)

---



## Project Overview

**Simple-SpringBoot-REST-Service-Example** is designed as a beginner-friendly reference project to demonstrate how to build RESTful APIs using Spring Boot. It covers:
- Creating a Spring Boot project (via Spring Initializr or manual setup).
- Configuring the application with Spring Boot’s auto-configuration and dependency injection.
- Defining REST endpoints using path parameters, query parameters, and JSON request bodies.
- Writing integration tests that run against an embedded server using REST Assured.

---

## Project Setup and Creation

You can create the project using one of the following methods:

### Option 1: Using Spring Initializr

1. Visit [https://start.spring.io/](https://start.spring.io/).
2. Configure your project:
    - **Project:** Maven or Gradle (choose Gradle for this example)
    - **Language:** Java
    - **Spring Boot:** Use the current stable version (e.g., 3.4.3)
    - **Group:** `dim.kal`
    - **Artifact:** `simple-springboot-rest-service-example`
    - **Dependencies:** Add **Spring Web** (for REST endpoints), and **Spring Boot Starter Test**.
3. Click **Generate** to download the ZIP file.
4. Extract the ZIP file into your desired project folder.

### Option 2: Manual Setup

1. **Create a New Project Folder:**  
   Create a folder and navigate into it.
2. **Initialize Your Gradle Project:**  
   Use your IDE or run:
   ```bash
   gradle init --type java-application
   ```
3. **Add Spring Boot Dependencies:**
Update your build.gradle (see the Package Configuration section below).
4. **Configure the Project Structure:**
Place your source code in src/main/java and your configuration files in src/main/resources. Your tests go in `src/test/java`.

---
## Installation and Running the Application
### Building the Application
From the root directory, run:

- **Gradle:**
```bash
./gradlew clean build
```
### Running in Development Mode
Spring Boot runs with an embedded server. Start the application with:
- **Gradle:**
```bash
./gradlew bootRun
```
By default, the application will be available at http://localhost:8089 as specified in `application.properties`.

### Running in Production Mode
Run the packaged JAR file:

```bash
java -jar build/libs/simple-springboot-rest-service-example-0.0.1-SNAPSHOT.jar
```
## Endpoints
### Root Endpoint
- **Path:** /
- **Method:** GET
- **Description:** Returns a static greeting message.
- **Response Example:**
```json
{
"msg": "Hello World!"
}
```
### Path Parameter Greeting
- **Path:** /greet/{name}
- **Method:** GET
- **Description:** Returns a greeting that includes the provided name.
- **Response Example:**
```json
{
"msg": "Hello Harry!",
}
```
### Query Parameter Greeting
- **Path:** /greeting
- **Method:** GET
- **Description:** Returns a personalized greeting using name and age query parameters. Returns HTTP 400 if parameters are missing.
- **Response Example:**
```json
{
"msg": "Hello my name is John and i'm 30 years old!",
}
```
### Request Body Greeting
- **Path:** /greeting
- **Method:** POST
- **Description:** Accepts a JSON request body containing name and age and returns a personalized greeting. Returns HTTP 400 for invalid request bodies.
- **Response Example:**
```json
{
"msg": "Hello my name is Bob Trench and i'm 25 years old!",
}
```

---
## Package Configuration
### Build Configuration (build.gradle)
```gradle
plugins {
id 'java'
id 'org.springframework.boot' version '3.4.3'
id 'io.spring.dependency-management' version '1.1.7'
}

group = 'dim.kal'
version = '0.0.1-SNAPSHOT'

java {
toolchain {
languageVersion = JavaLanguageVersion.of(17)
}
}

repositories {
mavenCentral()
}

dependencies {
implementation 'org.springframework.boot:spring-boot-starter-web'
testImplementation 'org.springframework.boot:spring-boot-starter-test'
testImplementation('io.rest-assured:rest-assured:5.3.0')
testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

tasks.named('test') {
useJUnitPlatform()
}
```
This configuration sets up the Spring Boot application with an embedded web server, configures the necessary dependencies, and enables JUnit 5 testing with REST Assured for integration tests.
---
## Testing
The project includes integration tests that use Spring Boot’s testing framework and REST Assured to perform real HTTP requests against the embedded server.

### Running Tests
- **Gradle:**
```bash
./gradlew clean test
```
### Example Test Scenarios
- **Hello World Test:**
Verifies that a GET request to `/` returns HTTP 200 with the message "Hello World!".

- **Path Parameter Test:**
Iterates over an array of names to test the `/greet/{name}` endpoint, ensuring that the correct greeting is returned.

- **Query Parameter Test:**
Uses a two-dimensional array to test the `/greeting` endpoint with valid and invalid query parameters.

- **POST Request Test:**
Verifies that sending a valid JSON body to `/greeting` returns the expected greeting and that invalid request bodies return error codes.

The tests use the fluent when-then syntax of REST Assured along with logging (using .log().all()) for debugging purposes.

---
## Code Explanation
### REST Controller (RestResource.java)
```java
package dim.kal.Spring_Rest_Service_Example.Resource;

import dim.kal.Spring_Rest_Service_Example.Model.Greeting;
import dim.kal.Spring_Rest_Service_Example.Model.GreetingRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class RestResource {

    @RequestMapping(value = "/", produces = "application/json")
    public Greeting helloWorld() {
        return new Greeting("Hello World!");
    }

    @GetMapping(value = "/greet/{name}", produces = "application/json")
    public Greeting pathParamGreeting(@PathVariable String name) {
        return new Greeting("Hello " + name + "!");
    }

    @GetMapping("/greeting")
    public Greeting queryParamGreeting(@RequestParam String name, @RequestParam Integer age) {
        return new Greeting("Hello my name is " + name + " and i'm " + age + "years old!");
    }

    @PostMapping(value = "/greeting", produces = "application/json", consumes = "application/json")
    public Greeting bodyRequestGreeting(@RequestBody GreetingRequest reqBody) {
        if (reqBody.getName() == null || reqBody.getAge() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required fields");
        }
        return new Greeting("Hello my name is " + reqBody.getName() + " and i'm " + reqBody.getAge() + " years old!");
    }
}
```
This controller defines endpoints to return greeting messages. It uses Spring’s @RestController annotation to indicate that it’s a RESTful controller and maps various HTTP requests to methods. Each method returns a Greeting object that is automatically converted to JSON.

### Model Classes
- **Greeting.java**

```java
package dim.kal.Spring_Rest_Service_Example.Model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Greeting {
private String msg;
private String name;
private Integer age;

    public Greeting() {
    }

    public Greeting(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
```

- **GreetingRequest.java**

```java
package dim.kal.Spring_Rest_Service_Example.Model;

public class GreetingRequest {
private String name;
private Integer age;

    public GreetingRequest(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public GreetingRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
```
The Greeting class represents the JSON response, and thanks to the @JsonInclude annotation, any null values are omitted. The GreetingRequest class represents the JSON payload for POST requests.

- **Test Class (RestResourceSpec.java)**
```java
package dim.kal.Spring_Rest_Service_Example.Resource;

import dim.kal.Spring_Rest_Service_Example.Model.Greeting;
import dim.kal.Spring_Rest_Service_Example.Model.GreetingRequest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestResourceSpec {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp(){
        RestAssured.port = port;
    }

    @Test
    public void testHelloWorldEndpoint(){
        Response response = given()
            .log().all()
            .when()
            .get("/")
            .then()
            .log().all()
            .statusCode(200)
            .extract().response();

        Greeting greeting = response.as(Greeting.class);
        assertEquals("Hello World!", greeting.getMsg(), "Greeting should equal 'Hello World!'");
    }

    @Test
    public void pathParamsGreeting(){
        String[] names = {"Harry", "Bob", "Dylan", "Mary"};
        for (String name : names) {
            Response response = given()
                    .pathParams("name", name)
                    .log().all()
                    .when()
                    .get("/greet/{name}")
                    .then()
                    .log().all()
                    .statusCode(200)
                    .extract().response();

            Greeting greeting = response.as(Greeting.class);
            assertEquals("Hello " + name + "!", greeting.getMsg(), "Greeting should equal 'Hello " + name + "!'");
        }
    }

    @Test
    public void queryParamsGreeting(){
        Object[][] values = {{"John", 33}, {"Mike", 40}, {"Alex", 42}};
        for (Object[] row : values) {
            String name = (String) row[0];
            Integer age = (Integer) row[1];
            Response response = given()
                    .queryParam("name", name)
                    .queryParam("age", age)
                    .when()
                    .get("/greeting")
                    .then()
                    .log().all()
                    .statusCode(200)
                    .extract().response();

            Greeting greeting = response.as(Greeting.class);
            assertEquals("Hello my name is " + name + " and i'm " + age + "years old!", greeting.getMsg());
        }
    }

    @Test
    public void bodyReqGreeting(){
        GreetingRequest reqBody1 = new GreetingRequest("John", 33);
        GreetingRequest reqBody2 = new GreetingRequest("Paul", 34);
        GreetingRequest reqBody3 = new GreetingRequest("Alexander", 35);
        GreetingRequest[] greetingRequests = {reqBody1, reqBody2, reqBody3};

        for (GreetingRequest reqBody : greetingRequests) {
            Response response = given()
                    .contentType("application/json")
                    .body(reqBody)
                    .log().all()
                    .when()
                    .post("/greeting")
                    .then()
                    .log().all()
                    .extract().response();

            Greeting greeting = response.as(Greeting.class);
            assertEquals("Hello my name is " + reqBody.getName() + " and i'm " + reqBody.getAge() + " years old!", greeting.getMsg());
        }
    }
}
```
This test class uses `@SpringBootTest` with a random port to launch an embedded server. It configures REST Assured to target that port, and it uses the fluent when-then syntax (with logging) to send HTTP requests and verify responses. The tests cover:

- GET requests to the root endpoint
- GET requests with path parameters
- GET requests with query parameters
- POST requests with a JSON body 
- ---
## Dependencies
Key dependencies in this project are:

- **Spring Boot Starter Web:** For building RESTful web services.
- **Spring Boot Starter Test:** For testing support (JUnit 5, Mockito, etc.).
- **REST Assured:** For integration testing of REST endpoints.
- **Jackson:** For JSON serialization/deserialization (automatically included with Spring Boot Starter Web).

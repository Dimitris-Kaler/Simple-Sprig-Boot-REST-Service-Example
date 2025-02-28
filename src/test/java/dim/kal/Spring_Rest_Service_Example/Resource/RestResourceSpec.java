package dim.kal.Spring_Rest_Service_Example.Resource;


import dim.kal.Spring_Rest_Service_Example.Model.Greeting;
import dim.kal.Spring_Rest_Service_Example.Model.GreetingRequest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestResourceSpec {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp(){
        RestAssured.port=port;

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
    assertEquals("Hello World!",greeting.getMsg(),"Greeting should equal 'Hello World!'");



    }

    @Test
    public void pathParamsGreeting() {
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
            assertEquals("Hello "+name+"!", greeting.getMsg(), "Greeting should equal 'Hello "+name+"!'");

        }
    }

    @Test
    public void queryParamsGreeting(){
        Object[][] values={{"John",33},{"Mike",40},{"Alex",42}};

        for(Object[] row:values){
            String name = (String)row[0];
            Integer age = (Integer)row[1];

            Response response = given()
                    .queryParam("name",name)
                    .queryParam("age",age)
                    .when()
                    .get("/greeting")
                    .then()
                    .log().all()
                    .statusCode(200)
                    .extract().response();

            Greeting greeting = response.as(Greeting.class);
            assertEquals("Hello my name is " + name + " and i'm " + age + "years old!",greeting.getMsg());
        }
    }

    @Test
    public void bodyReqGreeting(){
        GreetingRequest reqBody1= new GreetingRequest("John",33);
        GreetingRequest reqBody2= new GreetingRequest("Paul",34);
        GreetingRequest reqBody3= new GreetingRequest("Alexander",35);

        GreetingRequest[] greetingRequests={reqBody1,reqBody2,reqBody3};

        for(GreetingRequest reqBody:greetingRequests){
            Response response = given()
                    .contentType("application/json")
                    .body(reqBody)
                    .log().all()
                    .when()
                    .post("/greeting")
                    .then()
                    .log().all()
                    .extract().response();

            Greeting greeting =response.as(Greeting.class);
            assertEquals("Hello my name is " + reqBody.getName() + " and i'm " + reqBody.getAge() + " years old!",greeting.getMsg());
        }
    }
}

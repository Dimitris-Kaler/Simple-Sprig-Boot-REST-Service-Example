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

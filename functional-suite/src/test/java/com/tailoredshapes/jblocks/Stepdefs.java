package com.tailoredshapes.jblocks;


import cucumber.api.java8.En;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static com.tailoredshapes.underbar.IO.slurp;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Stepdefs implements En {

    private Response response;
    private RequestSpecification rs = given();

    public Stepdefs() {
        Given("I have I have the file {string}", (String filename) ->
            rs.body(slurp(getClass().getResourceAsStream(filename))));
        When("I post it to the service$", () ->
            response = rs.post("/checkpayload"));
        Then("I should receive a {int}", (Integer statusCode) -> {
            response.then().statusCode(statusCode);
        });
        Then("the body should be {string}", (String filename) -> {
            String expectedBody = slurp(getClass().getResourceAsStream(filename));
            response.then().body(equalTo(expectedBody));
        });
    }
}

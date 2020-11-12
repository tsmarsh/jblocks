package com.tailoredshapes.jblocks;

import static com.tailoredshapes.underbar.IO.slurp;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;
import static spark.Service.ignite;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Service;


public class AppTest {

    private static App app;

    @BeforeClass
    public static void setUp() throws Exception {
        app = new App(8066, "[.items[] | select(.status == \"doing\").body]", "");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        app.service.stop();
    }

    @Test
    public void canQueryAGoodJSON()throws Exception{
        String good = slurp(getClass().getResourceAsStream("/good.json"));
        given().port(8066).body(good).
                when().post("/").
                then().statusCode(200);
    }
}


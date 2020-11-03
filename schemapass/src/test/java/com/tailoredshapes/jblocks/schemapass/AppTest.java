package com.tailoredshapes.jblocks.schemapass;

import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Service;

import java.io.InputStream;

import static com.tailoredshapes.underbar.IO.slurp;
import static io.restassured.RestAssured.given;
import static spark.Service.ignite;

public class AppTest {

    private static App app;

    static Service testServer;

    @BeforeClass
    public static void setUp() throws Exception {
        testServer = ignite();
        testServer.port(7070);
        testServer.post("/", (req, resp) -> req.body());

        try (InputStream inputStream = AppTest.class.getResourceAsStream("/schema.json")) {
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));

            app = new App(8066, SchemaLoader.load(rawSchema), "http://localhost:7070/");
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        testServer.stop();
        app.service.stop();
    }

    @Test
    public void canValidateAGoodJSON()throws Exception{
        String good = slurp(getClass().getResourceAsStream("/good.json"));
        given().port(8066).body(good).
                when().post("/").
                then().statusCode(200);
    }

    @Test
    public void canValidateABadJSON()throws Exception{
        String good = slurp(getClass().getResourceAsStream("/bad.json"));
        given().port(8066).body(good).
                when().post("/").
                then().statusCode(400);
    }
}

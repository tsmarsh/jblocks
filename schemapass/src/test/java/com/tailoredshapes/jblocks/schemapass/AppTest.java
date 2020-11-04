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
    private static int testPort = 7073;
    private static int port = 8070;

    @BeforeClass
    public static void setUp() throws Exception {
        testServer = ignite();
        testServer.port(testPort);
        testServer.post("/", (req, resp) -> req.body());

        try (InputStream inputStream = AppTest.class.getResourceAsStream("/schema.json")) {
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));

            app = new App(port, SchemaLoader.load(rawSchema), "http://localhost:" + testPort);
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
        given().port(port).body(good).
                when().post("/").
                then().statusCode(200);
    }

    @Test
    public void canValidateABadJSON()throws Exception{
        String good = slurp(getClass().getResourceAsStream("/bad.json"));
        given().port(port).body(good).
                when().post("/").
                then().statusCode(400);
    }
}

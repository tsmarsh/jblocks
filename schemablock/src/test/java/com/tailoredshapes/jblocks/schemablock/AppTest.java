package com.tailoredshapes.jblocks.schemablock;

import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;

import static com.tailoredshapes.underbar.IO.slurp;
import static io.restassured.RestAssured.given;

public class AppTest {

    private static App app;
    static int port = 8068;

    @BeforeClass
    public static void setUp() throws Exception {
        try (InputStream inputStream = AppTest.class.getResourceAsStream("/schema.json")) {
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));


            app = new App(port, SchemaLoader.load(rawSchema));
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
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

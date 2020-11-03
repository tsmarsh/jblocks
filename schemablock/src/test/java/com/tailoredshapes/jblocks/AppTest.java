package com.tailoredshapes.jblocks;

import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;

import static com.tailoredshapes.underbar.IO.slurp;
import static io.restassured.RestAssured.given;

public class AppTest {

    @BeforeClass
    public static void setUp() throws Exception {
        try (InputStream inputStream = AppTest.class.getResourceAsStream("/schema.json")) {
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));

            new App(8066, SchemaLoader.load(rawSchema));
        }
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

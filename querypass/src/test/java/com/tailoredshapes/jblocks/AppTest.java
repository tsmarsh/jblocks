package com.tailoredshapes.jblocks;

import static com.tailoredshapes.underbar.IO.slurp;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;
import static spark.Service.ignite;

import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Service;

import java.io.InputStream;

public class AppTest {

    private static App app;

    static Service testServer;
    private static int testPort = 7070;
    private static int port = 8067;


    @BeforeClass
    public static void setUp() throws Exception {
        testServer = ignite();
        testServer.port(testPort);
        testServer.post("/", (req, resp) -> req.body());



        app = new App(
                port,
                "[.items[] | select(.status == \"doing\").body]",
                "", "http://localhost:" +
                testPort + "/");
    }

    @AfterClass
    public static void tearDown() {
        testServer.stop();
        app.service.stop();
    }

    @Test
    public void canQueryAGoodJSON(){
        var good = slurp(getClass().getResourceAsStream("/good.json"));
        given().port(port).body(good).
                when().post("/").
                then().statusCode(200);
    }
}

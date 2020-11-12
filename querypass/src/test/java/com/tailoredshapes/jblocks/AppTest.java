package com.tailoredshapes.jblocks;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Service;

import static com.tailoredshapes.underbar.IO.slurp;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static spark.Service.ignite;

public class AppTest {

    private static App app;

    private static Service testServer;
    private static int testPort = 7070;
    private static int port = 8067;
    private static String receivedBody;


    @BeforeClass
    public static void setUp() throws Exception {
        testServer = ignite();
        testServer.port(testPort);
        testServer.post("/", (req, resp) -> {
            receivedBody = req.body();
            return "bounced";
        });


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
    public void canQueryAGoodJSON() {
        var good = slurp(getClass().getResourceAsStream("/good.json"));
        var expected = slurp(getClass().getResourceAsStream(("/expected.json")));

        given().port(port).body(good).
                when().post("/").
                then()
                .body(equalTo("[\"bounced\"]"))
                .statusCode(200);
        assertEquals(expected, receivedBody);
    }
}

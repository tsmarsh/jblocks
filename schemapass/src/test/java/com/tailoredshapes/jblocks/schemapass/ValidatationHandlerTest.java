package com.tailoredshapes.jblocks.schemapass;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import spark.Request;
import spark.Response;
import spark.Service;

import java.io.InputStream;

import static com.tailoredshapes.underbar.IO.slurp;
import static org.mockito.Mockito.*;
import static spark.Service.ignite;

@RunWith(MockitoJUnitRunner.class)
public class ValidatationHandlerTest {

    @Mock
    Request request;

    @Mock
    Response response;

    @Mock
    static Response nextResponse;

    static Service testServer;


    @BeforeClass
    public static void setUp() throws Exception {
        testServer = ignite();
        testServer.port(6060);
        testServer.post("/", (req, resp) -> {
            nextResponse.body(req.body());
            return "OK";
        });

    }

    @AfterClass
    public static void tearDown() throws Exception {
        testServer.stop();
    }

    @Test
    public void canValidateAGoodJSON()throws Exception{
        Schema schema;

        String good = slurp(getClass().getResourceAsStream("/good.json"));

        when(request.body()).thenReturn(good);

        try (InputStream inputStream = getClass().getResourceAsStream("/schema.json")) {
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
            schema = SchemaLoader.load(rawSchema);
        }

        ValidatationHandler validatationHandler = new ValidatationHandler(schema, "http://localhost:6060/");
        validatationHandler.handle(request, response);

        verify(response).status(200);
        verify(nextResponse).body(good);
    }

    @Test
    public void canDetailProblemsWithBadJSON()throws Exception{
        Schema schema;

        String errors = "{\n" +
                "  \"schemaLocation\": \"#\",\n" +
                "  \"pointerToViolation\": \"#\",\n" +
                "  \"causingExceptions\": [\n" +
                "    {\n" +
                "      \"schemaLocation\": \"#/properties/owner\",\n" +
                "      \"pointerToViolation\": \"#/owner\",\n" +
                "      \"causingExceptions\": [\n" +
                "        {\n" +
                "          \"schemaLocation\": \"#/properties/owner\",\n" +
                "          \"pointerToViolation\": \"#/owner\",\n" +
                "          \"causingExceptions\": [],\n" +
                "          \"keyword\": \"required\",\n" +
                "          \"message\": \"required key [forename] not found\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"schemaLocation\": \"#/properties/owner/properties/surname\",\n" +
                "          \"pointerToViolation\": \"#/owner/surname\",\n" +
                "          \"causingExceptions\": [],\n" +
                "          \"keyword\": \"type\",\n" +
                "          \"message\": \"expected type: String, found: Boolean\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"message\": \"2 schema violations found\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"schemaLocation\": \"#/properties/title\",\n" +
                "      \"pointerToViolation\": \"#/title\",\n" +
                "      \"causingExceptions\": [],\n" +
                "      \"keyword\": \"type\",\n" +
                "      \"message\": \"expected type: String, found: Integer\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"schemaLocation\": \"#/properties/items/items/properties/status\",\n" +
                "      \"pointerToViolation\": \"#/items/0/status\",\n" +
                "      \"causingExceptions\": [{\n" +
                "        \"pointerToViolation\": \"#/items/0/status\",\n" +
                "        \"causingExceptions\": [],\n" +
                "        \"keyword\": \"enum\",\n" +
                "        \"message\": \"egg is not a valid enum value\"\n" +
                "      }],\n" +
                "      \"keyword\": \"allOf\",\n" +
                "      \"message\": \"#: only 1 subschema matches out of 2\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"message\": \"4 schema violations found\"\n" +
                "}";

        String bad = slurp(getClass().getResourceAsStream("/bad.json"));

        when(request.body()).thenReturn(bad);

        try (InputStream inputStream = getClass().getResourceAsStream("/schema.json")) {
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
            schema = SchemaLoader.load(rawSchema);
        }

        ValidatationHandler validatationHandler = new ValidatationHandler(schema, "http://localhost:6060/");
        validatationHandler.handle(request, response);

        verify(response).status(400);
        verify(nextResponse, never()).body(any());
        verify(response).body(errors);
    }
}
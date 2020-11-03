package com.tailoredshapes.jblocks.schemapass;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import spark.Service;

import java.net.URI;

import static com.tailoredshapes.underbar.Die.rethrow;
import static spark.Service.ignite;

public class App {

    Service service;

    public App(int port, Schema schema, String next) {
        service = ignite();
        service.port(port);

        ValidatationHandler handler = new ValidatationHandler(schema, next);
        service.post("/", handler);
    }

    public static void main(String[] args) {
        var schema_url = System.getenv("SCHEMA_URL");
        var port = Integer.parseInt(System.getenv("PORT"));
        var next = System.getenv("NEXT");

        var uri = rethrow(() -> new URI(schema_url), () -> "Invalid URI for schema");
        var schema = SchemaLoader.load(new JSONObject(uri));

        new App(port, schema, next);
    }
}

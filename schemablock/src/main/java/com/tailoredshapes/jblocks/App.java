package com.tailoredshapes.jblocks;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;

import java.net.URI;

import static com.tailoredshapes.underbar.Die.rethrow;
import static spark.Spark.port;
import static spark.Spark.post;

public class App {

    public App(int port, Schema schema) {
        port(port);
        ValidatationHandler handler = new ValidatationHandler(schema);
        post("/", handler);
    }

    public static void main(String[] args) {
        var schema_url = System.getenv("SCHEMA_URL");
        var port = Integer.parseInt(System.getenv("PORT"));

        var uri = rethrow(() -> new URI(schema_url), () -> "Invalid URI for schema");
        var schema = SchemaLoader.load(new JSONObject(uri));

        new App(port, schema);
    }
}

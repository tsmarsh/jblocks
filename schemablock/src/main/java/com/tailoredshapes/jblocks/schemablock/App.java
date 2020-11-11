package com.tailoredshapes.jblocks.schemablock;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import spark.Service;

import java.io.File;
import java.net.URI;

import static com.tailoredshapes.underbar.Die.rethrow;
import static com.tailoredshapes.underbar.IO.slurp;
import static spark.Service.ignite;

public class App {

    public Service service;

    public App(int port, Schema schema) {
        service = ignite();
        service.port(port);
        ValidatationHandler handler = new ValidatationHandler(schema);
        service.post("/", handler);
        service.get("/", ((request, response) -> {
            response.status(200);
            response.body("OK");
            return "OK";}));
    }

    public static void main(String[] args) {
        var schema_url = System.getenv("SCHEMA_URL");
        var port = Integer.parseInt(System.getenv("PORT"));

        var uri = rethrow(() -> new URI(schema_url), () -> "Invalid URI for schema");
        JSONObject schemaJson = new JSONObject(slurp(new File(uri)));
        System.out.println(" ------------ Using Schema: ------------");
        System.out.println(schemaJson.toString(4));
        System.out.println(" ------------ End Schema: ------------");

        var schema = SchemaLoader.load(schemaJson);

        new App(port, schema);
    }
}

package com.tailoredshapes.jblocks.schemablock;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import spark.Service;

import java.io.File;
import java.net.URI;
import java.util.logging.Logger;

import static com.tailoredshapes.underbar.Die.rethrow;
import static com.tailoredshapes.underbar.IO.slurp;
import static spark.Service.ignite;

public class App {

    public Service service;

    private static Logger log = Logger.getLogger(App.class.getName());


    public App(int port, Schema schema, String prefix) {
        service = ignite();
        service.port(port);
        ValidatationHandler handler = new ValidatationHandler(schema);
        service.before("/*", (request, response) -> log.info("Received request: " + request.pathInfo()));
        service.post("/" + prefix , handler);
        service.get("/" + prefix, ((request, response) -> {
            response.status(200);
            response.body("OK");
            return "OK";}));
    }

    public static void main(String[] args) {
        var schema_url = System.getenv("SCHEMA_URL");
        var port = Integer.parseInt(System.getenv("PORT"));
        var prefix = System.getenv("PATH_PREFIX");

        var uri = rethrow(() -> new URI(schema_url), () -> "Invalid URI for schema");
        JSONObject schemaJson = new JSONObject(slurp(new File(uri)));
        log.info(" ------------ Using Schema: ------------");
        log.info(schemaJson.toString(4));
        log.info(" ------------ End Schema: ------------");
        log.info("Path prefix: " + prefix);

        var schema = SchemaLoader.load(schemaJson);

        new App(port, schema, prefix);
    }
}

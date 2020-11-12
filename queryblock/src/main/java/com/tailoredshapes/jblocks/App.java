package com.tailoredshapes.jblocks;

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

    public App(int port, String query, String prefix) {
        service = ignite();
        service.port(port);
        var handler = new QueryHandler(query);
        service.before("/*", (request, response) -> log.info("Received request: " + request.pathInfo()));
        service.post("/" + prefix , handler);
        service.get("/" + prefix, ((request, response) -> {
            response.status(200);
            response.body("OK");
            return "OK";}));
    }

    public static void main(String[] args) {
        var query_url = System.getenv("QUERY_URL");
        var port = Integer.parseInt(System.getenv("PORT"));
        var prefix = System.getenv("PATH_PREFIX");

        var uri = rethrow(() -> new URI(query_url), () -> "Invalid URI for schema");
        var schema = slurp(new File(uri));

        log.info("----- Using jq ------");
        log.info(schema);
        log.info("----- end jq ------");
        new App(port, schema, prefix);
    }
}

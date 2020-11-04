package com.tailoredshapes.jblocks;

import spark.Service;

import java.io.File;
import java.net.URI;

import static com.tailoredshapes.underbar.Die.rethrow;
import static com.tailoredshapes.underbar.IO.slurp;
import static spark.Service.ignite;


public class App {

    public Service service;

    public App(int port, String query) {
        service = ignite();
        service.port(port);
        var handler = new QueryHandler(query);
        service.post("/", handler);
    }

    public static void main(String[] args) {
        var query_url = System.getenv("QUERY_URL");
        var port = Integer.parseInt(System.getenv("PORT"));

        var uri = rethrow(() -> new URI(query_url), () -> "Invalid URI for schema");
        var schema = slurp(new File(uri));

        new App(port, schema);
    }
}

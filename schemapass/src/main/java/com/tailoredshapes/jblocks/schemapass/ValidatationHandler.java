package com.tailoredshapes.jblocks.schemapass;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import static com.tailoredshapes.underbar.Requests.post;

public class ValidatationHandler implements Route {

    private Schema schema;
    private String next;

    public ValidatationHandler(Schema schema, String next) {
        this.schema = schema;
        this.next = next;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            schema.validate(new JSONObject(request.body()));
            post(next, request.body(), (resp) -> {
                response.body(resp);
                return  resp;
            });

            response.status(200);
        }catch (ValidationException ve){
            response.status(400);
            response.body(ve.toJSON().toString(2));
        }
        return response.body();
    }
}

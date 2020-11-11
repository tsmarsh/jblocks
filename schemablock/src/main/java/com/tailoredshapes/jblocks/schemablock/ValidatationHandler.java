package com.tailoredshapes.jblocks.schemablock;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import static com.tailoredshapes.underbar.UnderString.join;

public class ValidatationHandler implements Route {

    private Schema schema;

    public ValidatationHandler(Schema schema) {
        this.schema = schema;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String body = request.body();
        try {
            schema.validate(new JSONObject(body));
            response.status(200);
            response.body(body);
        }catch (ValidationException ve){
            response.status(400);
            response.body(ve.toJSON().toString(2));
        }
        return body;
    }
}

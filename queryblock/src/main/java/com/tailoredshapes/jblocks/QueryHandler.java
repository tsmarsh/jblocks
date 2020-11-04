package com.tailoredshapes.jblocks;

import com.arakelian.jq.ImmutableJqLibrary;
import com.arakelian.jq.ImmutableJqRequest;
import com.arakelian.jq.JqResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

public class QueryHandler implements Route {
    private String query;
    private final ImmutableJqLibrary jqLibrary;

    public QueryHandler(String query) {
        this.query = query;
        jqLibrary = ImmutableJqLibrary.of();
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        ImmutableJqRequest jq = ImmutableJqRequest.builder()
                                                  .lib(jqLibrary)
                                                  .input(request.body())
                                                  .filter(query).build();

        JqResponse result = jq.execute();
        if(result.hasErrors()){
            response.status(400);
            response.body(JSONArray.toJSONString(result.getErrors()));
            return "ERROR";
        }else {
            response.status(200);
            response.body(result.getOutput());
            return "OK";
        }
    }
}

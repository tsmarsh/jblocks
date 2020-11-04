package com.tailoredshapes.jblocks;

import com.arakelian.jq.ImmutableJqLibrary;
import com.arakelian.jq.ImmutableJqRequest;
import com.arakelian.jq.JqResponse;
import org.json.simple.JSONArray;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

import static com.tailoredshapes.underbar.Requests.post;
import static com.tailoredshapes.underbar.UnderBar.map;

public class QueryHandler implements Route {
    private String query;
    private String next;
    private final ImmutableJqLibrary jqLibrary;

    public QueryHandler(String query, String next) {
        this.query = query;
        this.next = next;
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
            String output = result.getOutput();

            String[] split = output.split("\n");

            String json = JSONArray.toJSONString(map(split, (obj) -> post(next, obj, (res) -> res)));
            response.body(json);
            return json;
        }
    }
}

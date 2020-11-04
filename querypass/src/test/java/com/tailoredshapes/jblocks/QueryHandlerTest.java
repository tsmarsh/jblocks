package com.tailoredshapes.jblocks;

import org.json.simple.JSONArray;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import spark.Request;
import spark.Response;
import spark.Service;

import static com.tailoredshapes.underbar.IO.slurp;
import static com.tailoredshapes.underbar.UnderBar.list;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static spark.Service.ignite;

@RunWith(MockitoJUnitRunner.class)
public class QueryHandlerTest {

    @Mock
    Request request;

    @Mock
    Response response;

    @Mock
    static Response nextResponse;

    static Service testServer;


    @BeforeClass
    public static void setUp() throws Exception {
        testServer = ignite();
        testServer.port(6060);
        testServer.post("/", (req, resp) -> {
            nextResponse.body(req.body());
            return req.body();
        });
    }

    @After
    public void tear() {
        reset(nextResponse);
    }
    @AfterClass
    public static void tearDown() throws Exception {
        testServer.stop();
    }


    @Test
    public void shouldExecuteAQueryOnAPayload() throws Exception {
        String query = "[.items[] | select(.status == \"doing\").body]";

        when(request.body()).thenReturn(slurp(getClass().getResourceAsStream("/good.json")));

        QueryHandler queryHandler = new QueryHandler(query, "http://localhost:6060");
        queryHandler.handle(request, response);

        verify(response).status(200);
        verify(nextResponse).body("[\"Tempora natus deleniti sint eligendi a repellat odio.\",\"Et natus amet quae.\",\"Sapiente ut maiores eos omnis temporibus nisi dolore.\",\"Enim illum nobis ex temporibus voluptatum.\"]");

    }

    @Test
    public void shouldExecuteAQueryOnAPayloadAndGetMultipleObjects() throws Exception {
        String query = ".items[] | select(.status == \"doing\")";

        when(request.body()).thenReturn(slurp(getClass().getResourceAsStream("/good.json")));

        QueryHandler queryHandler = new QueryHandler(query, "http://localhost:6060");
        queryHandler.handle(request, response);

        verify(response).status(200);
        verify(response).body(any());
        verify(nextResponse, times(4)).body(any());
    }
}
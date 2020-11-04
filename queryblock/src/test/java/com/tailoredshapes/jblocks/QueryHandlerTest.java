package com.tailoredshapes.jblocks;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import spark.Request;
import spark.Response;

import static com.tailoredshapes.underbar.IO.slurp;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QueryHandlerTest {

    @Mock
    Request request;
    @Mock
    Response response;


    @Test
    public void shouldExecuteAQueryOnAPayload() throws Exception{
        String query = "[.items[] | select(.status == \"doing\").body]";

        when(request.body()).thenReturn(slurp(getClass().getResourceAsStream("/good.json")));

        QueryHandler queryHandler = new QueryHandler(query);
        queryHandler.handle(request, response);

        verify(response).status(200);
        verify(response).body("[\"Tempora natus deleniti sint eligendi a repellat odio.\",\"Et natus amet quae.\",\"Sapiente ut maiores eos omnis temporibus nisi dolore.\",\"Enim illum nobis ex temporibus voluptatum.\"]");
    }
}
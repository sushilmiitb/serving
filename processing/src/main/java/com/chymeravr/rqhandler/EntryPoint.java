package com.chymeravr.rqhandler;

import com.chymeravr.adfetcher.AdFetcher;
import com.chymeravr.rqhandler.entities.request.Request;
import com.chymeravr.rqhandler.entities.response.Response;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

@RequiredArgsConstructor
public class EntryPoint extends AbstractHandler {

    private final AdFetcher adFetcher;

    public void handle(String target,
                       org.eclipse.jetty.server.Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException,
            ServletException {
        final UUID requestId = UUID.randomUUID();

        Request adRequest = parseRequest(request);
        Response adResponse = adFetcher.getAdResponse(adRequest);

        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        if (adResponse != null) {
            out.write(new Gson().toJson(adResponse));
        }
        out.flush();
        baseRequest.setHandled(true);
    }


    private Request parseRequest(HttpServletRequest request) throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String data = buffer.toString();
        return new Gson().fromJson(data, Request.class);
    }
}
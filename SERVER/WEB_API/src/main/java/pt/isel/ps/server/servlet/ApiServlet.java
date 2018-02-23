package pt.isel.ps.server.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("serial")
public class ApiServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("Thread: "+ Thread.currentThread().getId());
        resp.getOutputStream().write("omgf".getBytes());
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}


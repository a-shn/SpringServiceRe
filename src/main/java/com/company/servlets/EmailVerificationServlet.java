package com.company.servlets;

import com.company.services.SignUpPool;
import org.springframework.context.ApplicationContext;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/verification")
public class EmailVerificationServlet extends HttpServlet {
    SignUpPool signUpPool;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        ApplicationContext springContext = (ApplicationContext) context.getAttribute("springContext");
        signUpPool = springContext.getBean("signUpPool", SignUpPool.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String hash = req.getParameter("hash");
        String link = "http://localhost:8080/springservice/verification?hash=" + hash;
        boolean isAllGood = signUpPool.verify(link);
        RequestDispatcher dispatcher;
        if (isAllGood) {
            dispatcher = req.getRequestDispatcher("WEB-INF/templates/email_verified.ftlh");
        } else {
            dispatcher = req.getRequestDispatcher("WEB-INF/templates/invalid_link.ftlh");
        }
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}

package com.company.servlets;

import com.company.services.SignUpService;
import freemarker.cache.WebappTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
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
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {
    private SignUpService signUpService;
    Configuration cfg;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        ApplicationContext springContext = (ApplicationContext) context.getAttribute("springContext");
        signUpService = springContext.getBean("signUpService", SignUpService.class);
        cfg = new Configuration(Configuration.VERSION_2_3_28);
        WebappTemplateLoader templateLoader = new WebappTemplateLoader(context, "WEB-INF/templates");
        cfg.setTemplateLoader(templateLoader);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Template temp = cfg.getTemplate("signup.ftlh");
        Map<String, String> root = new HashMap<>();
        if (req.getParameter("email") != null && req.getParameter("email").equals("busy")) {
            root.put("email", "<font color=\"red\">*this e-mail is busy</font>");
        } else {
            root.put("email", "");
        }
        Writer out = resp.getWriter();
        try {
            temp.process(root, out);
        } catch (TemplateException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        boolean isAllGood = signUpService.signUp(login, email, password);
        if (isAllGood) {
            RequestDispatcher dispatcher = req.getRequestDispatcher("WEB-INF/templates/email_verifying.ftlh");
            dispatcher.forward(req, resp);
        } else {
            resp.sendRedirect("signup?email=busy");
        }
    }
}

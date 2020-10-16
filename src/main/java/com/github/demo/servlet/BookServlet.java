package com.github.demo.servlet;

import com.github.demo.model.Book;
import com.github.demo.service.BookService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

@WebServlet(
    name = "BookServlet",
    urlPatterns = {""},
    loadOnStartup = 1
)
@WebInitParam(name = "allowedTypes", value = "html")
public class BookServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(BookServlet.class);

    private BookService bookService;

    public BookServlet() {
        logger.info("Starting Bookstore Servlet...");
        bookService = new BookService();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Book> books = bookService.getBooks();

        Properties versionProperties = new Properties();
        versionProperties.load(getClass().getResourceAsStream("/version.properties"));

        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver(req.getServletContext());
        resolver.setPrefix("/");
        resolver.setSuffix(".html");

        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);

        WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
        ctx.setVariable("modified", Calendar.getInstance());
        ctx.setVariable("books", books);
        ctx.setVariable("version", versionProperties.getProperty("version"));

        resp.setHeader("Content-Type", "text/html; charset=UTF-8");
        engine.process("books", ctx, resp.getWriter());
    }
}

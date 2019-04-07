package org.assembly.pss;

import java.io.IOException;
import java.util.TimeZone;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assembly.pss.config.PropertyConfig;
import org.eclipse.jetty.server.Request;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.flywaydb.core.Flyway;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class Main {

    private static final Logger LOG = LogManager.getLogger();
    private static final int DEFAULT_PORT = 8080;
    private static final String CONTEXT_PATH = "";
    private static final String CONFIG_LOCATION = "org.assembly.pss.config";
    private static final String MAPPING_URL = "/";

    public static void main(String[] args) throws Exception {
        System.setProperty("user.timezone", "UTC");
        TimeZone.setDefault(null);
        LOG.info("Creating main instance");
        Main m = new Main();
        LOG.info("Upgrading database");
        m.upgradeDatabase();
        LOG.info("Starting Jetty");
        m.startJetty(PropertyConfig.getInt("http.port", DEFAULT_PORT));
        LOG.info("Jetty has exited, exiting application");
    }

    private void upgradeDatabase() {
        String url = PropertyConfig.get("database.url");
        String user = PropertyConfig.get("database.user");
        String pass = PropertyConfig.get("database.password");
        Flyway flyway = Flyway.configure().dataSource(url, user, pass).load();
        flyway.migrate();
    }

    private void startJetty(int port) throws Exception {
        LOG.info("Starting Jetty on port " + port);
        Server server = new Server(port);
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(CONFIG_LOCATION);
        ErrorPageErrorHandler errorHandler = new ErrorPageErrorHandler() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
                Throwable th = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
                if (th != null) {
                    LOG.error("Uncaught exception!", th);
                }
                super.handle(target, baseRequest, request, response);
            }
        };
        errorHandler.addErrorPage(403, "/errors/403");
        errorHandler.addErrorPage(404, "/errors/404");
        errorHandler.addErrorPage(405, "/errors/405");
        errorHandler.addErrorPage(500, "/errors/500");
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.setErrorHandler(errorHandler);
        servletContextHandler.setContextPath(CONTEXT_PATH);
        servletContextHandler.addServlet(new ServletHolder(new DispatcherServlet(context)), MAPPING_URL);
        servletContextHandler.addEventListener(new ContextLoaderListener(context));
        server.setHandler(servletContextHandler);
        server.start();
        server.join();
    }
}

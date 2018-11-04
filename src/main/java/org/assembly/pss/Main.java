package org.assembly.pss;

import java.util.TimeZone;
import org.apache.log4j.Logger;
import org.assembly.pss.config.PropertyConfig;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class);
    private static final int DEFAULT_PORT = 8080;
    private static final String CONTEXT_PATH = "";
    private static final String CONFIG_LOCATION = "org.assembly.pss.config";
    private static final String MAPPING_URL = "/";

    public static void main(String[] args) throws Exception {
        System.setProperty("user.timezone", "UTC");
        TimeZone.setDefault(null);
        new Main().startJetty(PropertyConfig.getAsInt("http.port", DEFAULT_PORT));
    }

    private void startJetty(int port) throws Exception {
        LOG.info("Starting Jetty on port " + port);
        Server server = new Server(port);
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(CONFIG_LOCATION);
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.setErrorHandler(null);
        servletContextHandler.setContextPath(CONTEXT_PATH);
        servletContextHandler.addServlet(new ServletHolder(new DispatcherServlet(context)), MAPPING_URL);
        servletContextHandler.addEventListener(new ContextLoaderListener(context));
        server.setHandler(servletContextHandler);
        server.start();
        server.join();
    }
}

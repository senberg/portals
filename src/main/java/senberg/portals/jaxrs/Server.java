package senberg.portals.jaxrs;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class Server {
    public static void main(String[] args) throws Exception {
        org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server(80);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        ServletHolder calculatorHolder = context.addServlet(ServletContainer.class, "/*");
        calculatorHolder.setInitOrder(0);
        calculatorHolder.setInitParameter("jersey.config.server.provider.classnames", Calculator.class.getCanonicalName());

        server.start();
        server.join();
        server.destroy();
    }
}

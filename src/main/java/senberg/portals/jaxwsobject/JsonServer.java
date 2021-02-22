package senberg.portals.jaxwsobject;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class JsonServer {
    public static void main(String[] args) throws Exception {
        org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server(80);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        ServletHolder jsonCalculatorHolder = context.addServlet(ServletContainer.class, "/*");
        jsonCalculatorHolder.setInitOrder(0);
        jsonCalculatorHolder.setInitParameter("jersey.config.server.provider.classnames", JsonCalculator.class.getCanonicalName());

        server.start();
        server.join();
        server.destroy();
    }
}

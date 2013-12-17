package info.kwarc.sally.jedit.stex.com;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.resource.Resource;
public class JettyServer {

	public static void main(String[] args) throws Exception {
        Server server;
		 server = new Server(8082);
         // Defines a list of handlers that will be processed in order in order to answer requests
         HandlerList handlers = new HandlerList();

         // ResourceHandler will serve static files 
         ResourceHandler resource_handler = new ResourceHandler();
         resource_handler.setDirectoriesListed(true);
         resource_handler.setWelcomeFiles(new String[] { "index.html" });
         resource_handler.setBaseResource(Resource.newClassPathResource("web"));

         // context will contain the rest of the servlets  
         ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
         context.setContextPath("/");
         context.setInitParameter("maxInterval", "1000");
         context.setInitParameter("logLevel", "2");

         // setting everything up
         handlers.setHandlers(new Handler[] { resource_handler, context});
         server.setHandler(handlers);

         CrossOriginFilter cof = new CrossOriginFilter();
         FilterHolder fh = new FilterHolder(cof);
         fh.setInitParameter("allowedOrigins", "*");
         fh.setInitParameter("allowedMethods", "GET,POST,DELETE,PUT,HEADt");
         fh.setInitParameter("allowedHeaders", "origin, content-type, accept");
         fh.setInitParameter("cross-origin", "/*");

         context.addFilter(fh, "/*", 1);
         
         context.addServlet(new ServletHolder(new AutoCompleteServlet()), "/autocomplete");
         context.addServlet(new ServletHolder(new DefaultServlet()), "/*");
         server.start();
         server.join();
	}

}

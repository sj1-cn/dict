package com.engreader;

import java.util.regex.Pattern;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.engreader.db.H2DB;
import com.engreader.db.UserWordsDB;
import com.engreader.entity.UserWordsHandler;
import com.engreader.entity.UserWordsStore;

public class JettyServer {
	public static void main(String[] args) throws Exception {
		int port = 80;

		if(args.length>0) {
			if(isInteger(args[0])) {
				port = Integer.parseInt(args[0]);
			}
		}
		
		Server server = new Server(port);
		
		
		ServletContextHandler context = new ServletContextHandler(server, "/");

//		ResourceHandler resourceHandler = new ResourceHandler();
		ResourceHandler resource_handler = new ResourceHandler();

		// Configure the ResourceHandler. Setting the resource base indicates
		// where the files should be served out of
		resource_handler.setDirectoriesListed(true);
		resource_handler.setWelcomeFiles(new String[] { "index.html" });
		resource_handler.setResourceBase("src/main/webapp/");

//		context.addServlet(new ServletHolder(new LoginServlet("anonymous")), "/login");
//		context.addServlet(AdminServlet.class, "/admin");
//		context.addServlet(TimerServlet.class, "/timer");
		context.addServlet(StanfordCoreNLPFrequencyHandler.class, "/NLPFrequency");
		context.addServlet(StanfordCoreNLPHandler.class, "/NLPWords");

		H2DB h2db = H2DB.connect();
		UserWordsDB db = new UserWordsDB(h2db);
		UserWordsStore userWordsStore = new UserWordsStore(db);
		UserWordsHandler userWordsHandler = new UserWordsHandler(userWordsStore);
		ServletHolder holderUserWordsHandler = new ServletHolder(userWordsHandler);
		context.addServlet(holderUserWordsHandler, "/api/userwords/");
		context.addServlet(holderUserWordsHandler, "/api/userwords/*");
		context.addServlet(StanfordCoreNLPHandler.class, "/api/NLPWords");

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resource_handler, context });

		server.setHandler(handlers);

		server.start();
		server.join();
	}
	public static boolean isInteger(String str) { 
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$"); 
        return pattern.matcher(str).matches(); 
}
}

package JettyServerlet;

import java.util.regex.Pattern;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import cn.sj1.aireader.StanfordNLPStemmer;
import cn.sj1.aireader.servlet.StanfordCoreNLPHttpServlet;
import cn.sj1.dict.db.H2DB;
import cn.sj1.dict.db.UserWordsDB;
import cn.sj1.dict.db.WordDefineDB;
import cn.sj1.dict.servlet.WordDefineHttpServlet;
import cn.sj1.dict.store.WordDefineStore;
import cn.sj1.user.UserWordsStore;
import cn.sj1.user.servlet.UserWordsHttpServlet;

public class JettyServer {
	public static void main(String[] args) throws Exception {
		int port = 80;

		if (args.length > 0) {
			if (isInteger(args[0])) {
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
//		context.addServlet(StanfordCoreNLPFrequencyHttpServlet.class, "/NLPFrequency");
		
//		H2DB h2db = H2DB.connect("./data/db.h2");

//		ServletHolder userWordsHttpServletHolder = initUserWordsServletHolder(h2db);
//
//		context.addServlet(userWordsHttpServletHolder, "/api/userwords/");
//		context.addServlet(userWordsHttpServletHolder, "/api/userwords/*");
//
//		WordDefineStore store = initWordsStore(h2db);
//
//		{
//			ServletHolder holder = initNLPServletHolder(store);
//			context.addServlet(holder, "/ana/words");
//		}
//
//		{
//			ServletHolder holder = initWordsServletHolder(store);
//			context.addServlet(holder, "/api/words/");
//			context.addServlet(holder, "/api/words/*");
//		}
//
//		HandlerList handlers = new HandlerList();
//		handlers.setHandlers(new Handler[] { resource_handler, context });
//
//		server.setHandler(handlers);
//
//		server.start();
//		server.join();
	}

	private static WordDefineStore initWordsStore(H2DB h2db) {
		WordDefineDB db = new WordDefineDB(h2db);
		WordDefineStore store = new WordDefineStore(db);
		return store;
	}

	private static ServletHolder initUserWordsServletHolder(H2DB h2db) {
		UserWordsDB db1 = new UserWordsDB(h2db);
		UserWordsStore userWordsStore = new UserWordsStore(db1);
		UserWordsHttpServlet userWordsHttpServlet = new UserWordsHttpServlet(userWordsStore);
		ServletHolder userWordsHttpServletHolder = new ServletHolder(userWordsHttpServlet);
		return userWordsHttpServletHolder;
	}

	private static ServletHolder initWordsServletHolder(WordDefineStore store) {
		WordDefineHttpServlet servlet = new WordDefineHttpServlet(store);
		ServletHolder holder = new ServletHolder(servlet);
		return holder;
	}

	private static ServletHolder initNLPServletHolder(WordDefineStore store) {
		StanfordNLPStemmer stanfordNLPStemmer = new StanfordNLPStemmer(store.getWords());
		StanfordCoreNLPHttpServlet servlet = new StanfordCoreNLPHttpServlet(stanfordNLPStemmer);
		ServletHolder holder = new ServletHolder(servlet);
		return holder;
	}


	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}
}

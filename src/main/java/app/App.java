package app;

import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.jvm.ThreadDeadlockHealthCheck;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.engreader.StanfordNLPStemmer;
import com.engreader.db.H2DB;
import com.engreader.db.WordDefineDB;
import com.engreader.entity.WordDefine;
import com.engreader.entity.WordDefineStore;

import io.jooby.AccessLogHandler;
import io.jooby.AssetHandler;
import io.jooby.AssetSource;
import io.jooby.Jooby;
import io.jooby.MediaType;
import io.jooby.StatusCode;
import io.jooby.metrics.MetricsModule;

public class App extends Jooby {

	Logger logger = LoggerFactory.getLogger(getClass());

	{
		// *************************************************************
		// *** Environment
		// *************************************************************
//		Environment env = getEnvironment();
//		Config config = env.getConfig();
		decorator(new AccessLogHandler());

		MetricRegistry metricRegistry = new MetricRegistry();

		install(new MetricsModule(metricRegistry)
				.threadDump()
				.ping()
				.healthCheck("deadlock", new ThreadDeadlockHealthCheck())
				.metric("memory", new MemoryUsageGaugeSet())
				.metric("threads", new ThreadStatesGaugeSet())
				.metric("gc", new GarbageCollectorMetricSet())
				.metric("fs", new FileDescriptorRatioGauge()));

		AssetSource www = AssetSource.create(Paths.get("conf/webapp"));
		AssetSource wwwlib = AssetSource.create(Paths.get("conf/webapp/lib"));
		assets("/static/lib/*", new AssetHandler(wwwlib).setMaxAge(Duration.ofDays(365)));
		assets("/static/*", new AssetHandler(www).setETag(true).setMaxAge(Duration.ofDays(1)));

		assets("/?*", new AssetHandler("index.html", www));
//
//		post("/pdfbox/upload", ctx -> {
//			return uploadFile(ctx);
//		});
//
//		post("/pdfbox/netease2wordlist", ctx -> {
//			return netease2wordlist(ctx);
//		});
//		
//		  
//		get("/", ctx -> {
//			ctx.setResponseType(MediaType.html);
//			SimpleHTMLBuilder html = new SimpleHTMLBuilder();
//
//			Value forwardby = ctx.header("forwardby");
//			String prefix = forwardby.isMissing() ? "" : "/" + forwardby.value();
//
//			for (Route route : this.getRoutes()) {
//				if (route.getPathKeys().size() == 0 && route.getMethod().equals("GET")) {
//					html.a(prefix + route.getPattern(), route.getPattern().replace('/', ' ')).br();
//				} else {
//					html.span(route.getPattern().replace('/', ' '));
//					html.br();
//				}
//			}
//			return html.toStandardPage("Home");
//		});
		H2DB h2db = null;
		try {
			h2db = H2DB.connect("./data/db.h2");
		} catch (ClassNotFoundException e) {
			throw new UnsupportedOperationException(e);
		} catch (SQLException e) {
			throw new UnsupportedOperationException(e);
		}

		WordDefineStore store = initWordsStore(h2db);
		StanfordNLPStemmer stanfordNLPStemmer = new StanfordNLPStemmer(store.getWords());

		get("/dict/{word}", ctx -> {
			String wordStr = ctx.path("word").value();

//			resp.setCharacterEncoding("utf-8");
//			resp.setHeader("content-type", "application/json;chartset=uft-8");
			ctx.setResponseType(MediaType.html);

			StringBuilder sb = new StringBuilder();
			sb.append("{");

			WordDefine wordDefine = store.get(wordStr);
			addToJson(sb, wordDefine);
			sb.append(',');
			sb.append("\"ret\":0");
			sb.append('}');

//			if (req.getParameter("callback") != null) {
//				resp.getWriter().write(req.getParameter("callback") + "(" + sb.toString() + ")");
//			} else {
//				resp.getWriter().write(sb.toString());
//			}
			return sb.toString();
		});

		get("/api/words/{word}", ctx -> {
			String wordStr = ctx.path("word").value();

//			resp.setCharacterEncoding("utf-8");
//			resp.setHeader("content-type", "application/json;chartset=uft-8");
			ctx.setResponseType(MediaType.json);

			StringBuilder sb = new StringBuilder();
			sb.append("{");

			WordDefine wordDefine = store.get(wordStr);
			addToJson(sb, wordDefine);
			sb.append(',');
			sb.append("\"ret\":0");
			sb.append('}');

//			if (req.getParameter("callback") != null) {
//				resp.getWriter().write(req.getParameter("callback") + "(" + sb.toString() + ")");
//			} else {
//				resp.getWriter().write(sb.toString());
//			}
			return sb.toString();
		});

		post("/ana/words", ctx -> {

//			// StemmerTest2.parse(srcText, pipeline)
//			resp.setHeader("Access-Control-Allow-Origin", "*");
//			resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//			resp.setHeader("Access-Control-Max-Age", "3600");
//			resp.setHeader("Access-Control-Allow-Headers", "x-requested-with");
//			// 是否支持cookie跨域
//			resp.addHeader("Access-Control-Allow-Credentials", "true");

			String codecontent = ctx.form("codecontent").value();// req.getParameter("codecontent");
			codecontent = codecontent.replaceAll("youjequalsign", "=").replaceAll("youjscryoujipttag", "script");

			StringBuffer awe = stanfordNLPStemmer.parseWords(codecontent);

			StringBuffer sb = new StringBuffer();
//			sb.append("<html><head>"
//					+ "<script src=\"./static/js/main.js\" type=\"text/javascript\"></script>"
//					+ "<link rel=\"stylesheet\" href=\"./css/words.css\">"
//					+ "<meta charset=\"utf-8\">"
//					+ "</head><body>");
			//
			sb.append(awe);
//			sb.append("</body></html>");

			ctx.setResponseType(MediaType.html, Charset.forName("utf-8"));
			return sb.toString();
		});

		get("/sm/{name}", ctx -> {
			ctx.setResponseCode(StatusCode.NO_CONTENT_CODE);
			return ctx;

		});

//		CollectorRegistry.defaultRegistry.register(new DropwizardExports(metricRegistry));

		get("/size", ctx -> {

			StringBuilder sb = new StringBuilder();

//			sb.append(taskEngineGraphLayout.toPrintable());

			return sb;
		});

	}

	private static WordDefineStore initWordsStore(H2DB h2db) {
		WordDefineDB db = new WordDefineDB(h2db);
		WordDefineStore store = new WordDefineStore(db);
		store.getAll();
		return store;
	}

	private void addToJson(StringBuilder sb, WordDefine w) {
		sb.append("\"data\":");
		sb.append('{');
		{
			sb.append("\"id\":").append(w.getId()).append(',');
			sb.append("\"word\":").append('\"').append(w.getWord()).append('\"').append(',');
			sb.append("\"cocaLevel\":").append('\"').append(w.getCocaLevel()).append('\"').append(',');
			sb.append("\"tense\":").append('\"').append(w.getTense()).append('\"').append(',');
			sb.append("\"accentEn\":").append('\"').append(w.getAccentEn()).append('\"').append(',');
			sb.append("\"accentUs\":").append('\"').append(w.getAccentUs()).append('\"').append(',');
			sb.append("\"meanZh\":").append('\"').append(w.getMeanZh()).append('\"').append(',');
			sb.append("\"meanBriefZh\":").append('\"').append(w.getMeanBriefZh()).append('\"').append(',');
			sb.append("\"meanEn\":").append('\"').append(w.getMeanEn()).append('\"').append(',');
			sb.append("\"pos\":").append('\"').append(w.getPos()).append('\"').append(',');
			sb.append("\"freq\":").append(w.getFreq()).append(',');
		}
		sb.setCharAt(sb.length() - 1, '}');
	}

//	@SuppressWarnings("unused")
//	public static void startPrometheusServer() {
//		try {
//			HTTPServer server = new HTTPServer.Builder()
//					.withPort(9100)
//					.build();
//			DefaultExports.initialize();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	public static void main(final String[] args) {
		runApp(args, App::new);
//		startPrometheusServer();
	}

}

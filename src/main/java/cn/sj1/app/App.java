package cn.sj1.app;

import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.jvm.ThreadDeadlockHealthCheck;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;

import cn.sj1.aireader.StanfordNLPStemmer;
import cn.sj1.aireader.WordFrequency;
import cn.sj1.dict.WordDefine;
import cn.sj1.dict.db.H2DB;
import cn.sj1.dict.db.WordDefineDB;
import cn.sj1.dict.store.WordDefineStore;
import io.jooby.AccessLogHandler;
import io.jooby.AssetHandler;
import io.jooby.AssetSource;
import io.jooby.Context;
import io.jooby.Jooby;
import io.jooby.MediaType;
import io.jooby.StatusCode;
import io.jooby.hikari.HikariModule;
import io.jooby.metrics.MetricsModule;
import mesoor.datasource.pgdb.DatabaseJoobyApp;

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

		install(new MetricsModule(metricRegistry).threadDump().ping()
				.healthCheck("deadlock", new ThreadDeadlockHealthCheck()).metric("memory", new MemoryUsageGaugeSet())
				.metric("threads", new ThreadStatesGaugeSet()).metric("gc", new GarbageCollectorMetricSet())
				.metric("fs", new FileDescriptorRatioGauge()));

		AssetSource www = AssetSource.create(Paths.get("conf/webapp"));
		AssetSource wwwlib = AssetSource.create(Paths.get("conf/webapp/lib"));
		assets("/static/lib/*", new AssetHandler(wwwlib).setMaxAge(Duration.ofDays(365)));
		assets("/static/*", new AssetHandler(www).setETag(true).setMaxAge(Duration.ofDays(1)));

		install(new HikariModule());
		mount("/db", new DatabaseJoobyApp());

		assets("/?*", new AssetHandler("index.html", www));

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
		H2DB dictH2db = null;
		DataSource ds = require(DataSource.class);
		dictH2db = new H2DB(ds);

		WordDefineStore store = initWordsStore(dictH2db);
		StanfordNLPStemmer stanfordNLPStemmer = new StanfordNLPStemmer(store.getWords());

		get("/dict/{word}", ctx -> {
			String wordStr = ctx.path("word").value();

//			resp.setCharacterEncoding("utf-8");
//			resp.setHeader("content-type", "application/json;chartset=uft-8");
			ctx.setResponseType(MediaType.json);

			StringBuilder sb = new StringBuilder();
//			sb.append("{\"word\":");

			WordDefine wordDefine = store.get(wordStr);
			addToJson(sb, wordDefine);
//			sb.append(',');
//			sb.append("\"ret\":0");
//			sb.append('}');

//			if (req.getParameter("callback") != null) {
//				resp.getWriter().write(req.getParameter("callback") + "(" + sb.toString() + ")");
//			} else {
//				resp.getWriter().write(sb.toString());
//			}
			return sb.toString();
		});

		get("/api/words/{word}", ctx -> {
			return dict(store, ctx);
		});

		get("/api/dict/{word}", ctx -> {
			return dictWord(store, ctx);
		});

		get("/api/dict", ctx -> {
			return dict(store, ctx);
		});

		post("/ana/words", ctx -> {

//			// StemmerTest2.parse(srcText, pipeline)
//			resp.setHeader("Access-Control-Allow-Origin", "*");
//			resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//			resp.setHeader("Access-Control-Max-Age", "3600");
//			resp.setHeader("Access-Control-Allow-Headers", "x-requested-with");
//			// 是否支持cookie跨域
//			resp.addHeader("Access-Control-Allow-Credentials", "true");
			MediaType requestType = ctx.getRequestType();
			String codecontent = "";
			if (requestType == MediaType.form) {
				codecontent = ctx.form("codecontent").value();// req.getParameter("codecontent");
				codecontent = codecontent.replaceAll("youjequalsign", "=").replaceAll("youjscryoujipttag", "script");
			} else if (requestType == MediaType.text) {
				codecontent = ctx.body().value();
			}

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

		post("/ana/count-text-word", ctx -> {

			MediaType requestType = ctx.getRequestType();

			String codecontent = "";
			if (requestType == MediaType.form) {
				codecontent = ctx.form("codecontent").value();// req.getParameter("codecontent");
				codecontent = codecontent.replaceAll("youjequalsign", "=").replaceAll("youjscryoujipttag", "script");
			} else if (requestType == MediaType.text) {
				codecontent = ctx.body().value();
			}

//			// StemmerTest2.parse(srcText, pipeline)
//			resp.setHeader("Access-Control-Allow-Origin", "*");
//			resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//			resp.setHeader("Access-Control-Max-Age", "3600");
//			resp.setHeader("Access-Control-Allow-Headers", "x-requested-with");
//			// 是否支持cookie跨域
//			resp.addHeader("Access-Control-Allow-Credentials", "true");

			List<WordFrequency> referWordList = stanfordNLPStemmer.parseWordCount(codecontent);
			List<WordFrequency> lws = referWordList.stream().filter(e -> e != null).filter(e->e.getWordDefine()!=null).filter(e->e.getWordDefine().getCocaRawFrequency()>0)
					.sorted((l, r) -> (r.getWordDefine() != null ? r.getWordDefine().getCocaRankFrequency() : 1000000)
							- (l.getWordDefine() != null ? l.getWordDefine().getCocaRankFrequency() : 1000000))
					.collect(Collectors.toList());

			StringBuffer sb = new StringBuffer();
			if (lws.size() > 0) {
				sb.append("[");
				for (int i = 0; i < lws.size(); i++) {
					WordFrequency w = lws.get(i);
					sb.append("\n{");
					sb.append("\"word\":\"");
					sb.append(w.getLema());
					sb.append("\",\"count\":");
					sb.append(w.getFrequency());
					sb.append(",\"details\":{");

					Map<String, Integer> sp = w.getSp();
					if (sp.size() > 0) {
						for (Entry<String, Integer> entry : sp.entrySet()) {
							String key = entry.getKey();
							int val = entry.getValue();
							sb.append("\"");
							sb.append(key);
							sb.append("\":");
							sb.append(val);
							sb.append(",");
						}
						sb.setCharAt(sb.length() - 1, '}');
					}

					WordDefine define = w.getWordDefine();
					if (define != null) {
						sb.append(",\"definition\":{");
						{
							sb.append("\"freq\":");
							sb.append(define.getFreq());
							sb.append(",");

							sb.append("\"cocaLevel\":");
							sb.append(define.getCocaLevel());
							sb.append(",");

							sb.append("\"meanBriefZh\":");
							sb.append(define.getMeanBriefZh());
							sb.append(",");

							sb.append("\"cocaRawFrequency\":");
							sb.append(define.getCocaRawFrequency());
							sb.append(",");
							sb.append("\"cocaRankFrequency\":");
							sb.append(define.getCocaRankFrequency());
							sb.append(",");
						}
						sb.setCharAt(sb.length() - 1, '}');
					}

					sb.append("},");
//				sb.append(wordFrequency.toString());
//				
//
////				return lema + "," + frequency + ",\"" + sp.toString() + "\"";
//				sb.append('\n');
				}
				sb.setCharAt(sb.length() - 1, ']');
			} else {
				sb.append("[]");
			}

			return sb;
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

	public Object dict(WordDefineStore store, Context ctx) {
		String words = ctx.query("words").value();

//			resp.setCharacterEncoding("utf-8");
//			resp.setHeader("content-type", "application/json;chartset=uft-8");
		ctx.setResponseType(MediaType.json);
		String[] ws = words.split(",");

		StringBuilder sb = new StringBuilder();

		sb.append("{");
		sb.append("\"data\":[");
		for (String wordStr : ws) {
			WordDefine wordDefine = store.get(wordStr);
			addToJson(sb, wordDefine);
			sb.append(',');
		}
		if (sb.charAt(sb.length() - 1) == ',') {
			sb.setCharAt(sb.length() - 1, ']');
		} else {
			sb.append("]");
		}
		sb.append(',');
		sb.append("\"ret\":0");
		sb.append('}');

//			if (req.getParameter("callback") != null) {
//				resp.getWriter().write(req.getParameter("callback") + "(" + sb.toString() + ")");
//			} else {
//				resp.getWriter().write(sb.toString());
//			}
		return sb.toString();
	}

	private Object dictWord(WordDefineStore store, Context ctx) {
		String wordStr = ctx.path("word").value();

//		resp.setCharacterEncoding("utf-8");
//		resp.setHeader("content-type", "application/json;chartset=uft-8");
		ctx.setResponseType(MediaType.json);

		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"data\":");
		WordDefine wordDefine = store.get(wordStr);
		addToJson(sb, wordDefine);
		sb.append(',');
		sb.append("\"ret\":0");
		sb.append('}');

//		if (req.getParameter("callback") != null) {
//			resp.getWriter().write(req.getParameter("callback") + "(" + sb.toString() + ")");
//		} else {
//			resp.getWriter().write(sb.toString());
//		}
		return sb.toString();
	}

	private static WordDefineStore initWordsStore(H2DB h2db) {
		WordDefineDB db = new WordDefineDB(h2db);
		WordDefineStore store = new WordDefineStore(db);
		store.getAll();
		return store;
	}

	private void addToJson(StringBuilder sb, WordDefine w) {
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

package app;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.jvm.ThreadDeadlockHealthCheck;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;

import io.jooby.AccessLogHandler;
import io.jooby.AssetHandler;
import io.jooby.AssetSource;
import io.jooby.Jooby;
import io.jooby.StatusCode;
import io.jooby.metrics.MetricsModule;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;

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

		AssetSource www = AssetSource.create(Paths.get("conf/www"));
		AssetSource wwwlib = AssetSource.create(Paths.get("conf/www/lib"));
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

		get("/sm/{name}", ctx -> {
			ctx.setResponseCode(StatusCode.NO_CONTENT_CODE);
			return ctx;

		});

		CollectorRegistry.defaultRegistry.register(new DropwizardExports(metricRegistry));

		get("/size", ctx -> {

			StringBuilder sb = new StringBuilder();

//			sb.append(taskEngineGraphLayout.toPrintable());

			return sb;
		});

	}


	@SuppressWarnings("unused")
	public static void startPrometheusServer() {
		try {
			HTTPServer server = new HTTPServer.Builder()
					.withPort(9100)
					.build();
			DefaultExports.initialize();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(final String[] args) {
		runApp(args, App::new);
		startPrometheusServer();
	}

}

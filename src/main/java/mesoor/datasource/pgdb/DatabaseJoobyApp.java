package mesoor.datasource.pgdb;

import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;

import cn.sj1.app.MesoorApp;
import commons.tinyhtml.SimpleHTMLBuilder;
import io.jooby.Context;
import io.jooby.MediaType;

public class DatabaseJoobyApp extends MesoorApp {

	Logger logger = LoggerFactory.getLogger(getClass());

	JsonFactory jsonFactory = new JsonFactory();

	public DatabaseJoobyApp() {
		super();
	}

	{
		get("/", ctx -> {
			// work with data source
			DataSource ds = require(DataSource.class);
			String sql = "SELECT * FROM information_schema.schemata";
			ctx.setResponseType(MediaType.html);
			SimpleHTMLBuilder html = new SimpleHTMLBuilder();
			try (Connection conn = ds.getConnection()) {
				try (ResultSet resultSet = conn.createStatement().executeQuery(sql)) {
//				for(int i=0;i<)

					html.table_();
					html.thead_();
					{
						html.tr_();

						html.th("left", 40, "catalog_name");
						html.th("left", 40, "schema_name");
						html.th("left", 40, "schema_owner");

						html._tr();
					}
					html._thead();

					html.tbody_();
					while (resultSet.next()) {
						html.tr_();
						{
							html.td("left", resultSet.getString("catalog_name"));
							String schemaName = resultSet.getString("schema_name");
							html.td_("left");
							html.a(requestPathOf(ctx) + "/" + resultSet.getString("schema_name"), schemaName);
							html._td();

							html.td("left", resultSet.getString("schema_owner"));
						}
						html._tr();
					}

					html._tbody();
					html._table();
				}
			}

			return html.toStandardPage("db");
		});

		get("/{schema}", ctx -> {
			String schema = ctx.path("schema").value();
			ctx.setResponseType(MediaType.html);
			SimpleHTMLBuilder html = new SimpleHTMLBuilder();
			String sql = "SELECT * FROM information_schema.tables WHERE table_schema = '" + schema + "'";
			// work with data source
			DataSource ds = require(DataSource.class);
			try (Connection conn = ds.getConnection()) {
				try (ResultSet resultSet = conn.createStatement().executeQuery(sql)) {
//				for(int i=0;i<)

					html.table_();
					html.thead_();
					{
						html.tr_();

						html.th("left", 40, "schema");
						html.th("left", 40, "tableName");
						html.th("left", 40, "tableType");

						html._tr();
					}
					html._thead();

					html.tbody_();
					while (resultSet.next()) {
						html.tr_();
						{
							html.td("left", resultSet.getString("table_schema"));
							String tableName = resultSet.getString("table_name");
							html.td_("left");
							html.a(requestPathOf(ctx) + "/" + tableName + "", tableName);
							html._td();

							html.td("left", resultSet.getString("table_type"));
						}
						html._tr();
					}

					html._tbody();
				}
				html._table();
			}

			return html.toStandardPage("db");
		});

//		get("/schema/tables", ctx -> {
//			DataSource ds = require(DataSource.class);
//			// work with data source
//			Connection conn = ds.getConnection();
//			ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM information_schema.tables WHERE table_schema = 'ruleengine'");
////				for(int i=0;i<)
//			ctx.setResponseType(MediaType.html);
//			SimpleHTMLBuilder html = new SimpleHTMLBuilder();
//			ResultSetMetaData metaData = resultSet.getMetaData();
//			buildTableOfMetaData(html, metaData);
//			return html.toStandardPage("db");
//		});

		get("/{schema}/{table}", ctx -> {
			String schema = ctx.path("schema").value();
			String table = ctx.path("table").value();
			// work with data source
			ctx.setResponseType(MediaType.html);
			SimpleHTMLBuilder html = new SimpleHTMLBuilder();

			DataSource ds = require(DataSource.class);
			try (Connection conn = ds.getConnection()) {
				String sql = "SELECT * FROM " + schema + "." + table + " LIMIT 100";
				try (ResultSet resultSet = conn.createStatement().executeQuery(sql)) {

					html.a(requestPathOf(ctx) + "/schema", "schema");

					buildDataList(ctx, html, resultSet);
				}
			}

			return html.toStandardPage(schema + "." + table);
		});

		get("/{schema}/{table}/schema", ctx -> {
			String schema = ctx.path("schema").value();
			String table = ctx.path("table").value();
			// work with data source
			String sql = "SELECT * FROM " + schema + "." + table + " LIMIT 100";
			ctx.setResponseType(MediaType.html);
			SimpleHTMLBuilder html = new SimpleHTMLBuilder();

			DataSource ds = require(DataSource.class);
			try (Connection conn = ds.getConnection()) {
				try (ResultSet resultSet = conn.createStatement().executeQuery(sql)) {

					String requestPathOf = requestPathOf(ctx);
					String url = requestPathOf.substring(0, requestPathOf.lastIndexOf('/'));

					html.a(url, "Data");

					buildTableOfMetaData(html, resultSet.getMetaData());
				}
			}
			return html.toStandardPage(schema + "." + table);
		});

		get("/{schema}/{table}/{openId}", ctx -> {
			String schema = ctx.path("schema").value();
			String table = ctx.path("table").value();
			String openId = ctx.path("openId").value();
			String sql = "SELECT * FROM " + schema + "." + table + " WHERE open_id=?";
			// work with data source
			DataSource ds = require(DataSource.class);
			ctx.setResponseType(MediaType.html);
			SimpleHTMLBuilder html = new SimpleHTMLBuilder();
			try (Connection conn = ds.getConnection()) {
				PreparedStatement prepareStatement = conn.prepareStatement(sql);
				prepareStatement.setString(1, openId);
				try (ResultSet resultSet = prepareStatement.executeQuery()) {

					html.a(requestPathOf(ctx) + "/schema", "schema");

					buildData(ctx, html, resultSet);
				}
			}
			return html.toStandardPage(schema + "." + table + " : " + openId);
		});

		post("/", ctx -> {
			ctx.setResponseType(MediaType.html);
			SimpleHTMLBuilder html = new SimpleHTMLBuilder();

			return html.toStandardPage("Json merge playground");
		});

	}

	public void buildTables(Context ctx, ResultSet resultSet, SimpleHTMLBuilder html) throws SQLException {
		html.table_();
		html.thead_();
		{
			html.tr_();

			html.th("left", 40, "schema");
			html.th("left", 40, "tableName");
			html.th("left", 40, "tableType");

			html._tr();
		}
		html._thead();

		html.tbody_();
		while (resultSet.next()) {
			html.tr_();
			{
				html.td("left", resultSet.getString("table_schema"));
				String tableName = resultSet.getString("table_name");
				html.td_("left");
				html.a(requestPathOf(ctx) + "/" + resultSet.getString("table_schema") + "/" + tableName + "", tableName);
				html._td();

				html.td("left", resultSet.getString("table_type"));
			}
			html._tr();
		}

		html._tbody();
		html._table();
	}

	public void buildDataList(Context ctx, SimpleHTMLBuilder html, ResultSet resultSet) throws SQLException {
		ResultSetMetaData metaData = resultSet.getMetaData();

		if (metaData.getColumnName(1).equals("open_id") && metaData.getColumnName(2).equals("json_content")) {
			System.out.println("json meta");
			html.table_();
			html.thead_();
			{
				html.tr_();

				html.th("left", 40, "openId");
				html.th("left", 40, "createAt");
				html.th("left", 40, "updateAT");
				html._tr();
			}
			html._thead();

			html.tbody_();
			while (resultSet.next()) {
				String openId = resultSet.getString("open_id");
				html.tr_();
				html.td_("left");
				html.a(requestPathOf(ctx) + "/" + openId, StringEscapeUtils.escapeHtml4(openId));
				html.td("left", resultSet.getTimestamp("create_at"));
				html.td("left", resultSet.getTimestamp("update_at"));
				html._tr();
			}

			html._tbody();
			html._table();
		} else {
			html.table_();
			html.thead_();
			{
				html.tr_();

				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					html.th("left", 40, metaData.getColumnName(i));
				}
				html._tr();
			}
			html._thead();

			html.tbody_();
			while (resultSet.next()) {
				html.tr_();
				{
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						String columnClassName = metaData.getColumnClassName(i);
						switch (columnClassName) {
						case "java.lang.String":
							String string = resultSet.getString(i);
							html.td("left", StringEscapeUtils.escapeHtml4(string));
							break;
						case "java.lang.Long":
							html.td("right", resultSet.getLong(i));
							break;
						case "java.lang.Integer":
							html.td("right", resultSet.getInt(i));
							break;
						case "java.lang.Byte":
							html.td("right", resultSet.getByte(i));
							break;
						case "java.lang.Short":
							html.td("right", resultSet.getShort(i));
							break;
						case "java.lang.Double":
							html.td("right", resultSet.getDouble(i));
							break;
						case "java.lang.Float":
							html.td("right", resultSet.getFloat(i));
							break;
						case "java.lang.Boolean":
							html.td("center", resultSet.getBoolean(i));
							break;
						case "java.math.BigDecimal":
							html.td("left", resultSet.getBigDecimal(i));
							break;
						case "java.sql.Time":
							html.td("left", resultSet.getTime(i));
							break;
						case "java.sql.Date":
							html.td("left", resultSet.getDate(i));
							break;
						case "java.sql.Timestamp":
							html.td("left", resultSet.getTimestamp(i));
							break;
						case "java.util.UUID": {
							Object object = resultSet.getObject(i);
							html.td("left", object);
						}
							break;
						case "java.sql.Array": {
							Object object = resultSet.getObject(i);
							html.td("left", object);
						}
							break;
						case "org.postgresql.util.PGInterval": {
							Object object = resultSet.getObject(i);
							html.td("left", object);
						}
							break;

						default:
							html.td("left", columnClassName);
							System.out.println(metaData.getColumnName(i) + " " + columnClassName);
						}
					}
				}
				html._tr();
			}

			html._tbody();
			html._table();
		}
	}

	public void buildData(Context ctx, SimpleHTMLBuilder html, ResultSet resultSet) throws SQLException {
		boolean result = resultSet.next();
		if (!result) return;

		html.tag_("pre");
		String jsonContent = resultSet.getString("json_content");
		html.append(jsonContent);

		html._tag("pre");

//		
//		html.table_();
//		html.thead_();
//		{
//			html.tr_();
//
//			for (int i = 1; i <= metaData.getColumnCount(); i++) {
//
//				html.th("left", 40, metaData.getColumnName(i));
//			}
//			html._tr();
//		}
//		html._thead();
//
//		html.tbody_();
//		while (resultSet.next()) {
//			html.tr_();
//			{
//				for (int i = 1; i <= metaData.getColumnCount(); i++) {
//					String columnClassName = metaData.getColumnClassName(i);
//					switch (columnClassName) {
//					case "java.lang.String":
//						String string = resultSet.getString(i);
//						html.td("left", StringEscapeUtils.escapeHtml4(string));
//						break;
//					case "java.lang.Long":
//						html.td("right", resultSet.getLong(i));
//						break;
//					case "java.lang.Integer":
//						html.td("right", resultSet.getInt(i));
//						break;
//					case "java.lang.Byte":
//						html.td("right", resultSet.getByte(i));
//						break;
//					case "java.lang.Short":
//						html.td("right", resultSet.getShort(i));
//						break;
//					case "java.lang.Double":
//						html.td("right", resultSet.getDouble(i));
//						break;
//					case "java.lang.Float":
//						html.td("right", resultSet.getFloat(i));
//						break;
//					case "java.lang.Boolean":
//						html.td("center", resultSet.getBoolean(i));
//						break;
//					case "java.math.BigDecimal":
//						html.td("left", resultSet.getBigDecimal(i));
//						break;
//					case "java.sql.Time":
//						html.td("left", resultSet.getTime(i));
//						break;
//					case "java.sql.Date":
//						html.td("left", resultSet.getDate(i));
//						break;
//					case "java.sql.Timestamp":
//						html.td("left", resultSet.getTimestamp(i));
//						break;
//					case "java.util.UUID": {
//						Object object = resultSet.getObject(i);
//						html.td("left", object);
//					}
//						break;
//					case "java.sql.Array": {
//						Object object = resultSet.getObject(i);
//						html.td("left", object);
//					}
//						break;
//					case "org.postgresql.util.PGInterval": {
//						Object object = resultSet.getObject(i);
//						html.td("left", object);
//					}
//						break;
//
//					default:
//						html.td("left", columnClassName);
//						System.out.println(metaData.getColumnName(i) + " " + columnClassName);
//					}
//				}
//			}
//			html._tr();
//		}
//
//		html._tbody();
//		html._table();
	}

	public void buildTableOfMetaData(SimpleHTMLBuilder html, ResultSetMetaData metaData) throws SQLException {
		html.table_();
		html.thead_();
		{
			html.tr_();
			html.th("left", 40, "getTableName");
			html.th("left", 40, "getColumnName");
			html.th("left", 40, "getColumnTypeName");
			html.th("left", 40, "getColumnType");
			html.th("left", 40, "getColumnDisplaySize");
			html.th("left", 40, "getColumnClassName");
			html._tr();
		}
		html._thead();

		html.tbody_();
		for (int i = 1; i <= metaData.getColumnCount(); i++) {
			html.tr_();
			{
				html.td("left", metaData.getTableName(i));
				html.td("left", metaData.getColumnName(i));
				html.td("left", metaData.getColumnTypeName(i));
				html.td("left", JDBCType.valueOf(metaData.getColumnType(i)).getName());
				html.td("left", metaData.getColumnDisplaySize(i));
				html.td("left", metaData.getColumnClassName(i));
			}
			html._tr();
		}
		html._tbody();
		html._table();
	}
}

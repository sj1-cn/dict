package mesoor.datasource.pgdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DatabaseServlet {
	public static final String DEFAULT_SCHEMA = "ruleengine";
	DataSource dataSource;
	Map<String, TenantDefine> tenants;

	public DatabaseServlet(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
		this.tenants = new ConcurrentHashMap<>();
	}

	public void init() throws SQLException {

		Connection conn = dataSource.getConnection();
		ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM " + DEFAULT_SCHEMA + "." + "entity_catalog" + " LIMIT 100");

		while (resultSet.next()) {
			String tenantId = resultSet.getString("tenant_id");
			String entityType = resultSet.getString("entity_type");
			String tableSchema = resultSet.getString("table_schema");
			String tableName = resultSet.getString("table_name");

			append(tenantId, entityType, tableSchema, tableName);
		}
		resultSet.close();

	}

	protected void append(String tenantId, String entityType, String tableSchema, String tableName) {
		TenantDefine tenant = getTenant(tenantId);
		tenant.append(entityType, tableSchema, tableName);
	}

	ObjectMapper objectMapper = new ObjectMapper();

	public String get(String tenantId, String entityType, String openId) throws SQLException, JsonMappingException, JsonProcessingException {
		Connection conn = dataSource.getConnection();
		TenantDefine tenantDefine = tenants.get(tenantId);
		if (tenantDefine == null) return null;
		EntityDefine entityDefine = tenantDefine.get(entityType);
		if (entityDefine == null) return null;

		PreparedStatement prepareStatement = conn.prepareStatement("SELECT open_id,json_content,create_at,update_at FROM " + entityDefine.tableSchema + "." + entityDefine.tableName + " WHERE OPEN_ID = ?");
		prepareStatement.setString(1, openId);
		ResultSet resultSet = prepareStatement.executeQuery();
		resultSet.next();
		String jsonContentString = resultSet.getString("json_content");
//		Timestamp stageCreateAt = resultSet.getTimestamp("create_at");
//		Timestamp stageUpdatAt = resultSet.getTimestamp("update_at");

		if (jsonContentString == null) {
			return null;
		}
		resultSet.close();

		return jsonContentString;

	}

	public String post(String tenantId, String entityType, String openId, String jsonContent) throws SQLException, JsonMappingException, JsonProcessingException {
		Connection conn = dataSource.getConnection();
		TenantDefine tenantDefine = tenants.get(tenantId);
		if (tenantDefine == null) {
			tenantDefine = new TenantDefine(tenantId);
			tenants.put(tenantId, tenantDefine);
		}

		EntityDefine entityDefine = getEntityType(tenantDefine, entityType);

		PreparedStatement prepareStatement = conn
				.prepareStatement("INSERT INTO " + entityDefine.tableSchema + "." + entityDefine.tableName + " (open_id,json_content,create_at,update_at) VALUES(?,?::JSON,?,?)");
		prepareStatement.setString(1, openId);
		prepareStatement.setString(2, jsonContent);
		prepareStatement.setTimestamp(3, Timestamp.from(Instant.now()));
		prepareStatement.setTimestamp(4, Timestamp.from(Instant.now()));

		prepareStatement.execute();
		conn.close();

		return get(tenantId, entityType, openId);
	}

	public String put(String tenantId, String entityType, String openId, String jsonContent) throws SQLException, JsonMappingException, JsonProcessingException {
		Connection conn = dataSource.getConnection();
		TenantDefine tenantDefine = tenants.get(tenantId);
		if (tenantDefine == null) {
			tenantDefine = new TenantDefine(tenantId);
			tenants.put(tenantId, tenantDefine);
		}

		EntityDefine entityDefine = getEntityType(tenantDefine, entityType);
		int updateCount = 0;

		{
			PreparedStatement prepareStatement = conn
					.prepareStatement("UPDATE " + entityDefine.tableSchema + "." + entityDefine.tableName + " SET json_content=?::JSON , create_at=?,update_at=? WHERE open_id=?");
			prepareStatement.setString(1, jsonContent);
			prepareStatement.setTimestamp(2, Timestamp.from(Instant.now()));
			prepareStatement.setTimestamp(3, Timestamp.from(Instant.now()));
			prepareStatement.setString(4, openId);

			prepareStatement.execute();
			updateCount = prepareStatement.getUpdateCount();
		}

		if (updateCount == 0) {
			PreparedStatement prepareStatement = conn
					.prepareStatement("INSERT INTO " + entityDefine.tableSchema + "." + entityDefine.tableName + " (open_id,json_content,create_at,update_at) VALUES(?,?::JSON,?,?)");
			prepareStatement.setString(1, openId);
			prepareStatement.setString(2, jsonContent);
			prepareStatement.setTimestamp(3, Timestamp.from(Instant.now()));
			prepareStatement.setTimestamp(4, Timestamp.from(Instant.now()));
			prepareStatement.execute();
		}
		conn.close();

		return get(tenantId, entityType, openId);
	}

	protected TenantDefine getTenant(String tenantId) {
		TenantDefine tenant = tenants.get(tenantId);
		if (tenant == null) {
			tenant = new TenantDefine(tenantId);
			tenants.put(tenantId, tenant);
		}
		return tenant;
	}

	protected EntityDefine getEntityType(TenantDefine tenant, String entityType) throws SQLException {
		Connection conn = dataSource.getConnection();
		EntityDefine entityDefine = tenant.get(entityType);
		if (entityDefine == null) {
			String tableSchema = DEFAULT_SCHEMA;
			String tableName = encodeForTableName(tenant, entityType);
			String createTableDDL = "CREATE TABLE " + tableSchema + "." + tableName + " (\n"
					+ "	open_id varchar(128) NOT NULL,\n"
					+ "	json_content json NULL,\n"
					+ "	create_at timestamp NOT NULL,\n"
					+ "	update_at timestamp NOT NULL,\n"
					+ "	CONSTRAINT " + tableSchema + "_" + tableName + "_pk PRIMARY KEY (open_id)\n"
					+ ");";

			boolean result = false;
			try {
				result = conn.createStatement().execute(createTableDDL);
			} catch (Exception e) {
				result = false;
			}

			if (!result) {
				conn.createStatement().execute("DROP TABLE " + tableSchema + "." + tableName + ";");
				result = conn.createStatement().execute(createTableDDL);

			}
//
//			String tenantId = resultSet.getString("tenant_id");
//			String entityType = resultSet.getString("entity_type");
//			String tableSchema = resultSet.getString("table_schema");
//			String tableName = resultSet.getString("table_name");

			PreparedStatement prepareStatement = conn
					.prepareStatement("INSERT INTO " + DEFAULT_SCHEMA + "." + "entity_catalog" + " (tenant_id,entity_type,table_schema,table_name) VALUES(?,?,?,?)");
			prepareStatement.setString(1, tenant.tenantId);
			prepareStatement.setString(2, entityType);
			prepareStatement.setString(3, tableSchema);
			prepareStatement.setString(4, tableName);
			prepareStatement.execute();

			entityDefine = tenant.append(entityType, tableSchema, tableName);
		}
		return entityDefine;
	}

	public String encodeForTableName(TenantDefine tenant, String entityType) {
		String tableName = tenant.tenantId + "_" + entityType;
		return tableName.toLowerCase();
	}

	/*
	 * 
	 * CREATE TABLE ruleengine.entity_rulenegine_config ( open_id varchar(128) NOT
	 * NULL, json_content jsonb NULL, create_at timestamp NOT NULL, update_at
	 * timestamp NOT NULL, CONSTRAINT entity_rulenegine_config_pk PRIMARY KEY
	 * (open_id) );
	 */

}

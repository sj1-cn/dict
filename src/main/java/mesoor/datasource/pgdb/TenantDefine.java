package mesoor.datasource.pgdb;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TenantDefine {

	final Map<String, EntityDefine> entities = new ConcurrentHashMap<>();
	final String tenantId;

	public TenantDefine(String tenantId) {
		this.tenantId = tenantId;
	}

	public EntityDefine append(String entityType, String tableSchema, String tableName) {
		EntityDefine value = new EntityDefine(tableSchema, tableName);
		entities.put(entityType, value);
		return value;
	}

	public EntityDefine get(String entityType) {
		return entities.get(entityType);
	}
}

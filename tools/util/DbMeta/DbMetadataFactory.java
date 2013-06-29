package util.DbMeta;

import util.DbMeta.impl.DB2Metadata;
import util.DbMeta.impl.HsqlDbMetadata;
import util.DbMeta.impl.MysqlMetadata;
import util.DbMeta.impl.OracleMetadata;
import util.DbMeta.impl.PostgreSQLMetadata;
import util.DbMeta.impl.SqlServerMetadata;


/**
 * DbMetadata的工厂，用于创建特定数据库类型的DbMetadata 
 * @author	王佳
 * @version	1.2
 */
public class DbMetadataFactory {

	public static String[] getDbTypes() {
		return new String[] {
			DbMetadata.TYPE_ORALCE, 
			DbMetadata.TYPE_DB2,
			DbMetadata.TYPE_MYSQL, 
			DbMetadata.TYPE_SQLSERVER, 
			DbMetadata.TYPE_HSQLDB,
			DbMetadata.TYPE_POSTGRES
			
		};
	}
	
	public static String[] getUrlExamples() {
		return new String[] {
			DbMetadata.ORACLE_URL,
			DbMetadata.DB2_URL,
			DbMetadata.MYSQL_URL,
			DbMetadata.SQLSERVER_URL,
			DbMetadata.HSQLDB_URL_SERVER,
			DbMetadata.HSQLDB_URL_WEB_SERVER,
			DbMetadata.HSQLDB_URL_STD_ALONG,
			DbMetadata.POSTGRESQL_URL
			
		};
	}
	
	private static DbMetadata dbMetadata;
	
	private static String dbTypeName;
	
	/**
	 * 通过JDBC驱动返回数据库类型
	 * @param jdbcDriver JDBC驱动
	 * @return 代表数据库类型的字符串
	 */
	public static String getDbType(String jdbcDriver) {
		if (jdbcDriver.equalsIgnoreCase(DbMetadata.ORACLE_DRIVER)) {
			dbTypeName = DbMetadata.TYPE_ORALCE;
			return dbTypeName;
		} else if (jdbcDriver.equalsIgnoreCase(DbMetadata.MYSQL_DRIVRE)) {
			dbTypeName = DbMetadata.TYPE_MYSQL;
			return dbTypeName;
		} else if (jdbcDriver.equalsIgnoreCase(DbMetadata.SQLSERVER_DRIVER)) {
			dbTypeName = DbMetadata.TYPE_SQLSERVER;
			return dbTypeName;
		}
		else if (jdbcDriver.equalsIgnoreCase(DbMetadata.HSQLDB_DRIVER)) {
			dbTypeName = DbMetadata.TYPE_HSQLDB;
			return dbTypeName;
		}
		else if (jdbcDriver.equalsIgnoreCase(DbMetadata.POSTGRESQL_DRIVER)) {
			dbTypeName = DbMetadata.TYPE_POSTGRES;
			return dbTypeName;
		}else if(jdbcDriver.equalsIgnoreCase(DbMetadata.DB2_DRIVER)){
			dbTypeName = DbMetadata.TYPE_DB2;
			return dbTypeName;
		}

		return null;
	}
	
	/**
	 * 通过数据库类型返回url示例
	 * @param dbTypes 数据库类型
	 * @return 代表url示例的字符串
	 */
	public static String getUrlExample(String dbTypes) {
		if (dbTypes.equalsIgnoreCase(DbMetadata.TYPE_ORALCE)) {
			return DbMetadata.ORACLE_URL;
		} else if (dbTypes.equalsIgnoreCase(DbMetadata.TYPE_MYSQL)) {
			return DbMetadata.MYSQL_URL;			
		} else if (dbTypes.equalsIgnoreCase(DbMetadata.TYPE_SQLSERVER)) {
			return DbMetadata.SQLSERVER_URL;
		} else if (dbTypes.equalsIgnoreCase(DbMetadata.TYPE_HSQLDB)) {
			return DbMetadata.HSQLDB_URL_SERVER;		 
		}else if (dbTypes.equalsIgnoreCase(DbMetadata.TYPE_POSTGRES)) {
			return DbMetadata.POSTGRESQL_URL;		 
		}else if(dbTypes.equalsIgnoreCase(DbMetadata.TYPE_DB2)){
			return DbMetadata.DB2_URL;
		}
		
		return null;
	}
	
	/**
	 * 根据dbTypeName的数据库类型，返回对应的数据库驱动� 
	 * 当数据库类型非oracle,mysql,sqlserve,返回null
	 * @return 数据库驱动字符串
	 */
	public static String getJdbcDriver() {
		if (dbTypeName.equalsIgnoreCase(DbMetadata.TYPE_ORALCE)) {
			return DbMetadata.ORACLE_DRIVER;
		} 
		else if (dbTypeName.equalsIgnoreCase(DbMetadata.TYPE_MYSQL)) {
			return DbMetadata.MYSQL_DRIVRE;
		} 
		else if (dbTypeName.equalsIgnoreCase(DbMetadata.TYPE_SQLSERVER)) {
			return DbMetadata.SQLSERVER_DRIVER;
		} 
		else if (dbTypeName.equalsIgnoreCase(DbMetadata.TYPE_HSQLDB)) {
			return DbMetadata.HSQLDB_DRIVER;
		} 
		else if (dbTypeName.equalsIgnoreCase(DbMetadata.TYPE_POSTGRES)) {
			return DbMetadata.POSTGRESQL_DRIVER;
		}
		else if(dbTypeName.equalsIgnoreCase(DbMetadata.TYPE_DB2)){
			return DbMetadata.DB2_DRIVER;
		}
		return null;
	}
	/**
	 * 验证JDBC连接参数并创建特定数据库类型的DbMetadata 
	 * @param jdbcDriver JDBC驱动
	 * @param jdbcUrl JDBC连接字符串
	 * @param username 数据库连接用户名
	 * @param password 数据库连接密码
	 * @return 创建是否成功
	 */
	public static boolean createDbMetadata(String dbType, String jdbcUrl, String username, String password) {
		DbMetadata metadata = null;
		
		if (dbType.equals(DbMetadata.TYPE_ORALCE)) {
			metadata = new OracleMetadata(DbMetadata.ORACLE_DRIVER, jdbcUrl, username, password);
		} 
		else if (dbType.equals(DbMetadata.TYPE_MYSQL)) {
			metadata = new MysqlMetadata(DbMetadata.MYSQL_DRIVRE, jdbcUrl, username, password);
		} 
		else if (dbType.equals(DbMetadata.TYPE_SQLSERVER)) {
			metadata = new SqlServerMetadata(DbMetadata.SQLSERVER_DRIVER, jdbcUrl, username, password);
		} 
		else if (dbType.equals(DbMetadata.TYPE_POSTGRES)) {
			metadata = new PostgreSQLMetadata(DbMetadata.POSTGRESQL_DRIVER, jdbcUrl, username, password);
		}
		else if (dbType.equals(DbMetadata.TYPE_HSQLDB)) {
			metadata = new HsqlDbMetadata(DbMetadata.HSQLDB_DRIVER,jdbcUrl, username, password);
		}
		else if(dbType.equals(DbMetadata.TYPE_DB2)){
			metadata = new DB2Metadata(DbMetadata.DB2_DRIVER,jdbcUrl, username, password);
		}

		if (metadata != null && metadata.connectToDb()) { 
			dbMetadata = metadata;
			return true;
		}
		
		return false;
	}

	/**
	 * 返回创建的DbMetadata实例 
	 * @return DbMetadata的实例，如果没有创建，返回null
	 */
	public static DbMetadata getDbMetadata() {
		return dbMetadata;
	}

	/**
	 * 设置dbTypeName 
	 * @param dbTypeName dbTypeName
	 */
	public static void setDbTypeName(String dbTypeName) {
		DbMetadataFactory.dbTypeName = dbTypeName;
	}

}

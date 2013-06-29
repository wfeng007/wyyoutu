package util.DbMeta;

import java.util.List;
import java.util.Map;

/**
 * 定义描述数据库元数据的接口，包含JDBC连接参数（driver，url，username及password），
 * 同时提供一些用于获取表空间，表及表映射信息的方法� 
 * @author	王佳
 * @version	1.2
 */
public interface DbMetadata {
	String TYPE_ORALCE = "Oracle";
	String TYPE_MYSQL = "MySQL";
	String TYPE_SQLSERVER = "SQL Server";
	String TYPE_HSQLDB = "HSQLDB";
	String TYPE_POSTGRES = "PostgreSQL";
	String TYPE_DB2="DB2";
	
	String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
	String MYSQL_DRIVRE = "com.mysql.jdbc.Driver";
	String SQLSERVER_DRIVER = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	String HSQLDB_DRIVER = "org.hsqldb.jdbcDriver";
//	String MOCK_DRIVER = "db.MockDriver";
	String POSTGRESQL_DRIVER = "org.postgresql.Driver";
	String DB2_DRIVER = "com.ibm.db2.jcc.DB2Driver";
	
	String ORACLE_URL = "jdbc:oracle:thin:@<host>:<port>:<sid>";
	String MYSQL_URL = "jdbc:mysql://<host>/<database>";
	String SQLSERVER_URL = "jdbc:microsoft:sqlserver://<host>:<port>;DatabaseName=<database>";
	String HSQLDB_URL_SERVER = "jdbc:hsqldb:hsql://<host>:<port>";
	String HSQLDB_URL_WEB_SERVER = "jdbc:hsqldb:http://<host>";
	String HSQLDB_URL_STD_ALONG = "jdbc:hsqldb:file:<database>|<path>";
	String POSTGRESQL_URL = "jdbc:postgresql://<host>:<port>/<database>";
	String DB2_URL="jdbc:db2://<server>:<port50000>/<database>";
	// 属性� 
	/**
	 * 获取jdbcDriver 
	 * @return jdbcDriver
	 */
	String getJdbcDriver();
	
	/**
	 * 获取jdbcUrl 
	 * @return jdbcUrl
	 */
	String getJdbcUrl();
	
	/**
	 * 获取username 
	 * @return username
	 */
	String getUsername();
	
	/**
	 * 获取password 
	 * @return password
	 */
	String getPassword();
	
	// 方法
	/**
	 * 测试使用提供的JDBC连接参数连接数据库� 
	 * @return 是否能够连接到数据库
	 */
	boolean connectToDb();
	
	/**
	 * 获取用户可访问的表空间� 
	 * 通常用于Oracle数据库，其它数据库返回一个空的List 
	 * @return 包含用户可访问的表空间名称的String列表
	 */
	List getSchemaList(); 
	
	/**
	 * 获取用户默认表空间� 
	 * 通常用于Oracle数据库，其它数据库返回null 
	 * @return 用户默认表空间名称
	 */
	String getDefaultSchema();
	
	/**
	 * 获取表的List 
	 * @return 包含表名称的List
	 */
	List getTableList();
	
	/**
	 * 获取指定表空间的表的List 
	 * @param tablespace 指定表空间的名称
	 * @return 包含表名称的String列表
	 */
	List getTableList(String tablespace);
	
	/**
	 * 选择获取表映射信息的表�
	 * 等价于调用selectTables(selectedTables, null) 
	 * @param selectedTables 选择的表名的List
	 */
	void selectTables(List selectedTables);
	
	/**
	 * 选择获取表映射信息的表及表空间� 
	 * @param selectedTables 选择的表名的List
	 * @param selectedTablespace 选择的表空间名，如果数据库不支持表空间，可设置为null
	 */
	void selectTables(List selectedTables, String selectedTablespace);
	

	/**
	 *  获取表映射信息的列表
	 * @param defaultPackage
	 * @param schema
	 * @param generateDbOrder
	 * @return
	 * @throws Exception
	 */
	List getTableMappingList(String defaultPackage, String schema, boolean generateDbOrder) throws Exception;
	
	/**
	 * 取得用户选定的表空间
	 * @return 用户选定的表空间
	 */
	String getSelectedTablespace();
	
	/**
	 * 取得用户选定的表
	 */
	List getSelectedTables ();
	
//	/**
//	 * 取得页面显示类型与字段的实际类型的映射列表
//	 */
//	Map getTypeMap ();
	
	/**
	 * 置入数据库类型
	 */
	void setDbType(String dbType);
	 
	/**
	 * 取得数据库类型
	 * @return 数据库类型 
	 */ 
	String getDbType(); 
	
	/**
	 * 验证是否具有选择的表的访问权限
	 * @return
	 */
	boolean validateAccessPermission(Object[] tableName, String schema);
}

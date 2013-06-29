package util.DbMeta.impl;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import util.DbMeta.DbMetadata;
import util.DbMeta.PropertyMapping;
import util.DbMeta.TableMapping;

//import oracle.jdbc.OracleConnection;



/**
 * 实现DbDataMeta接口,给出操作数据的各种方法的通用实现 
 * @author	王佳
 * @version	1.2
 * @history	2006-11-1	王佳创建 
 */
public abstract class AbstractDbMetadata implements DbMetadata {

	protected String jdbcDriver;

	protected String jdbcUrl;

	protected String username;

	protected String password;

	protected String selectedTablespace;

	protected List selectedTables = new ArrayList();

	protected static final String[] TABLE_TYPE = new String[] { "TABLE", "VIEW" };

	protected static final String TABLE_NAME = "TABLE_NAME";

	protected static final String SQL_SELECT = "select * from ";

	protected static final String FILE_TYPE = ".xml";

	private static final String COLUMN_NAME = "column_name";

	protected static final Map databaseTypeMap = new HashMap(); // 存放数据库类型与ｊａｖａ类型的映射1�7

	private String dbType;

	private static final String[] constantColumn = { "rec_creator",
			"rec_create_time", "rec_revisor", "rec_revise_time", "archive_flag" };

	//页面的显示类型与实际类型的映射，key为显示类型，value为实际类垄1�7
//	private static final Map typeMap = new HashMap();
	static {
		//初始化数据库类型与java类型的映射 这将会时原始的className替换为下面类型 并设置到PropertyType
		//替换判断忽略key大小写
		//databaseTypeMap.put("Integer", "Integer");
		databaseTypeMap.put("BigDecimal", "Double"); //大数字 还是用double
		databaseTypeMap.put("Timestamp", "java.util.Date"); //timestamp使用date?
		databaseTypeMap.put("Time", "java.util.Date");
		databaseTypeMap.put("CLOB", "java.lang.String"); //clob 使用String
		databaseTypeMap.put("Boolean", "Boolean"); // 这里要求都用Boolean?有这种类型?
		databaseTypeMap.put("String", "java.lang.String"); //
		databaseTypeMap.put("Date", "java.util.Date"); //

		//初始化界面显示类型与字段实际类型的映射，key为显示类型，value为实际类型
//		typeMap.put("Integer", "java.lang.Integer");
//		typeMap.put("Double", "java.lang.Double");
//		typeMap.put("Float", "java.lang.Float");
//		typeMap.put("Long", "java.lang.Long");
//		typeMap.put("String", "java.lang.String");
//		typeMap.put("Date", "java.util.Date");
//		typeMap.put("byte[]", "byte[]");
//		typeMap.put("Blob", "java.sql.Blob");
//		typeMap.put("Clob", "java.sql.Clob");
//		typeMap.put("BigDecimal", "java.math.BigDecimal");
//		typeMap.put("Timestamp", "java.sql.Timestamp");
//		typeMap.put("int", "int");
//		typeMap.put("double", "double");
//		typeMap.put("long", "long");
//		typeMap.put("float", "float");
//		typeMap.put("boolean", "boolean");
//		typeMap.put("Boolean", "java.lang.Boolean");

	}

	/**
	 * 构造函数
	 * @param jdbcDriver 数据库jdbc驱动字符串
	 * @param jdbcUrl    数据库的jdbcUrl
	 * @param username   数据库用户名
	 * @param password   数据库密码
	 */
	public AbstractDbMetadata(String jdbcDriver, String jdbcUrl,
			String username, String password) {
		this.jdbcDriver = jdbcDriver;
		this.jdbcUrl = jdbcUrl;
		this.username = username;
		this.password = password;

	}

	/**
	 * 测试使用提供的JDBC连接参数连接数据库� 
	 * @return 是否能够连接到数据库
	 */
	public boolean connectToDb() {
		Connection con = null;
		try {
			con = getConnection();
			if (con == null) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				con.close();
			} catch (Exception ex) {
				return false;
			}
			return false;
		} finally {
			closeConnection(con, null);
		}

		return true;
	}

	/**
	 * 获取jdbcDriver 
	 * @return jdbcDriver
	 */
	public String getJdbcDriver() {
		return this.jdbcDriver;
	}

	/**
	 * 获取jdbcUrl 
	 * @return jdbcUrl
	 */
	public String getJdbcUrl() {

		return this.jdbcUrl;
	}

	/**
	 * 获取password 
	 * @return password
	 */
	public String getPassword() {

		return this.password;
	}

	/**
	 * 获取用户可访问的表空间� 
	 * 通常用于Oracle数据库，其它数据库返回一个空的List 
	 * @return 包含用户可访问的表空间名称的String列表
	 */
	public List getSchemaList() {
		List schemList = new ArrayList();
		Connection con = null;
		try {
			con = getConnection();
			DatabaseMetaData metaData = con.getMetaData();
			ResultSet schemas = metaData.getSchemas();
			while (schemas.next()) {
				schemList.add(schemas.getString("TABLE_SCHEM"));
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(con, null, null);
		}
		return schemList;
	}

	/**
	 * 获取用户默认表空间� 
	 * 通常用于Oracle数据库，其它数据库返回null 
	 * @return 用户默认表空间名称
	 */
	public String getDefaultSchema() {

		return null;
	}

	/**
	 * 获取username 
	 * @return username
	 */
	public String getUsername() {

		return this.username;
	}

	/**
	 * 设置选定的表。该方法无法设置用户表空间� 
	 * @param  selectedTables 用户选定的表的LIST
	 */
	public void selectTables(List selectedTables) {

		this.selectedTables = selectedTables;
	}

	/**
	 * 设置选定的表和表空间,同时设置数据库表空间 
	 * @param  selectedTables 用户选定的表 
	 * @param  selectedTablespace 用户选定的表空间 
	 */
	public void selectTables(List selectedTables, String selectedTablespace) {

		this.selectedTables = selectedTables;
		this.selectedTablespace = selectedTablespace;
	}

	/**
	 * 获得数据库连接
	 * @return Connection  数据库联接� 
	 */

	protected Connection getConnection() throws Exception {

		Enumeration driverEnum = DriverManager.getDrivers(); //得到数据库驱动的enum
		while (driverEnum.hasMoreElements()) //清空enum中的驱动
		{
			Driver driver = (Driver) driverEnum.nextElement();
			DriverManager.deregisterDriver(driver);
		}

		DriverManager.registerDriver((Driver) Class.forName(jdbcDriver)
				.newInstance()); //加载数据库驱劄1�7	

		Connection con = DriverManager.getConnection(jdbcUrl, username,
				password);
		return con;
	}

	/**
	 * 将数据库的表的名字转换成java类的名字
	 * @param 数据库表名
	 * @return 转换后的类的名字.
	 */
	protected String mapTableToClass(String tableName) {

		StringBuffer buffer = new StringBuffer();
		char[] cs = tableName.toLowerCase().trim().toCharArray(); //先将表名转换为小冄1�7

		//将表名的首字母转换成大写
		if (cs[0] >= 97 && cs[0] <= 122) {
			cs[0] = (char) (cs[0] - 32);
		}
		for (int i = 0; i < cs.length; i++) {

			if (cs[i] == '_') { //将表名�1�7�_”后的字母转换成大写
				if (cs[i + 1] >= 97 && cs[i + 1] <= 122) {
					cs[i + 1] = (char) (cs[i + 1] - 32);
				}
			} else
				buffer.append(cs[i]);
		}

		return buffer.toString();
	}
	
	/**
	 * 将表名转为文件名
	 * 修改为 xxxxDao.mpr.xml
	 * @param tableName
	 * @return
	 */
	protected String mapTableTosqlFile(String tableName) {
		StringBuffer buffer = new StringBuffer();
		char[] cs = tableName.toLowerCase().trim().toCharArray(); //先将表名转换为小冄1�7

		//将表名的首字母转换成大写
		if (cs[0] >= 97 && cs[0] <= 122) {
			cs[0] = (char) (cs[0] - 32);
		}
		for (int i = 0; i < cs.length; i++) {

			if (cs[i] == '_' && i + 1 < cs.length) { //将表名��_”后的字母转换成大写
				if (cs[i + 1] >= 97 && cs[i + 1] <= 122) {
					cs[i + 1] = (char) (cs[i + 1] - 32);
				}
			} else
				buffer.append(cs[i]);
		}

		return (buffer.toString())+"Dao.mpr";
	}

	/**
	 * 将数据库的字段的名字转换成类的字段名
	 * @param 数据库字段的名字.
	 * @return 转换后的field的名字� 
	 */
	protected String mapColumnToField(String columnName) {
		StringBuffer buffer = new StringBuffer();
		char[] cs = columnName.toLowerCase().trim().toCharArray(); //将字段名转换成小冄1�7

		for (int i = 0; i < cs.length; i++) {

			if (cs[i] == '_' && i != 1 && (i + 1) < cs.length) { //后的字母转换成大冄1�7
				if (cs[i + 1] >= 97 && cs[i + 1] <= 122) {
					cs[i + 1] = (char) (cs[i + 1] - 32);
				}
			} else
				buffer.append(cs[i]);
		}

		return buffer.toString();
	}

	/**
	 * 获取表映射信息的列表
	 * <b>注意</b>: 请先调用selectTables方法选取表，否则将返回null 
	 * @param defaultPackage 表映射的实体类默认包名
	 * @return 表映射TableMapping的列表，包含表映射元数据信息,如果数据库联接异常，返回null
	 * @throws Exception 
	 */
	public List getTableMappingList(String defaultPackage, String schema,
			boolean generateDborder) throws Exception {
		List tableMappingList = new ArrayList();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = getConnection();

//			if (con instanceof OracleConnection) { //oracle必须设置才能读取comment
//				((OracleConnection) con).setRemarksReporting(true);
//			}
			DatabaseMetaData dbMeta = con.getMetaData();
			for (Iterator iter = selectedTables.iterator(); iter.hasNext();) {
				String element = (String) iter.next(); //取出选定的表的名称

				TableMapping tm = new TableMapping();
				tm.setPackageName(defaultPackage);
				tm.setTableName(element);
				tm.setClassName(mapTableToClass(element));

				//读取表的comment
				ResultSet tables = dbMeta.getTables(null, null, element,
						TABLE_TYPE);
				if (tables.next()) {
					String comment = tables.getString("REMARKS");
					if (comment != null) {
						tm.setComment(comment);
					} else {
						tm.setComment(" ");
					}
				}

				//设置sql-map的名称
				String namePrex = defaultPackage.replace('.', '/');
				String sqlMapName = generateSqlmapPath(namePrex) + "/"
						+ mapTableTosqlFile(element) + FILE_TYPE;
				tm.setSqlmapFile(sqlMapName);

				//设置表中字段的相关信息
				String sql = null;
				if (schema != null) {
					sql = SQL_SELECT + schema + "." + element;
				} else
					sql = SQL_SELECT + element;

				ps = con.prepareStatement(sql);
				rs = ps.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData(); // 得到表的ResultSetMetaData，映射字段信息
				tm.setPropertyMappingList(getPorpertyMappingList(rsmd, dbMeta,
						element, generateDborder)); //设置表中的字段的映射List
				tableMappingList.add(tm);
			}
			rs.close();
			ps.close();
			con.close();
		} catch (Exception e) {
			throw e;
		} finally {
			closeConnection(con, rs);
		}
		return tableMappingList;
	}



	/**
	 * 获取字段映射信息
	 * @param rsmd 数据库的ResultSetMetaData对象
	 * @param databaseMeta 数据库的DatabaseMetaData对象
	 * @param tableName 表名
	 * @return 包含字段映射的信息List
	 */
	protected List getPorpertyMappingList(ResultSetMetaData rsmd,
			DatabaseMetaData databaseMeta, String tableName,
			boolean generateDbOrder) throws Exception {

		List columnMapList = new ArrayList();
		int num = rsmd.getColumnCount(); // 得到表的列数
		List primaryKey = new ArrayList();

		//		 ResultSet columnSet= databaseMeta.getColumns(null, null, tableName,
		//		 null);
		// 得到主键
		ResultSet rs = databaseMeta.getPrimaryKeys(null, null, tableName);
		while (rs.next()) {
			primaryKey.add(rs.getString(COLUMN_NAME));
		}

		for (int i = 1; i <= num; i++) { // 遍历表的每一列 即所字段元数据
			PropertyMapping mapping = new PropertyMapping();

			int k = rsmd.isNullable(i); // 设置该字段是否为非空
			if (0 == k) {
				mapping.setNotNull(true);
			} else {
				mapping.setNotNull(false);
			}

			mapping.setColumnType(rsmd.getColumnTypeName(i)); // 保存数据库字段类型
			mapping.setType(generateType(rsmd.getColumnTypeName(i))); // 设置传递电文时需要的类型

			System.out.println(rsmd.getColumnName(i)+":type:"+rsmd.getColumnTypeName(i)+":className:"+rsmd.getColumnClassName(i));
			
			if (rsmd.getColumnTypeName(i).equalsIgnoreCase("BLOB")) { // mysql中, 没有解析 LONGBLOB的办法
				mapping.setPropertyType("byte[]");
			} else {
				String className = rsmd.getColumnClassName(i); //直接通过getColumnClassName获取jdbc认为对应的java类型而不是jdbctype
				
				//FIXME 这里将全名给去掉了 为何要去掉?
				int j = className.lastIndexOf("."); // 取得字段类型，去掉全名  比如如java.lang.String变成String
				if (j > 0) {
					className = className.substring(j + 1);
				}
				// 设置字段的长度，精度，映射java类型
				// FIXME 然后又在这里增加了全明parsePropertyType Sting -> java.lang.String
				mapping.setPropertyType(parsePropertyType(className, rsmd, i));
				setPropertySize(mapping, rsmd, i);
			}
			mapping.setColumnName(rsmd.getColumnName(i)); //字段名成
			mapping.setPropertyName(mapColumnToField(rsmd.getColumnName(i))); //生成java-property名称

			if (primaryKey.contains(rsmd.getColumnName(i))) { // 设置主键
				mapping.setPrimaryKey(true);
				mapping.setConditional(true);
			}

			columnMapList.add(mapping);  //添加到组
		}
		
		//根据是否为主键排序
		if (!generateDbOrder) {
			Collections.sort(columnMapList, new PrimiryKeyComparator());
			Collections.sort(columnMapList, new ArrangeColComparator());
		}
		return columnMapList;
	}

	/**
	 * 设置字段的分类varchar或者number，作为生成domain对象时的源数据信息
	 * 
	 * @param columnType
	 * @return
	 */
	abstract protected String generateType(String columnType);

	/**
	 * 设置字段的长度，和数字类型的小数的精度
	 * @param mapping
	 * @param rsmd
	 * @param i
	 * @throws SQLException
	 */
	abstract protected void setPropertySize(PropertyMapping mapping,
			ResultSetMetaData rsmd, int i) throws SQLException;

	/**
	 * 包装生成的字段类型
	 * TODO 这里其实应该根据数据类型进行影射判断...
	 * @param className
	 * @param precision
	 * @param scale
	 * @return
	 */
	protected String parsePropertyType(String className,
			ResultSetMetaData rsmd, int i) throws SQLException {

		//映射替换根据映射替换 有些值需要替换为可用
		for (Iterator iter = databaseTypeMap.entrySet().iterator(); iter
				.hasNext();) {
			Map.Entry element = (Map.Entry) iter.next();
			String key = (String) element.getKey();
			if (key.equalsIgnoreCase(className)) {

				return (String) element.getValue();
			}
		}
		
		return className;
	}


	/**
	 * 取得用户选定的表
	 * @see com.baosight.iplat4j.plugin.db.model.DbMetadata#getSelectedTables()
	 */
	public List getSelectedTables() {

		return this.selectedTables;
	}

	/**
	 * 取得用户选定的表空间
	 * @see com.baosight.iplat4j.plugin.db.model.DbMetadata#getSelectedTablespace()
	 */
	public String getSelectedTablespace() {

		return this.selectedTablespace;
	}

	/**
	 * 取得页面显示类型与字段的实际类型的映射列表
	 * @see com.baosight.iplat4j.plugin.db.model.DbMetadata#getTypeMap()
	 */
//	public Map getTypeMap() {
//
//		return AbstractDbMetadata.typeMap;
//	}

	/**
	 * 关闭数据库连接
	 */
	protected void closeConnection(Connection con, ResultSet rs) {

		this.closeConnection(con, rs, null);
	}

	/**
	 * 关闭数据库连接
	 */
	protected void closeConnection(Connection con, ResultSet rs,
			PreparedStatement ps) {
		try {
			if (con != null) {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取得数据库类型
	 * @see com.baosight.iplat4j.plugin.db.model.DbMetadata#getDbType()
	 */
	public String getDbType() {

		return this.dbType;
	}

	/**
	 * 置入数据库类型
	 * @see com.baosight.iplat4j.plugin.db.model.DbMetadata#setDbType(java.lang.String)
	 */
	public void setDbType(String dbType) {
		this.dbType = dbType;

	}

	protected String generateSqlmapPath(String namePrex) {

		int i = namePrex.lastIndexOf("/");
		if (i > 0) {
			return namePrex.substring(0, i) + "/sql";
		}
		return "sql";
	}

	/* (non-Javadoc)
	 * @see com.baosight.iPlat4j2.plugin.model.DbMeta.DbMetadata#validateAccessPermission(java.lang.String, java.lang.String)
	 */
	public boolean validateAccessPermission(Object[] tableName, String schema) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			for (int i = 0; i < tableName.length; i++) {
				String sql = null;
				if (schema != null) {
					sql = SQL_SELECT + schema + "." + (String) tableName[i];
				} else
					sql = SQL_SELECT + (String) tableName[i];
				ps = con.prepareStatement(sql);
				rs = ps.executeQuery();
				rs.close();
				ps.close();
			}

		} catch (Exception e) {
			closeConnection(con, rs, ps);
			return false;
		} finally {
			closeConnection(con, rs, ps);
		}
		return true;
	}

	 class ArrangeColComparator implements Comparator {

		public int compare(Object arg0, Object arg1) {

			PropertyMapping mapping1 = (PropertyMapping) arg0;
			PropertyMapping mapping2 = (PropertyMapping) arg1;

			String name1 = mapping1.getColumnName();
			String name2 = mapping2.getColumnName();

			boolean proper1 = false;
			boolean proper2 = false;

			for (int i = 0; i < constantColumn.length; i++) {
				if (constantColumn[i].equalsIgnoreCase(name1)) {
					proper1 = true;
					continue;
				}
				if (constantColumn[i].equalsIgnoreCase(name2)) {
					proper2 = true;
				}
			}
			if (proper1 && !proper2) {
				return 1;
			}
			if (!proper1 && proper2) {
				return -1;
			}

			return 0;
		}
	}
	
	 class PrimiryKeyComparator implements Comparator {

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Object arg0, Object arg1) {

			PropertyMapping mapping1 = (PropertyMapping)arg0;
			PropertyMapping mapping2 = (PropertyMapping)arg1;
			
			boolean isKey1 = mapping1.isPrimaryKey();
			boolean isKey2 = mapping2.isPrimaryKey();
			
			if(isKey1 && !isKey2) {
				return -1;
			}
			if(!isKey1 && isKey2) {
				return 1;
			}
			
			return 0;
		}

	}
}

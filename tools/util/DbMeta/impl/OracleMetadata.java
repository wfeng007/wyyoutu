package util.DbMeta.impl;




import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import util.DbMeta.PropertyMapping;

/**
 * 获取oracle数据库的表空间和表的数据信息
 * 
 * @author 王佳
 * @version 1.2
 * @history 2006-10-30 王佳 创建.
 * @history 2006-11-3 王佳将tablespaceList改为string[]
 * @history 2006-11-06 王佳将tablespaceList改为List
 */
public class OracleMetadata extends AbstractDbMetadata {

	private static final String TYPE_STRING = "java.lang.String";

	private static final String TYPE_NUMBER = "java.math.BigDecimal";

	private static final String COLUMN_NAME = "COLUMN_NAME";

	private static final String REMARKS = "REMARKS";

	public OracleMetadata(String jdbcDriver, String jdbcUrl, String username,
			String password) {
		super(jdbcDriver, jdbcUrl, username, password);
	}

	/**
	 * 取得用户当前的schema
	 * 
	 * @return 用户的schema
	 */
	public String getDefaultSchema() {
		return getUsername().toUpperCase();
	}

	/**
	 * 获取表的List
	 * 
	 * @return 包含表名称的List
	 */
	public List getTableList() {

		String schema = username.toUpperCase(); // 参数schemapatten区分大小写，oracl存储时为大写

		return getTableList(schema);
	}

	/**
	 * 获取指定schema的表的List
	 * 
	 * @param schema
	 *            指定表空间的名称
	 * @return 包含表名称的String列表
	 */
	public List getTableList(String schema) {

		List tableList = new ArrayList();
		Connection con = null;
		DatabaseMetaData datMeta = null;
		ResultSet rs = null;

		try {
			con = getConnection();
			datMeta = con.getMetaData();
			rs = datMeta.getTables("", schema, null, TABLE_TYPE); // 得到用户schema中的表，
			while (rs.next()) {
				String tableName = rs.getString(TABLE_NAME);
				if (tableName.indexOf("$") >= 0)
					continue; // 去掉表名带有$的表
				tableList.add(tableName);
			}
			rs.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			closeConnection(con, rs);
		}

		return tableList;
	}

	/**
	 * @param rsmd
	 * @param databaseMeta
	 * @param tableName
	 * @param schema
	 * @return
	 * @throws SQLException
	 */
	protected List getPorpertyMappingList(ResultSetMetaData rsmd,
			DatabaseMetaData databaseMeta, String tableName,
			boolean generateDbOrder) throws Exception {

		List list = super.getPorpertyMappingList(rsmd, databaseMeta, tableName,
				generateDbOrder);

		addComment(tableName, list, databaseMeta);

		return list;
	}

	/**
	 * 包装生成的字段类型
	 * 
	 * @param className
	 * @param precision
	 * @param scale
	 * @return
	 */
	protected String parsePropertyType(String className,
			ResultSetMetaData rsmd, int i) throws SQLException {
		// oracle的number类型
		if (className.equals("BigDecimal")) {
			int scale = rsmd.getScale(i);
			int precision = rsmd.getPrecision(i);

			if (0 == scale) {
				if (precision >= 10)
					return "Long";
				else
					return "Integer";
			}

			else {
				if (precision < 8) {
					return "Float";
				} else if (precision < 16) {
					return "Double";
				} else {
					return "BigDecimal";
				}
			}

		}
		// 其他非number类型
		else {
			return super.parsePropertyType(className, rsmd, i);
		}
	}

	protected void setPropertySize(PropertyMapping mapping,
			ResultSetMetaData rsmd, int i) throws SQLException {

		String className = rsmd.getColumnClassName(i);
		if (className.equals(TYPE_STRING)) {

			mapping.setColumnSize(rsmd.getPrecision(i));

		} else if (className.equals(TYPE_NUMBER)) {

			mapping.setColumnSize(rsmd.getPrecision(i));
			mapping.setScale(rsmd.getScale(i));

		} else
			return;
	}

	protected String generateType(String columnType) {

		if (columnType.equals("VARCHAR2") || columnType.equals("CHAR")) {
			return "VARCHAR";
		} else if (columnType.equals("NUMBER")) {
			return columnType;
		} else
			return " ";
	}

	private void addComment(String tableName, List list, DatabaseMetaData data)
			throws Exception {
		ResultSet columns = data.getColumns(null, null, tableName, null);

		while (columns.next()) {
			String name = columns.getString(COLUMN_NAME);

			for (Iterator iter = list.iterator(); iter.hasNext();) {
				PropertyMapping mapping = (PropertyMapping) iter.next();
				if (mapping.getColumnName().equals(name)) {
					String comment = columns.getString(REMARKS);
					if (comment == null)
						mapping.setComment(" ");
					else
						mapping.setComment(new String(
								comment.getBytes("UTF-8"), "UTF-8"));
					break;
				}
			}
		}
	}

}

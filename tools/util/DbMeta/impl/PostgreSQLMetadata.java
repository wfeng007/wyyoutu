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
 * @author 王佳
 * 
 */
public class PostgreSQLMetadata extends AbstractDbMetadata {

	private static final String COLUMN_NAME = "COLUMN_NAME";

	private static final String REMARKS = "REMARKS";

	private static final String TYPE_NUMBER = "java.math.BigDecimal";

	private static final String TYPE_STRING = "java.lang.String";

	public PostgreSQLMetadata(String jdbcDriver, String jdbcUrl,
			String username, String password) {
		super(jdbcDriver, jdbcUrl, username, password);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baosight.iPlat4j2.plugin.model.DbMeta.impl.AbstractDbMetadata#setPropertySize(com.baosight.iPlat4j2.plugin.model.DbMeta.PropertyMapping,
	 *      java.sql.ResultSetMetaData, int)
	 */
	protected void setPropertySize(PropertyMapping mapping,
			ResultSetMetaData rsmd, int i) throws SQLException {

		String className = rsmd.getColumnClassName(i);

		if (className.equals(TYPE_NUMBER)) {
			mapping.setColumnSize(rsmd.getPrecision(i));
			mapping.setScale(rsmd.getScale(i));
		} else if (className.equals(TYPE_STRING)) {
			mapping.setColumnSize(rsmd.getPrecision(i));
		} else
			return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baosight.iPlat4j2.plugin.model.DbMeta.DbMetadata#getTableList()
	 */
	public List getTableList() {

		return getTableList(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baosight.iPlat4j2.plugin.model.DbMeta.DbMetadata#getTableList(java.lang.String)
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

				tableList.add(rs.getString(TABLE_NAME));
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

	protected List getPorpertyMappingList(ResultSetMetaData rsmd,
			DatabaseMetaData databaseMeta, String tableName, boolean generateDbOrder)
			throws Exception {
		List list = super.getPorpertyMappingList(rsmd, databaseMeta, tableName, generateDbOrder);
		addComment(tableName, list, databaseMeta);

		return list;
	}

	private void addComment(String tableName, List list, DatabaseMetaData data)
			throws SQLException {
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
						mapping.setComment(comment);
					break;
				}

			}

		}

	}

	protected String generateType(String columnType) {

		if (columnType.equals("varchar")) {
			return "VARCHAR";
		} else if (columnType.equals("numeric") || columnType.equals("Integer")
				|| columnType.equals("Long")) {
			return "NUMBER";
		} else
			return " ";
	}

	public String getDefaultSchema() {

		List schemaList = super.getSchemaList();
		if (schemaList.contains("public")) {
			return "public";
		} else
			return null;
	}

}

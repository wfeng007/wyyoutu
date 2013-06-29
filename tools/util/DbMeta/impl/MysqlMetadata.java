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
 *获取mysql数据库的表空间和表的数据信息
 * @author	Wang Jia
 * @version	1.2
 * @history	2006-10-30	王佳创建
 * @history 2006-11-2   王佳将getTables()第一个参数设为null,删除getCatlog（）
 */
public class MysqlMetadata extends AbstractDbMetadata{
	
	
	public MysqlMetadata(String jdbcDriver, String jdbcUrl, String username,
			String password) {
		super(jdbcDriver, jdbcUrl, username, password);
	
	}

	/**
	 * 获取表的List〄1�7
	 * @return 包含表名称的List
	 */
	public List getTableList() {
		ArrayList tableList = new ArrayList();
		
		Connection con = null;
		DatabaseMetaData datMeta = null;
		ResultSet rs = null;
		
		try {
			con = getConnection();
			datMeta = con.getMetaData();
			rs = datMeta.getTables( null,"", "", TABLE_TYPE);   // 通过ＭｅｔａＤａｔａ得到有关数据库表的属怄1�7
			while(rs.next()) {
				tableList.add(rs.getString(TABLE_NAME));
			}
			rs.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			closeConnection(con, rs);
		}
		return tableList;
	}
	


	/**
	 * 获取表的List〄1�7
	 * @return 包含表名称的List
	 */
	public List getTableList(String tablespace) {
		return getTableList();
	}

	protected void setPropertySize(PropertyMapping mapping, ResultSetMetaData rsmd, int i) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	protected String generateType(String columnType) {

		return " ";
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
	
	private static final String COLUMN_NAME = "COLUMN_NAME";
	private static final String REMARKS = "REMARKS";
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

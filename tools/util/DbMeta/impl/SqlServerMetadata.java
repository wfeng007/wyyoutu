package util.DbMeta.impl;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.DbMeta.PropertyMapping;

/**
 *获取sqlserver数据库的表空间和表的数据信息
 * @author	Wang Jia
 * @version	1.2
 * @history	2006-10-30	Wang Jia Create.
 */
public class SqlServerMetadata extends AbstractDbMetadata{
	
	public SqlServerMetadata(String jdbcDriver, String jdbcUrl,
			String username, String password) {
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
			
//			得到用户可见的表，省略前三个参数查询条件没有影响
			rs = datMeta.getTables(null, null, null, TABLE_TYPE); 
			while (rs.next()) {
				tableList.add(rs.getString(TABLE_NAME));
			}
			rs.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
		// TODO Auto-generated method stub
		return " ";
	}
	
}

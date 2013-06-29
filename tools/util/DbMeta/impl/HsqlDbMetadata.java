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
 * 获取mysql数据库的表空间和表的数据信息
 * @author	王佳
 * @version	1.2
 * @history	2006-11-20	王佳创建〄1�7
 */
public class HsqlDbMetadata extends AbstractDbMetadata {
	
	private static final String SCHEM = "table_schem";
	private static final String SYS_SCHEM = "INFORMATION_SCHEMA";

	public HsqlDbMetadata(String jdbcDriver, String jdbcUrl, String username,
			String password) {
		super(jdbcDriver, jdbcUrl, username, password);
	}

	
	/**
	 * 本方法�1�7�择除系统schem以外的数据库schem，使HsqlDb的表按照schem分类
	 * @return 用户的schem
	 * @see com.baosight.iplat4j.plugin.db.model.impl.AbstractDbMetadata#getSchemaList()
	 */
	public List getSchemaList() {
		List list = new ArrayList();
		Connection con = null;
		DatabaseMetaData datMeta = null;
		ResultSet rs = null;

		try {
			con = getConnection();
			datMeta = con.getMetaData();
			rs = datMeta.getSchemas();
			while (rs.next()) {
				String schem = rs.getString(SCHEM);
				if (schem.equalsIgnoreCase(SYS_SCHEM))
					continue;
				list.add(schem);
			}
			rs.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(con, rs);
		}

		return list;
	}
	
	/**
	 * 得到丄1�7个非系统schem的数据库schem
	 * @return 用户的schem
	 * @see com.baosight.iplat4j.plugin.db.model.impl.AbstractDbMetadata#getDefaultSchema()
	 */
	public String getDefaultSchema() {

		Connection con = null;
		DatabaseMetaData datMeta = null;
		ResultSet rs = null;
		String userSchem = null;
		try {
			con = getConnection();
			datMeta = con.getMetaData();
			rs = datMeta.getSchemas();
			while (rs.next()) {
				userSchem = rs.getString(SCHEM); 
				if (userSchem.equalsIgnoreCase(SYS_SCHEM)) //得到丄1�7个用户schem，如果是系统的schem，则选择下一丄1�7
					continue;
				break;
			}
			rs.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(con, rs);
		}
		return userSchem;
	}
	
	
	
	
	/**
	 * 获取表的List〄1�7
	 * @return 用户可见的所有表
	 * @see com.baosight.iplat4j.plugin.db.model.DbMetadata#getTableList()
	 */
	public List getTableList() {
		ArrayList tableList = new ArrayList();

		Connection con = null;
		DatabaseMetaData datMeta = null;
		ResultSet rs = null;

		try {
			con = getConnection();
			datMeta = con.getMetaData();
			rs = datMeta.getTables(null, null, null, null); // 通过ＭｅｔａＤａｔａ得到有关数据库表的属怄1�7
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
	 *获取指定表空间的表的List〄1�7
	 *@param 用户指定的表空间
	 *@return 指定表空间的衄1�7
	 * @see com.baosight.iplat4j.plugin.db.model.DbMetadata#getTableList(java.lang.String)
	 */
	public List getTableList(String tablespace) {
		List list = new ArrayList();

		Connection con = null;
		DatabaseMetaData datMeta = null;
		ResultSet rs = null;

		try {
			con = getConnection();
			datMeta = con.getMetaData();
			rs = datMeta.getTables(null, tablespace.toUpperCase(), null, null);
			while (rs.next()) {
				list.add(rs.getString(TABLE_NAME));
			}
			rs.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(con, rs);
		}

		return list;
	}


	protected void setPropertySize(PropertyMapping mapping, ResultSetMetaData rsmd, int i) throws SQLException {
		// TODO Auto-generated method stub
		
	}


	protected String generateType(String columnType) {
		// TODO Auto-generated method stub
		return " ";
	}
}










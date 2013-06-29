/**
 * 
 */
package util.DbMeta.neogen.olddatameta;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import util.DbMeta.neogen.NameUtils;

/**
 * @author wfeng007
 * @date 2012-2-14 上午10:36:27
 * 
 */
public class DbMetaMap {

	private DataSource dataSource;

	// ********************************************************************************
	// 老代码迁移修改
	// ********************************************************************************

	
	/**
	 * 主要入口
	 * 
	 * defaultPackage: 默认所在包
	 * schema: sql语句用到的schema 没有则需要给null
	 * generateDborder: 是否进行字母排序字段
	 * selectedTableNameLs: 表名列表
	 * 
	 */
	public List<TableMapping> getTableMappingList(String defaultPackage,
			String schema, boolean generateDborder,
			List<String> selectedTableNameLs) {
		List<TableMapping> tableMappingList = new ArrayList<TableMapping>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = dataSource.getConnection();
			DatabaseMetaData dbMeta = con.getMetaData(); //元数据
			
			for (Iterator<String> iter = selectedTableNameLs.iterator(); iter.hasNext();) {
				String tableName = iter.next(); // 取出选定的表的名称

			
				// 设置表中字段的相关信息
				String sql = null;
				if (schema != null) { // "select * from "写死了
					sql = "select * from " + schema + "." + tableName;
				} else
					sql = "select * from " + tableName;

				//获取rsmd
				ps = con.prepareStatement(sql);
				rs = ps.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData(); // 得到表的ResultSetMetaData，映射字段信息
				
				
				//meta逻辑
				//TODO  通用处理 尽量屏蔽tableMapping这种特定的目标数据结构
				TableMapping tm = new TableMapping();
				tm.setPackageName(defaultPackage);
				tm.setTableName(tableName);
				tm.setClassName(mapTableToClass(tableName));

				// 读取表的comment 用getTables只是为了读取表的注释 ()...是否可以获取更多有用的信息呢? 真正使用的是reslut得到的meta
				ResultSet tables = dbMeta.getTables(null, null, tableName,
						new String[] { "TABLE", "VIEW" }); // 暂时写死
				
				//TODO  放到外面去吧...
				if (tables.next()) {
					String comment = tables.getString("REMARKS"); //
					if (comment != null) {
						tm.setComment(comment);
					} else {
						tm.setComment(" ");
					}
				}
				
				// 设置sql-map文件名称  -- 这段.... TODO 是否需要独立出去?
//				String namePrex = defaultPackage.replace('.', '/');
//				String sqlMapName = generateSqlmapPath(namePrex) + "/"
//						+ mapTableTosqlFile(tableName) + ".xml";// FIXME 写死了
//				// 
//				tm.setSqlmapFile(sqlMapName); 
				
				//
				// 获取表中的字段并声称PropertyMapping列表
				//
				tm.setPropertyMappingList(getPropertyMappingList(rsmd, dbMeta,
						tableName, generateDborder)); // 设置表中的字段的映射List     //更新处理内容
				//
				//
				// 用处理器方式 把处理逻辑以及放回类型一起封装
				// 
				// 
				
				// meta逻辑结束
				tableMappingList.add(tm);
			}
			
			// rs.close();
			// ps.close();
			// con.close();
			return tableMappingList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
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

	}
	
	
	/**
	 * 获取字段映射信息
	 * 
	 * @param rsmd
	 *            数据库的ResultSetMetaData对象
	 * @param databaseMeta
	 *            数据库的DatabaseMetaData对象
	 * @param tableName
	 *            表名
	 * @return 包含字段映射的信息List
	 */
	protected List getPropertyMappingList(ResultSetMetaData rsmd,
			DatabaseMetaData databaseMeta, String tableName,
			boolean generateDbOrder) throws Exception {

		List columnMapList = new ArrayList();//返回用的列影射列表
		
		int num = rsmd.getColumnCount(); // 得到表的列数 
		
		List primaryKey = new ArrayList(); //主键列表

		// ResultSet columnSet= databaseMeta.getColumns(null, null, tableName,
		// null);
		// 得到主键
		ResultSet rs = databaseMeta.getPrimaryKeys(null, null, tableName);
		//首先获取所有所有主键名称
		while (rs.next()) {
			primaryKey.add(rs.getString("column_name")); // FIXME 写死了? //// 从databaseMeta 获取该表的所有pk字段
		}
		
		//遍历还是使用计数循环
		for (int i = 1; i <= num; i++) { // 遍历表的每一列 即所字段元数据
			PropertyMapping mapping = new PropertyMapping();
			
			
			//1.是否为空
			int k = rsmd.isNullable(i); // 设置该字段是否为非空
			if (0 == k) {
				mapping.setNotNull(true);
			} else {
				mapping.setNotNull(false);
			}

			//2.数据库列类型     -------rsmd.getColumnTypeName
			mapping.setColumnType(rsmd.getColumnTypeName(i)); // 保存数据库字段类型
//			System.out.println(rsmd.getColumnTypeName(i));
			//3.类型（???）
			mapping.setType(generateType(rsmd.getColumnTypeName(i))); // 设置传递电文时需要的类型
			
			//4.Java属性类型(长名) 其中 blob类型特别处理(使用byte[])但是mysql特殊   ------rsmd.getColumnClassName而不是jdbctype?
//			System.out.println(rsmd.getColumnClassName(i));
			if (rsmd.getColumnTypeName(i).equalsIgnoreCase("BLOB")) { // mysql中，BLOB字段的映射类型会出现异常
				mapping.setPropertyType("byte[]"); 
			} else {
				String className = rsmd.getColumnClassName(i); // 直接通过getColumnClassName获取jdbc认为对应的java类型而不是jdbctype

				// FIXME 这里将全名给去掉了 为何要去掉? 当作基本property类型使用 , 有可能这种方法在不同的数据库中结果不一样..有的只有短名有的是长名
				int j = className.lastIndexOf("."); // 取得字段类型，去掉全名
													// 比如java.lang.String变成String
				if (j > 0) {
					className = className.substring(j + 1);
				}
				// 设置字段的长度，精度，映射java类型
				// FIXME 然后又在这里增加了全名parsePropertyType Sting -> java.lang.String
				//FIXME 这里每次设置进去的既有长名又有短名 Integer只有短名  导致后TableMapping生成类型倒入的时候有一个小报错,幸好不影响使用
				mapping.setPropertyType(parsePropertyType(className, rsmd, i)); 
				
				setPropertySize(mapping, rsmd, i); //???这是在干啥,获取精度信息???
			}
			
			//5.列名称  ----rsmd.getColumnName
			mapping.setColumnName(rsmd.getColumnName(i)); // 字段名成
			//6.属性名称 ----rsmd.getColumnName
			mapping.setPropertyName(mapColumnToField(rsmd.getColumnName(i))); // 生成java-property名称

			//7.检测是否为主键
			if (primaryKey.contains(rsmd.getColumnName(i))) { // 设置主键
				mapping.setPrimaryKey(true); //标记为主键
				mapping.setConditional(true); //标记为条件项
			}

			columnMapList.add(mapping); // 添加到组
		}

		// 根据是否为主键+字母排序
		if (!generateDbOrder) {
			Collections.sort(columnMapList, new PrimiryKeyComparator());
			Collections.sort(columnMapList, new ArrangeColComparator());
		}

		// *** 在mysql子类中实现 ***
		addComment(tableName, columnMapList, databaseMeta); //设置注释
		// *** 在mysql子类中实现 完成 ***

		return columnMapList;
	}

	// 在mysql子类中实现
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

	private String generateType(String columnType) {// 这个是在子类中实现的需要观察
		return " ";
	}

	protected void setPropertySize(PropertyMapping mapping,
			ResultSetMetaData rsmd, int i) throws SQLException { // 这个是在子类中实现的需要观察
		// TODO Auto-generated method stub

	}
	
	// 在mysql子类中实现

	protected static final Map databaseTypeMap = new HashMap(); // 存放数据库类型与ｊａｖａ类型的映射
	static {
		databaseTypeMap.put("BigDecimal", "Double"); //大数字 还是用double TODO 为何不用长名？因为不需要导入？但是这样会报错的...
		databaseTypeMap.put("Timestamp", "java.util.Date"); //timestamp使用date?
		databaseTypeMap.put("Time", "java.util.Date");
		databaseTypeMap.put("CLOB", "java.lang.String"); //clob 使用String
		databaseTypeMap.put("Boolean", "Boolean"); // 这里要求都用Boolean?有这种类型?
		databaseTypeMap.put("String", "java.lang.String"); //
		databaseTypeMap.put("Date", "java.util.Date"); //
	}

	/**
	 * 包装生成的字段类型 TODO 这里其实应该根据数据类型进行影射判断...
	 * 
	 * @param className
	 * @param precision
	 * @param scale
	 * @return
	 */
	protected String parsePropertyType(String className,
			ResultSetMetaData rsmd, int i) throws SQLException {

		// 映射替换根据映射替换 有些值需要替换为可用
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
	 * 
	 * FIXME 另一个类中已经有这个方法了 需要统一到一个单独util包中
	 * 
	 * 将数据库的表的名字转换成java类的名字
	 * 
	 * @param 数据库表名
	 * @return 转换后的类的名字.
	 */
	protected String mapTableToClass(String tableName) {

//		StringBuffer buffer = new StringBuffer();
//		char[] cs = tableName.toLowerCase().trim().toCharArray(); // 先将表名转换为小冄1�7
//
//		// 将表名的首字母转换成大写
//		if (cs[0] >= 97 && cs[0] <= 122) {
//			cs[0] = (char) (cs[0] - 32);
//		}
//		for (int i = 0; i < cs.length; i++) {
//
//			if (cs[i] == '_') { // 将表名�1�7�_”后的字母转换成大写
//				if (cs[i + 1] >= 97 && cs[i + 1] <= 122) {
//					cs[i + 1] = (char) (cs[i + 1] - 32);
//				}
//			} else
//				buffer.append(cs[i]);
//		}
//
//		return buffer.toString();
		
		return NameUtils.dbTableNameToJavaClassName(tableName);
	}

	/**
	 * 将数据库的字段的名字转换成类的字段名
	 * 
	 * @param 数据库字段的名字
	 * 
	 * @return 转换后的field的名字�
	 */
	protected String mapColumnToField(String columnName) {
//		StringBuffer buffer = new StringBuffer();
//		char[] cs = columnName.toLowerCase().trim().toCharArray(); // 将字段名转换成小冄1�7
//
//		for (int i = 0; i < cs.length; i++) {
//
//			if (cs[i] == '_' && i != 1 && (i + 1) < cs.length) { // 后的字母转换成大冄1�7
//				if (cs[i + 1] >= 97 && cs[i + 1] <= 122) {
//					cs[i + 1] = (char) (cs[i + 1] - 32);
//				}
//			} else
//				buffer.append(cs[i]);
//		}
//
//		return buffer.toString();
		return NameUtils.dbColumnNameToJavaPropertyName(columnName);
	}

//	protected String generateSqlmapPath(String namePrex) {
//		int i = namePrex.lastIndexOf("/");
//		if (i > 0) {
//			return namePrex.substring(0, i) + "/sql";
//		}
//		return "sql";
//	}

//	/**
//	 * 将表名转为文件名 修改为 xxxxDao.mpr.xml
//	 * 
//	 * @param tableName
//	 * @return
//	 */
//	protected String mapTableTosqlFile(String tableName) {
//		StringBuffer buffer = new StringBuffer();
//		char[] cs = tableName.toLowerCase().trim().toCharArray(); // 先将表名转换为小冄1�7
//
//		// 将表名的首字母转换成大写
//		if (cs[0] >= 97 && cs[0] <= 122) {
//			cs[0] = (char) (cs[0] - 32);
//		}
//		for (int i = 0; i < cs.length; i++) {
//
//			if (cs[i] == '_' && i + 1 < cs.length) { // 将表名��_”后的字母转换成大写
//				if (cs[i + 1] >= 97 && cs[i + 1] <= 122) {
//					cs[i + 1] = (char) (cs[i + 1] - 32);
//				}
//			} else
//				buffer.append(cs[i]);
//		}
//		return buffer.toString() + "Dao.mpr";
////		return NameUtils.dbTableNameToJavaClassName(tableName) + "Dao.mpr";
//		
//	}

	//
	// 以下代码主要给外部调用获取到底有多少数据库表用
	//

	// /**
	// * 获取表的List〄1�7
	// * @return 包含表名称的List
	// */
	// public List getTableList() {
	// ArrayList tableList = new ArrayList();
	//
	// Connection con = null;
	// DatabaseMetaData datMeta = null;
	// ResultSet rs = null;
	//
	// try {
	// con = getConnection();
	// datMeta = con.getMetaData();
	// rs = datMeta.getTables( null,"", "", TABLE_TYPE); //
	// 通过ＭｅｔａＤａｔａ得到有关数据库表的属怄1�7
	// while(rs.next()) {
	// tableList.add(rs.getString(TABLE_NAME));
	// }
	// rs.close();
	// con.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }finally {
	// closeConnection(con, rs);
	// }
	// return tableList;
	// }
	//
	// /**
	// * 获取表的List〄1�7
	// * @return 包含表名称的List
	// */
	// public List getTableList(String tablespace) {
	// return getTableList();
	// }

	private static final String[] constantColumn = { "rec_creator",
			"rec_create_time", "rec_revisor", "rec_revise_time", "archive_flag" };

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
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Object arg0, Object arg1) {

			PropertyMapping mapping1 = (PropertyMapping) arg0;
			PropertyMapping mapping2 = (PropertyMapping) arg1;

			boolean isKey1 = mapping1.isPrimaryKey();
			boolean isKey2 = mapping2.isPrimaryKey();

			if (isKey1 && !isKey2) {
				return -1;
			}
			if (!isKey1 && isKey2) {
				return 1;
			}

			return 0;
		}

	}
	// ********************************************************************************
	// 老代码迁移修改 结束
	// ********************************************************************************

	
	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource
	 *            the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}

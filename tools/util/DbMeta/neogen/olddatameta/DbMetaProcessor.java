/**
 * 
 */
package util.DbMeta.neogen.olddatameta;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import util.DbMeta.neogen.NameUtils;

/**
 * @author wfeng007
 * @date 2012-2-14 下午02:15:23
 *
 */
public class DbMetaProcessor {
//	private TableMapping tableMapping;
//	private String defaultPackage ; //需要输入
//	
//	/*
//	 * 新增处理器内容
//	 */
//	public void processTableMetaProcess(DatabaseMetaData dbmd,ResultSetMetaData rsmd,String tableName){
//		//meta逻辑
//		//TODO  通用处理 尽量屏蔽tableMapping这种特定的目标数据结构
//		TableMapping tm = new TableMapping();
//		tm.setPackageName(defaultPackage);
//		tm.setTableName(tableName);
////		tm.setClassName(mapTableToClass(tableName));
//		tm.setClassName(NameUtils.dbTableNameToJavaClassName(tableName));
//
//		// 读取表的comment 用getTables只是为了读取表的注释 ()...是否可以获取更多有用的信息呢? 真正使用的是reslut得到的meta
//		ResultSet tables = dbmd.getTables(null, null, tableName,
//				new String[] { "TABLE", "VIEW" }); // 暂时写死
//		
//		//TODO  放到外面去吧...
//		if (tables.next()) {
//			String comment = tables.getString("REMARKS"); //
//			if (comment != null) {
//				tm.setComment(comment);
//			} else {
//				tm.setComment(" ");
//			}
//		}
//		
//		
//		// 设置sql-map文件名称  -- 这段.... TODO 是否需要独立出去?
//		String namePrex = defaultPackage.replace('.', '/');
//		String sqlMapName = generateSqlmapPath(namePrex) + "/"
//				+ mapTableTosqlFile(tableName) + ".xml";// FIXME 写死了
//		tm.setSqlmapFile(sqlMapName);
//	}
//	
//	public void processcloumnMeta(){
//		
//	}
//	/*
//	 * 新增处理器内容结束
//	 */
//
//	/**
//	 * @return the tableMapping
//	 */
//	public TableMapping getTableMapping() {
//		return tableMapping;
//	}
}

/**
 * 
 */
package util.DbMeta.neogen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

//import util.DbMeta.PropertyMapping;
import util.DbMeta.neogen.olddatameta.DbMetaMap;
import util.DbMeta.neogen.olddatameta.PropertyMapping;
import util.DbMeta.neogen.olddatameta.TableMapping;
import util.genertator.GeneratorException;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * @author wfeng007
 * @date 2011-9-13 下午10:28:46
 *
 */
public class DbMetaManager {
	

	public static void main(String[] args) throws Exception {
		
		BasicDataSource ds=new BasicDataSource();
		
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://127.0.0.1:3306/iccspm22?autoReconnect=true");
		ds.setUsername("root");
		ds.setPassword("root");
		
		DbMetaManager dmm=new DbMetaManager();
		dmm.setDataSource(ds);
		DbMetaMap dmmap=new DbMetaMap();
		dmmap.setDataSource(ds);
		dmm.setDbMetaMap(dmmap);
		
		dmm.init();
		
		List<QueryMeta> queryList=new ArrayList<QueryMeta>();
		QueryMeta qm=new QueryMeta();
		qm.setName("test1_test2");
		qm.setSql("SELECT seq_id,`type`,source_type FROM pm_en_event");
		queryList.add(qm);

		dmm.generateByQueryMetaList(queryList);
		
		
		List<String> tableNameList=new ArrayList<String>();
		tableNameList.add("pm_en_event");
		//
		
		
		dmm.generateMappingFile("MySQL", "daopackage.test",tableNameList);
	}
	
	
	//
	private DataSource dataSource;
	
	/**
	 * 
	 */
	private DbMetaMap dbMetaMap;
	
	/**
	 * 
	 * 通过数据库将填充QueryMeta
	 * 用户需要写入name 以及 query的sql
	 * @param queryList
	 * @throws SQLException
	 */
	public void fullQueryMetaList(List<QueryMeta> queryList) throws SQLException{
		
		Connection conn=dataSource.getConnection();
		for (QueryMeta queryMeta : queryList) {
			ResultSet rset = null;
			String sqlStr = queryMeta.getSql(); 
			Statement smt = null; 
			smt = conn.createStatement(); 
			rset = smt.executeQuery(sqlStr);
			ResultSetMetaData rsmd = rset.getMetaData();
			
			List<QueryColumnMeta> qcmLs=new ArrayList<QueryColumnMeta>();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				QueryColumnMeta qcm=new QueryColumnMeta();
				qcm.setCatalogName(rsmd.getCatalogName(i));
				qcm.setColumnName(rsmd.getColumnLabel(i));
				qcm.setColumnClassName(rsmd.getColumnClassName(i));
				qcm.setColumnDisplaySize(rsmd.getColumnDisplaySize(i));
				qcm.setSchemaName(rsmd.getSchemaName(i));
				qcm.setColumnType(rsmd.getColumnType(i));
				qcm.setColumnTypeName(rsmd.getColumnTypeName(i));
				qcm.setTableName(rsmd.getTableName(i));
				qcm.setAutoIncrement(rsmd.isAutoIncrement(i));
				qcm.setNullable(rsmd.isNullable(i));
				qcm.setReadOnly(rsmd.isReadOnly(i));
				qcm.setSearchable(rsmd.isSearchable(i));
				qcmLs.add(qcm);
			}
			queryMeta.setColumnMetaList(qcmLs);
		}
		
	}
	
	/**
	 * 将QueryMeta映射为JavaMeta4Query
	 * 内部生成新的JavaMeta4Query并把query放入 关联
	 * @param queryList
	 * @return
	 * @throws SQLException
	 */
	public List<JavaMeta4Query> mapToJavaMeta(List<QueryMeta> queryList) throws SQLException{
		
		this.fullQueryMetaList(queryList);//FIXME 上层已经作了 
		
		//每个JavaMeta4Query包装一个QueryMeta
		List<JavaMeta4Query> javaList=new ArrayList<JavaMeta4Query>();
		for (QueryMeta query : queryList) {
			JavaMeta4Query javaMeta=new JavaMeta4Query(query);
			javaList.add(javaMeta);
		}
		
		return javaList;
	}

	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	private String templateRootPath;
	private String targetRootPath="mytest/test2";
	
	
	public void init(){
//		createTemplateRootPath();
	}
	
	
	
//	static private Writer createWriter(String parentPath,String fileName){
//		
//	}
//	static private Reader createReader(String parentPath,String fileName){
//		
//	}
	/**
	 * 通用方法
	 * 关闭Reader
	 */
	public static void destroyReader(Reader reader){
		if (reader != null) {
			try {
				reader.close();
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
			catch (Throwable ex) {
				ex.printStackTrace();
			}
		}
	}
	/**
	 * 通用方法
	 * 关闭Writer
	 */
	 public static void destroyWriter(Writer writer){
		if (writer != null) {
			try {
				writer.close();
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
			catch (Throwable ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * 创建模板的输入流
	 * @param parentPath
	 * @param fileName
	 * @return
	 */
	private Reader createTemplateReader(String parentPath, String fileName) {
		InputStream in = null;
		//
		try {
			if(parentPath!=null){
				File templatefile=new File(parentPath,fileName);
				if(!templatefile.exists()||!templatefile.isFile()){
					throw new RuntimeException("The template file is not exist or is not a file. file:"+templatefile);
				}
				in = new FileInputStream(templatefile);
			} else if(this.templateRootPath!=null){ //manager中制定的template路径 //FIXME ROOT PATH其实与parentpath还是有区别的. 应该是rootpath/parentpath/filename
				File templatefile=new File(this.templateRootPath,fileName);
				if(!templatefile.exists()||!templatefile.isFile()){
					throw new RuntimeException("The template file is not exist or is not a file. file:"+templatefile);
				}
				in = new FileInputStream(templatefile);
			}
			else {//默认读取当前类下template目录下的模板文件
				
				ClassLoader classLoader = this.getClass().getClassLoader();
				// 从插件的classloader中得到模板资源，并构造输入流in
				String packageName = this.getClass().getPackage().getName();
				String packagePath = packageName.replace('.', '/');
				in = classLoader.getResourceAsStream(packagePath+ File.separatorChar +"template"+ File.separatorChar + fileName);
			}
			
			Reader reader;
			reader = new InputStreamReader(in, "UTF-8"); //默认均使用utf-8编码
			return reader;
		}catch (FileNotFoundException e) {
			throw new RuntimeException(e); //
		}catch (UnsupportedEncodingException e) {
			try {
				if (in != null)
					in.close();
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * 生成输出文件 自动建立目录以及目标文件
	 * @param parentPath
	 * 		如果==NULL则使用ROOTPATH
	 * @param fileName
	 * @return
	 */
	private Writer createTargetWriter(String parentPath,String fileName){
		
		//
		String rp=parentPath;
		if(rp==null){
			rp=this.targetRootPath; //FIXME rootpath与parentpath还是有区别的
		}
		//TODO 注意优化目录拼接
		
		// 文件输出路径
		File targetFile=new File(rp,fileName);
		
		//检测目录没有则创建
		File destDir=targetFile.getParentFile();
		if(!destDir.exists()){
			if(!destDir.mkdirs()){
				throw new RuntimeException("create dest-dir fail!! dest-dir:"+destDir);
			}
		}else if(!destDir.isDirectory()){
			throw new RuntimeException("dest dir is a File!! dest-dir:"+destDir);
		}
		
		//创建文件 //FIXME 一定需要么?
		if(!targetFile.exists()){
			try {
				targetFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("create file fail", e);
			}
		}
		
		//建立输出文件的流
		OutputStream ops=null;
		try {
			ops=new FileOutputStream(targetFile);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		//生成writer
		Writer reWriter=null;
		try {
			reWriter=new OutputStreamWriter(ops,"UTF-8"); //默认 UTF-8
		} catch (UnsupportedEncodingException e) {
			try {
				if(ops!=null)ops.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			throw new RuntimeException(e);
		}
		
		return reWriter;
	}
	
	
	/**
	 * 业务上通用的
	 * 模板生成方法
	 * 指定输入模板文件 数据模型 以及 输出目录输出文件
	 * @param rootDataMap 
	 * @param templateParentPath 如果为null则使用默认路径
	 * @param templateFileName 
	 * @param targetParentPath 如果为null则使用默认路径
	 * @param targetFileName
	 */
	public void generate(Map<String, Object> rootDataMap,String templateParentPath,String templateFileName,String targetParentPath,String targetFileName) {
		
		//get Template config
		Configuration cfg = new Configuration();
		// cfg.setDirectoryForTemplateLoading( new File(path));
		// cfg.setObjectWrapper(new DefaultObjectWrapper());
		// cfg.setCacheStorage(new freemarker.cache.MruCacheStorage(20, 250));
		// Template temp = cfg.getTemplate(name);
		//
		
		//get Reader/inputStream
		Reader reader=createTemplateReader(templateParentPath,templateFileName);
		//
		//get Write/outputStream
		Writer writer=createTargetWriter(targetParentPath,targetFileName);
		
		try{
			Template temp = new Template("", reader, cfg);
			temp.process(rootDataMap, writer);
			writer.flush();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DbMetaManager.destroyReader(reader);
			reader=null;
			DbMetaManager.destroyWriter(writer);
			writer=null;
		}
	}
	
	/**
	 * 功能相关 query元 生成
	 * @param queryList
	 */
	public void generateByQueryMetaList(List<QueryMeta> queryList){
		//
		try {
			this.fullQueryMetaList(queryList);
	
			//
			System.out.println(queryList);
			
			//
			List<JavaMeta4Query> javaList=this.mapToJavaMeta(queryList);
			System.out.println(javaList);
			
			
			for (JavaMeta4Query javaMeta4Query : javaList) {
				HashMap<String,Object> hm=new HashMap<String, Object>();
				hm.put("javaMeta4Query", javaMeta4Query);
				
	//			File destFile = new File(File.separatorChar+"package/test");
				
				System.out.println(javaMeta4Query.getName());
				this.generate(hm,null,"test.ftl",null,javaMeta4Query.getName()+".txt");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 */
	public void generateDataModel(){
		
	}
	
	/**
	 * 
	 */
	public void generateMappingFile(String DBType,String daoPackage,List<String> tableNameList){
		//
		try {
//			this.fullQueryMetaList(queryList);
	
			//
//			System.out.println(queryList);
			
			//
//			List<JavaMeta4Query> javaList=this.mapToJavaMeta(queryList);
//			System.out.println(javaList);
			
//			for (JavaMeta4Query javaMeta4Query : javaList) {
//				HashMap<String,Object> hm=new HashMap<String, Object>();
//				hm.put("javaMeta4Query", javaMeta4Query);
//				
//	//			File destFile = new File(File.separatorChar+"package/test");
//				
//				System.out.println(javaMeta4Query.getName());
//				this.generate(hm,null,"test.ftl",null,javaMeta4Query.getName()+".txt");
//			}
			
			
//			List<String> tableNameList=new ArrayList<String>();
//			tableNameList.add("pm_en_event");
			
			String defaultPackage="ecdata.iccs.pm.model";
			
			List<TableMapping> ls=this.dbMetaMap.getTableMappingList(defaultPackage, null, false,tableNameList);
			
			for (TableMapping tableMapping : ls) {
				//获得当前时间
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
				String time = format.format(new Date());
				
				List list = new ArrayList();
				list = generateList(tableMapping.getPropertyMappingList());
				tableMapping.setPropertyMappingList(list);
				List pkList = generatePkList(tableMapping.getPropertyMappingList());
				
				HashMap infoMap = new HashMap();
				infoMap.put("mapping", tableMapping);
//				infoMap.put("nameSpace", fileName); //估计没有用了名称应该就是类名+Dao
				infoMap.put("daoPackage", daoPackage); //这部分应该不放入infomap才对
				infoMap.put("pk", pkList);
				infoMap.put("size", new Integer(pkList.size()));
				infoMap.put("time", time);
				infoMap.put("version", "0.1.1");
				infoMap.put("DBType", DBType); //FIXME 输入DBTYPE? 应该连接池判断吧..
//		        System.out.println("DBType:"+DBType);
				
				//
				// 目标文件名称 TODO 所有文件名目录操作应该独立出来
				//
		        
				// model 目标文件
		        String packagePath = tableMapping.getPackageName().replace('.', '/');
//		        File destDir = new File(packagePath); // source路径+package路径
//		        File outputFile = new File(destDir, tableMapping.getClassName()	+ ".java");
		        String modelFileName=packagePath+"/"+tableMapping.getClassName()+".java";
//		        if (outputFile.exists() && outputFile.isFile()) {
//		        	//TODO 处理覆盖方式
//		        }
		        System.out.println(modelFileName);
		        
		        // dao 目标文件
		        String daoPackagePath = daoPackage.replace('.', '/');
		        String daoFileName=daoPackagePath+"/"+tableMapping.getClassName()+"Dao"+ ".java";
		        System.out.println(daoFileName);
		        
		        // 设置sql-map文件名称  -- 这段.... TODO 是否需要独立出去?
				String namePrex = defaultPackage.replace('.', '/');
				String sqlMapName = generateSqlmapPath(namePrex) + "/"
						+ mapTableTosqlFile(tableMapping.getTableName()) + ".xml";// FIXME 写死了
				
		        System.out.println(sqlMapName);
		        
		        //
		        //
		        //  generate
		        //
		        
		        this.generate(infoMap,null,"model.java.ftl",null,modelFileName);
		        
		        this.generate(infoMap,null,"mpr.dao.java.ftl",null,daoFileName);
		        
				this.generate(infoMap,null,"mysql.mpr.xml.ftl",null,sqlMapName);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private String generateSqlmapPath(String namePrex) {
		int i = namePrex.lastIndexOf("/");
		if (i > 0) {
			return namePrex.substring(0, i) + "/sql";
		}
		return "sql";
	}
	/**
	 * 将表名转为文件名 修改为 xxxxDao.mpr
	 * 
	 * @param tableName
	 * @return
	 */
	private String mapTableTosqlFile(String tableName) {
//		StringBuffer buffer = new StringBuffer();
//		char[] cs = tableName.toLowerCase().trim().toCharArray(); // 先将表名转换为小写
//
//		// 将表名的首字母转换成大写
//		if (cs[0] >= 97 && cs[0] <= 122) {
//			cs[0] = (char) (cs[0] - 32);
//		}
//		for (int i = 0; i < cs.length; i++) {
//
//			if (cs[i] == '_' && i + 1 < cs.length) { // 将表名_后的字母转换成大写
//				if (cs[i + 1] >= 97 && cs[i + 1] <= 122) {
//					cs[i + 1] = (char) (cs[i + 1] - 32);
//				}
//			} else
//				buffer.append(cs[i]);
//		}
//		return buffer.toString() + "Dao.mpr";
		//略微有点出入
		return NameUtils.dbTableNameToJavaClassName(tableName) + "Dao.mpr";
		
	}
	/**
	 * 选择出是pk的字段
	 * @param propertyMappingList
	 * @return
	 */
	private List generatePkList(List propertyMappingList) {
		List list = new ArrayList();
		for (Iterator iter = propertyMappingList.iterator(); iter.hasNext();) {
			PropertyMapping property = (PropertyMapping) iter.next();
			if(property.isPrimaryKey()) {
				list.add(property);
			}
		}
		return list;
	}
	/**
	 * 选择需要生成的字段
	 * @param propertyMappingList
	 * @return
	 */
	private List generateList(List propertyMappingList) {

		List list = new ArrayList();
		for (Iterator iter = propertyMappingList.iterator(); iter.hasNext();) {
			PropertyMapping property = (PropertyMapping) iter.next();
			if(property.isGenerate()) {
				list.add(property);
			}
			
		}
		return list;
	}
	
	

	/**
	 * @return the finishedPartRootPath
	 */
	public String getFinishedPartRootPath() {
		return targetRootPath;
	}

	/**
	 * @param finishedPartRootPath the finishedPartRootPath to set
	 */
	public void setFinishedPartRootPath(String finishedPartRootPath) {
		this.targetRootPath = finishedPartRootPath;
	}

	/**
	 * @return the templateRootPath
	 */
	public String getTemplateRootPath() {
		return templateRootPath;
	}

	/**
	 * @return the dbMetaMap
	 */
	public DbMetaMap getDbMetaMap() {
		return dbMetaMap;
	}

	/**
	 * @param dbMetaMap the dbMetaMap to set
	 */
	public void setDbMetaMap(DbMetaMap dbMetaMap) {
		this.dbMetaMap = dbMetaMap;
	}

//	/**
//	 * @param templateRootPath the templateRootPath to set
//	 */
//	public void setTemplateRootPath(String templateRootPath) {
//		this.templateRootPath = templateRootPath;
//	}
	
}

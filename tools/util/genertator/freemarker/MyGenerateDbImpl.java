/**
 * 
 */
package util.genertator.freemarker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.utility.XmlEscape;

import util.DbMeta.DbMetadata;
import util.DbMeta.DbMetadataFactory;
import util.DbMeta.PropertyMapping;
import util.DbMeta.TableMapping;
import util.genertator.GeneratorException;

/**
 * 
 */
public class MyGenerateDbImpl {
	private static final String DOMAIN_OBJECT = "templates/simpleJsp/model.java.ftl"; //domainobject模板名称

//	private static final String SQLMAP_FILE = "templates/simpleJsp/sqlmap.ftl"; //sqlmap模板名称
	private static final String SQLMAP_FILE = "templates/simpleJsp/mysql.mpr.xml.ftl"; 

	private static final String DAO_TEMPLATE_FILE = "templates/simpleJsp/mpr.dao.java.ftl"; 

//	private static final String MODIFIER = "public";

	private String targetPath;

//	public FileExistsListener fileExistsListener;	// 文件存在时的监听器

	private String time;							//当前时间

	private String version;
	
	public static void main(String[] args) throws Exception {
		
		DbMetadataFactory.createDbMetadata(DbMetadata.TYPE_MYSQL, "jdbc:mysql://127.0.0.1:3306/wyyoutu", "root", "root");
		DbMetadata dbMetadata = DbMetadataFactory.getDbMetadata();
		List tableNameList=dbMetadata.getTableList();
		System.out.println(tableNameList);
		
		tableNameList = new ArrayList<String>();
		
		//summ.wuyoutu
//		tableNameList.add("RS_ITEM");
//		tableNameList.add("co_people");
//		tableNameList.add("RS_TAGGED");
//		tableNameList.add("co_people_exten");
//		tableNameList.add("RS_ITEM_EXTEN");
		tableNameList.add("rs_board");
		tableNameList.add("rs_board_item_ref");
		tableNameList.add("rs_book");
		
		//iccs.pm
//		tableNameList.add("PM_SM_SYSTEM_INFO");
//		tableNameList.add("pm_rm_region");
//		tableNameList.add("pm_rm_unit");
//		tableNameList.add("pm_rm_compute_node");
//		tableNameList.add("pm_sp_policy");
			//
//		tableNameList.add("pm_rm_vs_template");
//		tableNameList.add("PM_RM_OS_TYPE_PROPERTIES_DEF");
		
//		tableNameList.add("pm_rm_scene");
//		tableNameList.add("pm_rm_id_generator");
//		tableNameList.add("pm_ms_compute_node_his");
//		tableNameList.add("pm_en_event");
		
//		tableNameList.add("pm_rm_storage_node");
//		tableNameList.add("pm_rm_vstorage_device");
//		tableNameList.add("PM_RM_VS_VSTORAGE_DEVICE_RLT");
//		tableNameList.add("pm_rm_vstorage_device_pool");
		
//		tableNameList.add("PM_RM_VS_VNETWORK_CARD_RLT");
		
//		tableNameList.add("pm_rm_ip_segment");
		
//		tableNameList.add("pm_ms_compute_node_threshold");
//		tableNameList.add("pm_sy_user");
		
//		tableNameList.add("pm_ms_virtual_server_threshold");
//		tableNameList.add("pm_ms_vstorage_pool_threshold");
		
//		tableNameList.add("PM_MS_VIRTUAL_SERVER_HIS");
		
//		tableNameList.add("pm_id_vmc_target");
		
		
		
		dbMetadata.selectTables(tableNameList);
		
		List<TableMapping> tableConfigList = dbMetadata.getTableMappingList("wyyoutu.model", null, false); //通过pm.model生成
//		List<TableMapping> tableConfigList = dbMetadata.getTableList();
		
		MyGenerateDbImpl mgdi=new MyGenerateDbImpl();
		mgdi.generate(tableConfigList,"./gentarget/", DbMetadata.TYPE_MYSQL,"wyyoutu.dao");
		mgdi.generateDao(tableConfigList,"./gentarget/", DbMetadata.TYPE_MYSQL,"wyyoutu.dao");
		
		
		// querygen
		
		
		
	}
	
	
	
	/**
	 * 根据TableMapping列表生成实体类文件、sqlmap文件并更新到sqlmap-config文件中。<br>
	 * <b>注</b>：该方法通过调用generateDomainObject、generateSqlmap和updateSqlmapConfig
	 * 方法实现生成实体类文件、sqlmap文件和更新sqlmap-config文件。
	 * 
	 * @param tableMappingList TableMapping的列表
	 * @param targetPath Java Source目录路径
	 * @param sqlmapConfig sqlmap的配置文件
	 * @throws DbGeneratorException 自定义异常
	 * @see com.baosight.iplat4j.plugin.db.generator.DbGenerator#generate(java.util.List, String)    
	 */
	public void generate(List<TableMapping> tableConfigList, String targetPath,String DBType,String daoPackage) throws GeneratorException {
		this.targetPath = targetPath;
		
		if (!(tableConfigList.size() > 0)) {
			throw new GeneratorException("tableMapping is null");
		}
		//获得当前时间
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
		time = format.format(new Date());
		
		//获得当前插件版本
//		version = GeneratePagePlugin.getDefault().getBundle().getHeaders("").get(BUNDEL_NAME).toString();
		version = "3.0.0";
		for (int i = 0; i < tableConfigList.size(); i++) {
			
			TableMapping tableMapping = (TableMapping) tableConfigList.get(i);
			generateDomainObject(tableMapping);
			generateSqlmap(tableMapping,DBType,daoPackage);

			
		}
	}
	
	
	/**
	 * 根据TableMapping列表生成实体类文件、sqlmap文件并更新到sqlmap-config文件中。<br>
	 * <b>注</b>：该方法通过调用generateDomainObject、generateSqlmap和updateSqlmapConfig
	 * 方法实现生成实体类文件、sqlmap文件和更新sqlmap-config文件。
	 * 
	 * @param tableMappingList TableMapping的列表
	 * @param targetPath Java Source目录路径
	 * @param sqlmapConfig sqlmap的配置文件
	 * @throws DbGeneratorException 自定义异常
	 * @see com.baosight.iplat4j.plugin.db.generator.DbGenerator#generate(java.util.List, String)    
	 */
	public void generateDao(List<TableMapping> tableConfigList, String targetPath,String DBType,String daoPackage) throws GeneratorException {
		this.targetPath = targetPath;
		
		if (!(tableConfigList.size() > 0)) {
			throw new GeneratorException("tableMapping is null");
		}
		//获得当前时间
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
		time = format.format(new Date());
		
		//获得当前插件版本
//		version = GeneratePagePlugin.getDefault().getBundle().getHeaders("").get(BUNDEL_NAME).toString();
		version = "3.0.0";
		for (int i = 0; i < tableConfigList.size(); i++) {
			
			TableMapping tableMapping = (TableMapping) tableConfigList.get(i);

			generateDao4sqlMap(tableMapping,DBType,daoPackage);
		}
	}
	
	/**
	 * 根据单个TableMapping信息生成单个实体类的sqlmap映射文件。
	 * @param tableMapping TableMapping对象，为单个表映射信息。
	 * @throws GeneratorException 自定义异常
	 * @see com.baosight.iplat4j.plugin.db.generator.DbGenerator#generateSqlmap(com.baosight.iplat4j.plugin.db.model.TableMapping)
	 */
	private void generateSqlmap(TableMapping tableMapping,String DBType,String daoPackage)
			throws GeneratorException {
		String sqlMapFile = tableMapping.getSqlmapFile(); //这个还是meta中的内容
		System.out.println("sqlMapFile:"+sqlMapFile);
		
		
//		String sqlMapFile = tableMapping.getClassName()+"Dao"+".mpr.xml"; // 写死生成的
		
		List list = new ArrayList();
		list = generateList(tableMapping.getPropertyMappingList());
		tableMapping.setPropertyMappingList(list);
		List pkList = generatePkList(tableMapping.getPropertyMappingList());
		
		int index = sqlMapFile.lastIndexOf("/"); //获得文件的父路径
//		String tempPath = sqlMapFile.substring(0, index);
		String fileName = sqlMapFile.substring(index + 1);
		
		File filePath = new File( this.targetPath, sqlMapFile );
		File file = new File(filePath.getParent());  //判断sql目录是否存在
		if(!file.exists()) {
			file.mkdirs();
		}
		if (filePath.exists() && filePath.isFile()) {	// 如果文件已经存在
//			if (fileExistsListener != null && 			// 且用户选择不覆盖
//				!fileExistsListener.overwrite(filePath.getName())) {
//				return;
//			}
			//TODO 处理覆盖方式
		} else {
			try {
				filePath.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				throw new GeneratorException("create file fail", e);
			}
		}

		OutputStreamWriter out;
		try {
			out = new OutputStreamWriter(new FileOutputStream(filePath),"UTF-8");
		} catch (Exception e) {
			throw new GeneratorException(e);	
		}

		int j = fileName.lastIndexOf(".");
		
		if(j > 0) {
			fileName = fileName.substring(0, j);
		}
		
		HashMap infoMap = new HashMap();
		infoMap.put("mapping", tableMapping);
		infoMap.put("nameSpace", fileName); //估计没有用了名称应该就是类名+Dao
		infoMap.put("daoPackage", daoPackage);
		infoMap.put("pk", pkList);
		infoMap.put("size", new Integer(pkList.size()));
		infoMap.put("time", time);
		infoMap.put("version", version);
		infoMap.put("DBType", DBType); //FIXME 输入DBTYPE? 应该连接池判断吧..
        System.out.println("DBType:"+DBType);
         
//        System.out.println("all meta-data:"+infoMap); //for debug
        
        
		// 从插件的classloader中得到模板资源，并构造输入流in
		String packageName = this.getClass().getPackage().getName();
		String packagePath = packageName.replace('.', '/');
		ClassLoader classLoader = this.getClass().getClassLoader();
		InputStream in = classLoader.getResourceAsStream(packagePath + "/"
				+ SQLMAP_FILE);

		Configuration configuration = new Configuration();
		configuration.setSharedVariable("xml", new XmlEscape());
		
		try {
			Template template = new Template("", new InputStreamReader(in), configuration);
//			template.setEncoding("UTF-8");
			template.process(infoMap, out);
		} catch (Exception e) {
			throw new GeneratorException(e);
		} finally {
			try { in.close(); } catch (IOException e) {}
			try { out.close(); } catch (IOException e) {}
		}
	}
	
	/**
	 * 根据单个TableMapping信息生成单个实体类文件。
	 * @param tableMapping TableMapping对象，为单个表映射信息。
	 * @throws GeneratorException 自定义异常      
	 * @see com.baosight.iplat4j.plugin.db.generator.DbGenerator#generateDomainObject(com.baosight.iplat4j.plugin.db.model.TableMapping)
	 */
	private  void generateDomainObject(TableMapping tableMapping )
			throws GeneratorException {
		String packagePath = tableMapping.getPackageName().replace('.', '\\');
		File destDir = new File(this.targetPath, packagePath); // source路径+package路径

		if (!destDir.isDirectory() && !destDir.mkdirs())
			throw new GeneratorException("Create package "
					+ tableMapping.getPackageName() + " failed!");
		
		File outputFile = new File(destDir, tableMapping.getClassName()	+ ".java"); // 文件输出路径
		if (outputFile.exists() && outputFile.isFile()) {		// 如果文件已经存在
//			if (fileExistsListener != null && 					// 且用户选择不覆盖
//				!fileExistsListener.overwrite(outputFile.getName())) {
//				return;
//			}
			//TODO 处理覆盖方式
		}

		OutputStreamWriter out;
		try {
			out = new OutputStreamWriter(new FileOutputStream(outputFile),"UTF-8");
		} catch (Exception e) {
			throw new GeneratorException("文件路径错误", e);
		}
		
		HashMap infoMap = new HashMap();
		infoMap.put("mapping", tableMapping);
		infoMap.put("time", time);
		infoMap.put("version", version);

		// 从插件的classloader中得到模板资源，并构造输入流in
		String packageName = this.getClass().getPackage().getName();
		packagePath = packageName.replace('.', '/');
		ClassLoader classLoader = this.getClass().getClassLoader();
		InputStream in = classLoader.getResourceAsStream(packagePath + "/"
				+ DOMAIN_OBJECT);

		try {
			Template template = new Template("", new InputStreamReader(in), new Configuration());
			template.setEncoding("UTF-8");
			template.process(infoMap, out);
		} catch (Exception e) {
			throw new GeneratorException(e);
		} finally {
			try { in.close(); } catch (IOException e) {}
			try { out.close(); } catch (IOException e) {}
		}
	}
	
	/**
	 * 根据单个TableMapping信息生成单个实体类的sqlmap映射文件。
	 * @param tableMapping TableMapping对象，为单个表映射信息。
	 * @throws GeneratorException 自定义异常
	 * @see com.baosight.iplat4j.plugin.db.generator.DbGenerator#generateSqlmap(com.baosight.iplat4j.plugin.db.model.TableMapping)
	 */
	private void generateDao4sqlMap(TableMapping tableMapping,String DBType,String daoPackage)
			throws GeneratorException {

		
//		String packagePath = tableMapping.getPackageName().replace('.', '\\');
		String packagePath = daoPackage.replace('.', '\\');
		
		File destDir = new File(this.targetPath, packagePath); // source路径+package路径
		
		if (!destDir.isDirectory() && !destDir.mkdirs())
			throw new GeneratorException("Create package "
					+ tableMapping.getPackageName() + " failed!");
		
		
		File outputFile = new File(destDir, tableMapping.getClassName()+"Dao"+ ".java"); // 文件输出路径
		if (outputFile.exists() && outputFile.isFile()) {		// 如果文件已经存在
//			if (fileExistsListener != null && 					// 且用户选择不覆盖
//				!fileExistsListener.overwrite(outputFile.getName())) {
//				return;
//			}
			//TODO 处理覆盖方式
		}

		OutputStreamWriter out;
		try {
			out = new OutputStreamWriter(new FileOutputStream(outputFile),"UTF-8");
		} catch (Exception e) {
			throw new GeneratorException(e);	
		}
		
		
		List pkList = generatePkList(tableMapping.getPropertyMappingList());

		
		HashMap infoMap = new HashMap();
		infoMap.put("mapping", tableMapping);
//		infoMap.put("nameSpace", fileName);
		infoMap.put("pk", pkList);
//		infoMap.put("size", new Integer(pkList.size()));
		infoMap.put("daoPackage", daoPackage);
		infoMap.put("time", time);
		infoMap.put("version", version);
		infoMap.put("DBType", DBType); //FIXME 输入DBTYPE? 应该连接池判断吧..
        System.out.println("DBType:"+DBType);
         
//        System.out.println("all meta-data:"+infoMap); //for debug
        
        
		// 从插件的classloader中得到模板资源，并构造输入流in
		String packageName = this.getClass().getPackage().getName();
		packagePath = packageName.replace('.', '/');
		ClassLoader classLoader = this.getClass().getClassLoader();
		InputStream in = classLoader.getResourceAsStream(packagePath + "/"
				+ DAO_TEMPLATE_FILE);

		Configuration configuration = new Configuration();
		configuration.setSharedVariable("xml", new XmlEscape());
		
		try {
			Template template = new Template("", new InputStreamReader(in), new Configuration());
			template.setEncoding("UTF-8");
			template.process(infoMap, out);
		} catch (Exception e) {
			throw new GeneratorException(e);
		} finally {
			try { in.close(); } catch (IOException e) {}
			try { out.close(); } catch (IOException e) {}
		}
	}
	
	
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
	

	

}

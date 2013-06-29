package util.DbMeta;


import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 表映射信息类，包含表的名称��映射实体类的类名及包名
 * 生成的sqlmap文件以及属性�映射信息 
 * @author	王佳
 * @version	1.2
 */
public class TableMapping {
	private String tableName;
	private String className;
	private String packageName;
	private String sqlmapFile;
	private boolean sqlmapConfigWritable; //是否写入sqlmap-config文件
	private boolean hasDate ;          //标识该表中是否包含DATE型的数据
	private String comment;  //表格的中文注释
	
	
	private boolean generateDbType;  //标识是否将该表的内容生成为数据库原型模式
	private List propertyMappingList;
	private String propertyModifier = "private"; //标识生成的字段的可见范围修饰符 public private
	private Set importSet = new HashSet();
	
	//限定了数字的类型
	private static String[] numberClass = {
		"java.math.BigDecimal",
		"java.lang.Integer",
		"java.lang.Double",
		"java.lang.Long",
		"java.lang.Float",
		"java.lang.Boolean",
		"BigDecimal",
		"Integer",
		"Double",
		"Long",
		"Float",
		"Boolean",
		"int",
		"double",
		"long",
		"float",
		"boolean"
	};

	/**
	 * 默认构�造函数 
	 */
	public TableMapping() {
	}
	
	/**
	 * 指定tableName的构造函数
	 */
	public TableMapping(String tableName) {
		this.tableName = tableName;
	}
	
	/**
	 * 获取映射实体类的类名 
	 * @return 映射实体类的类名
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * 设置映射实体类的类名 
	 * @param className 映射实体类的类名
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * 获取映射实体类的包名 
	 * @return 映射实体类的包名
	 */
	public String getPackageName() {
		return packageName;
	}
	/**
	 * 设置映射实体类的包名 
	 * @param packageName 映射实体类的包名
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	/**
	 * 获取sqlmap文件全路径 
	 * @return sqlmap文件的全路径
	 */
	public String getSqlmapFile() {
		return sqlmapFile;
	}
	/**
	 * 设置sqlmap文件全路径 
	 * @param sqlmap文件的全路径
	 */
	public void setSqlmapFile(String sqlmapFile) {
		this.sqlmapFile = sqlmapFile;
	}
	/**
	 * 获取映射表名 
	 * @return 映射的表名
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * 设置映射表名 
	 * @param tableName 设置映射的表名
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	/**
	 * 获取属性�映射列表 
	 * @return PropertyMapping的列表，包含属性�的映射信息
	 */
	public List getPropertyMappingList() {
		return propertyMappingList;
	}
	/**
	 * 设置属性�映射列表 
	 * @param propertyMappingList 
	 */
	public void setPropertyMappingList(List propertyMappingList) {
		this.propertyMappingList = propertyMappingList;
	}
	
	/**
	 * 检查两个TableMapping是否相同 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (!(obj instanceof TableMapping))
			return false;
		
		TableMapping other = (TableMapping) obj;
		
		return tableName.equals(other.tableName);
	}
	
	/**
	 * 返回import的集合 
	 * @return import的集合
	 */
	public Set getImports() {
		
		if(importSet.isEmpty()) {
			
		
		Package langPackage = Package.getPackage("java.lang");		// TODO 其它可省略的
		boolean hasNumber = false;
//		boolean hasString = false;
		
		for (Iterator it = propertyMappingList.iterator(); it.hasNext(); ) {
			PropertyMapping propertyMapping = (PropertyMapping) it.next();
			
			String strPropertyType = propertyMapping.getPropertyType(); //这里用到了长名 dbmetadata没有定义好到底是长名还是啥..
			if( !hasNumber ) {							//判断是否包含数字
				
				hasNumber = hasNumber(strPropertyType);
			}
			if(!hasDate) {
				hasDate = hasDate(strPropertyType);
			}
			
			if(strPropertyType.equals("BigDecimal")) { //FIXME 这种判断不应该放在这里应该放在 DbMetaData
				strPropertyType = "java.math.BigDecimal";
			}
			
			//判断strPropertyType是否有
			Class propertyType = null;
			try {
				propertyType = Class.forName(strPropertyType);
			} catch (ClassNotFoundException e) { //integer 以及 double都回报错null 反正都用导入? 为何最后生成是正确的?因为strPropertyType本来就是短名?
				System.out.println("ClassNotFoundException:"+propertyType+" mapping:"+propertyMapping);
				
				continue;
			}
			//java.lang下不用导入
			Package propertyPackage= propertyType.getPackage();
			if (langPackage.equals(propertyPackage)) {
				continue;
			}
			//end
			
			//
			importSet.add(strPropertyType);
		}
		//附加import类型
//			if(hasDate) {
//			importSet.add("util.DateUtils");
//		}
//			if(hasNumber) {
//				importSet.add("util.NumberUtils");
//			}
		}
		
		return importSet;
	}

	private boolean hasDate(String strPropertyType) {
		
		if(strPropertyType.equals("java.util.Date"))
			return true;
		
		return false;
	}

	private boolean hasNumber(String strPropertyType) {

		for (int i = 0; i < numberClass.length; i++) {
			if(numberClass[i].equals(strPropertyType)) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * 是否注册sqlmap-config文件
	 * @return
	 */
	public boolean isSqlmapConfigWritable() {
		return sqlmapConfigWritable;
	}

	/**
	 * 设置是否注册sqlmap-config文件
	 * @param sqlmapConfigWritable
	 */
	public void setSqlmapConfigWritable(boolean sqlmapConfigWritable) {
		this.sqlmapConfigWritable = sqlmapConfigWritable;
	}

	/**
	 * 是否包含日期型字段
	 * @return
	 */
	public boolean isHasDate() {
		return hasDate;
	}

	/**
	 * 是否生成数据库原型字段
	 * @return
	 */
	public boolean isGenerateDbType() {
		return generateDbType;
	}

	/**
	 * 设置是否生成数据库原型字段
	 * @param generateDbType
	 */
	public void setGenerateDbType(boolean generateDbType) {
		this.generateDbType = generateDbType;
	}

	/**
	 * 获取生成字段的标识符（public , private）
	 * @return
	 */
	public String getPropertyModifier() {
		return propertyModifier;
	}

	/**
	 * 设置生成字段的标识
	 * @param propertyModifier
	 */
	public void setPropertyModifier(String propertyModifier) {
		this.propertyModifier = propertyModifier;
	}

	/**
	 * 获取表的注释
	 * @return
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * 设置表的注释
	 * @param comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TableMapping [tableName=" + tableName + ", className="
				+ className + ", packageName=" + packageName + ", sqlmapFile="
				+ sqlmapFile + ", sqlmapConfigWritable=" + sqlmapConfigWritable
				+ ", hasDate=" + hasDate + ", comment=" + comment
				+ ", generateDbType=" + generateDbType
				+ ", propertyMappingList=" + propertyMappingList
				+ ", propertyModifier=" + propertyModifier + ", importSet="
				+ importSet + "]";
	}
	
	

}

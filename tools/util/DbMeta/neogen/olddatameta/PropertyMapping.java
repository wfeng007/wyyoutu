package util.DbMeta.neogen.olddatameta;




/**
 * 属性映射信息类，包含映射表的列名 映射实体类的属性名及属性类型，
 * 该属性是否要生成 
 */
public class PropertyMapping {
	private String columnName;
	private String propertyName;
	private String propertyType;
	private boolean primaryKey;
	private boolean conditional;
	private boolean notNull;
	private String comment = " "; //字段对应的中文名
	private String columnType; //数据库中的字段类型
	private String type; //将各种数据库中的字段类型统一写法 数字类型 NUMBER，字符串为VARCHAR
	
	private int columnSize; //数据库中字段的长度。
	private int scale;     //数据库字段的数字型的小数精度
	
	private boolean generate = true;

	
	/**
	 * 获取notNull
	 * @return notNull
	 */
	public boolean isNotNull() {
		return notNull;
	}

	/**
	 * 设置notNull
	 * @param notNull notNull
	 */
	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	/**
	 * 默认构�造函数
	 */
	public PropertyMapping() {
	}
	
	/**
	 * 指定columnName的构造函数
	 */
	public PropertyMapping(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * 获取映射的列名�
	 * @return 映射的列各
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * 设置映射的列名�
	 * @param columnName 映射的列名
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * 是否要生成该属�
	 * @return 是否要生成该属�
	 */
	public boolean isGenerate() {
		return generate;
	}

	/**
	 * 设置是否要生成该属�
	 * @param generate 是否要生成该属
	 */
	public void setGenerate(boolean generate) {
		this.generate = generate;
	}

	/**
	 * 获取映射属�性名
	 * @return 映射属�性名
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * 设置映射属性�名
	 * @param propertyName 映射属�性名
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * 获取映射属性的类型
	 * @return 映射属�性�的类型
	 */
	public String getPropertyType() {
//		System.out.println(propertyType);
		return propertyType;
	}

	/**
	 * 获取映射属性�的类型（不包含包名）�
	 * @return 映射属�性的类型（不包含包名）
	 */
	public String getShortType() {
		int index = propertyType.lastIndexOf('.');
		
		//这里又判断一次是否长名
		return (index >= 0) ? propertyType.substring(index + 1) : propertyType;
	}
	
	/**
	 * 设置映射属性�的类型
	 * @param propertyType 映射属�性�的类型
	 */
	public void setPropertyType(String propertyType) {
//		System.out.println(propertyType);
		this.propertyType = propertyType;
	}

	/**
	 * 获取isPrimaryKey 
	 * @return isPrimaryKey
	 */
	public boolean isPrimaryKey() {
		return primaryKey;
	}

	/**
	 * 设置isPrimaryKey 
	 * @param isPrimaryKey isPrimaryKey
	 */
	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
//	public String toString() {
//		return "PropertyMapping [columnName=" + columnName + ", propertyName="
//				+ propertyName + ", propertyType=" + propertyType
//				+ ", primaryKey=" + primaryKey + ", conditional=" + conditional
//				+ ", notNull=" + notNull + ", comment=" + comment
//				+ ", columnType=" + columnType + ", type=" + type
//				+ ", columnSize=" + columnSize + ", scale=" + scale
//				+ ", generate=" + generate + "]";
//	}
	public String toString() {
		return this.propertyName;
	}
	
	/**
	 * 检查两个PropertyMapping是否相同 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (!(obj instanceof PropertyMapping))
			return false;
		
		PropertyMapping other = (PropertyMapping) obj;
		
		return columnName.equals(other.columnName);
	}

	/**
	 * 获取isConditional 
	 * @return isConditional
	 */
	public boolean getConditional() {
		return conditional;
	}

	/**
	 * 设置isConditional 
	 * @param isConditional 设置isConditional的�值
	 */
	public void setConditional(boolean conditional) {
		this.conditional = conditional;
	}

	/**
	 * 获取字段的注释
	 * @return comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * 设置字段的注释
	 * @param comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * 获取数据库字段类型
	 * @return columnType
	 */
	public String getColumnType() {
		return columnType;
	}

	/**
	 * 设置字段的数据库类型
	 * @param columnType
	 */
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	/**
	 * 获取数据库中字段的长度
	 * @return columnSize
	 */
	public int getColumnSize() {
		return columnSize;
	}

	/**
	 * 设置字段的长度
	 * @param columnSize
	 */
	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}

	/**
	 * 获取数据库中数字型字段的小数精度
	 * @return scale
	 */
	public int getScale() {
		return scale;
	}

	/**
	 *设置数字类型字段的小数精度 
	 * @param scale
	 */
	public void setScale(int scale) {
		this.scale = scale;
	}

	/**
	 * 获取用于模板生成时的类型
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置模板生成时的类型
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	
}

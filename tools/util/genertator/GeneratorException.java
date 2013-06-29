package util.genertator;



/**
 * 代码生成器自定义异常类
 * @author wfeng007
 * @date 2011-8-22
 */
public class GeneratorException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public GeneratorException() {
		super();
	}
	
	public GeneratorException(String message) {
		super(message);
	}

	/**
	 * 构造函数
	 */
	public GeneratorException(Throwable cause) {
		super(cause);
	}

	/**
	 * 构造函数
	 */
	public GeneratorException(String message, Throwable cause) {
		super(message, cause);
	}
}

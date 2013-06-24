package summ.framework;

/**
 * 
 * @author wfeng007
 *
 */
public class IccsRuntimeException extends org.springframework.core.NestedRuntimeException{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5814255263220459861L;

	/**
	 * 
	 */
	private String errCode;

	/**
	 * 
	 */
	private String description;
	
	/**
	 * 
	 * @param msg
	 */
	public IccsRuntimeException(String msg) {
		super(msg);
	}
	
	public IccsRuntimeException(String errCode, String description) {
		super("ERRCODE:"+errCode+" DESC:"+description);
	}
	
	public IccsRuntimeException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public IccsRuntimeException(String errCode, String description,Throwable cause) {
		super("ERRCODE:"+errCode+" DESC:"+description,cause);
	}
	
//	public static void main(String[] args) {
//		try{
//			dodo();
//		}catch(IccsRuntimeException e){
//			throw new IccsRuntimeException("456",new Exception("wf"));
//		}
//		
//	}
//	
//	public static void dodo(){
//		throw new IccsRuntimeException("123");
//	}

}

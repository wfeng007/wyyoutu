/**
 * 
 */
package summ.framework.util;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * 
 * @author wfeng007
 * 
 */
public class RestUtil {
	
	private static Logger logger = Logger.getLogger(RestUtil.class);
	
	/**
	 * ssl config cache
	 */
	private static ClientConfig sslConfig = null;
	/**
	 * get ssl config 
	 */
	public static synchronized ClientConfig getSslConfig() {
		if (sslConfig == null) {
			
			
			// ssl 
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
					// TODO Auto-generated method stub

					// Trust always
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
					// TODO Auto-generated method stub

					// Trust always
				}
			} };
			HostnameVerifier hostnameVerifier = new HostnameVerifier() {
				@Override
				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}

			};

			ClientConfig config = new DefaultClientConfig();

			SSLContext ctx = null;
			try {
				ctx = SSLContext.getInstance("SSL");
				ctx.init(null, trustAllCerts, new SecureRandom());
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (KeyManagementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			config.getProperties().put(
					HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
					new HTTPSProperties(hostnameVerifier, ctx));
			// ssl

			sslConfig = config;
		}

		return sslConfig;
	}
	

	/**
	 * rest result vo
	 * @author wfeng007
	 */
	public static class RestResult{
		private int status=-1;
		private String entity=null;
		private RestResult(int httpStatus,String httpEntity){
			this.status=httpStatus;
			this.entity=httpEntity;
		}
		/**
		 * @return the status
		 */
		public int getStatus() {
			return status;
		}
		/**
		 * @return the entity
		 */
		public String getEntity() {
			return entity;
		}
		
		/**
		 * entity 为null则返回 false
		 * @return
		 */
		public boolean hasEntity(){
			return (entity!=null)?true:false;
		}
		
		/**
		 * 200~299 则为true
		 * @return
		 */
		public boolean isSucceeded(){
			return (this.status>=200 && this.status<300)?true:false;
		}
		
		/**
		 * 
		 * @return
		 */
		public boolean isFailed(){
			return !isSucceeded();
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "RestResult [status=" + status + ", entity=" + entity + "]";
		}
		
		
	}
	
	/**
	 * 
	 * 常用rest请求通用方法
	 * 
	 * @param cfg 配置比如SSLConfig
	 * 		  null 则使用默认配置
	 * @param baseUrl 基本url
	 * @param uName 认证用户 
	 * 		  null 则不进行认证操作
	 * @param uPassword 认证用户密码 
	 * 		  null 则不进行认证操作
	 * @param method 请求方式 "GET" "POST" "PUT" "DELETE"
	 * 		  delete方式  不能有 requestEntity form等输出   get方式取决于服务端一般也不能有entity
	 * 		     否则会报 HTTP method DELETE doesn't support output
	 * @param requestMediaType 请求mime类型 //如 MediaType.APPLICATION_FORM_URLENCODED
	 * 		  null 则使用默认类型
	 * @param reponseMediaType 响应mime类型 //如 MediaType.APPLICATION_JSON
	 * 	      null 则使用默认类型
	 * @param queryParamMap queryParam
	 * @param form 需要form内容时提供
	 * 		  null 则不提供
	 * @param requestEntity 用户自定义内容 请求体内容 与 form互斥 优先级优于form
	 * 		  null 则不提供entity
	 * @param paths 子路径 数组或多个
	 * @return  RestResult 类型
	 */
	public static RestResult doRest(
			ClientConfig cfg,
			String baseUrl,
			String uName,
			String uPassword,
			String method,
			String requestMediaType,
			String reponseMediaType,
			Map<String,String> queryParamMap,
			Form form,
			String requestEntity,
			String ... paths){

		Client c = null;
		ClientResponse cr=null;
		try{
			//config 
			if(cfg!=null){
				c = Client.create(cfg);//for ssl
			}else{
				c = Client.create(); //for normal
			}
			
			//认证
			if(uName!=null||uPassword!=null){
				c.addFilter(new HTTPBasicAuthFilter(uName, uPassword)); //for auth
			}
			//基础url
			WebResource service = c.resource(baseUrl);
			
			//to-path
			for (int i = 0; i < paths.length; i++) {
				service=service.path(paths[i]);
			}
			
			logger.debug("path:"+service.getURI().toString());
			//
			if(queryParamMap!=null){
				Set<String> keySet = queryParamMap.keySet();
				Iterator<String> it = keySet.iterator();
				while(it.hasNext()){
					String key = it.next();
					String value = queryParamMap.get(key);
					service = service.queryParam(key, value);
				}
			}
			
			//build header
			Builder webBuilder = service.getRequestBuilder();
			if(requestMediaType!=null){
//			Builder webBuilder =service.type(MediaType.APPLICATION_JSON);
				webBuilder =webBuilder.type(requestMediaType);
			}
			if(reponseMediaType!=null){
				webBuilder =webBuilder.accept(reponseMediaType);
			}

			//do-rest-request
			logger.debug("requestEntity:"+requestEntity);
			if(requestEntity!=null){
				cr=webBuilder.method(method,ClientResponse.class,requestEntity); //body
//				webBuilder.meth
			}else if(form!=null){
				//FIXME 自动设置?
				webBuilder =webBuilder.accept(MediaType.APPLICATION_FORM_URLENCODED); //必须是APPLICATION_FORM_URLENCODED
				cr=webBuilder.method(method,ClientResponse.class,form); //form 
			}else{
				cr=webBuilder.method(method,ClientResponse.class); //没有特定内容
			}
			
			
			//create result
//			logger.debug("listAllServerByNode http-status:"+service.getURI().getPath());
			int httpS=cr.getStatus();
			String httpE=null;
			if(cr.hasEntity()){
				logger.info("has Entity");	
				httpE=cr.getEntity(String.class);
			}else{
				logger.info("no Entity");	
			}
			RestResult rr = new RestResult(httpS, httpE);
			if(rr.isSucceeded()){
				logger.info("success http-status:"+httpS);	
			}
			else{
				logger.info("failed http-status:"+httpS);
			}
			//
			return rr;

		}
		catch (Exception e) {
			e.printStackTrace();
			return new RestResult(-1, e.getMessage());
		}
		finally{
			if(cr!=null)cr.close();
			if(c!=null)c.destroy();
		}
		
	}
	
	/**
	 * 
	 * @return
	 */
	public static RestResult doRestNoConfigNoAuth(
			String baseUrl,
			String method,
			String requestMediaType,
			String reponseMediaType,
			Map<String,String> queryParamMap,
			Form form,
			String requestEntity,
			String ... paths
			){
		return doRest(null, baseUrl, null, null, method, requestMediaType, reponseMediaType, queryParamMap, form, requestEntity, paths);
	}
	
	/**
	 * 
	 * @param cfg
	 * @param baseUrl
	 * @param uName
	 * @param uPassword
	 * @param requestMediaType
	 * @param reponseMediaType
	 * @param queryParamMap
	 * @param paths
	 * @return
	 */
	public static RestResult doGet(ClientConfig cfg,
			String baseUrl,
			String uName,
			String uPassword,
//			String requestMediaType, //不需要
			String reponseMediaType,
			Map<String,String> queryParamMap,
			String ... paths){
		return doRest(cfg,baseUrl,uName,uPassword, "GET", null, reponseMediaType, queryParamMap, null, null, paths);
	}
	
	/*
	 * easy
	 */
	public static RestResult doGetEasy(String baseUrl,
//			String requestMediaType, //不需要
			String reponseMediaType,
			Map<String,String> queryParamMap,String ... paths){
		return doRestNoConfigNoAuth(baseUrl, "GET", null, reponseMediaType, queryParamMap, null, null, paths);
	}
	
	/**
	 * 盖方法取决于服务端
	 */
//	public static RestResult doGet(String baseUrl,
//			String requestMediaType,
//			String reponseMediaType,
//			Form form,String ... paths){
//		return doRestNoConfigNoAuth(baseUrl, "GET", requestMediaType, reponseMediaType, null, form, null, paths);
//	}
	
	/**
	 * 1
	 */
	public static RestResult doPost(ClientConfig cfg,
			String baseUrl,
			String uName,
			String uPassword,
			String requestMediaType,
			String reponseMediaType,
			Form form,String ... paths){
		return doRest(cfg,baseUrl,uName,uPassword, "POST", requestMediaType, reponseMediaType, null, form, null, paths);
	}
	
	/**
	 * 2
	 */
	public static RestResult doPostEasy(String baseUrl,
			String requestMediaType,
			String reponseMediaType,
			Form form,String ... paths){
		return doRestNoConfigNoAuth(baseUrl, "POST", requestMediaType, reponseMediaType, null, form, null, paths);
	}
	
	/**
	 * 3
	 * @param cfg
	 * @param baseUrl
	 * @param uName
	 * @param uPassword
	 * @param requestMediaType
	 * @param reponseMediaType
	 * @param form
	 * @param paths
	 * @return
	 */
	public static RestResult doPost(ClientConfig cfg,
			String baseUrl,
			String uName,
			String uPassword,
			String requestMediaType,
			String reponseMediaType,
			String requestEntity,String ... paths){
		return doRest(cfg,baseUrl,uName,uPassword, "POST", requestMediaType, reponseMediaType, null, null, requestEntity, paths);
	}
	
	/**
	 * 4
	 */
	public static RestResult doPostEasy(String baseUrl,
			String requestMediaType,
			String reponseMediaType,
			String requestEntity,String ... paths){
		return doRestNoConfigNoAuth(baseUrl, "POST", requestMediaType, reponseMediaType, null, null, requestEntity, paths);
	}
	
	/**
	 * 
	 */
	public static RestResult doPut(ClientConfig cfg,
			String baseUrl,
			String uName,
			String uPassword,
			String requestMediaType,
			String reponseMediaType,
			Form form,String ... paths){
		return doRest(cfg,baseUrl,uName,uPassword, "PUT", requestMediaType, reponseMediaType, null, form, null, paths);
	}
	
	/**
	 * 
	 */
	public static RestResult doPutEasy(String baseUrl,
			String requestMediaType,
			String reponseMediaType,
			Form form,String ... paths){
		return doRestNoConfigNoAuth(baseUrl, "PUT", requestMediaType, reponseMediaType, null, form, null, paths);
	}
	
	
	/**
	 * 3
	 * @param cfg
	 * @param baseUrl
	 * @param uName
	 * @param uPassword
	 * @param requestMediaType
	 * @param reponseMediaType
	 * @param form
	 * @param paths
	 * @return
	 */
	public static RestResult doPut(ClientConfig cfg,
			String baseUrl,
			String uName,
			String uPassword,
			String requestMediaType,
			String reponseMediaType,
			String requestEntity,String ... paths){
		return doRest(cfg,baseUrl,uName,uPassword, "PUT", requestMediaType, reponseMediaType, null, null, requestEntity, paths);
	}
	
	
	/**
	 * 
	 */
	public static RestResult doPutEasy(String baseUrl,
			String requestMediaType,
			String reponseMediaType,
			String requestEntity,String ... paths){
		return doRestNoConfigNoAuth(baseUrl, "PUT", requestMediaType, reponseMediaType, null, null, requestEntity, paths);
	}
	
	
	/**
	 * delete 好像不能有 requestEntity form等输出
	 * 否则会报 HTTP method DELETE doesn't support output
	 * 
	 * @param cfg
	 * @param baseUrl
	 * @param uName
	 * @param uPassword
	 * @param requestMediaType
	 * @param reponseMediaType
	 * @param queryParamMap
	 * @param paths
	 * @return
	 */
	public static RestResult doDelete(ClientConfig cfg,
			String baseUrl,
			String uName,
			String uPassword,
//			String requestMediaType, //不需要
			String reponseMediaType,
			Map<String,String> queryParamMap,
			String ... paths){
		return doRest(cfg,baseUrl,uName,uPassword, "DELETE", null, reponseMediaType, queryParamMap, null, null, paths);
	}
	
	/**
	 * delete 好像不能有 requestEntity form等输出
	 * 否则会报 HTTP method DELETE doesn't support output
	 * 
	 * @param baseUrl
	 * @param requestMediaType
	 * @param reponseMediaType
	 * @param requestEntity
	 * @param paths
	 * @return
	 */
	public static RestResult doDeleteEasy(String baseUrl,
//			String requestMediaType, //不需要
			String reponseMediaType,
			Map<String,String> queryParamMap,String ... paths){
		//
		return doRestNoConfigNoAuth(baseUrl, "DELETE", null, reponseMediaType, queryParamMap, null, null, paths);
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		//replacementVirtualAppliance
		//vmc PUT https://myserver:port/{webContext}/VMControl/virtualServers/2343
//		System.out.println(RestUtil.doPut(sslConfig, "https://172.21.140.14:8422/ibm/director/rest/VMControl",
//				"wusj", "wusj", "{\"virtualServer\":{\"replacementVirtualAppliance\":\"1111\"}}", "hosts","6798","virtualServers","s-oid"));
//		userValidateTest();
		//query map
		Map<String,String> queryParams=new HashMap<String,String>();
		queryParams.put("name", "wangfeng");
		//form 1
		Form form=new Form();
		form.add("name", "wangfeng");
//		form.putSingle("name", "wangfeng");
		//form 2
		JSONObject json1=new JSONObject();
		json1.put("name", "wangfeng");
//		form.putSingle("name", "wangfeng");
		
		
		System.out.println(RestUtil.doGetEasy("http://127.0.0.1:8180/resttest/rest", MediaType.APPLICATION_JSON, queryParams, "hello","test_get"));
		
		//get 不能有entity 这个取决于服务端
//		System.out.println(RestUtil.doGet("http://127.0.0.1:8180/resttest/rest", null, MediaType.APPLICATION_JSON,form, "hello","test_get_f"));
		

		System.out.println(RestUtil.doPostEasy("http://127.0.0.1:8180/resttest/rest", null, MediaType.APPLICATION_JSON, form, "hello","test_post"));
		
		System.out.println(RestUtil.doPostEasy("http://127.0.0.1:8180/resttest/rest", MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, json1.toString(), "hello","test_post_e"));

		System.out.println(RestUtil.doPutEasy("http://127.0.0.1:8180/resttest/rest",
				MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, json1.toString(), "hello","test_put"));

		System.out.println(RestUtil.doPostEasy("http://127.0.0.1:8180/resttest/rest", null, MediaType.APPLICATION_JSON, form, "hello","test_put_f"));
//		
		System.out.println(RestUtil.doDeleteEasy("http://127.0.0.1:8180/resttest/rest", MediaType.APPLICATION_JSON, queryParams, "hello","test_delete"));
//		
	}


}

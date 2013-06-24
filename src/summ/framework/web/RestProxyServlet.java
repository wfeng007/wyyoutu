package summ.framework.web;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import summ.framework.util.QueryStringParser;
import summ.framework.util.RestUtil;
import summ.framework.util.RestUtil.RestResult;

/**
 * Servlet implementation class AjaxProxyServlet
 */
public class RestProxyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
//	private String targeturi;
//	private String pathinfo;
//	private String requestMethod;
//	private String requestusername;
//	private String requestpassword;
//	private String qStr;
//	private String myPath;
//	private String requestContenttype;
//	private String requestbody;

	private static Logger logger = Logger
			.getLogger(RestProxyServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RestProxyServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response,"GET");
	}
	/**
	 * 获得pathinfo
	 * @param request
	 * @param response
	 * @param method
	 * @return
	 */
	protected String getPathInfoString(HttpServletRequest request,
			HttpServletResponse response, String method){
		return request.getPathInfo();
	}
	protected void doProcess(HttpServletRequest request,
			HttpServletResponse response, String method)
			throws ServletException, IOException {
		String targeturi = null;
		String pathinfo = null;
		String requestMethod = null;
		String requestusername = null;
		String requestpassword = null;
		String qStr = null;
		String myPath = null;
		String requestContenttype = null;
		String requestbody = null;
		/*
		 * handle requset
		 */
		// 设置目标地址
		 targeturi = "http://127.0.0.1:8080/cloud/api";
		// targeturi=ConfigurationUtil.getInstance.getValue("RestApi");
//		targeturi = SKCConfig.getBaseUrl();
		logger.info("targeturi的值是" + targeturi);
		// 设置请求的编码方式
		request.setCharacterEncoding("utf-8");
//		pathinfo = request.getPathInfo();
		
		pathinfo=getPathInfoString(request,response,method);
		
		myPath = pathinfo;
		logger.info("pathinfo的值是" + pathinfo);
		// 设置请求方式
		requestMethod = request.getMethod();
		logger.info("requestMethod的值是" + requestMethod);
		// 从请求中中获得请求用户名
		requestusername = request.getHeader("usr");
		logger.info("requestusername的值是" + requestusername);
		// 从请求中获得请求的密码
		requestpassword = request.getHeader("pwd");
		logger.info("requestusername的值是" + requestpassword);
		// 获得请求体的类型
		requestContenttype = request.getContentType();
		logger.info("requestContenttype的值是" + requestContenttype);
		// 获得请求的参数
		qStr = request.getQueryString();
		logger.info("qStr的值是" + qStr);
		
		
		// 如果请求的用户名和密码为空，那么就给他一个定值。
		if (requestusername == null || requestpassword == null
				|| "".equalsIgnoreCase(requestusername)
				|| "".equalsIgnoreCase(requestpassword)) {
			// requestusername="admin";
//			requestusername = SKCConfig.getUserName();
			requestusername = "";//
			// requestusername =
			// ConfigurationUtil.getInstance.getValue("username");
			logger.info("requestusername的值是" + requestusername);
			// requestpassword =
			// ConfigurationUtil.getInstance.getValue("password");
			// requestpassword="qiansy";
//			requestpassword = SKCConfig.getPassWord();
			requestpassword = "";
			logger.info("requestpassword的值是" + requestpassword);
		}
		// 这个地方的？怎么处理
		// myPath = (qStr != null ? pathinfo + "?" + qStr : pathinfo);
		// myPath=pathinfo+"?"+qStr;

		logger.info("myPath的值是" + myPath);
		int entityLen = request.getContentLength();
		if (entityLen > 0 && entityLen < 20 * 1024) { // 建议请求不要大于20KB
			byte[] b = new byte[entityLen];
			// 获得请求流中内容
			BufferedInputStream bis = new BufferedInputStream(
					request.getInputStream()); // 得到内容(body/entity)的输入流.
			bis.read(b);
			// 获得请求体内容
			requestbody = new String(b);
			logger.info("requestbody的值是" + requestbody);
		}
		else if(entityLen<0){
			logger.warn("entityLen:"+entityLen);
		}
		/*
		 * handle requset end
		 */

		/*
		 * do rest
		 */
		Map<String, String> map =null;
            if(qStr!=null){
            	 map = new QueryStringParser(qStr).getMap();
            }
		  RestResult rs = RestUtil.doRest(null, targeturi, requestusername,
				requestpassword, method, requestContenttype,
				MediaType.APPLICATION_JSON, map, null, requestbody, myPath);

		/*
		 * handle response
		 */
		// 设置回应的字符编码
		response.setCharacterEncoding("utf-8");
		// 设置返回的格式
		response.setContentType(MediaType.APPLICATION_JSON);
		response.getWriter().write(rs.getEntity());
		logger.info("这个请求处理完毕的值是" + rs.getEntity());
		// 清空缓存
		response.getWriter().flush();
		/*
		 * handle response end
		 */
	}

//	private void handleRequest(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		// 设置目标地址
//		// targeturi = "http://127.0.0.1:8080/cloud/api";
//		// targeturi=ConfigurationUtil.getInstance.getValue("RestApi");
//		targeturi = SKCConfig.getBaseUrl();
//		logger.info("targeturi的值是" + targeturi);
//		// 设置请求的编码方式
//		request.setCharacterEncoding("utf-8");
//		pathinfo = request.getPathInfo();
//		myPath = pathinfo;
//		logger.info("pathinfo的值是" + pathinfo);
//		// 设置请求方式
//		requestMethod = request.getMethod();
//		logger.info("requestMethod的值是" + requestMethod);
//		// 从请求中中获得请求用户名
//		requestusername = request.getHeader("usr");
//		logger.info("requestusername的值是" + requestusername);
//		// 从请求中获得请求的密码
//		requestpassword = request.getHeader("pwd");
//		logger.info("requestusername的值是" + requestpassword);
//		// 获得请求体的类型
//		requestContenttype = request.getContentType();
//		logger.info("requestContenttype的值是" + requestContenttype);
//		// 获得请求的参数
//		qStr = request.getQueryString();
//		logger.info("qStr的值是" + qStr);
//		// 如果请求的用户名和密码为空，那么就给他一个定值。
//		if (requestusername == null || requestpassword == null
//				|| "".equalsIgnoreCase(requestusername)
//				|| "".equalsIgnoreCase(requestpassword)) {
//			// requestusername="admin";
//			requestusername = SKCConfig.getUserName();
//			// requestusername =
//			// ConfigurationUtil.getInstance.getValue("username");
//			logger.info("requestusername的值是" + requestusername);
//			// requestpassword =
//			// ConfigurationUtil.getInstance.getValue("password");
//			// requestpassword="qiansy";
//			requestpassword = SKCConfig.getPassWord();
//			logger.info("requestpassword的值是" + requestpassword);
//		}
//		// 这个地方的？怎么处理
//		// myPath = (qStr != null ? pathinfo + "?" + qStr : pathinfo);
//		// myPath=pathinfo+"?"+qStr;
//
//		logger.info("myPath的值是" + myPath);
//		int entityLen = request.getContentLength();
//		if (entityLen > 0 && entityLen < 20 * 1024) { // 建议请求不要大于20KB
//			byte[] b = new byte[entityLen];
//			// 获得请求流中内容
//			BufferedInputStream bis = new BufferedInputStream(
//					request.getInputStream()); // 得到内容(body/entity)的输入流.
//			bis.read(b);
//			// 获得请求体内容
//			requestbody = new String(b);
//			logger.info("requestbody的值是" + requestbody);
//		}
//	}

//	private void handleResponse(HttpServletRequest request,
//			HttpServletResponse response, RestResult rs)
//			throws ServletException, IOException {
//		// 设置回应的字符编码
//		response.setCharacterEncoding("utf-8");
//		// 设置返回的格式
//		response.setContentType(MediaType.APPLICATION_JSON);
//		response.getWriter().write(rs.getEntity());
//		logger.info("这个请求处理完毕的值是" + rs.getEntity());
//		// 清空缓存
//		response.getWriter().flush();
//	}

	/*
	 * (non-Javadoc) 当请求的的方法是delete
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response,"DELETE");
		// TODO Auto-generated method stub
//		handleRequest(request, response);
//		Map<String, String> map = new QueryStringParser(qStr).getMap();
//		RestResult rs = RestUtil.doRest(null, targeturi, requestusername,
//				requestpassword, "DELETE", requestContenttype,
//				MediaType.APPLICATION_JSON, map, null, requestbody, myPath);
//		handleResponse(request, response, rs);
	}

	/*
	 * (non-Javadoc)当请求的方法是put
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response,"PUT");
//		if (requestMethod.equalsIgnoreCase("PUT")) {
//			handleRequest(request, response);
//			Map<String, String> map = new QueryStringParser(qStr).getMap();
//			RestResult rs = RestUtil.doRest(null, targeturi, requestusername,
//					requestpassword, "PUT", requestContenttype,
//					MediaType.APPLICATION_JSON, map, null, requestbody, myPath);
//			handleResponse(request, response, rs);
//		}
	}

	// TODO Auto-generated method stub
	/**
	 * 当请求 的方法是post
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("-- begin --");
		doProcess(request,response,"POST");
//		handleRequest(request, response);
//		if (requestMethod.equalsIgnoreCase("Get")) {
//			// 将请求后面的？值转换为map<String,String>
//			Map<String, String> map = new QueryStringParser(qStr).getMap();
//			RestResult rs = RestUtil.doRest(null, targeturi, requestusername,
//					requestpassword, "GET", requestContenttype,
//					MediaType.APPLICATION_JSON, map, null, requestbody, myPath);
//			handleResponse(request, response, rs);
//		}
//		if (requestMethod.equalsIgnoreCase("Post")) {
//			Map<String, String> map = new QueryStringParser(qStr).getMap();
//			RestResult rs = RestUtil.doRest(null, targeturi, requestusername,
//					requestpassword, "POST", requestContenttype,
//					MediaType.APPLICATION_JSON, map, null, requestbody, myPath);
//			handleResponse(request, response, rs);
//		}
	}

	// System.out.println("getRequestURI:"+request.getRequestURI()); // Request
	// URI = context path + servlet path + path info (没有queryString以及)
	// System.out.println("getRequestURL:"+request.getRequestURL());
	// //返回从协议到所有path 但不包括queryString
	//
	// System.out.println("getMethod:"+request.getMethod()); //方法
	// System.out.println("getScheme:"+request.getScheme()); //返回url中前面的协议描述头
	// System.out.println("getServerName:"+request.getServerName());
	// //url中服务器名称或ip
	// System.out.println("getServerPort:"+request.getServerPort()); //url中port
	//
	// //Request URI = context path + servlet path + path info(translated?)
	// System.out.println("getContextPath:"+request.getContextPath());
	// //返回servlet所在应用的contextpath 如果在rootpath下返回""
	// System.out.println("getServletPath:"+request.getServletPath());
	// //返回serlvet匹配路径path（web.xml中的设置的内容） 如果servlet用到/*匹配路径 则返回""
	//
	// System.out.println("getPathInfo:"+request.getPathInfo());
	// //serlvet之后的所有path 不包括queryString
	// System.out.println("getPathTranslated:"+request.getPathTranslated());
	// //本地文件系统路径(应用本地path)+servlet之后的path但不包括queryString
	//
	// System.out.println("getQueryString:"+request.getQueryString()); //返回
	// queryString
	//
	// System.out.println("getProtocol:"+request.getProtocol()); //返回请求行最后的协议

	// new org.apache.commons.httpclient.Header().
	// new
	// org.apache.commons.httpclient.methods.PostMethod().addRequestHeader(header);
	// new org.apache.commons.httpclient.methods.RequestEntity
	// new org.apache.commons.httpclient.methods.
	// new
	// org.apache.commons.httpclient.methods.PostMethod().setRequestEntity(requestEntity);
	// new
	// org.apache.commons.httpclient.methods.PostMethod().getResponseBodyAsStream();
	// new ByteArrayOutputStream();//可以捕获内存缓冲区的数据，转换成字节数组。
	// new ByteArrayInputStream();//可以将字节数组转化为输入流

	// PipedInputStream in = new PipedInputStream();
	// PipedOutputStream out = new PipedOutputStream(in);

	// Enumeration<String> headerNames= request.getHeaderNames();
	// while (headerNames.hasMoreElements()) {
	// String name = (String) headerNames.nextElement();
	// Enumeration<String> headerValues=request.getHeaders(name);
	// while (headerValues.hasMoreElements()) {
	// String value = (String) headerValues.nextElement();
	// System.out.println("name:"+name+" <-> value:"+value);
	// }
	// }
	//
	// //media type
	// System.out.println("request.getContentType:"+request.getContentType());
	// // == request.getHeader("Content-Type")
	// // basic http Auth
	// System.out.println("request.getHeader(\"Authorization\"):"+request.getHeader("Authorization"));
	// // context encoding 用于本地解析
	// System.out.println("request.getCharacterEncoding():"+request.getCharacterEncoding());
	//
	// // 内容大小
	// System.out.println("getContentLength:"+request.getContentLength());//获取内容(body/entity)的大小
	// -1表示未知
	//
	// int entityLen=request.getContentLength();
	// if(entityLen>0 &&entityLen<20*1024){ //建议请求不要大于20KB
	// byte[] b=new byte[entityLen];
	// BufferedInputStream bis=new
	// BufferedInputStream(request.getInputStream()); //得到内容(body/entity)的输入流.
	// bis.read(b);
	// System.out.println(new String(b));
	// //
	// }
	//
	//
	// System.out.println("getServletContext().getRealPath(\"/\"):"+this.getServletContext().getRealPath("/"));
	// //获取/目录的绝对路径 就是本地文件系统路径(应用本地path)
	//
	//
	//
	//
	// // response.setContentType
	// // 设置发送到客户端的响应的内容类型,可以包括字符编码说明.
	// // 也就是说在服务器端坐了这个设置，那么他将在浏览器端起到作用，在你打开浏览器时决定编码方式
	// // 如果该方法在response.getWriter()被调用之前调用，那么响应的字符编码将仅从给出的内容类型中设置。该方法如果在
	// response.getWriter()被调用之后或者在被提交之后调用，将不会设置响应的字符编码,在使用http协议的情况中，该方法设置
	// Content-type实体报头
	// //
	// // response.setCharacterEncoding设置响应的编码
	// // 如果服务器端使用response.setContentType设置了编码格式,
	// // 那么应该使用 response.setCharacterEncoding指定的编码格式，这样就会把之前的设置屏蔽掉
	// //
	// // 一般建议设置response.setCharacterEncoding
	// // response.setContentType 经常会遇到失效的情况(设置了 但是经常不起作用)
	//
	// request.setCharacterEncoding("utf-8"); //将客户端以utf-8方式解析 io输入
	// response.setCharacterEncoding("utf-8"); //返回时用utf-8作为字符集编码进行io输出
	// response.setContentType("text/html;charset=utf8"); // 要求浏览器用utf8
	// //字符编码配置建议放入filter中
	// org.springframework.web.filter.CharacterEncodingFilter
	// 只是设置request.setCharacterEncoding()
	//
	// response.getWriter().print("中文显示 haha!!"+(new Date()).getTime());
	//
	//
	// response.setStatus(HttpStatus.SC_ACCEPTED);
	// // response.getOutputStream();
	//
	// System.out.println("easyServlet done!!!");
}

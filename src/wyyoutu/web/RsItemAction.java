/**
 * 
 */
package wyyoutu.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import summ.framework.Paging;
import summ.framework.util.DateUtils;
import wyyoutu.model.RsItem;
import wyyoutu.service.RsItemService;

/**
 * @author wfeng007
 * @date 2012-10-28 下午05:23:45
 */
public class RsItemAction /*extends BasicAction*/ { //struts2不继承actionsupport也可以使用 //默认形式使用类似activerecord的方式//适合直接使用activerecord的类型
	
	private static Logger logger = Logger.getLogger(RsItemAction.class);
	
	// for spring DI
	private RsItemService rsItemService;
	private Integer itemId;
	// 分页参数
	private Integer pageNum;
	private Integer numPerPage;
	// 文本
	private String text;
	
	private String owner;
	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return the pageNum
	 */
	public Integer getPageNum() {
		
		return pageNum;
	}

	/**
	 * @param pageNum the pageNum to set
	 */
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	/**
	 * @return the numPerPage
	 */
	public Integer getNumPerPage() {
		return numPerPage;
	}

	/**
	 * @param numPerPage the numPerPage to set
	 */
	public void setNumPerPage(Integer numPerPage) {
		this.numPerPage = numPerPage;
	}

	// 分页参数 end
	/**
	 * @return the itemId
	 */
	public Integer getItemId() {
		return itemId;
	}

	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

    /**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	// 该功能是手工用out.println实现的
    // 返回给前台的内容，其实给ui的是result.toString()返回的字符串,即response内容默认用result返回
	// 这个不用result也可，实现了默认方法。
    private JSONObject result;

    
    //  其中 pmSmSystemInfo是aciton名字   listSystemInfo是方法 act为指定后缀(act就是后缀而已)
	//	pm/pmSmSystemInfo!listSystemInfo.act
	public void listItem(){
		
		//分页参数
		int pn=1;
		int npp=10;
		
//		System.out.println(pageNum);
//		System.out.println(numPerPage);
		
		System.out.print(owner);
		
		if(pageNum!=null){
			pn=pageNum.intValue();
		}
		
		if(numPerPage!=null){
			npp=numPerPage.intValue();
		}
		Paging paging=new Paging();
		paging.setCountPerPage(npp);
		paging.setCurrentPagePosition(pn-1);
		System.out.println(paging);
		//
		
		try{
		System.out.println(this.toString());
		
		//seq_id倒序
		//分页查询  paraMap应该兼容null(修改代码生成)
		Map<String,Object> paraMap=new HashMap<String, Object>();
		paraMap.put("ownerId", owner); //用户账号作为item拥有者查询。
		paraMap.put("orderBy", "`seq_id`  DESC ");
		
		
		//plugin钩子 aListItem
		//参数都是使用req传递
		HttpServletRequest req=	ServletActionContext.getRequest();
		HttpServletResponse resp=	ServletActionContext.getResponse();
		//参数
		req.setAttribute(WebPlugin.DATAKEY+".paraMap", paraMap);
		WebPlugin.doHandle("aPreListItem", req, resp);
		//
		
		
		
		List<RsItem> ls=this.rsItemService.listItem(paraMap, paging);
		
		//
		// TODO 这部分到底是界面逻辑还是后台service逻辑呢？
		//
		//如果url内容为#说明为使用本地item二进制数据作为url内容
		//使用应用默认的image访问配置
		//
		// 为了胜利，考虑在形成json对象后统一处理
//		for(RsItem item:ls){
//			if(item.getUrl()==null||"#".equalsIgnoreCase(item.getUrl())||"".equalsIgnoreCase(item.getUrl())){
//				item.setUrl("./svc/image?itemId="+item.getSeqId()); //这个路径已经配置化 TODO 需要提供两个不同的路径分别访问缩略图以及原始图
//			}
//		}
		
		//FIXME 临时解决办法后续需要后台改成每次创建就提供数据
		for(RsItem item:ls){
			if(item.getAddTs()==null){
				item.setAddTs(DateUtils.toDateTime("2013-01-01 08:00:00"));
			}
		}
		
		//替换界面显示内容结束
		
		
		// structure
		Map<String, Object> map = new HashMap<String, Object>(0);
		map.put("rows", ls); //一个固定结构
		//
		
		
		// 构建json result //JSONObject会把Bean中字符串数组的null写成"" 而如果是Map则不会，需要注意这个区别。
		this.result=JSONObject.fromObject(map);
		
		//解析内容进行一些业务上的修改。
		JSONArray jsonRows= (JSONArray)this.result.get("rows");
		for (Object	obj : jsonRows) {
			JSONObject jobj=(JSONObject)obj;
			if(jobj.get("url")==null||"#".equalsIgnoreCase(jobj.getString("url"))||"".equalsIgnoreCase(jobj.getString("url"))){
				jobj.put("url", "./svc/image?itemId="+jobj.get("seqId"));
				jobj.put("thumbnailUrl", "./svc/image?itemId="+jobj.get("seqId")+"&tq=75");  //对于只有url的而没有内容
			}else{
				jobj.put("thumbnailUrl", jobj.getString("url"));
			}
		}
		//
		
		//
//		System.out.println(this.result);
		
		ServletActionContext.getResponse().setContentType("application/json");// 设置http头部为json
		
		//输出
		PrintWriter out=null;
		try {
			out = ServletActionContext.getResponse().getWriter();
			out.print(result); //写的其实是内容response 
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			if(out!=null)
				out.close();
		}
		
		}
		catch(Throwable th){
			th.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 */
	public void getItemById(){
		System.out.println(this.toString());
		System.out.println(itemId);
		if(this.itemId!=null){
			RsItem ri=this.rsItemService.getItemById(this.itemId);
			
			//处理内容
			//FIXME 临时解决办法后续需要后台改成每次创建就提供数据
			if(ri.getAddTs()==null){
				ri.setAddTs(DateUtils.toDateTime("2013-01-01 08:00:00")); //这个路径已经配置化 TODO 需要提供两个不同的路径分别访问缩略图以及原始图
				System.out.println(ri.getAddTs());
			}
			//处理内容结束
			
			
			//
			Map<String, Object> map = new HashMap<String, Object>(0);
			map.put("item", ri);
			// 构建json result
			this.result=JSONObject.fromObject(map);

//			System.out.println(this.result);
			
			ServletActionContext.getResponse().setContentType("application/json");// 设置http头部为json
			//输出
			PrintWriter out=null;
			try {
				out = ServletActionContext.getResponse().getWriter();
				out.print(result); //写的其实是内容response 
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally{
				if(out!=null)
					out.close();
			}
		}
		
	}
	
	/**
	 * 修改项目文本内容
	 */
	public void modifyText(){
		if(this.itemId!=null){
			int uc=this.rsItemService.modifyItemText(this.itemId,this.text);
			// 
			//
			Map<String, Object> map = new HashMap<String, Object>(0);
			if(uc!=1){
				map.put("success", false);
				map.put("msg", "nothing!");
			}else{
				map.put("success", true);
				map.put("msg", "update ok!");
			}
			//
			
			// 构建json result
			this.result=JSONObject.fromObject(map);
			ServletActionContext.getResponse().setContentType("application/json");// 设置http头部为json
			//输出
			PrintWriter out=null;
			try {
				out = ServletActionContext.getResponse().getWriter();
				out.print(result); //写的其实是内容response 
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally{
				if(out!=null)
					out.close();
			}
		}
		
	}
	
	/**
	 * 增加 item
	 */
	public void addItem(){
		logger.info("addItem(),text.length:"+((this.text==null)?null:this.text.length()));
		
		//
		System.out.println(this.text);
		
		this.rsItemService.addItem(text);
		
		Map<String, Object> map = new HashMap<String, Object>(0);
		map.put("msg", "OK!");
		map.put("success", true);
		
		this.result=JSONObject.fromObject(map);
		
		//
		ServletActionContext.getResponse().setContentType("application/json");// 设置http头部为json
		//输出
		PrintWriter out=null;
		try {
			out = ServletActionContext.getResponse().getWriter();
			out.print(result); //写的其实是内容response 
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			if(out!=null)
				out.close();
		}
	}
	
	public void removeItem(){
		System.out.println(this.toString());
		System.out.println(itemId);
		if(this.itemId!=null){
			//
			this.rsItemService.removeItem(this.itemId);
			
			//
			Map<String, Object> map = new HashMap<String, Object>(0);
			map.put("success", true);
			map.put("msg", "remove ok!");
			// 构建json result
			this.result=JSONObject.fromObject(map);

//			System.out.println(this.result);
			
			ServletActionContext.getResponse().setContentType("application/json");// 设置http头部为json
			//输出
			PrintWriter out=null;
			try {
				out = ServletActionContext.getResponse().getWriter();
				out.print(result); //写的其实是内容response 
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally{
				if(out!=null)
					out.close();
			}
		}
	}
	
//	/*
//	 * 其中 pmSmSystemInfo是aciton名字中间不带方法  act为指定后缀(act就是后缀而已)
//	 * pm/pmSmSystemInfo.act
//	 * @see com.opensymphony.xwork2.ActionSupport#execute()
//	 */
//	@Override
//	public String execute() throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}
    
	/**
	 * @return the rsItemService
	 */
	public RsItemService getRsItemService() {
		return rsItemService;
	}

	/**
	 * @param rsItemService the rsItemService to set
	 */
	public void setRsItemService(RsItemService rsItemService) {
		this.rsItemService = rsItemService;
	}

	/**
	 * @return the result
	 */
	public JSONObject getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(JSONObject result) {
		this.result = result;
	}
    
}

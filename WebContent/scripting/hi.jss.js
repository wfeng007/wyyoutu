
importClass(java.lang.System);
sysout=java.lang.System.out;
importPackage(java.lang);
importPackage(java.util);
importPackage(net.sf.json);
//importClass(Packages.summ.framework.Paging);
importPackage(Packages.summ.framework);//org net com java javax意外的需要加上Packages.前缀
importPackage(Packages.summ.framework.web);
importPackage(Packages.summ.framework.util);

var pn=1;
var npp=10;

//System.out.println(pageNum);
//System.out.println(numPerPage);
//		if(pageNum!=null){
//			pn=pageNum.intValue();
//		}
//		
//		if(numPerPage!=null){
//			npp=numPerPage.intValue();
//		}

var paging=Paging(); //new 与 没有都可以
//paging.setCountPerPage(10);
paging.countPerPage=npp; 
paging.setCurrentPagePosition(pn-1);
System.out.println(paging);

//for(var wfwfw in paging){
//	println(wfwfw);
//	println(paging[wfwfw]);
//}
	
		
try{
	var paraMap=new HashMap();
	paraMap.put("orderBy", "`seq_id`  DESC ");
	
	//执行
	var springAppContext=SpringContextHolder.getApplicationContext();
	var ls=springAppContext.getBean("rsItemService").listItem(paraMap, paging);
	
	// structure
	var map = new HashMap(0);
	map.put("rows", ls); //一个固定结构
	
	var result=JSONObject.fromObject(map);
	
	response.setContentType("application/json");
	
	out.print(result);
	out.flush();
	//
}catch(e if e.javaException instanceof java.lang.ClassNotFoundException){
	//多个exception处理
	
}catch (e) {
	
//	 println("fileName:"+e.fileName);
//	 println("message:"+e.message);
//	 println("name:"+e.name);
//	 println("lineNumber:"+e.lineNumber);
	 if(e.rhinoException !=null){
		 e.rhinoException.printStackTrace();
	 }
	 
//	 throw e.message; //只能抛出 文本
	 
//	 if(e.javaException instanceof java.lang.Throwable){
//		 e.javaException.printStackTrace();
//	 }
	//println(e);
}



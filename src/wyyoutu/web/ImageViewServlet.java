/**
 * 
 */
package wyyoutu.web;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import summ.framework.SpringContextHolder;
import wyyoutu.service.RsItemService;

/**
 * 将后二进制数据当做图片流展示到界面
 * 展示图片
 * @author wfeng007
 * @date 2012-11-25 下午09:10:05
 */
public class ImageViewServlet extends HttpServlet {
	 private static final long serialVersionUID = 1L;  
	    @Override  
	    protected void doGet(HttpServletRequest req, HttpServletResponse resp)  
	            throws ServletException, IOException {  
	        this.doPost(req, resp);  
	    }  
	    @Override  
	    protected void doPost(HttpServletRequest req, HttpServletResponse resp)  
	            throws ServletException, IOException {  
	        try {  
//	            FileInputStream in = ImageUtil.getByteImage("D://me.jpg");  
//	            Blob blob = Hibernate.createBlob(in);  
//	            InputStream inputStream = blob.getBinaryStream();// IO流  
//	            int length = (int) blob.length();  
//	            byte[] b = new byte[length];  
//	            inputStream.read(b, 0, length);  
//	            PrintWriter out = resp.getWriter();  
//	            InputStream is = new ByteArrayInputStream(b);  
//	            int a = is.read();  
//	            while (a != -1) {  
//	                out.print((char) a);  
//	                a = is.read();  
//	            }  
	        	
	        	//参数
	        	String itemId=req.getParameter("itemId");
	        	System.out.println("itemId:"+itemId);
	        	//id参数检测 
	        	int id=-1;
		    	try { 
		    		if(itemId==null){
		    			resp.setStatus(404);
		    			return ;
		    		}
					id= Integer.valueOf(itemId);// 把字符串强制转换为数字
				} catch (NumberFormatException e) {
					resp.sendError(404, "NumberFormatException:"+e.getMessage());
					e.printStackTrace();
	    			return ;
				}
				
				String thumbnailQualify=req.getParameter("tq");//只要不为null就是缩略图。TODO 考虑在第一次访问的时候生成缩略图，而不是插入时。用一个状态版本控制缩略图版本。
				System.out.println("thumbnailQualify:"+thumbnailQualify);
				
				
				
	        	resp.setContentType("image/jpeg");
	        	OutputStream out=resp.getOutputStream(); //浏览器连接过来的out
	        	
	        	RsItemService rsItemService=(RsItemService)SpringContextHolder.getApplicationContext().getBean("rsItemService");
	        	//
	        	if(thumbnailQualify!=null){
					//缩略图展示
	        		rsItemService.loadItemThumbnailWithSteam(out, id, 0);
				}else{
					//原图展示
					rsItemService.loadItemBinaryWithSteam(out,id, 0); //
				}
	    		
	            out.flush();  
	            out.close();   // serlvt也可以容器来关闭？  浏览器可能会复用 //
	            /*OutputStream outputStream = resp.getOutputStream();// 从response中获取getOutputStream 
	            outputStream.write(b);// 写 
	            inputStream.close(); 
	            outputStream.close();*/  
	        } catch (Exception e) {  
	            System.out.println("error");  
	        }  
	    }  
	    
	    private Integer toInteger(String strNum){
	    	if(strNum==null)return null;
	    	try { 
				return Integer.valueOf(strNum);// 把字符串强制转换为数字
			} catch (Exception e) {
				return null;// 如果抛出异常，返回False
			}
	    }

}

/**
 * 
 */
package summ.framework.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

/**
 * 简易的上传到指定目录的上传实现
 * 可以作为一个例子只用。
 * @author wfeng007
 * @date 2012-11-25 下午04:08:48
 */
public class UploadFileSimpleServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		return;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		//
		// 判断
		if (!ServletFileUpload.isMultipartContent(request)) {
			throw new IllegalArgumentException("upload file content-type start with which must be:"
					+ ServletFileUpload.MULTIPART);
		}

		//
		// 为该请求创建一个DiskFileItemFactory对象，通过它来配置环境、以及生成FileItem的逻辑。
		//
		//
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 设置参数
		{
			// 设定存放临时文件的地方默认 System.getProperty("java.io.tmpdir")
			// factory.setRepository(new File("../webapps/fileupload/tmp"));
			// 每个FileItem最大内存最大占用 如果超过这个值则写入 临时文件目录
			factory.setSizeThreshold(1024000);
			
			//清理临时文件可以用 清理器
//			factory.setFileCleaningTracker();
			
		}

		//ServletFileUpload 处理一个请求
		// 上传处理引擎 使用磁盘上传项 核心类ServletFileUpload
		ServletFileUpload uploader = new ServletFileUpload(factory);
		{
			//单个文件最大值byte
			uploader.setFileSizeMax(1024*500);
			//一起请求所有上传文件的总和最大值byte  
			uploader.setSizeMax(1024*500*3);
		}

		// 表单中的所有项
		// 执行解析后，所有的表单项目都保存在一个List中。
		List<FileItem> list = null;
		try {
			list = (List<FileItem>)uploader.parseRequest(request); // 核心访问将http中form的信息以及文件流封装成fileitem对象。
			
		} catch (SizeLimitExceededException e) {  //超过大小限制则抛出异常
            System.out.println("size limit exception!"); 
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		// 核心类FileItem
		for (FileItem item : list) {
			try {
				
				if (item.isFormField()) { //form中的普通input
					// 处理普通表单域
					String field = item.getFieldName();// 表单域名
					String value = item.getString("utf-8");
					System.out.println("text:" + field + " value:" + value);
					// ....
				} else {

					// 将临时文件保存到指定目录
					String fileName = item.getName();// 文件名称 //如果界面表单没有填则为空字符串
					if(fileName==null|| "".equals(fileName)){
						continue;
					}
					String filepath = "d:/temp/" + fileName;
					System.out.println("fileName:" + fileName);
					//简单写到一个文件
//					try {
//						item.write(new File(filepath));
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}// 执行保存
					
					//流方法写
					BufferedInputStream in = new BufferedInputStream(item.getInputStream());  
                    //文件存储在工程的upload目录下,这个目录也得存在  
                    BufferedOutputStream out = new BufferedOutputStream(  
                            new FileOutputStream(new File(filepath))); 
                    
                    
                    // 将in流写到out中 in一定会被关闭 out当pClose为true时关闭
                    Streams.copy(in, out, true);  

				}
			} finally {
				System.out.println("del");
				//必须删除 不用的临时数据
				if(item!=null)item.delete();
			}
		}
		
	}

}

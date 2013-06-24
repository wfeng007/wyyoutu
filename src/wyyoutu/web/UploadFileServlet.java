/**
 * 
 */
package wyyoutu.web;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.FileItem;

import summ.framework.SpringContextHolder;
import wyyoutu.service.RsItemService;

/**
 * @author wfeng007
 * @date 2012-11-25 下午06:17:46
 */
public class UploadFileServlet extends summ.framework.web.UploadFileServlet {

	/**
	 * 
	 * @param in
	 * @return 是否需要调用者关闭 input
	 * @throws IOException 
	 */
	@Override
	protected boolean processInput(InputStream in,String originalFilename,FileItem fileItem,AccountInfo accountInfo) throws IOException{
		in.available();

		RsItemService rsItemService=(RsItemService)SpringContextHolder.getApplicationContext().getBean("rsItemService");
//		rsItemService.modifyItemAtBinaryWithStream(new BufferedInputStream(in), originalFilename, 1,fileItem.getSize()); //
		if(accountInfo==null){
			return false;
		}
		rsItemService.addItemAtBinaryWithStream(new BufferedInputStream(in), originalFilename,accountInfo.getUserId());
		return true;
		
	}
	
}

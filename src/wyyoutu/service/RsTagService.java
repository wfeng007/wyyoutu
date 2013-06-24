/**
 * 
 */
package wyyoutu.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

//import jodd.jerry.Jerry;

import org.apache.commons.fileupload.util.Streams;

import summ.framework.Paging;
import summ.framework.util.BasicImageUtils;
import wyyoutu.dao.RsItemDao;
import wyyoutu.dao.RsTaggedDao;
import wyyoutu.model.RsItem;
import wyyoutu.model.RsTagged;

/**
 * @author wfeng007
 * @date 2012-10-28 下午05:17:31
 */
public class RsTagService {
	private RsTaggedDao rsTaggedDao;
	
	
	//add service
	//列出所有可用tag
	//

	/**
	 * @return the rsTaggedDao
	 */
	public RsTaggedDao getRsTaggedDao() {
		return rsTaggedDao;
	}

	/**
	 * @param rsTaggedDao the rsTaggedDao to set
	 */
	public void setRsTaggedDao(RsTaggedDao rsTaggedDao) {
		this.rsTaggedDao = rsTaggedDao;
	}
	
}

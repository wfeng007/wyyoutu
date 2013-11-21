/**
 * 
 */
package wyyoutu.service;

import java.awt.image.BufferedImage;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

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
public class RsItemService {
	private RsItemDao rsItemDao;
	private RsTaggedDao rsTaggedDao;

	/**
	 * 
	 * @param mp
	 * @param paging
	 * @return
	 */
	public void removeItem(int seqId) {
		HashMap<String,Object> pm=new HashMap<String,Object>();
		pm.put("seqId", seqId);
		int deleteC=this.rsItemDao.delete(pm); //TODO 代码生成器应该生成bypk
		System.out.println("delete count:"+deleteC);
	}
	
	//stutas值 其他状态默认为私有状态，其实所有状态owner都可以查看到。 审核者可以看到申请状态。
//	final static String ITEM_STUTAS_PUBLISHED="PUBLISHED";
//	final static String ITEM_STUTAS_PRIVATE="PRIVATE";
//	final static String ITEM_STUTAS_DELETED="DELETED";
//	final static String ITEM_STUTAS_RESERVED="RESERVED";
	
	public final static int ITEM_STUTAS_PUBLISHED=1; //发布、公开
	public final static int ITEM_STUTAS_PRIVATE=0; //私有、隐藏
	public final static int ITEM_STUTAS_DELETED=2; //删除
	public final static int ITEM_STUTAS_RESERVED=3; //私有、预留
	public final static int ITEM_STUTAS_APPLYING_PUBLICATION=4; //申请发布状态
	
	//
	/**
	 * 增加关联查询即有其扩展属性的条件查询。
	 * 查询分页
	 * @param mp 其他参数。
	 * @param paging
	 * 
	 * @param isExten
	 * @return
	 */
	public List<RsItem> listItem(final Map<String,Object> mp,Paging paging,boolean isExten) {
		if(isExten && mp.containsKey("condition")){
//			System.out.println("*******************************condition:"+mp.get("condition"));
			mp.put("orderBy", "RS_ITEM.`seq_id`  DESC ");
			return this.rsItemDao.queryConditionAndExten(mp, paging);
		}else{
//			System.out.println("no condition");
			return this.rsItemDao.query(mp, paging);
		}
	}
	
	
	/**
	 * 查询所有内容
	 * @return
	 */
	public List<RsItem> listItem() {
		List<RsItem> reLs=this.rsItemDao.query((RsItem)null);
		System.out.println(reLs);
		return reLs;
	}

	
	/**
	 * 
	 * @param seqId
	 * @return
	 */
	public RsItem getItemById(int seqId){
		return this.rsItemDao.getByPk(seqId);
	}
	
	/**
	 * 更新某个item的所有tags，整体替换。
	 * @param tagList
	 * @return
	 */
	public int replaceItemTags(List<String> tagList,String itemId){
		if(itemId==null){
			return 0;
		}
		
		this.rsTaggedDao.deleteByTargetId(itemId);
		
		int retC=0;
		for (String tagId : tagList) {
			RsTagged tag=new RsTagged();
//			Map<String,Object> mpi=new HashMap<String,Object>();
			tag.setTargetId(itemId);
			tag.setTagId(tagId);
			this.rsTaggedDao.insert(tag);
			retC++;
		}
		
		return retC;
	}
	
	/**
	 * 
	 * @param itemId
	 * @return
	 */
	public List<String> listTag(String itemId){
//		org.apache.commons.collections.
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("targetId",itemId);
		
		//将List<Map> 以及List<JavaBean>这种 取一列的功能单独领出来。 以后可以用lambda表达式来进行处理。
		List<String> retList=new ArrayList<String>();
		
		List<RsTagged> tL= this.rsTaggedDao.query(mp);
		if(tL==null)return retList;
		for (RsTagged rsTagged : tL) {
			retList.add(rsTagged.getTagId());
		}
		return retList;
	}
	
	
	public BeanInfo getInfo() throws IntrospectionException{
		return java.beans.Introspector.getBeanInfo(this.getClass());
	}
	
	/**
	 * 增加一条只有文本内容的
	 * @param text 文本内容
	 * @return
	 */
	public int addItem(String text){
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("name", text.substring(10));//TODO 过滤掉html 然后取值保存 ?
//		mp.put("url", fetchImgUrl(text)); //直接从text中获取img？ 可能暂时不需要
		mp.put("url", "#");
		mp.put("text", text);//TODO 过滤掉html 然后取值保存 ?
		return this.rsItemDao.insert(mp);
	}
	
	/**
	 * 修改特定item的文本内容
	 * @param itemId seqid
	 * @param text 文本内容
	 * @return 修改完成记录数
	 */
	public int modifyItemText(int itemId,String text){
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("seqId", itemId);
		mp.put("text", text);//TODO 过滤掉html 然后取值保存 ?
		return this.rsItemDao.update(mp);
	}
	
	
	/**
	 * 发布公开与私有之前切换
	 * @param itemId seqid
	 * @param orginStatus 原始状态//这样可能不好原始状态应该从数据库中取出
	 * @return newStatus
	 */
	public Integer publishToggle(int itemId,int orginStatus){
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("seqId", itemId);
		int newStatus;
		if(orginStatus==ITEM_STUTAS_PRIVATE){
			newStatus=ITEM_STUTAS_PUBLISHED;
			mp.put("status", newStatus);//
			
		}else if(orginStatus==ITEM_STUTAS_PUBLISHED){
			newStatus=ITEM_STUTAS_PRIVATE;
			mp.put("status", newStatus);//
		}
		else{
			return null;
		}
		int c=this.rsItemDao.update(mp);
		if(c!=1){
			return null;
		}
		return new Integer(newStatus);
	}
	
	/**
	 * 生成在表中唯一的IID（暂时使用UUID实现）
	 * @return IID
	 */
	private String generateIID(){
		return UUID.randomUUID().toString();
	}

	/**
	 * 
	 * 根据输入流增加一个item，该item的二进制字段被填充。
	 * 默认生成iid。
	 * 写入数据后生成缩略媒体。
	 * 
	 * @param in 从用户上传接口读取二进制数据的流
	 * @param originalFilename 文件原始名称
	 * @param ownerId 新增数据的拥有者
	 * @return 是否成功  TODO 考虑返回iid
	 */
	public boolean addItemAtBinaryWithStream(InputStream in, String originalFilename,String ownerId){	
		if(ownerId==null || "".equals(ownerId)){
			throw new IllegalArgumentException("ownerId must be valid! ownerId:"+ownerId);
		}
		String iid=generateIID();
		boolean ok=false;
		ok=addItemAtBinaryWithStream(iid,in,originalFilename,ownerId); //被包装的写入。。
		if(!ok)return false;
		try{
			generateThumbnailByIID(iid); //生成缩略图
			return true; 
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * 根据输入流增加一个item，该item的二进制字段被填充。
	 * 
	 * @param iid itemId()
	 * @param in 从用户上传接口读取二进制数据的流
	 * @param originalFilename 文件原始名称
	 * @param ownerId 新增数据的拥有者
	 * @return 是否成功
	 */
	public boolean addItemAtBinaryWithStream(String iid,InputStream in, String originalFilename,String ownerId){	
		// return this.rsItemDao.insert(mp);insert into test(id,pic) values(?,?)
		String sql = "insert into RS_ITEM(`iid`,`name`,`url`,`add_ts`,`BINARY`,`owner_id`) values(?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		try {
//			con = this.rsItemDao.getSessionTemplate().getConnection();//这样是spring+mybatis维护的缓存connection 不应该关闭他
			// 这个是从源头得到datasource来获取底层connection的方法
			con = this.rsItemDao.getSessionTemplate().getConfiguration().getEnvironment().getDataSource().getConnection();
			// 处理事务
			// con.setAutoCommit(false);
			ps = con.prepareStatement(sql);
			ps.setString(1, iid);
//			ps.setBlob(1, in);
//			ps.setBinaryStream(1, in,(int)binarySize);
			ps.setString(2, "#"+originalFilename); //这种行为都是web层做的？
			ps.setString(3, "#");//这种行为都是web层做的？
			ps.setTimestamp(4, new Timestamp(System.currentTimeMillis())); //加入时间戳
			ps.setBinaryStream(5, in,in.available()); //mysql写法 mysql的驱动内部可能会进行循环 获取流数据
			ps.setString(6, ownerId); //用户
			
			int count = ps.executeUpdate(); 
			// con.commit();
			System.out.println("count:"+count);
			return true;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return false;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		} finally {
			try {
				if(ps!=null)ps.close();
				if(con!=null)con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 
	 * 针对特定Item项生成其Thumbnail（缩略图）
	 * 这个逻辑可以考虑使用数据库侧程序。
	 * 
	 */
	public void generateThumbnailByIID(String iid){
		
		String readSql = "SELECT rs_item.binary FROM rs_item WHERE rs_item.iid=?";
		String wirteSql = "UPDATE RS_ITEM SET RS_ITEM.thumbnail=? WHERE rs_item.iid=?";
		Connection conn = null;
		PreparedStatement readPs = null;
		PreparedStatement writePs = null;
		ResultSet rs=null;
		try {
//			con = this.rsItemDao.getSessionTemplate().getConnection();//这样是spring+mybatis维护的缓存connection 不应该关闭他
			// 这个是从源头得到datasource来获取底层connection的方法
			conn = this.rsItemDao.getSessionTemplate().getConfiguration().getEnvironment().getDataSource().getConnection();
			
			// 处理事务
			// con.setAutoCommit(false);
			readPs = conn.prepareStatement(readSql);
//			ps.setBlob(1, in);
//			ps.setBinaryStream(1, in,(int)binarySize);
//			ps.setBinaryStream(1, in,in.available()); //mysql写法
			readPs.setString(1, iid);
			rs= readPs.executeQuery();
			
			Blob blob =null;
			
			//应该只有一行
			while(rs.next()){
				blob=rs.getBlob(1);break; //只取一行
			}
			
			//FIXME 当二进制字段中没有数据时 blob其实就是null 20121215
			if(blob==null){
				//TODO 考虑向OUT输出个 显示错误的数据 //或保护输出任何行为
				throw new NullPointerException("Item binary is null");
			}
			
			//
			// 建立输出
			//
			writePs = conn.prepareStatement(wirteSql);
			
			//应该是个方法
			{
				InputStream itemBinIs =null;
				InputStream thumbnailIS =null;
				try{
				itemBinIs = blob.getBinaryStream();// IO流  
				
				int length = (int) blob.length(); 
//				Streams.copy(inputStream, out, false);
				
				//
				// 生成缩小版的Image
				//
				thumbnailIS=createThumbnailStream(itemBinIs,280);
				
				//
				writePs.setBinaryStream(1, thumbnailIS,thumbnailIS.available()); //mysql写法 mysql的驱动内部可能会进行循环 获取流数据
				
//				out.flush();
				
				}finally{
					try {
						if(itemBinIs!=null)itemBinIs.close();
						if(thumbnailIS!=null)thumbnailIS.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			writePs.setString(2, iid);
			int count = writePs.executeUpdate(); 
			// con.commit();
			System.out.println("count:"+count);
			// con.commit();
//			return true;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
//			return true;
		}catch (Exception e){
			e.printStackTrace();
//			return true;
		} finally {
			try {
				if(rs!=null)rs.close();
				if(writePs!=null)writePs.close();
				if(readPs!=null)readPs.close();
				if(conn!=null)conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * 一个生成缩略图的算法。当前只是根据指定宽度同比例缩小。TODO 还没有计算容量大小限制、高度限制、压缩质量限定、格式限定等功能呢。
	 * 
	 * mysql特殊 写入数据库也是用InputStream
	 * TODO 可以考虑使用FilterInputStream 来实现一个 ImageBufferedInputStream 完成本功能
	 * 
	 * @param is
	 * @param width
	 * @return  用于写入Mysql库的stream，PrepareStatement.setBinaryStream()。
	 * @throws IOException
	 */
	private InputStream createThumbnailStream(InputStream is,int targetWidth) throws IOException{
		if (targetWidth <= 10 || targetWidth >300) { //缩略图宽度不能大于300像素 //其实应该根据原有image来判断
            throw new IllegalArgumentException("targetWidth out-of-bounds!");
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		//
		// 根据比例缩小
		//
//		BasicImageUtils.reduceImageEqualProportionWithStream(is,baos,1.0f);
		// 其实应该根据源Image来判断目标宽度以缩小比例
		
    	//根据流自动生成image
		BufferedImage image = ImageIO.read(is);//会关闭is
		
		int srcWidth=image.getWidth();
		int srcHight=image.getHeight();
		if(srcWidth<=0){
			 throw new RuntimeException("SrcWidth out-of-bounds!");
		}
		if(targetWidth>=srcWidth){
			System.out.println("SrcImage is smaller.");
			
//			return is; //直接将原图作为缩略图。不能直接返回IS 因为 IS已经被读取过一次了
			targetWidth=srcWidth; //直接用原图的大小
		}
		//计算比例
		float radio=(float)((float)targetWidth)/((float)srcWidth);
		//
		// 
		BufferedImage newOne =BasicImageUtils.reduceImageEqualProportion(image,  radio);
		//
		BasicImageUtils.writeImageWithStream(baos,"jpg", newOne,  true, 0.75f);
		//
		//
		
		//
		byte[] data = baos.toByteArray();
		//
		System.out.println("Thumbnail Size:"+data.length);
		
		
		ByteArrayInputStream bais=new ByteArrayInputStream(data); //这样就从Image获取了InputStream
		return bais; 
	}

	
	/**
	 * 更新已有item的二进制数据 部分
	 * @return 是否需要上层关闭 in输入流
	 */
	public boolean modifyItemAtBinaryWithStream(InputStream in, String originalFilename,
			int seqId,long binarySize) {

		String sql = "UPDATE RS_ITEM SET RS_ITEM.binary=? WHERE seq_id=?";
		Connection con = null;
		PreparedStatement ps = null;
		try {
//			con = this.rsItemDao.getSessionTemplate().getConnection();//这样是spring+mybatis维护的缓存connection 不应该关闭他
			// 这个是从源头得到datasource来获取底层connection的方法
			con = this.rsItemDao.getSessionTemplate().getConfiguration().getEnvironment().getDataSource().getConnection();
			// 处理事务
			// con.setAutoCommit(false);
			ps = con.prepareStatement(sql);
//			ps.setBlob(1, in);
//			ps.setBinaryStream(1, in,(int)binarySize);
			ps.setBinaryStream(1, in,in.available()); //mysql写法 mysql的驱动内部可能会进行循环 获取流数据
			
			
			ps.setInt(2, seqId);
			int count = ps.executeUpdate(); 
			// con.commit();
			System.out.println("count:"+count);
			return true;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return true;
		} finally {
			try {
				if(ps!=null)ps.close();
				if(con!=null)con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 从后台获取二进制数据并写入 一个输出流
	 * @param out 用来输出二进制数据的输出流
	 * @param id 二进制数据的后台id
	 * @param type @TODO 暂时没用
	 */
	public void loadItemBinaryWithSteam(OutputStream out,int id,int type) { 
		String sql = "SELECT rs_item.binary FROM rs_item WHERE rs_item.seq_id=?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		try {
//			con = this.rsItemDao.getSessionTemplate().getConnection();//这样是spring+mybatis维护的缓存connection 不应该关闭他
			// 这个是从源头得到datasource来获取底层connection的方法
			con = this.rsItemDao.getSessionTemplate().getConfiguration().getEnvironment().getDataSource().getConnection();
			// 处理事务
			// con.setAutoCommit(false);
			ps = con.prepareStatement(sql);
//			ps.setBlob(1, in);
//			ps.setBinaryStream(1, in,(int)binarySize);
//			ps.setBinaryStream(1, in,in.available()); //mysql写法
			ps.setInt(1, id);
			rs= ps.executeQuery();
			
			Blob blob =null;
			
			//应该只有一行
			while(rs.next()){
				blob=rs.getBlob(1);break; //只取一行
				
			}
			
			//FIXME 当二进制字段中没有数据时 blob其实就是null 20121215
			if(blob==null){
				//TODO 考虑向OUT输出个 显示错误的数据 //或保护输出任何行为
			}
			
			//应该是个方法
			{
				InputStream inputStream =null;
				try{
				inputStream = blob.getBinaryStream();// IO流  
				
				int length = (int) blob.length(); 
				Streams.copy(inputStream, out, false);
//				out.flush();
				
				}finally{
					if(inputStream!=null)inputStream.close();
				}
			}
			
			// con.commit();
//			return true;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
//			return true;
		}catch (Exception e){
			e.printStackTrace();
//			return true;
		} finally {
			try {
				if(rs!=null)rs.close();
				if(ps!=null)ps.close();
				if(con!=null)con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 从后台获取缩略二进制数据并写入 一个输出流
	 * TODO 几乎与loadItemBinaryWithSteam一模一样考虑合并代码。
	 * 
	 * @param out 用来输出二进制数据的输出流
	 * @param id 二进制数据的后台id
	 * @param type @TODO 暂时没用
	 */
	public void loadItemThumbnailWithSteam(OutputStream out,int id,int type) { 
		String sql = "SELECT rs_item.thumbnail FROM rs_item WHERE rs_item.seq_id=?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		try {
//			con = this.rsItemDao.getSessionTemplate().getConnection();//这样是spring+mybatis维护的缓存connection 不应该关闭他
			// 这个是从源头得到datasource来获取底层connection的方法
			con = this.rsItemDao.getSessionTemplate().getConfiguration().getEnvironment().getDataSource().getConnection();
			// 处理事务
			// con.setAutoCommit(false);
			ps = con.prepareStatement(sql);
//			ps.setBlob(1, in);
//			ps.setBinaryStream(1, in,(int)binarySize);
//			ps.setBinaryStream(1, in,in.available()); //mysql写法
			ps.setInt(1, id);
			rs= ps.executeQuery();
			
			Blob blob =null;
			
			//应该只有一行
			while(rs.next()){
				blob=rs.getBlob(1);break; //只取一行
				
			}
			
			//FIXME 当二进制字段中没有数据时 blob其实就是null 20121215
			if(blob==null){
				//TODO 考虑向OUT输出个 显示错误的数据 //或保护输出任何行为
				
				return;//直接返回。不输出任何东西
			}
			
			//应该是个方法
			{
				InputStream inputStream =null;
				try{
				inputStream = blob.getBinaryStream();// IO流  
				
				int length = (int) blob.length(); 
				Streams.copy(inputStream, out, false);
//				out.flush();
				
				}finally{
					if(inputStream!=null)inputStream.close();
				}
			}
			
			// con.commit();
//			return true;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
//			return true;
		}catch (Exception e){
			e.printStackTrace();
//			return true;
		} finally {
			try {
				if(rs!=null)rs.close();
				if(ps!=null)ps.close();
				if(con!=null)con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

//	/**
//	 * FIXME 这个行为其实是web的行为 
//	 * 提取第一个img html 标签指向的url 
//	 * @deprecated 该方法暂时不需要 可能可以用在其他地方
//	 * jerry实现
//	 * @param txt
//	 * @return #或路径
//	 */
//	private String fetchImgUrl(String txt){		
//		Jerry doc = Jerry.jerry(txt);
//		String url=doc.$("img").first().attr("src");
//		return (url==null||url=="")?"#":url;
//	}
	
	
	/**
	 * 
	 */
	public void addTagForItem(String itemId,String tagId){
		throw new UnsupportedOperationException();
//		return ;
	}
	
	
	/**
	 * @return the rsItemDao
	 */
	public RsItemDao getRsItemDao() {
		return rsItemDao;
	}

	/**
	 * @param rsItemDao the rsItemDao to set
	 */
	public void setRsItemDao(RsItemDao rsItemDao) {
		this.rsItemDao = rsItemDao;
	}
	
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

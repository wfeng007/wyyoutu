/**
 * 
 */
package wyyoutu.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import wyyoutu.dao.CoPeopleDao;
import wyyoutu.dao.CoPeopleExtenDao;
import wyyoutu.model.CoPeople;
import wyyoutu.model.CoPeopleExten;

/**
 * @author wfeng007
 * @date 2013-10-19 下午03:36:58
 */
public class CoService {
	
	private static Logger logger = Logger.getLogger(CoService.class);
	
	private CoPeopleDao coPeopleDao;
	private CoPeopleExtenDao coPeopleExtenDao;
	private LoginService loginService;
	
	//临时权限数据
	//permission
	static String[] permissions=new String[]{
		"read"
		,"edit_posts"
		,"delete_posts"
		,"edit_others_posts"
		,"delete_others_pages"
//		,"switch_themes"
//		,"edit_themes"
//		,"activate_plugins"
//		,"edit_plugins"
//		,"edit_users"
//		,"edit_files"
//		,"manage_options"
//		,"moderate_comments"
//		,"manage_categories"
//		,"manage_links"
//		,"upload_files"
//		,"import"
//		,"unfiltered_html"
//		,"edit_published_posts"
//		,"publish_posts"
//		,"edit_pages"
//		,"edit_others_pages"
//		,"edit_published_pages"
//		,"edit_published_pages"
//		,"delete_pages"
//		,"delete_published_pages"
//		,"delete_others_posts"
//		,"delete_published_posts"
//		,"delete_private_posts"
//		,"edit_private_posts"
//		,"read_private_posts"
//		,"delete_private_pages"
//		,"edit_private_pages"
//		,"read_private_pages"
//		,"delete_users"
//		,"create_users"
//		,"unfiltered_upload"
//		,"edit_dashboard"
//		,"update_plugins"
//		,"delete_plugins"
	};
	
	//roles
//	static String[] roles=new String[]{
//		"Administrator",
//		"Editor",
//		"Author",
//		"Contributor"};
	
	
	/**
	 * FIXME 权限返回应该用set而不是list？
	 */
	//roles-permission
	static Map<String,Set<String>> RPMapping=new HashMap<String,Set<String>>();
	static{
		Set<String> p=new HashSet<String>();
		p.add("read");
		p.add("edit_posts");
		p.add("delete_posts");
		p.add("edit_others_posts");
		p.add("delete_others_pages");
		RPMapping.put("Administrator", p);
		
		p=new HashSet<String>();
		p.add("read");
		p.add("edit_posts");
		p.add("delete_posts");
		RPMapping.put("Author", p);
	}
	
	
	//
	// 权限管理
	//
	/**
	 * 
	 * @param peopleId
	 * @return
	 */
	public List<String> listPermIdByPeopleId(String peopleId){
		HashMap<String,Object> mp=new HashMap<String, Object>();
		mp.put("peopleId", peopleId);
		mp.put("permission", peopleId);
		List<CoPeopleExten> perms=this.coPeopleExtenDao.query(mp);
		
		List<String> reLs=new ArrayList<String>();
		for (CoPeopleExten ext : perms) {
			reLs.add(ext.getExtenValue());
		}
		return reLs;
	}
	
	/**
	 * 
	 * @param peopleId
	 * @return
	 */
	public List<String> listRoleIdByPeopleId(String peopleId){
		HashMap<String,Object> mp=new HashMap<String, Object>();
		mp.put("peopleId", peopleId);
		mp.put("role", peopleId);
		List<CoPeopleExten> perms=this.coPeopleExtenDao.query(mp);
		List<String> reLs=new ArrayList<String>();
		for (CoPeopleExten ext : perms) {
			reLs.add(ext.getExtenValue());
		}
		return reLs;
	}
	
	/**
	 * 
	 * @param roleId
	 * @return 没有该角色则返回null
	 */
	public List<String> listPermIdByRoleId(String roleId){
		List<String> reLs=new ArrayList<String>();
		if(RPMapping.get(roleId)!=null)
			reLs.addAll(RPMapping.get(roleId));
		return reLs;
	}
	
	/**
	 * 
	 * 根据用户id获取所有role对应的以及直接的permission权限。
	 * 
	 * @param peopleId
	 * @return
	 */
	public List<String> listAllPermIdByPeopleId(String peopleId){
		List<String> roleLs=this.listRoleIdByPeopleId(peopleId);
		List<String> reLs=new ArrayList<String>();
		for (String roleId : roleLs) {
			reLs.addAll(this.listPermIdByRoleId(roleId));
		}
		reLs.addAll(this.listPermIdByPeopleId(peopleId));
		return reLs;
	}
	
	//
	// 用户管理
	//
	
	/**
	 * 修改用户基本内容，根据id修改其他内容。
	 * 部分内容屏蔽不进行修改。
	 * @return
	 */
	public boolean modifyPeopleBaisc(CoPeople peopleInfo){
		//一下几项不能修改。
		peopleInfo.setSeqId(null);
		peopleInfo.setAddTs(null);
		peopleInfo.setPassword(null);
		//
		return this.coPeopleDao.update(peopleInfo) > 0?true:false;
	}
	
	/**
	 * 修改密码，输入为密码明文。
	 * 内部进行加密存储。
	 * @return
	 */
	public boolean modifyPassword(String userId,String curPassword,String newPassword){
		logger.debug("to modify pwd userId:"+userId+" curPassword:"+curPassword+" newPassword"+newPassword);
		if(userId==null||curPassword==null||newPassword==null)return false;
		
		//
		//检查现有密码
		CoPeople thisPeople=this.loginService.login(userId, curPassword);//是否考虑单向依赖而不是双向
		if(thisPeople==null){
			return false;
		}
		
		//一下几项不能修改。
		String newPwdCoded=encodePassword(newPassword);
		CoPeople peopleInfo =new CoPeople();
		if(newPwdCoded==null)return false;
		peopleInfo.setId(userId);
		peopleInfo.setPassword(newPwdCoded);
		//
		return this.coPeopleDao.update(peopleInfo) > 0?true:false;
	}
	
	//
	//
	//
	
	/**
	 * 计算出md5码
	 * 默认密码admin的md5密文为:21232f297a57a5a743894a0e4a801fc3 
	 * @param pwd 加密前密码	
	 * @return 加密后密码
	 */
	public String encodePassword(String pwd){
		try {
			return DigestUtils.md5Hex(pwd.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);//不可能出现
			//return null; 
		}
	}

	
	/**
	 * @return the coPeopleDao
	 */
	public CoPeopleDao getCoPeopleDao() {
		return coPeopleDao;
	}
	/**
	 * @param coPeopleDao the coPeopleDao to set
	 */
	public void setCoPeopleDao(CoPeopleDao coPeopleDao) {
		this.coPeopleDao = coPeopleDao;
	}

	/**
	 * @return the coPeopleExtenDao
	 */
	public CoPeopleExtenDao getCoPeopleExtenDao() {
		return coPeopleExtenDao;
	}

	/**
	 * @param coPeopleExtenDao the coPeopleExtenDao to set
	 */
	public void setCoPeopleExtenDao(CoPeopleExtenDao coPeopleExtenDao) {
		this.coPeopleExtenDao = coPeopleExtenDao;
	}
	
	
	public static void main(String[] args) {
		System.out.println(new CoService().encodePassword("admin"));
	}

	/**
	 * @return the loginService
	 */
	public LoginService getLoginService() {
		return loginService;
	}

	/**
	 * @param loginService the loginService to set
	 */
	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}
	
	
}

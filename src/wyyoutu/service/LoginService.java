/**
 * 
 */
package wyyoutu.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import wyyoutu.dao.CoPeopleDao;
import wyyoutu.model.CoPeople;
import wyyoutu.web.SessionAction;

/**
 * 人员信息或账户的相关服务。当前包括登录等服务功能。
 * @author wfeng007
 */
public class LoginService {
	private static Logger logger = Logger.getLogger(LoginService.class);
	private CoPeopleDao coPeopleDao;
	private CoService coService;
	
	/**
	 * 登录失败返回null。
	 * 注意密码是明文。
	 * @param id 账号
	 * @param password 密码 
	 * @return 登录成功返回用户对象，登录不成功 则返回null密码账号为空串或null时返回null。
	 */
	public CoPeople login(String id,String password){
		// FIXME 需要过滤id特殊字符比如'单引号等? 
		if(id==null || "".equals(id) ||  password==null || "".equals(password) ){
			//id 以及 password都不能为空或空字符串 //FIXME 考虑抛出异常
			return null;
		}
		HashMap<String,Object> mp=new HashMap<String, Object>();
		mp.put("id", id);
		mp.put("password", this.coService.encodePassword(password));
		List<CoPeople> ls=this.coPeopleDao.query(mp); //FIXME 不能直接用query 因为password=null或""的时候会出现 查询问题。
		if(ls==null || ls.size()!=1 ){
			return null;
		}else{
			return ls.get(0); //只有一个 取第一个
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
	 * @return the coService
	 */
	public CoService getCoService() {
		return coService;
	}


	/**
	 * @param coService the coService to set
	 */
	public void setCoService(CoService coService) {
		this.coService = coService;
	}
	

	
	
	
}

/**
 * 
 */
package wyyoutu.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import wyyoutu.dao.CoPeopleDao;
import wyyoutu.model.CoPeople;

/**
 * @author wfeng007
 */
public class LoginService {
	
	private CoPeopleDao coPeopleDao;
	
	/**
	 * 登录失败返回null
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
		mp.put("password", encodePassword(password));
		List<CoPeople> ls=this.coPeopleDao.query(mp); //FIXME 不能直接用query 因为password=null或""的时候会出现 查询问题。
		if(ls==null || ls.size()!=1 ){
			return null;
		}else{
			return ls.get(0); //只有一个则去第一个
		}
	}
	
	/**
	 * 计算出md5码
	 * 默认密码admin的md5密文为:21232f297a57a5a743894a0e4a801fc3 
	 * @param pwd 加密前密码	
	 * @return 加密后密码
	 */
	private String encodePassword(String pwd){
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
	
	public static void main(String[] args) {
		System.out.println(new LoginService().encodePassword("admin"));
	}
	
	

}

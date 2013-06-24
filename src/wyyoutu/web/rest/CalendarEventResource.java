/**
 * 
 */
package wyyoutu.web.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

/**
 * @author wfeng007
 * @date 2012-6-3 上午11:11:29
 */
@Path("/event")
public class CalendarEventResource {
	private static Logger logger = Logger.getLogger(CalendarEventResource.class);
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public String lastEvents(@Context HttpServletRequest request){
		JSONObject json=new JSONObject();
		JSONArray jsonarray=new JSONArray();
		Map<String, Object> e1=new HashMap<String, Object>();
		e1.put("title", "事件1");
		e1.put("start", "2012-06-03");
		e1.put("end", "2012-06-03");
		e1.put("url", "www.163.com");
		e1.put("mydata", "wfwfwfwfwf");
		e1.put("color","#ff9911");
		
		Map<String, Object> e2=new HashMap<String, Object>();
		e2.put("title", "事件2");
		e2.put("start", "");
		e2.put("end","");
		e2.put("allDay", false);
		e2.put("url", "www.163.com");
		e2.put("mydata", "2");
		
		jsonarray.add(e1);
		jsonarray.add(e2);
		
//		json.put("root", map);
		
		return jsonarray.toString();
	}

}

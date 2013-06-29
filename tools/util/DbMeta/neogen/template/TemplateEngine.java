/**
 * 
 */
package util.DbMeta.neogen.template;

import java.util.Map;

/**
 * @author wfeng007
 * @date 2011-9-28 下午02:32:13
 *
 */
public interface TemplateEngine {
	
	/**
	 * 模板执行合并行为生成需要的内容
	 * @param context
	 */
	public void run(Map<String,Object> context)throws Exception;

}

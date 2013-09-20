/**
 * 
 */
package summ.framework.scripting.ringo;

import java.util.List;

import org.ringojs.engine.RhinoEngine;
import org.ringojs.engine.RingoWorker;
import org.ringojs.engine.ScriptError;
import org.ringojs.tools.RingoRunner;

/**
 * 引用全局唯一的一个ringoengine。spring或web 容器外单独保存。
 * 
 * 注意必须在初始化之后才能时候内部engine。否则npe错误。
 * 似乎只是在servlet中初始化，ringoEngine。
 * 
 * @author wfeng007
 * @date 2013-9-20 下午04:35:14
 */
public class RingoEngineHolder {
	static private RhinoEngine ringoEngine;

	/**
	 * @return the ringoEngine
	 */
	public static RhinoEngine getRingoEngine() {
		return ringoEngine;
	}

	/**
	 * @param ringoEngine the ringoEngine to set
	 */
	public static void setRingoEngine(RhinoEngine ringoEngine) {
		RingoEngineHolder.ringoEngine = ringoEngine;
	}
	
	/**
	 * 调用某模块中的顶层函数。注意不是exports的函数。
	 * @param module
	 * @param function
	 * @param args
	 * @return
	 */
	public static Object invoke(Object module, Object function, Object... args){
		//调用某个模块文件中的全局函数，注意：这里foo不是exports出来的函数引用，而是模块顶层的函数。
		RingoWorker worker = ringoEngine.getWorker();
		try {
			return worker.invoke(module,function,args); 
		} catch (Exception x) {
			List<ScriptError> errors = worker.getErrors();
			RingoRunner.reportError(x, System.err, errors, false);
			throw new RuntimeException(x);
		}
		finally{
			worker.release();
		}
	}
}

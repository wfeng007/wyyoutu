/**
 * 
 */
package summ.framework.scripting;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


/**
 * 使用Java.util.LinkedHashMap对象为被编译的脚本实现了一个脚本仓库。
 * 这个map的初始容量被设定为缓存脚本的最大数，并且装载系数是1，
 * 这两个参数保证了cacheMap不必重新进行哈希计算。
 * 默认条件下，LinkedHashMap类为其内部实体采用插入顺，
 * 因此比需把LinkedHashMap()构造器的第三个参数设定为true，
 * 这样map中的实体对象就可以用访问顺来代替默认顺。
 * 到达缓存的最大容量以后，removeEldestEntry()方法开始返回true，
 * 使得每次有新的被编译的脚本添加到缓存中时，一个脚本实体能够自动的从cacheMap中删除。
 * 通过LinkedHashMap的自动删除机制和访问顺相结合，在有新的脚本被添加时，ScriptCache确保最近使用的脚本从整个缓存中被删除。
 * @author wfeng007
 * @date 2013-5-11 下午02:12:15
 */
public abstract class ScriptCache {
    public static final String ENGINE_NAME = "ECMAScript";
    private Compilable scriptEngine;
    private LinkedHashMap<String, CachedScript> cacheMap;
    
    /**
     * 创建缓存，限制缓存最大值。
     * @param maxCachedScripts
     */
    public ScriptCache(final int maxCachedScripts) {
    	//引擎工厂不保存。? 考虑注入且保存关联？
        ScriptEngineManager manager = new ScriptEngineManager();
        //获取引擎，类型也可以注入。
        scriptEngine = (Compilable) manager.getEngineByName(ENGINE_NAME);
        //使用一个限长的缓存容器 超出则丢弃一个旧的script
        cacheMap = new LinkedHashMap<String, CachedScript>(
                maxCachedScripts, 1, true) {
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > maxCachedScripts;
            }
        };
    }
    
    /**
     * 
     * 这个方法来计算每次 通过一个关键字 创建一个file文件。
     * 内部使用。当穿件script前通过key生成对应的file文件。
     * 
     * @param key
     * @return
     */
    protected abstract File getScriptFile(String key); //改名比较好newScriptFile比较好? 不一定可能从另一个提供file的资源池中查找到。
    
    /**
     * 
     * @param key
     * @return
     * @throws ScriptException
     * @throws IOException 如无法找到具体文件？
     */
    public synchronized CompiledScript getScript(String key)
            throws ScriptException, IOException {
    	
        CachedScript script = cacheMap.get(key);
        if (script == null) {
        	//TODO getScriptFile的返回影响异常逻辑。
            script = new CachedScript(scriptEngine, getScriptFile(key)); //现在的策略是每个脚本都用同一个engine，其实可以为每个脚本用不同的引擎？
            cacheMap.put(key, script);
        }
        return script.getCompiledScript();//这样用的话 cachedscript就是内部用类了
    }
    
    /**
     * 直接获取CachedScript对象 。
     * 这里应该用另一种实现？ 如果创建不了抛异常？
     * @param key
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    public synchronized CachedScript getCachedScript(String key)
	    throws ScriptException, IOException {
		CachedScript script = cacheMap.get(key);
		if (script == null) {
			//TODO getScriptFile的返回影响异常逻辑。
		    script = new CachedScript(scriptEngine, getScriptFile(key)); //现在的策略是每个脚本都用同一个engine，其实可以为每个脚本用不同的引擎？
		    cacheMap.put(key, script);
		}
		return script; 
    }
    
    public ScriptEngine getEngine() {
        return (ScriptEngine) scriptEngine;
    }
    
}
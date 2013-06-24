/**
 * 
 */
package summ.framework.scripting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptException;



/**
 * 接收一个脚本文件，并且只有在脚本源代码被再次编辑时才从新编译。
 * getCompiledScript()方法调用了脚本引擎的compile()方法，
 * 这方法返回了执行脚本的javax.script.CompiledScript对象的eval()方法。
 * 
 * @author wfeng007
 * @date 2013-5-11 
 */
public class CachedScript {
    private Compilable scriptEngine;
    private File scriptFile;
    private CompiledScript compiledScript;
    private Date compiledDate;
    public CachedScript(Compilable scriptEngine, File scriptFile) {
    	//TODO 是否判断一下 scriptFile的情况。
        this.scriptEngine = scriptEngine;
        this.scriptFile = scriptFile;
    }
    
    /**
     * 获取脚本对应的File对象。
     * @return
     */
    public File getScriptFile(){
    	return this.scriptFile;
    }
    
    /**
     * 获取已经编译后的脚本。如果发现对应的文件修改时间晚于编译时间则重新进行编译，并放入缓存。
     * 否则直接从缓存中获取。
     * @return
     * @throws ScriptException
     * @throws IOException
     */
    public CompiledScript getCompiledScript()
            throws ScriptException, IOException {
    	
        Date scriptDate = new Date(scriptFile.lastModified());
        
        //这里可能有并发性问题 ?
        //新修改者重新编译放入缓存
        if (compiledDate == null || scriptDate.after(compiledDate)) {
        	
        	//TODO 其实这里不一定只可以从File中获取流，用Resource获取各种资源中读取到的流更好。在scripting模块完成。。。
//          Reader reader = new FileReader(scriptFile);
        	Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(scriptFile) ,"utf-8")); 
        	
            try {
                compiledScript = scriptEngine.compile(reader);
                compiledDate = scriptDate;
            } finally {
                reader.close();
            }
        }
        //
        return compiledScript;
    }
}
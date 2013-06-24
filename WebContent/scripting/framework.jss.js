println("config.jss.js beginning...");
println("..basic function and params init..");

importPackage(java.lang);
importPackage(java.io);
importPackage(java.net);
importPackage(javax.script);

//
//for load(); importent!!
//
/**
 * 
 * @param pathname
 * @returns {Boolean}
 */
function rm(pathname) {
    file = pathToFile(pathname);
    if (!file.exists()) {
        println("file not found: " + pathname);
        return false;
    }
    println(file["delete"]() ? "deleted" : "can not delete"); //
}

/**
 * create a file object
 * @param pathname
 * @returns
 */
function pathToFile(pathname) {
	/**
	 * 生成File对象
	 */
    var tmp = pathname;
    if (!(tmp instanceof File)) {
        tmp = new File(tmp); //这里引用File,在binding上就会生成一个类对应的对象
    }
    if (!tmp.isAbsolute()) {
        return new File(curDir, pathname);
    } else {
        return tmp;
    }
}

/**
 * create a inputstream
 * '-' is sys-in
 * 
 * @param str
 * @returns
 */
function inStream(str) {
    if (typeof (str) == "string") {
        if (str == "-") {
            return java.lang.System["in"];
        }
        var file = null;
        try {
            file = pathToFile(str);
        }
        catch (e) {
        	//throw e.toString(); //added
        }
        if (file && file.exists()) {
            return new FileInputStream(file);
        } else {
            try {
                return new URL(str).openStream();
            }
            catch (e) {
                throw "file or URL " + str + " not found or "+e.toString();
            }
        }
    } else {
        if (str instanceof InputStream) {
            return str;
        } else {
            if (str instanceof URL) {
                return str.openStream();
            } else {
                if (str instanceof File) {
                    return new FileInputStream(str);
                }
            }
        }
    }
    return java.lang.System["in"];
}

/**
 * cloase one stream
 * @param stream
 */
function streamClose(stream) {
    if (stream) {
        if (stream != java.lang.System["in"] && stream != java.lang.System.out && stream != java.lang.System.err) {
            try {
                stream.close();
            }
            catch (e) {
                println(e);
            }
        }
    }
}

/**
 * load a file/stream for eval
 * @param str
 * @returns
 */
function load(str) {
    var stream = inStream(str);
    var bstream = new BufferedInputStream(stream);
    var reader = new BufferedReader(new InputStreamReader(bstream));
//    var oldFilename = engine.get(engine.FILENAME);
    var oldFilename = context.getAttribute(engine.FILENAME);
    //engine.put(engine.FILENAME, str); //需要改为context 设置当当前context而不是当前engine
    context.setAttribute(engine.FILENAME, str,context.ENGINE_SCOPE);
    //
    try {
//        engine.eval(reader);  // engine需要java部分的环境给出. 
 //   	return engine.eval(reader); //需要返回 需要注意如果不给出ScriptContext 则eval会使用默认的sc而不一定是当前sc。
    	return engine.eval(reader,context); //FIXED 必须选择当前的context 如果只用eval(reader)会使用默认的context而不是“当前”，当外部使用费默认执行到这里时就会有问题。
    	
    }
    finally {
        //engine.put(engine.FILENAME, oldFilename);
    	context.setAttribute(engine.FILENAME, oldFilename,context.ENGINE_SCOPE);
    }
    streamClose(stream);
}


//
// for FS
//
/**
 * FS mkdir
 * @param dir
 */
function mkdir(dir) {
    var dir = pathToFile(dir);
    println(dir.mkdir() ? "created" : "can not create dir");
}

/**
 * FS mkdirs
 * @param dir
 */
function mkdirs(dir) {
    var dir = pathToFile(dir);
    println(dir.mkdirs() ? "created" : "can not create dirs");
}

/**
 * FS ls/dir
 * @param dir
 * @param filter
 */
function ls(dir, filter) {
    if (dir) {
        dir = pathToFile(dir);
    } else {
        dir = curDir;
    }
    if (dir.isDirectory()) {
        var files = dir.listFiles();
        for (var i in files) {
            var f = files[i];
            if (filter) {
                if (!f.getName().match(filter)) {
                    continue;
                }
            }
            printFile(f);
        }
    } else {
        printFile(dir);
    }
}

/**
 * FS dir
 * @param d
 * @param filter
 */
function dir(d, filter) {
    ls(d, filter);
}

/**
 * 
 * @param pathname
 */
function rmdir(pathname) {
    rm(pathname);
}
//
//for cmd/shell/arguments 
//
curDir=new File(".");
/**
 * 
 */
function pwd() {
    println(curDir.getAbsolutePath());
}


/********************
 * summ framework 
 ********************/
println("..summ framework function and params init..");
/**
 * 框架内部命名空间
 */
summjs={};
/**
 * 绑定数据内部引用
 */
summjs.bindings=this; //当前绑定属性


$S=summjs;
//***********
//核心框架部分
//***********

/**
 * 内部框架模块。核心方法为获取spring模块bean。
 * 获取springcontext中的bean对象。
 */
importPackage(Packages.summ.framework);
//summjs.fw={};
summjs.fw=function(beanId){//string
	return SpringContextHolder.getApplicationContext().getBean(beanId); //暂时没有可包装的需求
}

//*********
//for debug
//*********
/**
 *  print binding-names function for debug
 */
summjs.fw.pbnsF=function(){ 
	for(var f in summjs.bindings){
	    println(f);
	}
}
/**
 * print one function in binding for debug
 */
summjs.fw.pbF=function(name){
	 	print('**************************')
	    print(name+":begin")
	    println('**************************');
	    println(summjs.bindings[name]);
	    print('**************************')
	    print(name+":end")
	    println('**************************');
}

/**
 * print bindings function for debug
 */
summjs.fw.pbsF=function(){ 
	for(var name in summjs.bindings){
	    print('**************************')
	    print(name+":begin")
	    println('**************************');
	    println(summjs.bindings[name]);
	    print('**************************')
	    print(name+":end")
	    println('**************************');
	}
}

$SF=summjs.fw;

//*******
//for web 
//*******
/*
 * 绑定对象中已经提供servletContext request response 三个web对象。
 * outJSON函数依赖json2.js模块。
 */
summjs.web={};
/**
 * 输入：
 * request 全局变量中已提供servlet-request实现。
 * 输出：
 * param request中的参数值对象（全局变量）
 * paramValues 数组化的参数值对象 （全局变量）
 */
summjs.web.parseParams=function () { //参考这个提供一个 java-map/list 到 js-json 以及 js-json到java-map/list的拷贝

	/*
	 * 生成新的全局变量
	 */
	param = {};
	paramValues = {};
	
    var paramNames = request.getParameterNames();
    while (paramNames.hasMoreElements()) { // 注意queryparam 会覆盖 headerparam
        var name = paramNames.nextElement();
        param[name] = String(request.getParameter(name));
        paramValues[name] = new Array();
        var values = request.getParameterValues(name);
        for (var i = 0; i < values.length; i++)
            paramValues[name][i] = String(values[i]);
    }
}
//application request session
//针对所有带有getAttribute //setAttribute的操作
/**
 * 输入:
 * scope 一般包括request session servletContext
 * 		 等带有getAttribute/setAttribute方法且预先绑定的servlet对象。
 * id 对象key
 * 输出：
 * 返回scope中对应的对象。
 */
summjs.web.getBean=function(scope, id) {
  return eval(scope).getAttribute(id);
}
summjs.web.setBean=function(scope, id, bean) {
  if (!bean)
      bean = eval(id);
  return eval(scope).setAttribute(id, bean);
}

/**
 * 输入
 * str：字符串
 * response：引擎提供 
 * id 对象key
 * 输出：
 * 返回scope中对应的对象。
 */
summjs.web.outStr=function(str){
	out.print(str);
	out.flush();
}

//依赖json2模块 默认web应用路径scripting下json2.js文件 //之后为每个模块单独提供一个依赖模块默认路径
load(servletContext.getRealPath("/")+"/scripting/json2.js");
/**
 * 输入
 * obj：javascript属性对象。
 * response：引擎提供 
 * id 对象key
 * 输出：
 * 返回scope中对应的对象。
 */
summjs.web.outJSON=function(obj){
	response.setContentType("application/json");// 设置http头部为json
	out.print(JSON.stringify(obj));
	out.flush();
}


$SW=summjs.web;

//eval('println("eval....")');
println("config.jss.js end...");
//utf-8
//main.js
//
/**
 * require & import
 */
var strings = require("ringo/utils/strings");
var summjs=require("summ");

//日志器
var log = require("ringo/logging").getLogger(module.id);
/**
 * export 的模块函数，同时也是插件的执行句柄。
 */
export("prtHello"
		,"vPosNeck"); //导出插件handle

//
log.info("插件初始化时运行，初始化插件"+module.id);
//
//exports.app = function(request){
//var app = function(request){
//
function prtHello(req,resp){
	var out=resp.getWriter();
	out.print("hi 我是插件， hello World!以下是hello dolly的歌词!<br/>");
	out.print(getLyricsLine()+"<br/>");
	
	//测试导入的其他模块内容
	summjs.foo();
	//
	return;
};

var vPosNeck=function(req,resp){
	prtHello(req,resp);
	log.info("prtHello!!");
	return;
};

function getLyricsLine(){
	// These are the lyrics to Hello Dolly  
	var lyrics = ["Hello, Dolly",
	"Well, hello, Dolly",  
	"It's so nice to have you back where you belong",  
	"You're lookin' swell, Dolly",  
	"I can tell, Dolly",  
	"You're still glowin', you're still crowin",  
	"You're still goin' strong",  
	"We feel the room swayin",  
	"While the band's playin",  
	"One of your old favourite songs from way back when",  
	"So, take her wrap, fellas",  
	"Find her an empty lap, fellas",  
	"Dolly'll never go away again",  
	"Hello, Dolly",  
	"Well, hello, Dolly",  
	"It's so nice to have you back where you belong",  
	"You're lookin' swell, Dolly",  
	"I can tell, Dolly",  
	"You're still glowin', you're still crowin",  
	"You're still goin' strong",  
	"We feel the room swayin",  
	"While the band's playin",  
	"One of your old favourite songs from way back when",  
	"Golly, gee, fellas",  
	"Find her a vacant knee, fellas",  
	"Dolly'll never go away",  
	"Dolly'll never go away",  
	"Dolly'll never go away again"]; 
	
	function getRandomNum(Min,Max)
	{   
		var Range = Max - Min;   
		var Rand = Math.random();   
		return(Min + Math.round(Rand * Range));   
	}
	
//	var numOutput = new Number(Math.random() * lyrics.length).toFixed(0);
	var reN=getRandomNum(0, lyrics.length);
	return lyrics[reN];
}


//如果不是模块
if (require.main === module) {
    print("not support cmdmain!!");
}


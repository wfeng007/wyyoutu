var Application = require("stick").Application;

//日志器
var log = require("ringo/logging").getLogger(module.id);
var strings = require("ringo/utils/strings");

export("app"); 

var app=Application();
app.configure("route");

/**
 * rjs/mounted/hi
 */
app.get("/hi",function(req){
	log.info("srv pathInfo:"+req.pathInfo);
	print("do requset...中文");
    return {
        status: 200,
        headers: {"Content-Type": "text/plain"},
        body: [module.id+":运行完成 /hi ！"+"srv pathInfo:"+req.pathInfo]
    };
});

/**
 * rjs/mounted/2
 */
app.get("/2",function(req){
	log.info("srv pathInfo:"+req.pathInfo);
	print("do requset...中文");
    return {
        status: 200,
        headers: {"Content-Type": "text/plain"},
        body: [module.id+":运行完成 /2 ！"+"srv pathInfo:"+req.pathInfo]
    };
});

/**
 * rjs/mounted & rjs/mounted/*
 */
app.get("/*",function(req){
	log.info("srv pathInfo:"+req.pathInfo);
    return {
        status: 200,
        headers: {"Content-Type": "text/plain"},
        body: [module.id+":运行完成 * ！"+"srv pathInfo:"+req.pathInfo]
    };
});


var Headers = require('ringo/utils/http').Headers;

exports.middleware = function doFilter(next, app) {

	/**
	 * 当jsgi-middleware 找不到对应资源，直接调用执行下一个serlvet-filter。
	 * 在使用filter环境中使用。依赖request.env.filterChain。
	 * 
	 * 执行到这里性能会较慢。应该是在JsgiFilter中提供进一步path以及后缀过滤。
	 * 
	 * @param request
	 * @returns
	 */
    return function doFilter(request) {
        try {
        	var resp=next(request);
        	//如果底层jsgi应用要求也可以进行doFilter()继续后续filter。
        	if ( Headers(headers).contains("X-JSGI-Do-Filter")) {
        		request.env.filterChain.doFilter(request.env.servletRequest,request.env.servletResponse);
        		return  {
        			status: 404, //无实际作用。
        			headers: {"X-JSGI-Skip-Response": "true"}, //让connector忽略这个response对象返回。
        			body: []
        		};
        	}
            return resp
        } catch (e if e.notfound) { //next(request)时执行jsgi的函数时没有找到资源
        	//没有资源则直接调用serlvet filter中的下个filter。
        	request.env.filterChain.doFilter(request.env.servletRequest,request.env.servletResponse);
        	return  {
        			status: 404, //无实际作用。
        			headers: {"X-JSGI-Skip-Response": "true"}, //让connector忽略这个response对象返回。
        			body: []
        	};
        }
    };
};
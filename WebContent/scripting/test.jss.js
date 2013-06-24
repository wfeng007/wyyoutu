

println(scriptname);

println("this.file:"+context.getAttribute(engine.FILENAME));
println(context.getAttribute(engine.ENGINE));
println(context.getAttribute(engine.ENGINE_VERSION));
println(context.getAttribute(engine.NAME));
println(context.getAttribute(ScriptEngine.LANGUAGE));
println(context.getAttribute(engine.LANGUAGE_VERSION));

println("hello web world!! i am webserver-side js");
load(servletContext.getRealPath("/")+"/scripting/json2.js");

////println(Date.prototype);
//
var text = JSON.stringify({"wf":['e', {pluribus: 'unum'}],"wf2":new Date()});
println(text);
//println(JSON.parse(text));
//println(JSON.parse(text).toSource());
$SF.pbnsF();
//for(var f in this){
//    println(f);
//}
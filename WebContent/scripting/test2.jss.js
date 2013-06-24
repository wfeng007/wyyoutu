



// 打印指定的函数体
//println(cat)
println(println) //默认函数这个是有的
println(print) //默认函数这个是有的
println(context) //默认函数这个是有的

// 打印全局函数    
for(var name in this){
    //printGlobal(name,this);
}



// helper
function printGlobal(name,ref){
    print('**************************')
    print(f);
    println('**************************')
    println(ref[f])
}


//引入Class
importClass(java.lang.System);
println(java.lang.System.currentTimeMillis());
println(System.currentTimeMillis());

// 没有引入
try{
    var sb = new StringBuilder();
}catch(e){
    println(e);
}

// 引入package
importPackage(java.lang)
var sb = new StringBuilder();
sb.append("is ")
sb.append("String");
sb.append("Builder")
println(sb);


222;
java

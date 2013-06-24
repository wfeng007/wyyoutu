/*
 * Created on 2005-3-23
 */
package summ.framework.util.lab;
import java.util.*;
/**
 * 
 * 简单的栈 内部使用ArrayList实现
 * 注意这个栈只是一个引用的栈
 * 
 * 见apache commons collection组件的ArrayStack类
 * @author wangfeng
 * 
 * 
 */
public class CuteStack {
	
	private ArrayList stackSpace;

	/**
	 * 
	 *
	 */
	public CuteStack(){
		this(100);
	}
	/**
	 * 
	 * @param n
	 */
	public CuteStack(int n){
		this.stackSpace=new ArrayList(n);
	}
	/**
	 * 
	 * @return
	 */
	public Object peek(){
		System.out.println(this.stackSpace.get(this.stackSpace.size()-1));
		return this.stackSpace.get(this.stackSpace.size()-1);
	}
	/**
	 * 
	 * @return
	 */
	public Object pop(){
		System.out.println(this.stackSpace.get(this.stackSpace.size()-1));
		
		return this.stackSpace.remove(this.stackSpace.size()-1);
	}
	
	/**
	 * 
	 * @param o
	 */
	public void push(Object o){
		System.out.println("clazz: "+o.getClass().getName());
		this.stackSpace.add(o);
	}
	
	/**
	 * 
	 * @return
	 */
	public int size(){
		return this.stackSpace.size();
	}
	
	/**
	 * 
	 *
	 */
	public void clear(){
		this.stackSpace.clear();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isEmpty(){
		return this.stackSpace.isEmpty();
	}

	
}

package com.jimi.smt.eps_server.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 角色权限划分注解
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Role {
	
	enum RoleType{
		PRODUCER("生产管理员"),
		IPQC("品质管理员"),
		ENGINEER("工程管理员");
		private final String text;
        private RoleType(final String text){
            this.text=text;
        }
        @Override
        public String toString(){
            return text;
        }
	}
	/**
	 * 在该数组内的成员拥有该接口的权限（超级管理员默认拥有所有权限，不需要注解）
	 * <br>
	 * 可以写的内容有生产管理员，品质管理员，工程管理员
	 * @return
	 */
	RoleType[] value();
}

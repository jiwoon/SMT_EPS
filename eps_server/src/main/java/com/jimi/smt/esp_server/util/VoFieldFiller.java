package com.jimi.smt.esp_server.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 传一个bo或者数据库实体，填充缺少的字段，返回vo。所有BO转换成VO继承这个类
 * @author Lenovo
 *
 */
public abstract class VoFieldFiller<T, K> {
	
	/**
	 * 封装为vo
	 * @param t
	 * @return
	 */
	public abstract K fill(T t);
	
	/**
	 * 封装为vo
	 * @param tList
	 * @return vo的List
	 */
	public  List<K> fill(List<T> tList) {
		List<K> list = new ArrayList<K>(tList.size());
		for (T t : tList) {
			K vo = fill(t);
			list.add(vo);
		}
		return list;
	}
	
	/**
	 * 从vo解封为bo
	 * @param k
	 * @return
	 */
	public T unfill(K k) {
		throw new RuntimeException("如果要是用该方法，必须在子类重写该方法");
	}
	
	public List<T> unFill(List<K> kList) {
		List<T> tList = new ArrayList<T>(kList.size());
		for (K k : kList) {
			T bo = unfill(k);
			tList.add(bo);
		}
		return tList;
	}
}

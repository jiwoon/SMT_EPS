package com.jimi.smt.eps_server.service;

import java.util.List;

import com.jimi.smt.eps_server.entity.MaterialInfo;

public interface MaterialService {
	
	/**
	 * 新增一个物料信息
	 * @param materialNo
	 * @param perifdOfValidity
	 * @return
	 */
	public String add(String materialNo, Integer perifdOfValidity);
	
	/**
	 * 修改对应id的物料保质期信息
	 * @param id
	 * @param materialNo
	 * @param perifdOfValidity
	 * @return
	 */
	public String update(Integer id, String materialNo, Integer perifdOfValidity);
	
	/**
	 * 根据id删除物料保质期信息
	 * @param id
	 * @return
	 */
	public String delete(Integer id);
	
	/**
	 * 根据条件列出物料保质期信息
	 * 前面三个为查询条件
	 * orderBy：
	 * 根据字段名排序
	 * @param materialNo
	 * @param perifdOfValidity
	 * @param orderBy
	 * @return
	 */
	public List<MaterialInfo> list(Integer id, String materialNo, Integer perifdOfValidity, String orderBy);
}

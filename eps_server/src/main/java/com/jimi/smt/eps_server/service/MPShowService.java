package com.jimi.smt.eps_server.service;

import java.text.ParseException;
import java.util.List;

import com.jimi.smt.eps_server.entity.vo.MPShowItemVO;

public interface MPShowService {

	/**
	 * 根据时间区间返回公粽号报表
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws ParseException 
	 */
	List<MPShowItemVO> show(String startTime, String endTime) throws ParseException;

}

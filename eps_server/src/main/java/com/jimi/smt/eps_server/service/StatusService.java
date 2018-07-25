package com.jimi.smt.eps_server.service;

import java.util.List;

import com.jimi.smt.eps_server.entity.StatusDetail;
import com.jimi.smt.eps_server.entity.vo.StatusDetailsVO;

public interface StatusService {

	/**
	 * 查询某条产线近24小时内操作细节，并按小时分类
	 * @param Line
	 * @return
	 */
	public StatusDetailsVO ListStatusDetailsByHour(String Line);
	
	/**
	 * 查询每条产线近24小时的操作数据，按产线分类，并进行统计
	 * @return
	 */
	public List<StatusDetail> ListStatusDetailsByDay();
	
}

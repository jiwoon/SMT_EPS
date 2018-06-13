package com.jimi.smt.eps_server.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jimi.smt.eps_server.entity.Operation;
import com.jimi.smt.eps_server.entity.OperationExample;
import com.jimi.smt.eps_server.entity.bo.MPShowItemBO;
import com.jimi.smt.eps_server.entity.filler.MPShowItemBOToMPShowItemVOFiller;
import com.jimi.smt.eps_server.entity.vo.MPShowItemVO;
import com.jimi.smt.eps_server.mapper.OperationMapper;
import com.jimi.smt.eps_server.service.MPShowService;
import cc.darhao.dautils.api.DateUtil;

@Service
public class MPShowServiceImpl implements MPShowService {

	@Autowired
	private OperationMapper OperationMapper;
	
	@Autowired
	private MPShowItemBOToMPShowItemVOFiller filler;
	
	@Override
	public List<MPShowItemVO> show(String startTime, String endTime) throws ParseException {
		List<MPShowItemBO> mpShowItemBOs = new ArrayList<MPShowItemBO>();
		//筛选时间
		OperationExample example = new OperationExample();
		example.createCriteria()
			.andTimeGreaterThanOrEqualTo(DateUtil.yyyyMMddHHmmss(startTime))
			.andTimeLessThanOrEqualTo(DateUtil.yyyyMMddHHmmss(endTime));
		List<Operation> operations = OperationMapper.selectByExample(example);
		//遍历并分组
		for (Operation operation : operations) {
			boolean isMatchLine = false;
			//判断是否存在该线号
			for (MPShowItemBO bo : mpShowItemBOs) {
				if(bo.getLine().equals(operation.getLine())){
					isMatchLine = true;
					count(operation, bo);
					break;
				}
			}
			//如果没有找到线号则创建一个bo
			if(!isMatchLine) {
				MPShowItemBO bo = new MPShowItemBO();
				bo.setLine(operation.getLine());
				count(operation, bo);
				mpShowItemBOs.add(bo);
			}
		}
		//封装成VO
		return filler.fill(mpShowItemBOs);
	}

	/**
	 * 清点数目
	 * @param operation
	 * @param bo
	 */
	private void count(Operation operation, MPShowItemBO bo) {
		switch (operation.getType()) {
		case 0:
			if(operation.getResult().equals("PASS")){
				bo.setFeedSuc(bo.getFeedSuc() + 1);
			}else {
				bo.setFeedFail(bo.getFeedFail() + 1);
			}
			break;
		case 1:
			if(operation.getResult().equals("PASS")){
				bo.setChangeSuc(bo.getChangeSuc() + 1);
			}else {
				bo.setChangeFail(bo.getChangeFail() + 1);
			}
			break;
		case 2:
			if(operation.getResult().equals("PASS")){
				bo.setSomeSuc(bo.getSomeSuc() + 1);
			}else {
				bo.setSomeFail(bo.getSomeFail() + 1);
			}
			break;
		case 3:
			if(operation.getResult().equals("PASS")){
				bo.setAllsSuc(bo.getAllsSuc() + 1);
			}else {
				bo.setAllsFail(bo.getAllsFail() + 1);
			}
			break;
		default:
			break;
		}
	}

}

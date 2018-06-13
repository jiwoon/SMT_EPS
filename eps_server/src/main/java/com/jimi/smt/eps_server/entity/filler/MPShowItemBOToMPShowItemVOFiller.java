package com.jimi.smt.eps_server.entity.filler;

import org.springframework.stereotype.Component;

import com.jimi.smt.eps_server.entity.bo.MPShowItemBO;
import com.jimi.smt.eps_server.entity.vo.MPShowItemVO;
import com.jimi.smt.eps_server.util.EntityFieldFiller;

@Component
public class MPShowItemBOToMPShowItemVOFiller extends EntityFieldFiller<MPShowItemBO, MPShowItemVO>{

	@Override
	public MPShowItemVO fill(MPShowItemBO bo) {
		MPShowItemVO vo = new MPShowItemVO();
		//填充线号
		vo.setLine(bo.getLine());
		//数量转百分比文本
		int suc = 0;
		int fail = 0;
		suc = (int) ((1.0d * bo.getAllsSuc() / (bo.getAllsFail() + bo.getAllsSuc())) * 100);
		fail = 100 - suc;
		if(bo.getAllsFail() == 0 && bo.getAllsSuc() == 0) {
			vo.setAllsSuc(" - ");
			vo.setAllsFail(" - ");
		}else {
			vo.setAllsSuc(suc + "%");
			vo.setAllsFail(fail + "%");
		}
		suc = (int) ((1.0d * bo.getSomeSuc() / (bo.getSomeFail() + bo.getSomeSuc())) * 100);
		fail = 100 - suc;
		if(bo.getSomeFail() == 0 && bo.getSomeSuc() == 0) {
			vo.setSomeSuc(" - ");
			vo.setSomeFail(" - ");
		}else {
			vo.setSomeSuc(suc + "%");
			vo.setSomeFail(fail + "%");
		}
		suc = (int) ((1.0d * bo.getFeedSuc() / (bo.getFeedFail() + bo.getFeedSuc())) * 100);
		fail = 100 - suc;
		if(bo.getFeedFail() == 0 && bo.getFeedSuc() == 0) {
			vo.setFeedSuc(" - ");
			vo.setFeedFail(" - ");
		}else {
			vo.setFeedSuc(suc + "%");
			vo.setFeedFail(fail + "%");
		}
		suc = (int) ((1.0d * bo.getChangeSuc() / (bo.getChangeFail() + bo.getChangeSuc())) * 100);
		fail = 100 - suc;
		if(bo.getChangeFail() == 0 && bo.getChangeSuc() == 0) {
			vo.setChangeSuc(" - ");
			vo.setChangeFail(" - ");
		}else {
			vo.setChangeSuc(suc + "%");
			vo.setChangeFail(fail + "%");
		}
		return vo;
	}

}

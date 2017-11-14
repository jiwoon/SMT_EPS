package com.jimi.smt.eps_server.entity.filler;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.jimi.smt.eps_server.entity.ProgramItem;
import com.jimi.smt.eps_server.entity.vo.ProgramItemVO;
import com.jimi.smt.eps_server.util.EntityFieldFiller;

@Component
public class ProgramItemToProgramItemVOFiller extends EntityFieldFiller<ProgramItem, ProgramItemVO> {

	@Override
	public ProgramItemVO fill(ProgramItem programItem) {
		ProgramItemVO programItemVO = new ProgramItemVO();
		BeanUtils.copyProperties(programItem, programItemVO);
		programItemVO.setIsAlternative(programItemVO.getAlternative() ? "是" : "否");
		return programItemVO;
	}

}

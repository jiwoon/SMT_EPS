package com.jimi.smt.eps_server.entity.filler;

import java.text.SimpleDateFormat;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.jimi.smt.eps_server.entity.Program;
import com.jimi.smt.eps_server.entity.vo.ProgramVO;
import com.jimi.smt.eps_server.util.EntityFieldFiller;

@Component
public class ProgramToProgramVOFiller extends EntityFieldFiller<Program, ProgramVO> {

	@Override
	public ProgramVO fill(Program program) {
		ProgramVO programVO = new ProgramVO();
		BeanUtils.copyProperties(program, programVO);
		switch (programVO.getBoardType()) {
		case 0:
			programVO.setBoardTypeName("默认");
			break;
		case 1:
			programVO.setBoardTypeName("AB面");
			break;
		case 2:
			programVO.setBoardTypeName("A面");
			break;
		case 3:
			programVO.setBoardTypeName("B面");
			break;
		default:
			break;
		}
		switch (programVO.getState()) {
		case 0:
			programVO.setStateName("未开始");
			break;
		case 1:
			programVO.setStateName("进行中");
			break;
		case 2:
			programVO.setStateName("已完成");
			break;
		case 3:
			programVO.setStateName("已作废");
			break;
		default:
			break;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		programVO.setCreateTimeString(sdf.format(programVO.getCreateTime()));
		return programVO;
	}

}

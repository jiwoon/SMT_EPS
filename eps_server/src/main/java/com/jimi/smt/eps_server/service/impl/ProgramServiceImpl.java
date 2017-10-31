package com.jimi.smt.eps_server.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jimi.smt.eps_server.entity.Program;
import com.jimi.smt.eps_server.entity.ProgramBackup;
import com.jimi.smt.eps_server.entity.ProgramExample;
import com.jimi.smt.eps_server.entity.ProgramItem;
import com.jimi.smt.eps_server.entity.ProgramItemBackup;
import com.jimi.smt.eps_server.entity.ProgramItemExample;
import com.jimi.smt.eps_server.entity.filler.ProgramToProgramVOFiller;
import com.jimi.smt.eps_server.entity.vo.ProgramVO;
import com.jimi.smt.eps_server.mapper.ProgramBackupMapper;
import com.jimi.smt.eps_server.mapper.ProgramItemBackupMapper;
import com.jimi.smt.eps_server.mapper.ProgramItemMapper;
import com.jimi.smt.eps_server.mapper.ProgramMapper;
import com.jimi.smt.eps_server.service.ProgramService;
import com.jimi.smt.eps_server.util.ExcelHelper;
import com.jimi.smt.eps_server.util.FieldUtil;
import com.jimi.smt.eps_server.util.UuidUtil;

@Service
public class ProgramServiceImpl implements ProgramService {

	@Autowired
	private ProgramMapper programMapper;
	@Autowired
	private ProgramItemMapper programItemMapper;
	@Autowired
	private ProgramBackupMapper programBackupMapper;
	@Autowired
	private ProgramItemBackupMapper programItemBackupMapper;
	@Autowired
	private ProgramToProgramVOFiller filler;
	
	@Override
	public Map<String, Object> upload(MultipartFile programFile, Integer boardType) throws IOException {
		ExcelHelper helper = ExcelHelper.from(programFile);
		//校验
		final String header = "SMT FEEDER LIST";
		if(!header.equals(helper.getString(1, 0))) {
			throw new RuntimeException("头部错误：没有找到\"SMT FEEDER LIST\"标题栏");
		}
		//填充表头
		Program program = new Program();
		program.setClient(helper.getString(2, 1));
		program.setMachineName(helper.getString(2, 4));
		program.setVersion(helper.getString(2, 6));
		program.setMachineConfig(helper.getString(3, 1));
		program.setProgramNo(helper.getString(3, 4));
		program.setLine(helper.getString(3, 6));
		program.setEffectiveDate(helper.getDate(4, 1).toString());
		program.setPcbNo(helper.getString(4, 4));
		program.setBom(helper.getString(5, 1));
		program.setProgramName(helper.getString(6, 1));
		program.setAuditor(helper.getString(7, 4).substring(3));
		program.setFileName(programFile.getOriginalFilename());
		program.setId(UuidUtil.get32UUID());
		program.setCreateTime(new Date());
		program.setBoardType(boardType);
		program.setWorkOrder(helper.getString(4, 6));
		
		//初始化结果
		Map<String, Object> result = new HashMap<String , Object>();
		int sum = helper.getBook().getNumberOfSheets();
		result.put("real_parse_num", sum);
		result.put("plan_parse_num", sum);
		result.put("action_name", "上传");
		
		//填充表项
		for(int i = 0; i < sum; i++) {
			helper.switchSheet(i);
			for(int j = 9; j < helper.getBook().getSheetAt(i).getLastRowNum() - 3; j++) {
				ProgramItem programItem = new ProgramItem();
				//空表判断
				if(helper.getString(j, 0).equals("")) {
					int temp = (int) result.get("real_parse_num");
					result.put("real_parse_num", temp--);
					break;
				}
				programItem.setLineseat(formatLineseat(helper.getString(j, 0)));
				programItem.setMaterialNo(helper.getString(j, 1));
				programItem.setAlternative(helper.getBoolean(j, 2));
				programItem.setSpecitification(helper.getString(j, 3));
				programItem.setPosition(helper.getString(j, 4));
				programItem.setQuantity(helper.getInt(j, 5));
				//设置programId
				programItem.setProgramId(program.getId());
				//忽略重复项
				try {
					//插入表项
					programItemMapper.insert(programItem);
					//备份表项
					ProgramItemBackup programItemBackup = new ProgramItemBackup();
					BeanUtils.copyProperties(programItem, programItemBackup);
					programItemBackupMapper.insert(programItemBackup);
					//打印到控制台
					FieldUtil.print(programItem);
				}catch (DuplicateKeyException e) {
				}
			}
		}
		
		//重复文件移除（根据工单和板面类型）
		ProgramExample programExample = new ProgramExample();
		programExample.createCriteria()
			.andWorkOrderEqualTo(program.getWorkOrder())
			.andBoardTypeEqualTo(program.getBoardType());
		List<Program> programs = programMapper.selectByExample(programExample);
		if(!programs.isEmpty()) {
			Program p = programs.get(0);
			programMapper.deleteByPrimaryKey(p.getId());
			ProgramItemExample programItemExample = new ProgramItemExample();
			programItemExample.createCriteria().andProgramIdEqualTo(p.getId());
			programItemMapper.deleteByExample(programItemExample);
			result.put("action_name", "覆盖");
		}
		
		//插入新表
		programMapper.insert(program);
		//备份表
		ProgramBackup programBackup = new ProgramBackup();
		BeanUtils.copyProperties(program, programBackup);
		programBackupMapper.insert(programBackup);
		//打印到控制台
		FieldUtil.print(program);
		
		return result;
	}

	
	@Override
	public List<ProgramVO> list() {
		return filler.fill(programMapper.selectByExample(null));
	}


	@Override
	public boolean delete(String id) {
		if(programMapper.deleteByPrimaryKey(id) == 1) {
			ProgramItemExample programItemExample = new ProgramItemExample();
			programItemExample.createCriteria().andProgramIdEqualTo(id);
			if(programItemMapper.deleteByExample(programItemExample) != 0) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}


	private String formatLineseat(String in) {
		try {
			String[] array = in.split("-");
			array[0] = Integer.valueOf(array[0]) <= 9 ? "0" + array[0] : array[0];
			array[1] = Integer.valueOf(array[1]) <= 9 ? "0" + array[1] : array[1];
			return array[0] + "-" + array[1];
		}catch (NumberFormatException | PatternSyntaxException e) {
			return in;
		}
	}
	
}

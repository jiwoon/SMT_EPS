package com.jimi.smt.eps_server.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jimi.smt.eps_server.entity.Program;
import com.jimi.smt.eps_server.entity.ProgramExample;
import com.jimi.smt.eps_server.entity.ProgramItem;
import com.jimi.smt.eps_server.entity.ProgramItemExample;
import com.jimi.smt.eps_server.entity.ProgramItemVisit;
import com.jimi.smt.eps_server.entity.ProgramItemVisitExample;
import com.jimi.smt.eps_server.entity.filler.ProgramItemToProgramItemVOFiller;
import com.jimi.smt.eps_server.entity.filler.ProgramItemToProgramItemVisitFiller;
import com.jimi.smt.eps_server.entity.filler.ProgramToProgramVOFiller;
import com.jimi.smt.eps_server.entity.vo.ProgramItemVO;
import com.jimi.smt.eps_server.entity.vo.ProgramVO;
import com.jimi.smt.eps_server.mapper.ProgramItemMapper;
import com.jimi.smt.eps_server.mapper.ProgramItemVisitMapper;
import com.jimi.smt.eps_server.mapper.ProgramMapper;
import com.jimi.smt.eps_server.service.ProgramService;
import com.jimi.smt.eps_server.util.ExcelHelper;
import com.jimi.smt.eps_server.util.FieldUtil;
import com.jimi.smt.eps_server.util.ResultUtil;
import com.jimi.smt.eps_server.util.UuidUtil;

@Service
public class ProgramServiceImpl implements ProgramService {

	@Autowired
	private ProgramMapper programMapper;
	@Autowired
	private ProgramItemMapper programItemMapper;
	@Autowired
	private ProgramItemVisitMapper programItemVisitMapper;
	@Autowired
	private ProgramToProgramVOFiller programToProgramVOFiller;
	@Autowired
	private ProgramItemToProgramItemVOFiller programItemToProgramItemVOFiller;
	@Autowired
	private ProgramItemToProgramItemVisitFiller programItemToProgramItemVisitFiller;
	
	@Override
	public List<Map<String, Object>> upload(MultipartFile programFile, Integer boardType) throws IOException {
		//读文件
		ExcelHelper helper = ExcelHelper.from(programFile);
		
		//初始化结果
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		
		
		//校验
		final String header = "SMT FEEDER LIST";
		if(!header.equals(helper.getString(1, 0))) {
			throw new RuntimeException("头部错误：没有找到\"SMT FEEDER LIST\"标题栏");
		}
		
		//分割解析工单和线号
		String[] workOrders = helper.getString(4, 6).split("\\+");
		String[] lines = helper.getString(3, 6).split("\\+");
		
		//创建所有工单
		List<Program> programs = new ArrayList<Program>(workOrders.length * lines.length);
		for (String line : lines) {
			for (String workOrder : workOrders) {
				Program program = new Program();
				program.setClient(helper.getString(2, 1));
				program.setMachineName(helper.getString(2, 4));
				program.setVersion(helper.getString(2, 6));
				program.setMachineConfig(helper.getString(3, 1));
				program.setProgramNo(helper.getString(3, 4));
				program.setLine(line);
				program.setEffectiveDate(helper.getDate(4, 1).toString());
				program.setPcbNo(helper.getString(4, 4));
				program.setBom(helper.getString(5, 1));
				program.setProgramName(helper.getString(6, 1));
				program.setAuditor(helper.getString(7, 4).substring(3));
				program.setFileName(programFile.getOriginalFilename());
				program.setId(UuidUtil.get32UUID());
				program.setCreateTime(new Date());
				program.setBoardType(boardType);
				program.setWorkOrder(workOrder);
				programs.add(program);
			}
		}
		
		for (Program program : programs) {
			//初始化结果Item
			Map<String, Object> result = new HashMap<String , Object>();
			int sum = helper.getBook().getNumberOfSheets();
			result.put("real_parse_num", sum);
			result.put("plan_parse_num", sum);
			result.put("action_name", "上传");
			
			//覆盖：如果“未开始”的工单列表中存在板面类型、工单号、线号同时一致的工单项目，将被新文件内容覆盖
			ProgramExample programExample = new ProgramExample();
			programExample.createCriteria()
				.andWorkOrderEqualTo(program.getWorkOrder())
				.andBoardTypeEqualTo(program.getBoardType())
				.andLineEqualTo(program.getLine())
				.andStateEqualTo(0);
			//如果存在符合条件的工单
			List<Program> programs2 = programMapper.selectByExample(programExample);
			if(!programs2.isEmpty()) {
				programMapper.updateByExampleSelective(program, programExample);
				ProgramItemExample programItemExample = new ProgramItemExample();
				programItemExample.createCriteria().andProgramIdEqualTo(programs2.get(0).getId());
				programItemMapper.deleteByExample(programItemExample);
				result.put("action_name", "覆盖");
			}else {
				programMapper.insertSelective(program);
			}
			
			//打印到控制台
			FieldUtil.print(program);
			
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
						//打印到控制台
						FieldUtil.print(programItem);
					}catch (DuplicateKeyException e) {
					}
				}
			}
			resultList.add(result);
		}
		
		return resultList;
	}


	@Override
	public List<ProgramVO> list(String programName, String fileName, String line, String workOrder, Integer state,
			String ordBy) {
		ProgramExample programExample = new ProgramExample();
		ProgramExample.Criteria programCriteria = programExample.createCriteria();
		
		 //排序
		if(ordBy == null) {
			//默认按时间降序
			programExample.setOrderByClause("create_time desc");
		}else {
			programExample.setOrderByClause(ordBy);
		}
		
		 //筛选程序名
		if(programName != null && !programName.equals("")) {
			programCriteria.andProgramNameEqualTo(programName);
		}
		 //筛选文件名
		if(fileName != null && !fileName.equals("")) {
			programCriteria.andFileNameEqualTo(fileName);
		}
		 //筛选线别
		if(line != null && !line.equals("")) {
			programCriteria.andLineEqualTo(line);
		}
		 //筛选工单号
		if(workOrder != null && !workOrder.equals("")) {
			programCriteria.andWorkOrderEqualTo(workOrder);
		}
		 //筛选状态
		if(state != null) {
			programCriteria.andStateEqualTo(state);
		}
		
		List<Program> programs = programMapper.selectByExample(programExample);
		return programToProgramVOFiller.fill(programs);
	}



	@Override
	public List<ProgramItemVO> listItem(String id) {
		ProgramItemExample example = new ProgramItemExample();
		example.createCriteria().andProgramIdEqualTo(id);
		return programItemToProgramItemVOFiller.fill(programItemMapper.selectByExample(example));
	}


	@Override
	public boolean cancel(String workOrder, String line, Integer boardType) {
		ProgramExample example = new ProgramExample();
		example.createCriteria()
			.andWorkOrderEqualTo(workOrder)
			.andLineEqualTo(line)
			.andBoardTypeEqualTo(boardType);
		//状态判断
		List<Program> programs = programMapper.selectByExample(example);
		if(programs.isEmpty()) {
			return false;
		}
		Program program = programs.get(0);
		if(program.getState() >= 2) {
			ResultUtil.failed("状态不可逆");
			return false;
		}
		Program program2 = new Program();
		program2.setState(3);
		int result = programMapper.updateByExampleSelective(program2, example);
		if(result != 0) {
			//清除ProgramItemVisit
			clearVisits(program.getId());
			return true;
		}else {
			return false;
		}
	}


	@Override
	public boolean finish(String workOrder, String line, Integer boardType) {
		ProgramExample example = new ProgramExample();
		example.createCriteria()
			.andWorkOrderEqualTo(workOrder)
			.andLineEqualTo(line)
			.andBoardTypeEqualTo(boardType);
		//状态判断
		List<Program> programs = programMapper.selectByExample(example);
		if(programs.isEmpty()) {
			return false;
		}
		Program program = programs.get(0);
		if(program.getState() >= 2) {
			ResultUtil.failed("状态不可逆");
			return false;
		}
		Program program2 = new Program();
		program2.setState(2);
		int result = programMapper.updateByExampleSelective(program2, example);
		if(result != 0) {
			//清除ProgramItemVisit
			clearVisits(program.getId());
			return true;
		}else {
			return false;
		}
	}



	@Override
	public boolean start(String workOrder, String line, Integer boardType) {
		ProgramExample example = new ProgramExample();
		example.createCriteria()
			.andWorkOrderEqualTo(workOrder)
			.andLineEqualTo(line)
			.andBoardTypeEqualTo(boardType);
		//状态判断
		List<Program> programs = programMapper.selectByExample(example);
		if(programs.isEmpty()) {
			return false;
		}
		Program program = programs.get(0);
		if(program.getState() >= 1) {
			ResultUtil.failed("状态不可逆");
			return false;
		}
		Program program2 = new Program();
		program2.setState(1);
		int result = programMapper.updateByExampleSelective(program2, example);
		//初始化Program_Item_Visit
		ProgramItemExample programItemExample = new ProgramItemExample();
		programItemExample.createCriteria().andProgramIdEqualTo(program.getId());
		List<ProgramItem> programItems = programItemMapper.selectByExample(programItemExample);
		List<ProgramItemVisit> programItemVisits = programItemToProgramItemVisitFiller.fill(programItems);
		for (ProgramItemVisit programItemVisit : programItemVisits) {
			programItemVisitMapper.insertSelective(programItemVisit);
		}
		if(result != 0) {
			return true;
		}else {
			return false;
		}
	}



	private void clearVisits(String programId) {
		ProgramItemVisitExample programItemVisitExample = new ProgramItemVisitExample();
		programItemVisitExample.createCriteria().andProgramIdEqualTo(programId);
		programItemVisitMapper.deleteByExample(programItemVisitExample);
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

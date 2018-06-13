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

import com.jimi.smt.eps_server.entity.Display;
import com.jimi.smt.eps_server.entity.DisplayExample;
import com.jimi.smt.eps_server.entity.Program;
import com.jimi.smt.eps_server.entity.ProgramExample;
import com.jimi.smt.eps_server.entity.ProgramItem;
import com.jimi.smt.eps_server.entity.ProgramItemExample;
import com.jimi.smt.eps_server.entity.ProgramItemVisit;
import com.jimi.smt.eps_server.entity.ProgramItemVisitExample;
import com.jimi.smt.eps_server.entity.bo.EditProgramItemBO;
import com.jimi.smt.eps_server.entity.filler.ProgramItemToProgramItemVOFiller;
import com.jimi.smt.eps_server.entity.filler.ProgramItemToProgramItemVisitFiller;
import com.jimi.smt.eps_server.entity.filler.ProgramToProgramVOFiller;
import com.jimi.smt.eps_server.entity.vo.ProgramItemVO;
import com.jimi.smt.eps_server.entity.vo.ProgramVO;
import com.jimi.smt.eps_server.mapper.DisplayMapper;
import com.jimi.smt.eps_server.mapper.ProgramItemMapper;
import com.jimi.smt.eps_server.mapper.ProgramItemVisitMapper;
import com.jimi.smt.eps_server.mapper.ProgramMapper;
import com.jimi.smt.eps_server.service.ProgramService;
import com.jimi.smt.eps_server.timer.TimeoutTimer;
import com.jimi.smt.eps_server.util.ExcelSpringHelper;
import com.jimi.smt.eps_server.util.ResultUtil;

import cc.darhao.dautils.api.FieldUtil;
import cc.darhao.dautils.api.UuidUtil;

@Service
public class ProgramServiceImpl implements ProgramService {

	@Autowired
	private ProgramMapper programMapper;
	@Autowired
	private ProgramItemMapper programItemMapper;
	@Autowired
	private ProgramItemVisitMapper programItemVisitMapper;
	@Autowired
	private DisplayMapper displayMapper;
	@Autowired
	private ProgramToProgramVOFiller programToProgramVOFiller;
	@Autowired
	private ProgramItemToProgramItemVOFiller programItemToProgramItemVOFiller;
	@Autowired
	private ProgramItemToProgramItemVisitFiller programItemToProgramItemVisitFiller;
	@Autowired
	private TimeoutTimer timeoutTimer;
	
	
	@Override
	public List<Map<String, Object>> upload(MultipartFile programFile, Integer boardType) throws IOException {
		//读文件
		ExcelSpringHelper helper = ExcelSpringHelper.from(programFile);
		
		//初始化结果
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		
		//因添加流水号而导致的表格列号偏移量
		int offset = 1;
		
		//因添加流水号而进行的站位表校验
		if(helper.getString(0, 0) != null && !"".equals(helper.getString(0, 0))) {
			throw new RuntimeException("站位表版本错误：请使用带有序列号的站位表");
		}
		
		//校验头部
		final String header = "SMT FEEDER LIST";
		if(!header.equals(helper.getString(1, 0 + offset))) {
			throw new RuntimeException("头部错误：没有找到\"SMT FEEDER LIST\"标题栏");
		}
		
		//分割解析工单和线号
		String[] workOrders = helper.getString(4, 6 + offset).split("\\+");
		String[] lines = helper.getString(3, 6 + offset).split("\\+");
		
		//创建所有工单
		List<Program> programs = new ArrayList<Program>(workOrders.length * lines.length);
		for (String line : lines) {
			for (String workOrder : workOrders) {
				Program program = new Program();
				program.setClient(helper.getString(2, 1 + offset));
				program.setMachineName(helper.getString(2, 4 + offset));
				program.setVersion(helper.getString(2, 6 + offset));
				program.setMachineConfig(helper.getString(3, 1 + offset));
				program.setProgramNo(helper.getString(3, 4 + offset));
				program.setLine(line);
				program.setEffectiveDate(helper.getDate(4, 1 + offset).toString());
				program.setPcbNo(helper.getString(4, 4 + offset));
				program.setBom(helper.getString(5, 1 + offset));
				program.setProgramName(helper.getString(6, 1 + offset));
				program.setAuditor(helper.getString(7, 4 + offset).substring(3));
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
					if(helper.getString(j, 0 + offset).equals("")) {
						int temp = (int) result.get("real_parse_num");
						result.put("real_parse_num", temp--);
						break;
					}
					//排除手盖
					String lineseat = helper.getString(j, 0 + offset);
					if("手盖".equals(lineseat)) {
						continue;
					}
					programItem.setLineseat(formatLineseat(lineseat));
					programItem.setMaterialNo(helper.getString(j, 1 + offset));
					programItem.setAlternative(helper.getBoolean(j, 2 + offset));
					programItem.setSpecitification(helper.getString(j, 3 + offset));
					programItem.setPosition(helper.getString(j, 4 + offset));
					programItem.setQuantity(helper.getInt(j, 5 + offset));
					programItem.setSerialNo(helper.getInt(j, -1 + offset));
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
	public String updateItem(List<EditProgramItemBO> BOs) {
		//获取ProgramId
		String oldProgramId = BOs.get(0).getProgramId();
		String newProgramId = UuidUtil.get32UUID();
		
		//判断旧Program是否存在
		ProgramExample programExample = new ProgramExample();
		programExample.createCriteria().andIdEqualTo(oldProgramId);
		List<Program> programs = programMapper.selectByExample(programExample);
		if(programs == null || programs.isEmpty()) {
			ResultUtil.failed("找不到站位表，ID不存在");
			throw new RuntimeException("failed_program_not_found");
		}
		
		//创建新Program
		Program newProgram = new Program();
		
		//从旧Program复制属性
		Program oldProgram = programs.get(0);
		FieldUtil.copy(oldProgram, newProgram);
		
		//根据旧Program状态设置新Program状态
		if(oldProgram.getState() > 1) {
			ResultUtil.failed("只能编辑未开始和进行中的工单");
			throw new RuntimeException("failed_illegal_state");
		}else if(oldProgram.getState() == 1){
			newProgram.setState(1);
		}
		
		//作废旧Program
		oldProgram.setState(3);
		
		//对新Program进行赋值
		newProgram.setId(newProgramId);
		newProgram.setCreateTime(new Date());
		
		//提交新旧Program
		programMapper.updateByPrimaryKey(oldProgram);
		programMapper.insert(newProgram);
		
		//获取旧的Program_Item，根据修改记录，生成新的Item并插入
		ProgramItemExample programItemExample = new ProgramItemExample();
		programItemExample.createCriteria().andProgramIdEqualTo(oldProgramId);
		List<ProgramItem> items = programItemMapper.selectByExample(programItemExample);
		for (EditProgramItemBO bo : BOs) {
			ProgramItem newItem = new ProgramItem();
			FieldUtil.copy(bo, newItem);
			for (int i = 0; i < items.size() ; i++) {
				ProgramItem programItem = items.get(i); 
				//匹配记录
				if(bo.getTargetLineseat().equals(programItem.getLineseat()) && bo.getTargetMaterialNo().equals(programItem.getMaterialNo())) {
					int index = items.indexOf(programItem);
					//执行编辑操作
					switch (bo.getOperation()) {
					case 0:
						items.add(index, newItem);
						break;
					case 1:
						items.set(index, newItem);
						break;
					case 2:
						items.remove(index);
						break;
					default:
						break;
					}
					break;
				}else if(bo.getTargetLineseat().equals("") && bo.getTargetMaterialNo().equals("") && bo.getOperation() == 0) {
					//如果目标站位和料号为空并且操作类型为增加，则追加在列表尾部
					items.add(newItem);
					break;
				}
			}
		}
		//插入数据库
		try {
			for (ProgramItem programItem : items) {
				//设置新id
				programItem.setProgramId(newProgramId);
				//插入
				programItemMapper.insert(programItem);
			}
		} catch (DuplicateKeyException e) {
			//主键重复
			ResultUtil.failed("Item主键重复");
			throw new RuntimeException("failed_item_key_duplicate");
		}
		
		//如果状态为进行中，则用旧visit表数据覆盖新visit数据（匹配的记录就覆盖，不匹配的就跳过）
		if(newProgram.getState() == 1) {
			List<ProgramItemVisit> newVisits = programItemToProgramItemVisitFiller.fill(items);
			ProgramItemVisitExample programItemVisitExample = new ProgramItemVisitExample();
			programItemVisitExample.createCriteria().andProgramIdEqualTo(oldProgramId);
			List<ProgramItemVisit> oldVisits = programItemVisitMapper.selectByExample(programItemVisitExample);
			if(oldVisits == null || oldVisits.isEmpty()) {
				ResultUtil.failed("找不到VisitItem，ID不存在");
				throw new RuntimeException("failed_visit_not_found");
			}
			for (ProgramItemVisit newVisit : newVisits) {
				for (ProgramItemVisit oldVisit : oldVisits) {
					if(newVisit.getLineseat().equals(oldVisit.getLineseat()) && newVisit.getMaterialNo().equals(oldVisit.getMaterialNo())) {
						//覆盖数据
						FieldUtil.copy(oldVisit, newVisit);
						//设置新id
						newVisit.setProgramId(newProgramId);
					}
					//删除旧visit数据
					programItemVisitMapper.deleteByPrimaryKey(oldVisit);
				}
				//插入新visit数据
				programItemVisitMapper.insert(newVisit);
			}
		}
		return "succeed";
	}


	@Override
	public boolean cancel(String id) {
		ProgramExample example = new ProgramExample();
		example.createCriteria().andIdEqualTo(id);
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
	public boolean finish(String id) {
		ProgramExample example = new ProgramExample();
		example.createCriteria().andIdEqualTo(id);
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
	public String start(String id) {
		ProgramExample example = new ProgramExample();
		example.createCriteria().andIdEqualTo(id);
		//状态判断
		List<Program> programs = programMapper.selectByExample(example);
		if(programs.isEmpty()) {
			ResultUtil.failed("id不存在");
			return "failed_id_not_exist";
		}
		Program program = programs.get(0);
		if(program.getState() >= 1) {
			ResultUtil.failed("状态不可逆");
			return "failed_state_error";
		}
		//判断是否存在已开始的相同工单（相同的定义：工单号、板面类型、线号均一致）
		ProgramExample example2 = new ProgramExample();
		example2.createCriteria()
			.andLineEqualTo(program.getLine())
			.andWorkOrderEqualTo(program.getWorkOrder())
			.andBoardTypeEqualTo(program.getBoardType())
			.andStateEqualTo(1);
		List<Program> programs2 = programMapper.selectByExample(example2);
		if(!programs2.isEmpty()) {
			ResultUtil.failed("已存在相同的正在进行的工单");
			return "failed_already_started";
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
			return "succeed";
		}else {
			return "failed_unknown";
		}
	}



	@Override
	public String switchWorkOrder(String line, String workOrder, Integer boardType) {
		if(line == null || line.equals("")) {
			return "succeed";
		}
		//获取Display
		DisplayExample displayExample = new DisplayExample();
		displayExample.createCriteria()
			.andLineEqualTo(line);
		Display display = displayMapper.selectByExample(displayExample).get(0);
		//判断是否是停止监控
		if(workOrder == null && boardType == null) {
			display.setWorkOrder(null);
			display.setBoardType(null);
		}else {
			List<ProgramItemVisit> programItemVisits = getVisits(line, workOrder, boardType);
			if(programItemVisits.isEmpty()) {
				return "failed_not_exist";
			}
			display.setWorkOrder(workOrder);
			display.setBoardType(boardType);
		}
		displayMapper.updateByPrimaryKey(display);
		return "succeed";
	}


	@Override
	public String operate(String line, String workOrder, Integer boardType, Integer type, String lineseat,
			String materialNo, String scanLineseat, String scanMaterialNo, Integer operationResult) {
		List<ProgramItemVisit> programItemVisits = getVisits(line, workOrder, boardType);
		if(programItemVisits.isEmpty()) {
			return "failed_not_exist";
		}
		for (ProgramItemVisit programItemVisit : programItemVisits) {
			if(programItemVisit.getLineseat().equals(lineseat) && programItemVisit.getMaterialNo().equals(materialNo)) {
				programItemVisit.setScanLineseat(scanLineseat);
				programItemVisit.setScanMaterialNo(scanMaterialNo);
				switch (type) {
				//核料
				case 2:
					programItemVisit.setLastOperationType(2);
					programItemVisit.setLastOperationTime(new Date());
					programItemVisit.setCheckTime(new Date());
					programItemVisit.setCheckResult(operationResult);
					//如果核料成功，把换料结果也置为成功
					if(operationResult == 1) {
						programItemVisit.setChangeResult(1);
					}
					break;
				//全检
				case 3:
					programItemVisit.setLastOperationType(3);
					programItemVisit.setLastOperationTime(new Date());
					programItemVisit.setCheckAllTime(new Date());
					programItemVisit.setCheckAllResult(operationResult);
					break;
				default:
					break;
				}
				synchronized (timeoutTimer.getLock(line)) {
					updateVisit(programItemVisit);
				}
				return "succeed";
			}
		}
		return "failed_not_exist_item";
	}


	@Override
	public String reset(String line, String workOrder, Integer boardType) {
		List<ProgramItemVisit> programItemVisits = getVisits(line, workOrder, boardType);
		if(programItemVisits.isEmpty()) {
			return "failed_not_exist";
		}
		synchronized (timeoutTimer.getLock(line)) {
			for (ProgramItemVisit programItemVisit : programItemVisits) {
				programItemVisit.setLastOperationTime(new Date());
				programItemVisit.setLastOperationType(null);
				programItemVisit.setFeedResult(2);
				programItemVisit.setChangeResult(2);
				programItemVisit.setCheckResult(2);
				programItemVisit.setCheckAllResult(2);
				programItemVisit.setFirstCheckAllResult(2);
				updateVisit(programItemVisit);
			}
		}
		return "succeed";
	}

	
	@Override
	public List<ProgramItemVisit> getVisits(String line, String workOrder, Integer boardType){
		ProgramExample programExample = new ProgramExample();
		programExample.createCriteria()
			.andLineEqualTo(line)
			.andWorkOrderEqualTo(workOrder)
			.andBoardTypeEqualTo(boardType)
			.andStateEqualTo(1);
		List<Program> programs = programMapper.selectByExample(programExample);
		if(! programs.isEmpty()) {
			String programId = programs.get(0).getId();
			//查询Visits
			ProgramItemVisitExample programItemVisitExample = new ProgramItemVisitExample();
			programItemVisitExample.createCriteria()
				.andProgramIdEqualTo(programId);
			return programItemVisitMapper.selectByExample(programItemVisitExample);
		}
		return new ArrayList<ProgramItemVisit>();
	}
	

	@Override
	public void updateVisit(ProgramItemVisit visit) {
		programItemVisitMapper.updateByPrimaryKey(visit);
	}
	
	
	@Override
	public List<Display> listDisplays() {
		return displayMapper.selectByExample(null);
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

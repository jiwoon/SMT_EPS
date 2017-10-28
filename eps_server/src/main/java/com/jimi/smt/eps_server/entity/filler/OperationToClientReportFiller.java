package com.jimi.smt.eps_server.entity.filler;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jimi.smt.eps_server.entity.Operation;
import com.jimi.smt.eps_server.entity.ProgramBackup;
import com.jimi.smt.eps_server.entity.ProgramItemBackup;
import com.jimi.smt.eps_server.entity.vo.ClientReport;
import com.jimi.smt.eps_server.mapper.ProgramBackupMapper;
import com.jimi.smt.eps_server.mapper.ProgramItemBackupMapper;
import com.jimi.smt.eps_server.util.VoFieldFiller;

@Component
public class OperationToClientReportFiller extends VoFieldFiller<Operation, ClientReport> {

	@Autowired
	private ProgramItemBackupMapper programItemBackupMapper;
	
	private List<ProgramItemBackup> programItemBackups;
	
	@Autowired
	private ProgramBackupMapper programBackupMapper;
	
	private List<ProgramBackup> programBackups;
	
	@PostConstruct
	public void init() {
		programItemBackups = programItemBackupMapper.selectByExample(null);
		programBackups = programBackupMapper.selectByExample(null);
	}
	
	@Override
	public ClientReport fill(Operation operation) {
		ClientReport clientReport = new ClientReport();
		BeanUtils.copyProperties(operation, clientReport);
		
		//匹配程序表子项目和操作日志
		for (ProgramItemBackup programItemBackup : programItemBackups) {
			if(programItemBackup.getProgramId().equals(operation.getFileid()) 
				&& programItemBackup.getLineseat().equals(operation.getLineseat())
				&& programItemBackup.getMaterialNo().equals(operation.getMaterialNo())
			) {
				//解析料描述和料规格
				try {
					String specitification = programItemBackup.getSpecitification();
					String materialDescription = specitification.substring(0, specitification.indexOf(","));
					String temp = specitification.substring(specitification.indexOf(";") + 5, specitification.lastIndexOf(";") - 4);
					if(!temp.equals(materialDescription)) {
						throw new RuntimeException("规格格式不规范");
					}
					String materialSpecitification = specitification.substring(specitification.indexOf(",") + 1, specitification.indexOf(";"));
					clientReport.setMaterialDescription(materialDescription);
					clientReport.setMaterialSpecitification(materialSpecitification);
				}catch (RuntimeException e) {
					clientReport.setMaterialDescription("规格格式不规范");
					clientReport.setMaterialSpecitification("规格格式不规范");
				}
				break;
			}
		}
		//解析操作类型
		switch (operation.getType()) {
		case 0:
			clientReport.setOperationType("上料");
			break;
		case 1:
			clientReport.setOperationType("换料");
			break;
		case 2:
			clientReport.setOperationType("检料");
			break;
		case 3:
			clientReport.setOperationType("核对全料");
			break;
		default:
			break;
		}
		
		//填写线别
		for (ProgramBackup programBackup : programBackups) {
			if(programBackup.getId().equals(operation.getFileid())){
				clientReport.setLine(programBackup.getLine());
				break;
			}
		}
		
		clientReport.setOrderNo("-");
		clientReport.setWorkOrderNo("-");
		
		return clientReport;
	}

}

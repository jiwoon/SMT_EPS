package com.jimi.smt.eps_server.test;

import java.text.ParseException;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jimi.smt.eps_server.base.BaseJunit4Test;
import com.jimi.smt.eps_server.entity.vo.ClientReport;
import com.jimi.smt.eps_server.service.OperationService;
import com.jimi.smt.eps_server.util.FieldUtil;

public class ClientReportTest extends BaseJunit4Test{

	@Autowired
	private OperationService operationService;
	
	@Test
	public void list() throws ParseException {
		System.out.println(System.currentTimeMillis());
		List<ClientReport> clientReports = operationService.listClientReport("XGD", null,null,null,null,null,null);
		System.out.println(System.currentTimeMillis());
		for (ClientReport clientReport : clientReports) {
			FieldUtil.print(clientReport);
		}
	}
	
}

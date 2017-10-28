package com.jimi.smt.eps_server.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jimi.smt.eps_server.base.BaseJunit4Test;
import com.jimi.smt.eps_server.service.OperationService;
import com.jimi.smt.eps_server.util.FieldUtil;

public class DisplayReportTest extends BaseJunit4Test {

	@Autowired
	private OperationService operationService;
	
	@Test
	public void list() {
		for (int i = 0; i < 100; i++) {
			long a = System.currentTimeMillis();
			com.jimi.smt.eps_server.entity.vo.DisplayReport displayReport = operationService.listDisplayReport("308");
	//		FieldUtil.print(displayReport);
			long b = System.currentTimeMillis();
			System.out.println(b - a + "毫秒");
		}
	}
	
}

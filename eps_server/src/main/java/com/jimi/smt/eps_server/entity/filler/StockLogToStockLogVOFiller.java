package com.jimi.smt.eps_server.entity.filler;

import java.text.SimpleDateFormat;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.jimi.smt.eps_server.entity.StockLog;
import com.jimi.smt.eps_server.entity.vo.StockLogVO;
import com.jimi.smt.eps_server.util.EntityFieldFiller;

@Component
public class StockLogToStockLogVOFiller extends EntityFieldFiller<StockLog, StockLogVO> {

	@Override
	public StockLogVO fill(StockLog stockLog) {
		StockLogVO stockLogVO = new StockLogVO();
		BeanUtils.copyProperties(stockLog, stockLogVO);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		stockLogVO.setOperationTimeString(sdf.format(stockLogVO.getOperationTime()));
		return stockLogVO;
	}

}

package com.jimi.smt.eps.printer.mapper;

import com.jimi.smt.eps.printer.entity.StockLog;
import com.jimi.smt.eps.printer.entity.StockLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StockLogMapper {
    long countByExample(StockLogExample example);

    int deleteByExample(StockLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StockLog record);

    int insertSelective(StockLog record);

    List<StockLog> selectByExample(StockLogExample example);

    StockLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StockLog record, @Param("example") StockLogExample example);

    int updateByExample(@Param("record") StockLog record, @Param("example") StockLogExample example);

    int updateByPrimaryKeySelective(StockLog record);

    int updateByPrimaryKey(StockLog record);
}
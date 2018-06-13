package com.jimi.smt.eps_server.mapper;

import com.jimi.smt.eps_server.entity.ProgramItem;
import com.jimi.smt.eps_server.entity.ProgramItemExample;
import com.jimi.smt.eps_server.entity.ProgramItemKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProgramItemMapper {
    int countByExample(ProgramItemExample example);

    int deleteByExample(ProgramItemExample example);

    int deleteByPrimaryKey(ProgramItemKey key);

    int insert(ProgramItem record);

    int insertSelective(ProgramItem record);

    List<ProgramItem> selectByExample(ProgramItemExample example);

    ProgramItem selectByPrimaryKey(ProgramItemKey key);

    int updateByExampleSelective(@Param("record") ProgramItem record, @Param("example") ProgramItemExample example);

    int updateByExample(@Param("record") ProgramItem record, @Param("example") ProgramItemExample example);

    int updateByPrimaryKeySelective(ProgramItem record);

    int updateByPrimaryKey(ProgramItem record);
}
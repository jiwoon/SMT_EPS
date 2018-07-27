package com.jimi.smt.eps.serversocket.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.jimi.smt.eps.serversocket.entity.Program;
import com.jimi.smt.eps.serversocket.entity.ProgramExample;

public interface ProgramMapper {
    int countByExample(ProgramExample example);

    int deleteByExample(ProgramExample example);

    int deleteByPrimaryKey(String id);

    int insert(Program record);

    int insertSelective(Program record);

    List<Program> selectByExample(ProgramExample example);

    Program selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Program record, @Param("example") ProgramExample example);

    int updateByExample(@Param("record") Program record, @Param("example") ProgramExample example);

    int updateByPrimaryKeySelective(Program record);

    int updateByPrimaryKey(Program record);
}
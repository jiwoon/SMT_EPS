package com.jimi.smt.eps.display.mapper;

import com.jimi.smt.eps.display.entity.Program;
import com.jimi.smt.eps.display.entity.ProgramItemVisit;
import com.jimi.smt.eps.display.entity.ProgramItemVisitExample;
import com.jimi.smt.eps.display.entity.ProgramItemVisitKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProgramItemVisitMapper {
    long countByExample(ProgramItemVisitExample example);

    int deleteByExample(ProgramItemVisitExample example);

    int deleteByPrimaryKey(ProgramItemVisitKey key);

    int insert(ProgramItemVisit record);

    int insertSelective(ProgramItemVisit record);

    List<ProgramItemVisit> selectByExample(ProgramItemVisitExample example);

    ProgramItemVisit selectByPrimaryKey(ProgramItemVisitKey key);

    int updateByExampleSelective(@Param("record") ProgramItemVisit record, @Param("example") ProgramItemVisitExample example);

    int updateByExample(@Param("record") ProgramItemVisit record, @Param("example") ProgramItemVisitExample example);

    int updateByPrimaryKeySelective(ProgramItemVisit record);

    int updateByPrimaryKey(ProgramItemVisit record);
    
    List<ProgramItemVisit> selectByProgram(Program record);
}
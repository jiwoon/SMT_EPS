package com.jimi.smt.eps_server.mapper;

import com.jimi.smt.eps_server.entity.ProgramItemVisit;
import com.jimi.smt.eps_server.entity.ProgramItemVisitExample;
import com.jimi.smt.eps_server.entity.ProgramItemVisitKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProgramItemVisitMapper {
    int countByExample(ProgramItemVisitExample example);

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
}
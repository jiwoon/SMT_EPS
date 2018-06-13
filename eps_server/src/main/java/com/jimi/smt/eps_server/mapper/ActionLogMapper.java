package com.jimi.smt.eps_server.mapper;

import com.jimi.smt.eps_server.entity.ActionLog;
import com.jimi.smt.eps_server.entity.ActionLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ActionLogMapper {
    int countByExample(ActionLogExample example);

    int deleteByExample(ActionLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActionLog record);

    int insertSelective(ActionLog record);

    List<ActionLog> selectByExample(ActionLogExample example);

    ActionLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActionLog record, @Param("example") ActionLogExample example);

    int updateByExample(@Param("record") ActionLog record, @Param("example") ActionLogExample example);

    int updateByPrimaryKeySelective(ActionLog record);

    int updateByPrimaryKey(ActionLog record);
}
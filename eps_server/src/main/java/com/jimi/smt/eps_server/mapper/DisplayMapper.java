package com.jimi.smt.eps_server.mapper;

import com.jimi.smt.eps_server.entity.Display;
import com.jimi.smt.eps_server.entity.DisplayExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DisplayMapper {
    int countByExample(DisplayExample example);

    int deleteByExample(DisplayExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Display record);

    int insertSelective(Display record);

    List<Display> selectByExample(DisplayExample example);

    Display selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Display record, @Param("example") DisplayExample example);

    int updateByExample(@Param("record") Display record, @Param("example") DisplayExample example);

    int updateByPrimaryKeySelective(Display record);

    int updateByPrimaryKey(Display record);
}
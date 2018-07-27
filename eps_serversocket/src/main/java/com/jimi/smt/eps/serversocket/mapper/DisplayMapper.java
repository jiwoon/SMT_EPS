package com.jimi.smt.eps.serversocket.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.jimi.smt.eps.serversocket.entity.Display;
import com.jimi.smt.eps.serversocket.entity.DisplayExample;

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
package com.jimi.smt.eps_server.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.jimi.smt.eps_server.entity.ProgramItem;
import com.jimi.smt.eps_server.entity.ProgramItemExample;
import com.jimi.smt.eps_server.entity.ProgramItemKey;

public interface ProgramItemMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table program_item
     *
     * @mbggenerated Sat Oct 28 10:49:05 CST 2017
     */
    int countByExample(ProgramItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table program_item
     *
     * @mbggenerated Sat Oct 28 10:49:05 CST 2017
     */
    int deleteByExample(ProgramItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table program_item
     *
     * @mbggenerated Sat Oct 28 10:49:05 CST 2017
     */
    int deleteByPrimaryKey(ProgramItemKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table program_item
     *
     * @mbggenerated Sat Oct 28 10:49:05 CST 2017
     */
    int insert(ProgramItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table program_item
     *
     * @mbggenerated Sat Oct 28 10:49:05 CST 2017
     */
    int insertSelective(ProgramItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table program_item
     *
     * @mbggenerated Sat Oct 28 10:49:05 CST 2017
     */
    List<ProgramItem> selectByExample(ProgramItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table program_item
     *
     * @mbggenerated Sat Oct 28 10:49:05 CST 2017
     */
    ProgramItem selectByPrimaryKey(ProgramItemKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table program_item
     *
     * @mbggenerated Sat Oct 28 10:49:05 CST 2017
     */
    int updateByExampleSelective(@Param("record") ProgramItem record, @Param("example") ProgramItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table program_item
     *
     * @mbggenerated Sat Oct 28 10:49:05 CST 2017
     */
    int updateByExample(@Param("record") ProgramItem record, @Param("example") ProgramItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table program_item
     *
     * @mbggenerated Sat Oct 28 10:49:05 CST 2017
     */
    int updateByPrimaryKeySelective(ProgramItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table program_item
     *
     * @mbggenerated Sat Oct 28 10:49:05 CST 2017
     */
    int updateByPrimaryKey(ProgramItem record);
}
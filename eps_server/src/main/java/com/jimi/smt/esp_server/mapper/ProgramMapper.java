package com.jimi.smt.esp_server.mapper;

import com.jimi.smt.esp_server.entity.Program;
import com.jimi.smt.esp_server.entity.ProgramExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProgramMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table program
     *
     * @mbggenerated Tue Sep 26 15:29:29 CST 2017
     */
    int countByExample(ProgramExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table program
     *
     * @mbggenerated Tue Sep 26 15:29:29 CST 2017
     */
    int deleteByExample(ProgramExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table program
     *
     * @mbggenerated Tue Sep 26 15:29:29 CST 2017
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table program
     *
     * @mbggenerated Tue Sep 26 15:29:29 CST 2017
     */
    int insert(Program record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table program
     *
     * @mbggenerated Tue Sep 26 15:29:29 CST 2017
     */
    int insertSelective(Program record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table program
     *
     * @mbggenerated Tue Sep 26 15:29:29 CST 2017
     */
    List<Program> selectByExample(ProgramExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table program
     *
     * @mbggenerated Tue Sep 26 15:29:29 CST 2017
     */
    Program selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table program
     *
     * @mbggenerated Tue Sep 26 15:29:29 CST 2017
     */
    int updateByExampleSelective(@Param("record") Program record, @Param("example") ProgramExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table program
     *
     * @mbggenerated Tue Sep 26 15:29:29 CST 2017
     */
    int updateByExample(@Param("record") Program record, @Param("example") ProgramExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table program
     *
     * @mbggenerated Tue Sep 26 15:29:29 CST 2017
     */
    int updateByPrimaryKeySelective(Program record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table program
     *
     * @mbggenerated Tue Sep 26 15:29:29 CST 2017
     */
    int updateByPrimaryKey(Program record);
}
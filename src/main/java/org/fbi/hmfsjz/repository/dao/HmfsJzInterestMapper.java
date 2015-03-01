package org.fbi.hmfsjz.repository.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.fbi.hmfsjz.repository.model.HmfsJzInterest;
import org.fbi.hmfsjz.repository.model.HmfsJzInterestExample;

public interface HmfsJzInterestMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_INTEREST
     *
     * @mbggenerated Wed Oct 22 15:31:58 CST 2014
     */
    int countByExample(HmfsJzInterestExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_INTEREST
     *
     * @mbggenerated Wed Oct 22 15:31:58 CST 2014
     */
    int deleteByExample(HmfsJzInterestExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_INTEREST
     *
     * @mbggenerated Wed Oct 22 15:31:58 CST 2014
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_INTEREST
     *
     * @mbggenerated Wed Oct 22 15:31:58 CST 2014
     */
    int insert(HmfsJzInterest record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_INTEREST
     *
     * @mbggenerated Wed Oct 22 15:31:58 CST 2014
     */
    int insertSelective(HmfsJzInterest record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_INTEREST
     *
     * @mbggenerated Wed Oct 22 15:31:58 CST 2014
     */
    List<HmfsJzInterest> selectByExample(HmfsJzInterestExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_INTEREST
     *
     * @mbggenerated Wed Oct 22 15:31:58 CST 2014
     */
    HmfsJzInterest selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_INTEREST
     *
     * @mbggenerated Wed Oct 22 15:31:58 CST 2014
     */
    int updateByExampleSelective(@Param("record") HmfsJzInterest record, @Param("example") HmfsJzInterestExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_INTEREST
     *
     * @mbggenerated Wed Oct 22 15:31:58 CST 2014
     */
    int updateByExample(@Param("record") HmfsJzInterest record, @Param("example") HmfsJzInterestExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_INTEREST
     *
     * @mbggenerated Wed Oct 22 15:31:58 CST 2014
     */
    int updateByPrimaryKeySelective(HmfsJzInterest record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_INTEREST
     *
     * @mbggenerated Wed Oct 22 15:31:58 CST 2014
     */
    int updateByPrimaryKey(HmfsJzInterest record);
}
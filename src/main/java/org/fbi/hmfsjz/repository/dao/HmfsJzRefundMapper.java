package org.fbi.hmfsjz.repository.dao;

import org.fbi.hmfsjz.repository.model.HmfsJzRefund;
import org.fbi.hmfsjz.repository.model.HmfsJzRefundExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HmfsJzRefundMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_REFUND
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int countByExample(HmfsJzRefundExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_REFUND
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int deleteByExample(HmfsJzRefundExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_REFUND
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_REFUND
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int insert(HmfsJzRefund record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_REFUND
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int insertSelective(HmfsJzRefund record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_REFUND
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    List<HmfsJzRefund> selectByExample(HmfsJzRefundExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_REFUND
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    HmfsJzRefund selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_REFUND
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int updateByExampleSelective(@Param("record") HmfsJzRefund record, @Param("example") HmfsJzRefundExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_REFUND
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int updateByExample(@Param("record") HmfsJzRefund record, @Param("example") HmfsJzRefundExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_REFUND
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int updateByPrimaryKeySelective(HmfsJzRefund record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_REFUND
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int updateByPrimaryKey(HmfsJzRefund record);
}
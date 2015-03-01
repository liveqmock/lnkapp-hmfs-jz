package org.fbi.hmfsjz.repository.dao;

import org.fbi.hmfsjz.repository.model.HmfsJzVoucher;
import org.fbi.hmfsjz.repository.model.HmfsJzVoucherExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HmfsJzVoucherMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_VOUCHER
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int countByExample(HmfsJzVoucherExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_VOUCHER
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int deleteByExample(HmfsJzVoucherExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_VOUCHER
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_VOUCHER
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int insert(HmfsJzVoucher record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_VOUCHER
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int insertSelective(HmfsJzVoucher record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_VOUCHER
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    List<HmfsJzVoucher> selectByExample(HmfsJzVoucherExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_VOUCHER
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    HmfsJzVoucher selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_VOUCHER
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int updateByExampleSelective(@Param("record") HmfsJzVoucher record, @Param("example") HmfsJzVoucherExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_VOUCHER
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int updateByExample(@Param("record") HmfsJzVoucher record, @Param("example") HmfsJzVoucherExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_VOUCHER
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int updateByPrimaryKeySelective(HmfsJzVoucher record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_VOUCHER
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    int updateByPrimaryKey(HmfsJzVoucher record);
}
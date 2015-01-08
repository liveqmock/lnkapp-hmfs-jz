package org.fbi.hmfsjz.repository.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.fbi.hmfsjz.repository.model.HmfsJzAct;
import org.fbi.hmfsjz.repository.model.HmfsJzActExample;

public interface HmfsJzActMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_ACT
     *
     * @mbggenerated Fri Oct 17 13:00:52 CST 2014
     */
    int countByExample(HmfsJzActExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_ACT
     *
     * @mbggenerated Fri Oct 17 13:00:52 CST 2014
     */
    int deleteByExample(HmfsJzActExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_ACT
     *
     * @mbggenerated Fri Oct 17 13:00:52 CST 2014
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_ACT
     *
     * @mbggenerated Fri Oct 17 13:00:52 CST 2014
     */
    int insert(HmfsJzAct record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_ACT
     *
     * @mbggenerated Fri Oct 17 13:00:52 CST 2014
     */
    int insertSelective(HmfsJzAct record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_ACT
     *
     * @mbggenerated Fri Oct 17 13:00:52 CST 2014
     */
    List<HmfsJzAct> selectByExample(HmfsJzActExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_ACT
     *
     * @mbggenerated Fri Oct 17 13:00:52 CST 2014
     */
    HmfsJzAct selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_ACT
     *
     * @mbggenerated Fri Oct 17 13:00:52 CST 2014
     */
    int updateByExampleSelective(@Param("record") HmfsJzAct record, @Param("example") HmfsJzActExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_ACT
     *
     * @mbggenerated Fri Oct 17 13:00:52 CST 2014
     */
    int updateByExample(@Param("record") HmfsJzAct record, @Param("example") HmfsJzActExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_ACT
     *
     * @mbggenerated Fri Oct 17 13:00:52 CST 2014
     */
    int updateByPrimaryKeySelective(HmfsJzAct record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JM_ACT
     *
     * @mbggenerated Fri Oct 17 13:00:52 CST 2014
     */
    int updateByPrimaryKey(HmfsJzAct record);
}
package org.fbi.hmfsjz.repository.dao;

import org.fbi.hmfsjz.repository.model.HmfsJzActDel;
import org.fbi.hmfsjz.repository.model.HmfsJzActDelExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HmfsJzActDelMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_ACT_DEL
     *
     * @mbggenerated Tue Nov 19 16:19:20 CST 2013
     */
    int countByExample(HmfsJzActDelExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_ACT_DEL
     *
     * @mbggenerated Tue Nov 19 16:19:20 CST 2013
     */
    int deleteByExample(HmfsJzActDelExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_ACT_DEL
     *
     * @mbggenerated Tue Nov 19 16:19:20 CST 2013
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_ACT_DEL
     *
     * @mbggenerated Tue Nov 19 16:19:20 CST 2013
     */
    int insert(HmfsJzActDel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_ACT_DEL
     *
     * @mbggenerated Tue Nov 19 16:19:20 CST 2013
     */
    int insertSelective(HmfsJzActDel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_ACT_DEL
     *
     * @mbggenerated Tue Nov 19 16:19:20 CST 2013
     */
    List<HmfsJzActDel> selectByExample(HmfsJzActDelExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_ACT_DEL
     *
     * @mbggenerated Tue Nov 19 16:19:20 CST 2013
     */
    HmfsJzActDel selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_ACT_DEL
     *
     * @mbggenerated Tue Nov 19 16:19:20 CST 2013
     */
    int updateByExampleSelective(@Param("record") HmfsJzActDel record, @Param("example") HmfsJzActDelExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_ACT_DEL
     *
     * @mbggenerated Tue Nov 19 16:19:20 CST 2013
     */
    int updateByExample(@Param("record") HmfsJzActDel record, @Param("example") HmfsJzActDelExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_ACT_DEL
     *
     * @mbggenerated Tue Nov 19 16:19:20 CST 2013
     */
    int updateByPrimaryKeySelective(HmfsJzActDel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.HMFS_JZ_ACT_DEL
     *
     * @mbggenerated Tue Nov 19 16:19:20 CST 2013
     */
    int updateByPrimaryKey(HmfsJzActDel record);
}
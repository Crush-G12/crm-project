package com.xie.crm.workbench.mapper;

import com.xie.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Fri Apr 22 22:49:49 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Fri Apr 22 22:49:49 CST 2022
     */
    int insert(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Fri Apr 22 22:49:49 CST 2022
     */
    int insertSelective(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Fri Apr 22 22:49:49 CST 2022
     */
    Activity selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Fri Apr 22 22:49:49 CST 2022
     */
    int updateByPrimaryKeySelective(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Fri Apr 22 22:49:49 CST 2022
     */
    int updateByPrimaryKey(Activity record);

    /**
     * 创建市场活动
     */
    int insertActivity(Activity activity);

    /**
     * 根据条件分页查询市场活动
     */
    List<Activity> selectActivityByConditionForPage(Map<String,Object> map);

    /**
     * 根据条件查询市场活动总条数
     */
    int selectCountOfActivityByCondition(Map<String,Object> map);

    /**
     * 根据id删除市场活动
     */
    int deleteActivityByIds(String[] ids);

    /**
     * 根据id查询市场活动
     * @param id
     * @return
     */
    Activity selectActivityById(String id);


    /**
     * 修改市场活动
     * @param activity
     * @return
     */
    int updateActivity(Activity activity);

    /**
     * 查询所有市场活动
     * @return
     */
    List<Activity> selectAllActivities();
}
package com.happychat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description:联系人申请Mapper
 * @author:某某某
 * @date:2024/08/19
 */
@Mapper
public interface UserContactApplyMapper<T, P> extends BaseMapper {

    /**
     * 根据ApplyId查询联系人申请
     */
    T selectByApplyId(@Param("applyId") Integer applyId);

    /**
     * 根据ApplyId更新联系人申请
     */
    Integer updateByApplyId(@Param("bean") T t, @Param("applyId") Integer applyId);

    /**
     * 根据ApplyId删除联系人申请
     */
    Integer deleteByApplyId(@Param("applyId") Integer applyId);

    /**
     * 根据ApplyUserIdAndReceiveUserIdAndContactId查询联系人申请
     */
    T selectByApplyUserIdAndReceiveUserIdAndContactId(@Param("applyUserId") String applyUserId, @Param("receiveUserId") String receiveUserId, @Param("contactId") String contactId);

    /**
     * 根据ApplyUserIdAndReceiveUserIdAndContactId更新联系人申请
     */
    Integer updateByApplyUserIdAndReceiveUserIdAndContactId(@Param("bean") T t, @Param("applyUserId") String applyUserId, @Param("receiveUserId") String receiveUserId, @Param("contactId") String contactId);

    /**
     * 根据ApplyUserIdAndReceiveUserIdAndContactId删除联系人申请
     */
    Integer deleteByApplyUserIdAndReceiveUserIdAndContactId(@Param("applyUserId") String applyUserId, @Param("receiveUserId") String receiveUserId, @Param("contactId") String contactId);


}
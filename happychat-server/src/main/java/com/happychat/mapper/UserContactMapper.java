package com.happychat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
/**
 * @Description:联系人Mapper
 * @author:某某某
 * @date:2024/08/19
 */
@Mapper
public interface UserContactMapper<T, P> extends BaseMapper {

	/**
	 * 根据UserIdAndContactId查询联系人
	 */
	T selectByUserIdAndContactId(@Param("userId") String userId, @Param("contactId") String contactId);

	/**
	 * 根据UserIdAndContactId更新联系人
	 */
	Integer updateByUserIdAndContactId(@Param("bean") T t, @Param("userId") String userId, @Param("contactId") String contactId);

	/**
	 * 根据UserIdAndContactId删除联系人
	 */
	Integer deleteByUserIdAndContactId(@Param("userId") String userId, @Param("contactId") String contactId);

}
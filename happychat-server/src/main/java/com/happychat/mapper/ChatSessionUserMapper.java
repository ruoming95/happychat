package com.happychat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
/**
 * @Description:会话用户Mapper
 * @author:某某某
 * @date:2024/08/26
 */
@Mapper
public interface ChatSessionUserMapper<T, P> extends BaseMapper {

	/**
	 * 根据UserIdAndContactId查询会话用户
	 */
	T selectByUserIdAndContactId(@Param("userId") String userId, @Param("contactId") String contactId);

	/**
	 * 根据UserIdAndContactId更新会话用户
	 */
	Integer updateByUserIdAndContactId(@Param("bean") T t, @Param("userId") String userId, @Param("contactId") String contactId);

	/**
	 * 根据UserIdAndContactId删除会话用户
	 */
	Integer deleteByUserIdAndContactId(@Param("userId") String userId, @Param("contactId") String contactId);

}
package com.happychat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
/**
 * @Description:聊天消息表Mapper
 * @author:某某某
 * @date:2024/08/26
 */
@Mapper
public interface ChatMessageMapper<T, P> extends BaseMapper {

	/**
	 * 根据MessageId查询聊天消息表
	 */
	T selectByMessageId(@Param("messageId") Long messageId);

	/**
	 * 根据MessageId更新聊天消息表
	 */
	Integer updateByMessageId(@Param("bean") T t, @Param("messageId") Long messageId);

	/**
	 * 根据MessageId删除聊天消息表
	 */
	Integer deleteByMessageId(@Param("messageId") Long messageId);

}
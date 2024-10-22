package com.happychat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
/**
 * @Description:会话信息Mapper
 * @author:某某某
 * @date:2024/08/26
 */
@Mapper
public interface ChatSessionMapper<T, P> extends BaseMapper {

	/**
	 * 根据SessionId查询会话信息
	 */
	T selectBySessionId(@Param("sessionId") String sessionId);

	/**
	 * 根据SessionId更新会话信息
	 */
	Integer updateBySessionId(@Param("bean") T t, @Param("sessionId") String sessionId);

	/**
	 * 根据SessionId删除会话信息
	 */
	Integer deleteBySessionId(@Param("sessionId") String sessionId);

}
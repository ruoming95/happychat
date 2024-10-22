package com.happychat.service;

import com.happychat.entity.pojo.ChatSession;
import com.happychat.entity.query.ChatSessionQuery;
import com.happychat.entity.vo.ResultVo;

import java.util.List;

/**
 * @Description:会话信息Service
 * @author:某某某
 * @date:2024/08/26
 */
public interface ChatSessionService {

	/**
	 * 根据条件查询列表
	 */
	List<ChatSession> findListByParam(ChatSessionQuery query);

	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(ChatSessionQuery query);

	/**
	 * 分页查询
	 */
	ResultVo<ChatSession> findListByPage(ChatSessionQuery query);

	/**
	 * 新增
	 */
	Integer add(ChatSession bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<ChatSession> listBean);

	/**
	 * 批量新增或更新
	 */
	Integer addOrUpdateBatch(List<ChatSession> listBean);

	/**
	 * 根据SessionId查询会话信息
	 */
	ChatSession getChatSessionBySessionId(String sessionId);

	/**
	 * 根据SessionId更新会话信息
	 */
	Integer updateChatSessionBySessionId(ChatSession bean, String sessionId);

	/**
	 * 根据SessionId删除会话信息
	 */
	Integer deleteChatSessionBySessionId(String sessionId);

}
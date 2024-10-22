package com.happychat.service;

import com.happychat.entity.pojo.ChatSessionUser;
import com.happychat.entity.query.ChatSessionUserQuery;
import com.happychat.entity.vo.ResultVo;

import java.util.List;

/**
 * @Description:会话用户Service
 * @author:某某某
 * @date:2024/08/26
 */
public interface ChatSessionUserService {

	/**
	 * 根据条件查询列表
	 */
	List<ChatSessionUser> findListByParam(ChatSessionUserQuery query);

	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(ChatSessionUserQuery query);

	/**
	 * 分页查询
	 */
	ResultVo<ChatSessionUser> findListByPage(ChatSessionUserQuery query);

	/**
	 * 新增
	 */
	Integer add(ChatSessionUser bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<ChatSessionUser> listBean);

	/**
	 * 批量新增或更新
	 */
	Integer addOrUpdateBatch(List<ChatSessionUser> listBean);

	/**
	 * 根据UserIdAndContactId查询会话用户
	 */
	ChatSessionUser getChatSessionUserByUserIdAndContactId(String userId, String contactId);

	/**
	 * 根据UserIdAndContactId更新会话用户
	 */
	Integer updateChatSessionUserByUserIdAndContactId(ChatSessionUser bean, String userId, String contactId);

	/**
	 * 根据UserIdAndContactId删除会话用户
	 */
	Integer deleteChatSessionUserByUserIdAndContactId(String userId, String contactId);

}
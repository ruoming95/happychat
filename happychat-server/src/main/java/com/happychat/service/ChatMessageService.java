package com.happychat.service;

import com.happychat.entity.dto.MessageSendDto;
import com.happychat.entity.dto.TokenUserInfoDto;
import com.happychat.entity.pojo.ChatMessage;
import com.happychat.entity.query.ChatMessageQuery;
import com.happychat.entity.vo.ResultVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * @Description:聊天消息表Service
 * @author:某某某
 * @date:2024/08/26
 */
public interface ChatMessageService {

	/**
	 * 根据条件查询列表
	 */
	List<ChatMessage> findListByParam(ChatMessageQuery query);

	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(ChatMessageQuery query);

	/**
	 * 分页查询
	 */
	ResultVo<ChatMessage> findListByPage(ChatMessageQuery query);

	/**
	 * 新增
	 */
	Integer add(ChatMessage bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<ChatMessage> listBean);

	/**
	 * 批量新增或更新
	 */
	Integer addOrUpdateBatch(List<ChatMessage> listBean);

	/**
	 * 根据MessageId查询聊天消息表
	 */
	ChatMessage getChatMessageByMessageId(Long messageId);

	/**
	 * 根据MessageId更新聊天消息表
	 */
	Integer updateChatMessageByMessageId(ChatMessage bean, Long messageId);

	/**
	 * 根据MessageId删除聊天消息表
	 */
	Integer deleteChatMessageByMessageId(Long messageId);

	MessageSendDto saveChatMessage(ChatMessage chatMessage, TokenUserInfoDto tokenUserInfoDto);

    void saveMessageFile(String userId, Long messageId, MultipartFile file, MultipartFile cover);

    File downloadFile(TokenUserInfoDto tokenUserInfo, Long fileId, Boolean showCover);
}
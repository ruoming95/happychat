package com.happychat.service.impl;

import com.happychat.entity.pojo.ChatSession;
import com.happychat.entity.query.ChatSessionQuery;
import com.happychat.entity.query.SimplePage;
import com.happychat.entity.vo.ResultVo;
import com.happychat.enums.PageSize;
import com.happychat.mapper.ChatSessionMapper;
import com.happychat.service.ChatSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:会话信息ServiceImpl
 * @author:某某某
 * @date:2024/08/26
 */
@Service("ChatSessionService")
public class ChatSessionServiceImpl implements ChatSessionService {

	@Autowired
	private ChatSessionMapper<ChatSession, ChatSessionQuery> chatSessionMapper;

	/**
	 * 根据条件查询列表
	 */
	public List<ChatSession> findListByParam(ChatSessionQuery query) {
		return this.chatSessionMapper.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByParam(ChatSessionQuery query) {
		return this.chatSessionMapper.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	public ResultVo<ChatSession> findListByPage(ChatSessionQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();

		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<ChatSession> list = this.findListByParam(query);
		ResultVo<ChatSession> result = new ResultVo<>(count, pageSize, page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(ChatSession bean){
		return this.chatSessionMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<ChatSession> list) {
		if (list == null || list.size() <= 0 ) {
		return 0;
	}
		return this.chatSessionMapper.insertBatch(list);
		}

	/**
	 * 批量新增或更新
	 */
	public Integer addOrUpdateBatch(List<ChatSession> list) {
		if (list == null || list.size() <= 0 ) {
		return 0;
		}
		return this.chatSessionMapper.insertOrUpdateBatch(list);
	}

	/**
	 * 根据SessionId查询会话信息
	 */
	public ChatSession getChatSessionBySessionId(String sessionId) {
		return this.chatSessionMapper.selectBySessionId(sessionId);
	}

	/**
	 * 根据SessionId更新会话信息
	 */
	public Integer updateChatSessionBySessionId(ChatSession bean, String sessionId) {
		return this.chatSessionMapper.updateBySessionId(bean, sessionId);
	}

	/**
	 * 根据SessionId删除会话信息
	 */
	public Integer deleteChatSessionBySessionId(String sessionId) {
		return this.chatSessionMapper.deleteBySessionId(sessionId);
	}

}
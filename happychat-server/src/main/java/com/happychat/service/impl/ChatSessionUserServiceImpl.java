package com.happychat.service.impl;

import com.happychat.entity.pojo.ChatSessionUser;
import com.happychat.entity.query.ChatSessionUserQuery;
import com.happychat.entity.query.SimplePage;
import com.happychat.entity.vo.ResultVo;
import com.happychat.enums.PageSize;
import com.happychat.mapper.ChatSessionUserMapper;
import com.happychat.service.ChatSessionUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:会话用户ServiceImpl
 * @author:某某某
 * @date:2024/08/26
 */
@Service("ChatSessionUserService")
public class ChatSessionUserServiceImpl implements ChatSessionUserService {

	@Autowired
	private ChatSessionUserMapper<ChatSessionUser, ChatSessionUserQuery> chatSessionUserMapper;

	/**
	 * 根据条件查询列表
	 */
	public List<ChatSessionUser> findListByParam(ChatSessionUserQuery query) {
		return this.chatSessionUserMapper.selectList(query);
	}

	/**
	 * 根据条件查询数量
	 */
	public Integer findCountByParam(ChatSessionUserQuery query) {
		return this.chatSessionUserMapper.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	public ResultVo<ChatSessionUser> findListByPage(ChatSessionUserQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();

		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<ChatSessionUser> list = this.findListByParam(query);
		ResultVo<ChatSessionUser> result = new ResultVo<>(count, pageSize, page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	public Integer add(ChatSessionUser bean){
		return this.chatSessionUserMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	public Integer addBatch(List<ChatSessionUser> list) {
		if (list == null || list.size() <= 0 ) {
		return 0;
	}
		return this.chatSessionUserMapper.insertBatch(list);
		}

	/**
	 * 批量新增或更新
	 */
	public Integer addOrUpdateBatch(List<ChatSessionUser> list) {
		if (list == null || list.size() <= 0 ) {
		return 0;
		}
		return this.chatSessionUserMapper.insertOrUpdateBatch(list);
	}

	/**
	 * 根据UserIdAndContactId查询会话用户
	 */
	public ChatSessionUser getChatSessionUserByUserIdAndContactId(String userId, String contactId) {
		return this.chatSessionUserMapper.selectByUserIdAndContactId(userId, contactId);
	}

	/**
	 * 根据UserIdAndContactId更新会话用户
	 */
	public Integer updateChatSessionUserByUserIdAndContactId(ChatSessionUser bean, String userId, String contactId) {
		return this.chatSessionUserMapper.updateByUserIdAndContactId(bean, userId, contactId);
	}

	/**
	 * 根据UserIdAndContactId删除会话用户
	 */
	public Integer deleteChatSessionUserByUserIdAndContactId(String userId, String contactId) {
		return this.chatSessionUserMapper.deleteByUserIdAndContactId(userId, contactId);
	}

}
package com.happychat.service;

import com.happychat.entity.dto.GroupInfoDto;
import com.happychat.entity.dto.TokenUserInfoDto;
import com.happychat.entity.pojo.GroupInfo;
import com.happychat.entity.query.GroupInfoQuery;
import com.happychat.entity.vo.ResultVo;
import com.happychat.enums.MessageTypeEnum;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Description:Service
 * @author:某某某
 * @date:2024/08/19
 */
public interface GroupInfoService {

	/**
	 * 根据条件查询列表
	 */
	List<GroupInfo> findListByParam(GroupInfoQuery query);

	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(GroupInfoQuery query);

	/**
	 * 分页查询
	 */
	ResultVo<GroupInfo> findListByPage(GroupInfoQuery query);

	/**
	 * 新增
	 */
	Integer add(GroupInfo bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<GroupInfo> listBean);

	/**
	 * 批量新增或更新
	 */
	Integer addOrUpdateBatch(List<GroupInfo> listBean);

	/**
	 * 根据GroupId查询
	 */
	GroupInfo getGroupInfoByGroupId(String groupId);

	/**
	 * 根据GroupId更新
	 */
	Integer updateGroupInfoByGroupId(GroupInfo bean, String groupId);

	/**
	 * 根据GroupId删除
	 */
	Integer deleteGroupInfoByGroupId(String groupId);

	public void saveGroup(GroupInfo groupInfo, MultipartFile avatarFile, MultipartFile avatarCover) throws IOException;

    void dissolutionGroup(String groupOwnerId, String groupId);

    void addOrRemoveGroupUser(TokenUserInfoDto tokenUserInfo, String groupId, String selectContacts, Integer opType);

	void leaveGroup(String userId, String groupId, MessageTypeEnum messageTypeEnum);
}
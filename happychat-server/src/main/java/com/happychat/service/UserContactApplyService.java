package com.happychat.service;

import com.happychat.entity.dto.TokenUserInfoDto;
import com.happychat.entity.pojo.UserContactApply;
import com.happychat.entity.query.UserContactApplyQuery;
import com.happychat.entity.vo.ResultVo;

import java.util.List;
/**
 * @Description:联系人申请Service
 * @author:某某某
 * @date:2024/08/19
 */
public interface UserContactApplyService {

	/**
	 * 根据条件查询列表
	 */
	List<UserContactApply> findListByParam(UserContactApplyQuery query);

	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(UserContactApplyQuery query);

	/**
	 * 分页查询
	 */
	ResultVo<UserContactApply> findListByPage(UserContactApplyQuery query);

	/**
	 * 新增
	 */
	Integer add(UserContactApply bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<UserContactApply> listBean);

	/**
	 * 批量新增或更新
	 */
	Integer addOrUpdateBatch(List<UserContactApply> listBean);

	/**
	 * 根据ApplyId查询联系人申请
	 */
	UserContactApply getUserContactApplyByApplyId(Integer applyId);

	/**
	 * 根据ApplyId更新联系人申请
	 */
	Integer updateUserContactApplyByApplyId(UserContactApply bean, Integer applyId);

	/**
	 * 根据ApplyId删除联系人申请
	 */
	Integer deleteUserContactApplyByApplyId(Integer applyId);

	/**
	 * 根据ApplyUserIdAndReceiveUserIdAndContactId查询联系人申请
	 */
	UserContactApply getUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(String applyUserId, String receiveUserId, String contactId);

	/**
	 * 根据ApplyUserIdAndReceiveUserIdAndContactId更新联系人申请
	 */
	Integer updateUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(UserContactApply bean, String applyUserId, String receiveUserId, String contactId);

	/**
	 * 根据ApplyUserIdAndReceiveUserIdAndContactId删除联系人申请
	 */
	Integer deleteUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(String applyUserId, String receiveUserId, String contactId);

    void dealWithApply(TokenUserInfoDto tokenUserInfoDto, Integer applyId, Integer status);

	public void addContact(String applyId, String receiveId,String contactId, Integer contactType, String applyInfo);
}
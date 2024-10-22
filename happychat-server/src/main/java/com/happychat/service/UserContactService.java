package com.happychat.service;

import com.happychat.entity.dto.ContactInfoDto;
import com.happychat.entity.dto.TokenUserInfoDto;
import com.happychat.entity.dto.UserContactSearchResultDto;
import com.happychat.entity.pojo.UserContact;
import com.happychat.entity.query.UserContactQuery;
import com.happychat.entity.vo.ResultVo;
import com.happychat.enums.UserContactStatusEnum;

import java.util.List;
/**
 * @Description:联系人Service
 * @author:某某某
 * @date:2024/08/19
 */
public interface UserContactService {

	/**
	 * 根据条件查询列表
	 */
	List<UserContact> findListByParam(UserContactQuery query);

	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(UserContactQuery query);

	/**
	 * 分页查询
	 */
	ResultVo<UserContact> findListByPage(UserContactQuery query);

	/**
	 * 新增
	 */
	Integer add(UserContact bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<UserContact> listBean);

	/**
	 * 批量新增或更新
	 */
	Integer addOrUpdateBatch(List<UserContact> listBean);

	/**
	 * 根据UserIdAndContactId查询联系人
	 */
	UserContact getUserContactByUserIdAndContactId(String userId, String contactId);

	/**
	 * 根据UserIdAndContactId更新联系人
	 */
	Integer updateUserContactByUserIdAndContactId(UserContact bean, String userId, String contactId);

	/**
	 * 根据UserIdAndContactId删除联系人
	 */
	Integer deleteUserContactByUserIdAndContactId(String userId, String contactId);

    UserContactSearchResultDto search(String userId, String contactId);

	Integer applyAdd(TokenUserInfoDto tokenUserInfo, String contactId, String applyInfo);

	ContactInfoDto getContactInfo(TokenUserInfoDto tokenUserInfo, String contactId);

	void removeUserContact(String userId, String contactId, UserContactStatusEnum del);

	void updateContactName(String contactId, String contactName);
}
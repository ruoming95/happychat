package com.happychat.service;

import java.io.*; 
import java.util.*;

import com.happychat.entity.dto.TokenUserInfoDto;
import com.happychat.entity.pojo.UserInfo;
import com.happychat.entity.query.UserInfoQuery;
import com.happychat.entity.vo.ResultVo;
import com.happychat.entity.vo.UserInfoVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description:用户信息Service
 * @author:某某某
 * @date:2024/08/17
 */
public interface UserInfoService {

	/**
	 * 根据条件查询列表
	 */
	List<UserInfo> findListByParam(UserInfoQuery query);

	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(UserInfoQuery query);

	/**
	 * 分页查询
	 */
	ResultVo<UserInfo> findListByPage(UserInfoQuery query);

	/**
	 * 新增
	 */
	Integer add(UserInfo bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<UserInfo> listBean);

	/**
	 * 批量新增或更新
	 */
	Integer addOrUpdateBatch(List<UserInfo> listBean);

	/**
	 * 根据UserId查询用户信息
	 */
	UserInfo getUserInfoByUserId(String userId);

	/**
	 * 根据UserId更新用户信息
	 */
	Integer updateUserInfoByUserId(UserInfo bean, String userId);

	/**
	 * 根据UserId删除用户信息
	 */
	Integer deleteUserInfoByUserId(String userId);

	/**
	 * 根据Email查询用户信息
	 */
	UserInfo getUserInfoByEmail(String email);

	/**
	 * 根据Email更新用户信息
	 */
	Integer updateUserInfoByEmail(UserInfo bean, String email);

	/**
	 * 根据Email删除用户信息
	 */
	Integer deleteUserInfoByEmail(String email);

	void register(String email, String password, String nickName);

	UserInfoVo login(String email, String password);

    void updateUserInfo(UserInfo userInfo, MultipartFile avatarFile, MultipartFile avatarCover) throws IOException;

	void forceOffLine(String userId);

	void addContactRobot(String userid);
}
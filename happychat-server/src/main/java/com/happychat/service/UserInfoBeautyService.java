package com.happychat.service;

import java.io.*; 
import java.util.*;

import com.happychat.entity.pojo.UserInfoBeauty;
import com.happychat.entity.query.UserInfoBeautyQuery;
import com.happychat.entity.vo.ResultVo;
/**
 * @Description:靓号表Service
 * @author:某某某
 * @date:2024/08/17
 */
public interface UserInfoBeautyService {

	/**
	 * 根据条件查询列表
	 */
	List<UserInfoBeauty> findListByParam(UserInfoBeautyQuery query);

	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(UserInfoBeautyQuery query);

	/**
	 * 分页查询
	 */
	ResultVo<UserInfoBeauty> findListByPage(UserInfoBeautyQuery query);

	/**
	 * 新增
	 */
	Integer add(UserInfoBeauty bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<UserInfoBeauty> listBean);

	/**
	 * 批量新增或更新
	 */
	Integer addOrUpdateBatch(List<UserInfoBeauty> listBean);

	/**
	 * 根据Id查询靓号表
	 */
	UserInfoBeauty getUserInfoBeautyById(Integer id);

	/**
	 * 根据Id更新靓号表
	 */
	Integer updateUserInfoBeautyById(UserInfoBeauty bean, Integer id);

	/**
	 * 根据Id删除靓号表
	 */
	Integer deleteUserInfoBeautyById(Integer id);

	/**
	 * 根据UserId查询靓号表
	 */
	UserInfoBeauty getUserInfoBeautyByUserId(String userId);

	/**
	 * 根据UserId更新靓号表
	 */
	Integer updateUserInfoBeautyByUserId(UserInfoBeauty bean, String userId);

	/**
	 * 根据UserId删除靓号表
	 */
	Integer deleteUserInfoBeautyByUserId(String userId);

	/**
	 * 根据Email查询靓号表
	 */
	UserInfoBeauty getUserInfoBeautyByEmail(String email);

	/**
	 * 根据Email更新靓号表
	 */
	Integer updateUserInfoBeautyByEmail(UserInfoBeauty bean, String email);

	/**
	 * 根据Email删除靓号表
	 */
	Integer deleteUserInfoBeautyByEmail(String email);

	void saveAccount(UserInfoBeauty beauty);
}
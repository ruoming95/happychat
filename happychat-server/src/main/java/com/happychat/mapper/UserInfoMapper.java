package com.happychat.mapper;

import java.io.*; 
import java.util.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
/**
 * @Description:用户信息Mapper
 * @author:某某某
 * @date:2024/08/17
 */
@Mapper
public interface UserInfoMapper<T, P> extends BaseMapper {

	/**
	 * 根据UserId查询用户信息
	 */
	T selectByUserId(@Param("userId") String userId);

	/**
	 * 根据UserId更新用户信息
	 */
	Integer updateByUserId(@Param("bean") T t, @Param("userId") String userId);

	/**
	 * 根据UserId删除用户信息
	 */
	Integer deleteByUserId(@Param("userId") String userId);

	/**
	 * 根据Email查询用户信息
	 */
	T selectByEmail(@Param("email") String email);

	/**
	 * 根据Email更新用户信息
	 */
	Integer updateByEmail(@Param("bean") T t, @Param("email") String email);

	/**
	 * 根据Email删除用户信息
	 */
	Integer deleteByEmail(@Param("email") String email);

}
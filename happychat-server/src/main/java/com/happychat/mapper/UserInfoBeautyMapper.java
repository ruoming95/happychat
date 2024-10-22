package com.happychat.mapper;

import java.io.*; 
import java.util.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
/**
 * @Description:靓号表Mapper
 * @author:某某某
 * @date:2024/08/17
 */
@Mapper
public interface UserInfoBeautyMapper<T, P> extends BaseMapper {

	/**
	 * 根据Id查询靓号表
	 */
	T selectById(@Param("id") Integer id);

	/**
	 * 根据Id更新靓号表
	 */
	Integer updateById(@Param("bean") T t, @Param("id") Integer id);

	/**
	 * 根据Id删除靓号表
	 */
	Integer deleteById(@Param("id") Integer id);

	/**
	 * 根据UserId查询靓号表
	 */
	T selectByUserId(@Param("userId") String userId);

	/**
	 * 根据UserId更新靓号表
	 */
	Integer updateByUserId(@Param("bean") T t, @Param("userId") String userId);

	/**
	 * 根据UserId删除靓号表
	 */
	Integer deleteByUserId(@Param("userId") String userId);

	/**
	 * 根据Email查询靓号表
	 */
	T selectByEmail(@Param("email") String email);

	/**
	 * 根据Email更新靓号表
	 */
	Integer updateByEmail(@Param("bean") T t, @Param("email") String email);

	/**
	 * 根据Email删除靓号表
	 */
	Integer deleteByEmail(@Param("email") String email);

}
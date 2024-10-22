package com.happychat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description:Mapper
 * @author:某某某
 * @date:2024/08/19
 */
@Mapper
public interface GroupInfoMapper<T, P> extends BaseMapper {

	/**
	 * 根据GroupId查询
	 */
	T selectByGroupId(@Param("groupId") String groupId);

	/**
	 * 根据GroupId更新
	 */
	Integer updateByGroupId(@Param("bean") T t, @Param("groupId") String groupId);

	/**
	 * 根据GroupId删除
	 */
	Integer deleteByGroupId(@Param("groupId") String groupId);

}
package com.happychat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description:app发布Mapper
 * @author:某某某
 * @date:2024/08/23
 */
@Mapper
public interface AppUpdateMapper<T, P> extends BaseMapper {

    /**
     * 根据Id查询app发布
     */
    T selectById(@Param("id") Integer id);

    /**
     * 根据Id更新app发布
     */
    Integer updateById(@Param("bean") T t, @Param("id") Integer id);

    /**
     * 根据Id删除app发布
     */
    Integer deleteById(@Param("id") Integer id);

    /**
     * 根据Version查询app发布
     */
    T selectByVersion(@Param("version") String version);

    /**
     * 根据Version更新app发布
     */
    Integer updateByVersion(@Param("bean") T t, @Param("version") String version);

    /**
     * 根据Version删除app发布
     */
    Integer deleteByVersion(@Param("version") String version);

    T selectLatestVersion(@Param("appVersion") String appVersion, @Param("uid") String uid);

}
package com.happychat.service;

import com.happychat.entity.pojo.AppUpdate;
import com.happychat.entity.query.AppUpdateQuery;
import com.happychat.entity.vo.ResultVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Description:app发布Service
 * @author:某某某
 * @date:2024/08/23
 */
public interface AppUpdateService {

	/**
	 * 根据条件查询列表
	 */
	List<AppUpdate> findListByParam(AppUpdateQuery query);

	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(AppUpdateQuery query);

	/**
	 * 分页查询
	 */
	ResultVo<AppUpdate> findListByPage(AppUpdateQuery query);

	/**
	 * 新增
	 */
	Integer add(AppUpdate bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<AppUpdate> listBean);

	/**
	 * 批量新增或更新
	 */
	Integer addOrUpdateBatch(List<AppUpdate> listBean);

	/**
	 * 根据Id查询app发布
	 */
	AppUpdate getAppUpdateById(Integer id);

	/**
	 * 根据Id更新app发布
	 */
	Integer updateAppUpdateById(AppUpdate bean, Integer id);

	/**
	 * 根据Id删除app发布
	 */
	Integer deleteAppUpdateById(Integer id);

	/**
	 * 根据Version查询app发布
	 */
	AppUpdate getAppUpdateByVersion(String version);

	/**
	 * 根据Version更新app发布
	 */
	Integer updateAppUpdateByVersion(AppUpdate bean, String version);

	/**
	 * 根据Version删除app发布
	 */
	Integer deleteAppUpdateByVersion(String version);

	void saveUpdate(AppUpdate update, MultipartFile file) throws IOException;

	void postUpdate(Integer id, Integer status, String grayscaleUid);

    AppUpdate findLatestVersion(String uid, String appVersion);
}
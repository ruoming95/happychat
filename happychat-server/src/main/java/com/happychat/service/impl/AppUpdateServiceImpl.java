package com.happychat.service.impl;

import com.happychat.config.AppConfig;
import com.happychat.constants.Constants;
import com.happychat.entity.pojo.AppUpdate;
import com.happychat.entity.query.AppUpdateQuery;
import com.happychat.entity.query.SimplePage;
import com.happychat.entity.vo.ResultVo;
import com.happychat.enums.AppUpdateFileTypeEnum;
import com.happychat.enums.AppUpdateStatusEnum;
import com.happychat.enums.PageSize;
import com.happychat.enums.ResponseCodeEnum;
import com.happychat.exception.BusinessException;
import com.happychat.mapper.AppUpdateMapper;
import com.happychat.service.AppUpdateService;
import com.happychat.utils.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @Description:app发布ServiceImpl
 * @author:某某某
 * @date:2024/08/23
 */
@Service("AppUpdateService")
public class AppUpdateServiceImpl implements AppUpdateService {

    @Autowired
    private AppUpdateMapper<AppUpdate, AppUpdateQuery> appUpdateMapper;

    @Autowired
    private AppConfig appConfig;

    /**
     * 根据条件查询列表
     */
    public List<AppUpdate> findListByParam(AppUpdateQuery query) {
        return this.appUpdateMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    public Integer findCountByParam(AppUpdateQuery query) {
        return this.appUpdateMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
    public ResultVo<AppUpdate> findListByPage(AppUpdateQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();

        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<AppUpdate> list = this.findListByParam(query);
        ResultVo<AppUpdate> result = new ResultVo<>(count, pageSize, page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    public Integer add(AppUpdate bean) {
        return this.appUpdateMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    public Integer addBatch(List<AppUpdate> list) {
        if (list == null || list.size() <= 0) {
            return 0;
        }
        return this.appUpdateMapper.insertBatch(list);
    }

    /**
     * 批量新增或更新
     */
    public Integer addOrUpdateBatch(List<AppUpdate> list) {
        if (list == null || list.size() <= 0) {
            return 0;
        }
        return this.appUpdateMapper.insertOrUpdateBatch(list);
    }

    /**
     * 根据Id查询app发布
     */
    public AppUpdate getAppUpdateById(Integer id) {
        return this.appUpdateMapper.selectById(id);
    }

    /**
     * 根据Id更新app发布
     */
    public Integer updateAppUpdateById(AppUpdate bean, Integer id) {
        return this.appUpdateMapper.updateById(bean, id);
    }

    /**
     * 根据Id删除app发布
     */
    public Integer deleteAppUpdateById(Integer id) {
        AppUpdate update = appUpdateMapper.selectById(id);
        if (!update.getStatus().equals(AppUpdateStatusEnum.INIT.getStatus())) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        return this.appUpdateMapper.deleteById(id);
    }

    /**
     * 根据Version查询app发布
     */
    public AppUpdate getAppUpdateByVersion(String version) {
        return this.appUpdateMapper.selectByVersion(version);
    }

    /**
     * 根据Version更新app发布
     */
    public Integer updateAppUpdateByVersion(AppUpdate bean, String version) {
        return this.appUpdateMapper.updateByVersion(bean, version);
    }

    /**
     * 根据Version删除app发布
     */
    public Integer deleteAppUpdateByVersion(String version) {
        return this.appUpdateMapper.deleteByVersion(version);
    }

    @Override
    public void saveUpdate(AppUpdate appUpdate, MultipartFile file) throws IOException {
        AppUpdateFileTypeEnum type = AppUpdateFileTypeEnum.getByType(appUpdate.getFileType());
        if (type == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        if (appUpdate.getId() != null) {
            AppUpdate dbUpdate = appUpdateMapper.selectById(appUpdate.getId());
            if (!dbUpdate.getStatus().equals(AppUpdateStatusEnum.INIT.getStatus())) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
        }
        AppUpdateQuery query = new AppUpdateQuery();
        query.setOrderBy("id desc");
        query.setSimplePage(new SimplePage(0, 1));
        List<AppUpdate> dbList = findListByParam(query);
        if (!dbList.isEmpty()) {
            //			最新版本
            AppUpdate dbUpdate = dbList.get(0);
            Long dbVersion = Long.parseLong(dbUpdate.getVersion().replace(".", ""));
            Long curVersion = Long.parseLong(appUpdate.getVersion().replace(".", ""));
//            新增
            if (appUpdate.getId() == null && dbVersion >= curVersion) {
                throw new BusinessException("当前版本必须大于历史版本");
            }
//			修改
            if (appUpdate.getId() != null && dbVersion <= curVersion && !appUpdate.getId().equals(dbUpdate.getId())) {
                throw new BusinessException("当前版必须大于历史版本");
            }
            AppUpdate update = appUpdateMapper.selectByVersion(appUpdate.getVersion());
            if (appUpdate.getId() != null && update != null && !update.getId().equals(appUpdate.getId())) {
                throw new BusinessException("版本已经存在");
            }
        }
        if (appUpdate.getId() == null) {
            appUpdate.setCreateTime(new Date());
            appUpdate.setStatus(AppUpdateStatusEnum.INIT.getStatus());
            appUpdateMapper.insert(appUpdate);
        } else {
            appUpdateMapper.updateById(appUpdate, appUpdate.getId());
        }

        if (file != null) {
            File appFile = new File(appConfig.getProjectFolder() + Constants.APP_UPDATE_FILE);
            if (!appFile.exists()) {
                appFile.mkdirs();
            }
            file.transferTo(new File(appFile.getAbsoluteFile() + "/" + appUpdate.getId() + Constants.APP_SUFFIX));
        }
    }

    @Override
    public void postUpdate(Integer id, Integer status, String grayscaleUid) {
        AppUpdateStatusEnum type = AppUpdateStatusEnum.getByStatus(status);
        if (type == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        if (AppUpdateStatusEnum.GRAYSCALE == type && StringTools.isEmpty(grayscaleUid)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        if (AppUpdateStatusEnum.GRAYSCALE != type) {
            grayscaleUid = "";
        }
        AppUpdate appUpdate = new AppUpdate();
        appUpdate.setStatus(status);
        appUpdate.setGrayscaleUid(grayscaleUid);
        appUpdateMapper.updateById(appUpdate, id);
    }

    @Override
    public AppUpdate findLatestVersion(String uid, String appVersion) {
        return appUpdateMapper.selectLatestVersion(appVersion, uid);
    }
}
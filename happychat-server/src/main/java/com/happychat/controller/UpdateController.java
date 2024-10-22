package com.happychat.controller;


import com.happychat.annotation.GlobalInterceptor;
import com.happychat.config.AppConfig;
import com.happychat.constants.Constants;
import com.happychat.entity.pojo.AppUpdate;
import com.happychat.entity.vo.AppUpdateVo;
import com.happychat.entity.vo.ResponseVo;
import com.happychat.enums.AppUpdateFileTypeEnum;
import com.happychat.enums.ResponseCodeEnum;
import com.happychat.exception.BusinessException;
import com.happychat.service.AppUpdateService;
import com.happychat.utils.CopyTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Arrays;

@RestController("/updateController")
@RequestMapping("/update")
public class UpdateController extends ABaseController {

    @Autowired
    private AppUpdateService appUpdateService;

    @Autowired
    private AppConfig appConfig;

    @RequestMapping("/checkVersion")
    public ResponseVo checkVersion(String uid, String appVersion) {
        if (appVersion == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        AppUpdate latestVersion = appUpdateService.findLatestVersion(uid, appVersion);
        if (latestVersion == null) {
            return getSuccessResponseVo(null);
        }
        AppUpdateVo vo = CopyTools.copy(latestVersion, AppUpdateVo.class);
        if (AppUpdateFileTypeEnum.LOCAL.getType() == latestVersion.getFileType()) {
            File fileFolder = new File(appConfig.getProjectFolder() + Constants.APP_UPDATE_FILE + latestVersion.getId() + Constants.APP_SUFFIX);
            vo.setSize(fileFolder.length());
        } else {
            vo.setSize(0L);
        }
        vo.setUpdateList(Arrays.asList(latestVersion.getUpdateDescArray()));
        String fileName = Constants.APP_Name + latestVersion.getVersion() + Constants.APP_SUFFIX;
        vo.setFileName(fileName);
        return getSuccessResponseVo(vo);
    }
}

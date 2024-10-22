package com.happychat.controller.admin;


import com.happychat.annotation.GlobalInterceptor;
import com.happychat.config.AppConfig;
import com.happychat.constants.Constants;
import com.happychat.controller.ABaseController;
import com.happychat.entity.dto.SyssettingDto;
import com.happychat.entity.vo.ResponseVo;
import com.happychat.utils.RedisComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController("adminSysSettingController")
@RequestMapping("/admin")
@Validated
public class AdminSysSettingController extends ABaseController {

    @Autowired
    private RedisComponent redisComponent;

    @Autowired
    private AppConfig appConfig;

    @RequestMapping("/getSysSetting")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVo getSysSetting() {
        return getSuccessResponseVo(redisComponent.getSyssetting());
    }

    @RequestMapping("/saveSysSetting")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVo saveSysSetting(SyssettingDto dto,
                                     MultipartFile rbootAvatar,
                                     MultipartFile rbootCover) throws IOException {
      if (rbootAvatar != null) {
          String baseFile = appConfig.getAdminEmails() + Constants.FILE_FOLDER_FILE;
          File avatarFile = new File(baseFile +Constants.FILE_FOLDER_AVATAR);
          if (!avatarFile.exists()) {
              avatarFile.mkdirs();
          }
          String filePath = avatarFile.getPath() + "/" + Constants.ROBOT_ID + Constants.IMG_SUFFIX;
          rbootAvatar.transferTo(new File(filePath));
          rbootCover.transferTo(new File(filePath + Constants.COVER_IMG_SUFFIX));
      }
        redisComponent.saveSyssetting(dto);
        return getSuccessResponseVo(null);
    }

}

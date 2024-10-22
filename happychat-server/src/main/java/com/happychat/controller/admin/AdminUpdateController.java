package com.happychat.controller.admin;


import com.happychat.annotation.GlobalInterceptor;
import com.happychat.controller.ABaseController;
import com.happychat.entity.pojo.AppUpdate;
import com.happychat.entity.query.AppUpdateQuery;
import com.happychat.entity.vo.ResponseVo;
import com.happychat.entity.vo.ResultVo;
import com.happychat.service.AppUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

@RestController("/adminUpdateController")
@RequestMapping("/admin")
@Validated
public class AdminUpdateController extends ABaseController {

    @Autowired
    private AppUpdateService appUpdateService;


    @RequestMapping("/loadUpdateList")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVo loadUpdateList(AppUpdateQuery query) {
        query.setOrderBy("id desc");
        ResultVo<AppUpdate> listByPage = appUpdateService.findListByPage(query);
        return getSuccessResponseVo(listByPage);
    }

    @RequestMapping("/saveUpdate")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVo saveUpdate(Integer id,
                                 @NotEmpty String version,
                                 @NotEmpty String updateDesc,
                                 @NotNull Integer fileType,
                                 String outerLink,
                                 MultipartFile file) throws IOException {
        AppUpdate update = new AppUpdate();
        update.setId(id);
        update.setVersion(version);
        update.setUpdateDesc(updateDesc);
        update.setFileType(fileType);
        update.setOuterLink(outerLink);
        appUpdateService.saveUpdate(update, file);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/delUpdate")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVo delUpdate(@NotNull Integer id) {
        appUpdateService.deleteAppUpdateById(id);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/postUpdate")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVo postUpdate(@NotNull Integer id, @NotNull Integer status, String grayscaleUid) {
        appUpdateService.postUpdate(id, status, grayscaleUid);
        return getSuccessResponseVo(null);
    }

}

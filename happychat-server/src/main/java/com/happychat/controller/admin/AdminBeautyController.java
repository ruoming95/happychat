package com.happychat.controller.admin;


import com.happychat.annotation.GlobalInterceptor;
import com.happychat.controller.ABaseController;
import com.happychat.entity.pojo.UserInfoBeauty;
import com.happychat.entity.query.UserInfoBeautyQuery;
import com.happychat.entity.vo.ResponseVo;
import com.happychat.entity.vo.ResultVo;
import com.happychat.service.UserInfoBeautyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/adminBeautyController")
@RequestMapping("/admin")
@Validated
public class AdminBeautyController extends ABaseController {

    @Autowired
    private UserInfoBeautyService userInfoBeautyService;

    @RequestMapping("/loadBeautyAccountList")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVo loadBeautyAccountList(UserInfoBeautyQuery query) {
        query.setOrderBy("id asc");
        ResultVo<UserInfoBeauty> listByPage = userInfoBeautyService.findListByPage(query);
        return getSuccessResponseVo(listByPage);
    }

    @RequestMapping("/saveBeautAccount")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVo saveBeautAccount(UserInfoBeauty beauty) {
        userInfoBeautyService.saveAccount(beauty);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/delBeautAccount")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVo delBeautAccount(Integer id) {
        userInfoBeautyService.deleteUserInfoBeautyById(id);
        return getSuccessResponseVo(null);
    }

}

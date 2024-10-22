package com.happychat.controller.admin;

import com.happychat.annotation.GlobalInterceptor;
import com.happychat.controller.ABaseController;
import com.happychat.entity.pojo.GroupInfo;
import com.happychat.entity.query.GroupInfoQuery;
import com.happychat.entity.vo.ResponseVo;
import com.happychat.entity.vo.ResultVo;
import com.happychat.enums.ResponseCodeEnum;
import com.happychat.exception.BusinessException;
import com.happychat.service.GroupInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;


@RestController("/adminGroupController")
@RequestMapping("/admin")
@Validated
public class AdminGroupController extends ABaseController {

    @Autowired
    private GroupInfoService groupInfoService;


    @RequestMapping("/loadGroup")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVo loadGroup(GroupInfoQuery query) {
        query.setOrderBy("create_time desc");
        query.setQueryMemberCount(true);
        query.setQueryGroupOwnerNickName(true);
        ResultVo<GroupInfo> listByPage = groupInfoService.findListByPage(query);
        return getSuccessResponseVo(listByPage);
    }

    @RequestMapping("/dissolutionGroup")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVo dissolutionGroup(@NotEmpty String groupId) {
        GroupInfo groupInfo = groupInfoService.getGroupInfoByGroupId(groupId);
        if (null == groupId) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        groupInfoService.dissolutionGroup(groupInfo.getGroupOwnerId(), groupId);
        return getSuccessResponseVo(null);
    }
}

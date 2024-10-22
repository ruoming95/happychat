package com.happychat.controller;

import com.happychat.annotation.GlobalInterceptor;
import com.happychat.entity.dto.TokenUserInfoDto;
import com.happychat.entity.pojo.GroupInfo;
import com.happychat.entity.pojo.UserContact;
import com.happychat.entity.query.GroupInfoQuery;
import com.happychat.entity.query.UserContactQuery;
import com.happychat.entity.vo.GroupInfoVo;
import com.happychat.entity.vo.ResponseVo;
import com.happychat.enums.GroupStatusEnum;
import com.happychat.enums.MessageTypeEnum;
import com.happychat.enums.UserContactStatusEnum;
import com.happychat.exception.BusinessException;
import com.happychat.service.GroupInfoService;
import com.happychat.service.UserContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

/**
 * @Description:Controller
 * @author:某某某
 * @date:2024/08/19
 */
@RestController
@RequestMapping("/group")
public class GroupInfoController extends ABaseController {

    private static final Logger logger = LoggerFactory.getLogger(GroupInfoController.class);

    @Autowired
    private GroupInfoService groupInfoService;

    @Autowired
    private UserContactService userContactService;


    @RequestMapping("/saveGroup")
    @GlobalInterceptor
    public ResponseVo saveGroup(HttpServletRequest request,
                                @NotEmpty String groupName,
                                @NotNull Integer joinType,
                                String groupNotice,
                                MultipartFile avatarFile,
                                MultipartFile avatarCover
    ) throws IOException {
        TokenUserInfoDto token = getTokenUserInfo(request);
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setGroupOwnerId(token.getUserId());
        groupInfo.setGroupName(groupName);
        groupInfo.setJoinType(joinType);
        groupInfo.setGroupNotice(groupNotice);
        groupInfoService.saveGroup(groupInfo, avatarFile, avatarCover);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/loadMyGroup")
    @GlobalInterceptor
    public ResponseVo loadMyGroup(HttpServletRequest request) {
        TokenUserInfoDto tokenUserInfo = getTokenUserInfo(request);
        GroupInfoQuery query = new GroupInfoQuery();
        query.setGroupOwnerId(tokenUserInfo.getUserId());
        query.setOrderBy("create_time desc");
        query.setStatus(1);
        List<GroupInfo> list = groupInfoService.findListByParam(query);
        return getSuccessResponseVo(list);
    }

    @RequestMapping("/getGroupInfo")
    @GlobalInterceptor
    public ResponseVo getGroupInfo(HttpServletRequest request,
                                   @NotEmpty String groupId) {
        logger.info(groupId);
        GroupInfo groupInfo = getGroupInfoCommon(request, groupId);
        UserContactQuery query = new UserContactQuery();
        query.setContactId(groupId);
        query.setStatus(UserContactStatusEnum.FRIEND.getStatus());
        Integer count = userContactService.findCountByParam(query);
        groupInfo.setMemberCount(count);
        return getSuccessResponseVo(groupInfo);
    }

    @RequestMapping("/getGroupInfo4Chat")
    @GlobalInterceptor
    public ResponseVo getGroupInfoForChat(HttpServletRequest request,
                                          @NotEmpty String groupId) {
        GroupInfo groupInfo = getGroupInfoCommon(request, groupId);
        UserContactQuery query = new UserContactQuery();
        query.setContactId(groupId);
//        设置为true关联查询群组成员信息
        query.setQueryUserInfo(true);
        query.setOrderBy("create_time asc");
        query.setStatus(UserContactStatusEnum.FRIEND.getStatus());
        List<UserContact> list = userContactService.findListByParam(query);
        GroupInfoVo groupInfoVo = GroupInfoVo.builder()
                .groupInfo(groupInfo)
                .userContactList(list)
                .build();
        return getSuccessResponseVo(groupInfoVo);
    }

    private GroupInfo getGroupInfoCommon(HttpServletRequest request, String groupId) {
        TokenUserInfoDto tokenUserInfo = getTokenUserInfo(request);
        //确保用户在群里
        UserContact userContact = userContactService.getUserContactByUserIdAndContactId(tokenUserInfo.getUserId(), groupId);
        if (null == userContact || !UserContactStatusEnum.FRIEND.getStatus().equals(userContact.getStatus())) {
            throw new BusinessException("您不在该群里或群聊不存在或已解散");
        }
        GroupInfo groupInfo = groupInfoService.getGroupInfoByGroupId(groupId);
        if (null == groupInfo || !GroupStatusEnum.NORMAL.getStatus().equals(groupInfo.getStatus())) {
            throw new BusinessException("群聊不存在或已解散");
        }
        return groupInfo;
    }

    @RequestMapping("/addOrRemoveGroupUser")
    @GlobalInterceptor
    public ResponseVo addOrRemoveGroupUser(HttpServletRequest request,
                                           @NotEmpty String groupId,
                                           @NotEmpty String selectContacts,
                                           @NotNull Integer opType) {
        TokenUserInfoDto tokenUserInfo = getTokenUserInfo(request);
        groupInfoService.addOrRemoveGroupUser(tokenUserInfo, groupId, selectContacts, opType);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/leaveGroup")
    @GlobalInterceptor
    public ResponseVo leaveGroup(HttpServletRequest request,
                                 @NotEmpty String groupId) {
        TokenUserInfoDto tokenUserInfo = getTokenUserInfo(request);
        groupInfoService.leaveGroup(tokenUserInfo.getUserId(), groupId, MessageTypeEnum.LEAVE_GROUP);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/dissolutionGroup")
    @GlobalInterceptor
    public ResponseVo dissolutionGroup(HttpServletRequest request,
                                       @NotEmpty String groupId) {
        TokenUserInfoDto tokenUserInfo = getTokenUserInfo(request);
        groupInfoService.dissolutionGroup(tokenUserInfo.getUserId(), groupId);
        return getSuccessResponseVo(null);
    }

}

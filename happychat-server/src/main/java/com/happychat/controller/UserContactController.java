package com.happychat.controller;

import com.happychat.annotation.GlobalInterceptor;
import com.happychat.entity.dto.TokenUserInfoDto;
import com.happychat.entity.dto.UserContactSearchResultDto;
import com.happychat.entity.pojo.UserContact;
import com.happychat.entity.pojo.UserContactApply;
import com.happychat.entity.pojo.UserInfo;
import com.happychat.entity.query.UserContactApplyQuery;
import com.happychat.entity.query.UserContactQuery;
import com.happychat.entity.vo.ResponseVo;
import com.happychat.entity.vo.ResultVo;
import com.happychat.entity.vo.UserInfoVo;
import com.happychat.enums.ResponseCodeEnum;
import com.happychat.enums.UserContactStatusEnum;
import com.happychat.enums.UserContactTypeEnum;
import com.happychat.exception.BusinessException;
import com.happychat.service.UserContactApplyService;
import com.happychat.service.UserContactService;
import com.happychat.service.UserInfoService;
import com.happychat.utils.CopyTools;
import jodd.util.ArraysUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description:联系人Controller
 * @author:某某某
 * @date:2024/08/19
 */
@RestController
@RequestMapping("/contact")
public class UserContactController extends ABaseController {

    @Autowired
    private UserContactService userContactService;

    @Autowired
    private UserContactApplyService userContactApplyService;

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping("/search")
    @GlobalInterceptor
    public ResponseVo search(HttpServletRequest request, @NotEmpty String contactId) {
        TokenUserInfoDto tokenUserInfo = getTokenUserInfo(request);
        UserContactSearchResultDto search = userContactService.search(tokenUserInfo.getUserId(), contactId);
        return getSuccessResponseVo(search);
    }

    @RequestMapping("/applyAdd")
    @GlobalInterceptor
    public ResponseVo applyAdd(HttpServletRequest request, @NotEmpty String contactId, String applyInfo) {
        TokenUserInfoDto tokenUserInfo = getTokenUserInfo(request);
        Integer joinType = userContactService.applyAdd(tokenUserInfo, contactId, applyInfo);
        return getSuccessResponseVo(joinType);
    }

    @RequestMapping("/loadApply")
    @GlobalInterceptor
    public ResponseVo loadApply(HttpServletRequest request, Integer pageNo) {
        TokenUserInfoDto tokenUserInfo = getTokenUserInfo(request);
        UserContactApplyQuery query = new UserContactApplyQuery();
        query.setPageNo(pageNo);
        query.setReceiveUserId(tokenUserInfo.getUserId());
        query.setQueryContact(true);
        query.setOrderBy("last_apply_time desc");
        ResultVo<UserContactApply> page = userContactApplyService.findListByPage(query);
        return getSuccessResponseVo(page);
    }

    @RequestMapping("/dealWithApply")
    @GlobalInterceptor
    public ResponseVo dealWithApply(HttpServletRequest request, @NotNull Integer applyId, @NotNull Integer status) {
        TokenUserInfoDto tokenUserInfo = getTokenUserInfo(request);
        userContactApplyService.dealWithApply(tokenUserInfo, applyId, status);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/loadContact")
    @GlobalInterceptor
    public ResponseVo loadContact(HttpServletRequest request, @NotEmpty String contactType) {
        TokenUserInfoDto tokenUserInfo = getTokenUserInfo(request);
        UserContactTypeEnum type = UserContactTypeEnum.getByPrefix(contactType);
        if (type == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        UserContactQuery query = new UserContactQuery();
        query.setQueryUserContactInfo(true);
        query.setContactType(type.getType());
        if (type.equals(UserContactTypeEnum.USER)) {
            query.setUserId(tokenUserInfo.getUserId());
        } else {
            query.setQueryGroupInfo(true);
        }
        query.setStatusArrays(new Integer[]
                {UserContactStatusEnum.DEL_BE.getStatus(),
                        UserContactStatusEnum.BLACKLIST_BE.getStatus(),
                        UserContactStatusEnum.FRIEND.getStatus()});
        List<UserContact> list = userContactService.findListByParam(query);
        return getSuccessResponseVo(list);
    }

    @RequestMapping("/delContact")
    @GlobalInterceptor
    public ResponseVo delContact(HttpServletRequest request, @NotEmpty String contactId) {
        TokenUserInfoDto tokenUserInfo = getTokenUserInfo(request);
        userContactService.removeUserContact(tokenUserInfo.getUserId(), contactId, UserContactStatusEnum.DEL);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/addContact2BlackList")
    @GlobalInterceptor
    public ResponseVo addContact2BlackList(HttpServletRequest request, @NotEmpty String contactId) {
        TokenUserInfoDto tokenUserInfo = getTokenUserInfo(request);
        userContactService.removeUserContact(tokenUserInfo.getUserId(), contactId, UserContactStatusEnum.BLACKLIST);
        return getSuccessResponseVo(null);
    }


    @RequestMapping("/getContactInfo")
    @GlobalInterceptor
    public ResponseVo getContactInfo(HttpServletRequest request, @NotEmpty String contactId) {
        TokenUserInfoDto tokenUserInfo = getTokenUserInfo(request);
        UserInfo userInfo = userInfoService.getUserInfoByUserId(contactId);
        UserInfoVo vo = CopyTools.copy(userInfo, UserInfoVo.class);
        vo.setStatus(UserContactStatusEnum.NOT_FRIEND.getStatus());
        vo.setAreaName(userInfo.getAreaName());
        UserContact contact = userContactService.getUserContactByUserIdAndContactId(tokenUserInfo.getUserId(), contactId);
        if (contact != null) {
            vo.setStatus(UserContactStatusEnum.FRIEND.getStatus());
        }
        return getSuccessResponseVo(vo);
    }

    @RequestMapping("/getContactUserInfo")
    @GlobalInterceptor
    public ResponseVo getContactUserInfo(HttpServletRequest request, @NotEmpty String contactId) {
        TokenUserInfoDto tokenUserInfo = getTokenUserInfo(request);
        UserContact contact = userContactService.getUserContactByUserIdAndContactId(tokenUserInfo.getUserId(), contactId);
        if (contact == null || !ArraysUtil.contains(new Integer[]{
                UserContactStatusEnum.FRIEND.getStatus(),
                UserContactStatusEnum.BLACKLIST_BE.getStatus(),
                UserContactStatusEnum.DEL_BE.getStatus()
        }, contact.getStatus())) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        UserInfo userInfo = userInfoService.getUserInfoByUserId(contactId);
        UserInfoVo vo = CopyTools.copy(userInfo, UserInfoVo.class);
        vo.setAreaName(userInfo.getAreaName());
        return getSuccessResponseVo(vo);
    }
}
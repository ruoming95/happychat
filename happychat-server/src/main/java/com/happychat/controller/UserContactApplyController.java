package com.happychat.controller;

import com.happychat.entity.pojo.UserContactApply;
import com.happychat.entity.query.UserContactApplyQuery;
import com.happychat.entity.vo.ResponseVo;
import com.happychat.service.UserContactApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * @Description:联系人申请Controller
 * @author:某某某
 * @date:2024/08/19
 */
@RestController
@RequestMapping("userContactApply")
public class UserContactApplyController extends ABaseController {

	@Autowired
	private UserContactApplyService userContactApplyService;

	/**
	 * 查询列表
	 */
	@RequestMapping("loadDataList")
	public ResponseVo loadDataList(UserContactApplyQuery query) {
		return getSuccessResponseVo(userContactApplyService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("add")
	public ResponseVo add(UserContactApply bean){
		return getSuccessResponseVo(this.userContactApplyService.add(bean));
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("addBatch")
	public ResponseVo addBatch(@RequestBody List<UserContactApply> list) {
		return getSuccessResponseVo(this.userContactApplyService.addBatch(list));
	}

	/**
	 * 批量新增或更新
	 */
	@RequestMapping("addOrUpdateBatch")
	public ResponseVo addOrUpdateBatch(@RequestBody List<UserContactApply> list) {
		return getSuccessResponseVo(this.userContactApplyService.addOrUpdateBatch(list));
	}

	/**
	 * 根据ApplyId查询联系人申请
	 */
	@RequestMapping("getUserContactApplyByApplyId")
	public ResponseVo getUserContactApplyByApplyId(Integer applyId) {
		return getSuccessResponseVo(this.userContactApplyService.getUserContactApplyByApplyId(applyId));
	}

	/**
	 * 根据ApplyId更新联系人申请
	 */
	@RequestMapping("updateUserContactApplyByApplyId")
	public ResponseVo updateUserContactApplyByApplyId(UserContactApply bean, Integer applyId) {
		return getSuccessResponseVo(this.userContactApplyService.updateUserContactApplyByApplyId(bean, applyId));
	}

	/**
	 * 根据ApplyId删除联系人申请
	 */
	@RequestMapping("deleteUserContactApplyByApplyIdApplyId")
	public ResponseVo deleteUserContactApplyByApplyId(Integer applyId) {
		return getSuccessResponseVo(this.userContactApplyService.deleteUserContactApplyByApplyId(applyId));
	}

	/**
	 * 根据ApplyUserIdAndReceiveUserIdAndContactId查询联系人申请
	 */
	@RequestMapping("getUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId")
	public ResponseVo getUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(String applyUserId, String receiveUserId, String contactId) {
		return getSuccessResponseVo(this.userContactApplyService.getUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(applyUserId, receiveUserId, contactId));
	}

	/**
	 * 根据ApplyUserIdAndReceiveUserIdAndContactId更新联系人申请
	 */
	@RequestMapping("updateUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId")
	public ResponseVo updateUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(UserContactApply bean, String applyUserId, String receiveUserId, String contactId) {
		return getSuccessResponseVo(this.userContactApplyService.updateUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(bean, applyUserId, receiveUserId, contactId));
	}

	/**
	 * 根据ApplyUserIdAndReceiveUserIdAndContactId删除联系人申请
	 */
	@RequestMapping("deleteUserContactApplyByApplyUserIdAndReceiveUserIdAndContactIdApplyUserIdAndReceiveUserIdAndContactId")
	public ResponseVo deleteUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(String applyUserId, String receiveUserId, String contactId) {
		return getSuccessResponseVo(this.userContactApplyService.deleteUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(applyUserId, receiveUserId, contactId));
	}

}
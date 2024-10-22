package com.happychat.entity.vo;

import com.happychat.entity.pojo.GroupInfo;
import com.happychat.entity.pojo.UserContact;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GroupInfoVo {
    private GroupInfo groupInfo;
    private List<UserContact> userContactList ;
}

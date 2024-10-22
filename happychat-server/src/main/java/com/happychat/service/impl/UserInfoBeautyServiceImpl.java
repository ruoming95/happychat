package com.happychat.service.impl;

import com.happychat.entity.pojo.UserInfo;
import com.happychat.entity.pojo.UserInfoBeauty;
import com.happychat.entity.query.SimplePage;
import com.happychat.entity.query.UserInfoBeautyQuery;
import com.happychat.entity.query.UserInfoQuery;
import com.happychat.entity.vo.ResultVo;
import com.happychat.enums.BeautyAccountStatusEnum;
import com.happychat.enums.PageSize;
import com.happychat.enums.ResponseCodeEnum;
import com.happychat.enums.UserStatusEnum;
import com.happychat.exception.BusinessException;
import com.happychat.mapper.UserInfoBeautyMapper;
import com.happychat.mapper.UserInfoMapper;
import com.happychat.service.UserInfoBeautyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:靓号表ServiceImpl
 * @author:某某某
 * @date:2024/08/17
 */
@Service("UserInfoBeautyService")
public class UserInfoBeautyServiceImpl implements UserInfoBeautyService {

    @Autowired
    private UserInfoBeautyMapper<UserInfoBeauty, UserInfoBeautyQuery> userInfoBeautyMapper;

    @Autowired
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    /**
     * 根据条件查询列表
     */
    public List<UserInfoBeauty> findListByParam(UserInfoBeautyQuery query) {
        return this.userInfoBeautyMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    public Integer findCountByParam(UserInfoBeautyQuery query) {
        return this.userInfoBeautyMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
    public ResultVo<UserInfoBeauty> findListByPage(UserInfoBeautyQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();

        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<UserInfoBeauty> list = this.findListByParam(query);
        ResultVo<UserInfoBeauty> result = new ResultVo<>(count, pageSize, page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    public Integer add(UserInfoBeauty bean) {
        return this.userInfoBeautyMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    public Integer addBatch(List<UserInfoBeauty> list) {
        if (list == null || list.size() <= 0) {
            return 0;
        }
        return this.userInfoBeautyMapper.insertBatch(list);
    }

    /**
     * 批量新增或更新
     */
    public Integer addOrUpdateBatch(List<UserInfoBeauty> list) {
        if (list == null || list.size() <= 0) {
            return 0;
        }
        return this.userInfoBeautyMapper.insertOrUpdateBatch(list);
    }

    /**
     * 根据Id查询靓号表
     */
    public UserInfoBeauty getUserInfoBeautyById(Integer id) {
        return this.userInfoBeautyMapper.selectById(id);
    }

    /**
     * 根据Id更新靓号表
     */
    public Integer updateUserInfoBeautyById(UserInfoBeauty bean, Integer id) {
        return this.userInfoBeautyMapper.updateById(bean, id);
    }

    /**
     * 根据Id删除靓号表
     */
    public Integer deleteUserInfoBeautyById(Integer id) {
        return this.userInfoBeautyMapper.deleteById(id);
    }

    /**
     * 根据UserId查询靓号表
     */
    public UserInfoBeauty getUserInfoBeautyByUserId(String userId) {
        return this.userInfoBeautyMapper.selectByUserId(userId);
    }

    /**
     * 根据UserId更新靓号表
     */
    public Integer updateUserInfoBeautyByUserId(UserInfoBeauty bean, String userId) {
        return this.userInfoBeautyMapper.updateByUserId(bean, userId);
    }

    /**
     * 根据UserId删除靓号表
     */
    public Integer deleteUserInfoBeautyByUserId(String userId) {
        return this.userInfoBeautyMapper.deleteByUserId(userId);
    }

    /**
     * 根据Email查询靓号表
     */
    public UserInfoBeauty getUserInfoBeautyByEmail(String email) {
        return this.userInfoBeautyMapper.selectByEmail(email);
    }

    /**
     * 根据Email更新靓号表
     */
    public Integer updateUserInfoBeautyByEmail(UserInfoBeauty bean, String email) {
        return this.userInfoBeautyMapper.updateByEmail(bean, email);
    }

    /**
     * 根据Email删除靓号表
     */
    public Integer deleteUserInfoBeautyByEmail(String email) {
        return this.userInfoBeautyMapper.deleteByEmail(email);
    }

    @Override
    public void saveAccount(UserInfoBeauty beauty) {
        if (beauty.getId() != null) {
            UserInfoBeauty dbInfo = this.userInfoBeautyMapper.selectById(beauty.getId());
            // 检查账号是否正在使用
            if (BeautyAccountStatusEnum.USERD.getStatus().equals(dbInfo.getStatus())) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
        }
        // 新增的时候判断邮箱是否存在
        UserInfoBeauty dbInfo = this.userInfoBeautyMapper.selectByEmail(beauty.getEmail());
        if (beauty.getId() == null && dbInfo != null) {
            throw new BusinessException("靓号邮箱已经存在");
        }
        // 修改时判断邮箱是否存在
        if (beauty.getId() != null && dbInfo != null && !beauty.getId().equals(dbInfo.getId())) {
            throw new BusinessException("靓号邮箱已经存在");
        }

        // 判断靓号是否存在
        dbInfo = this.userInfoBeautyMapper.selectByUserId(beauty.getUserId());
        if (beauty.getId() != null && dbInfo != null) {
            throw new BusinessException("靓号已经存在");
        }

        if (beauty.getId() != null && dbInfo != null && dbInfo.getId() != null && !beauty.getId().equals(dbInfo.getId())) {
            throw new BusinessException("靓号已经存在");
        }

        //邮箱是否注册
        UserInfo userInfo = userInfoMapper.selectByUserId(beauty.getUserId());
        if (userInfo != null) {
            throw new BusinessException("邮箱已经注册");
        }
        if (beauty.getId() != null) {
            userInfoBeautyMapper.updateById(beauty, beauty.getId());
        } else {
            beauty.setStatus(BeautyAccountStatusEnum.NO_USER.getStatus());
            userInfoBeautyMapper.insert(beauty);
        }
    }
}
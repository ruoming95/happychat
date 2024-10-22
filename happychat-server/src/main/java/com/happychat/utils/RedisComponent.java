package com.happychat.utils;

import com.happychat.constants.Constants;
import com.happychat.entity.dto.SyssettingDto;
import com.happychat.entity.dto.TokenUserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("redisComponent")
public class RedisComponent {

    @Autowired
    private RedisUtils redisUtils;

    public void cleanUpUserContact(String userId) {
        redisUtils.delete(Constants.REDIS_WS_USER_CONTACT + userId);
    }

    //保存心跳
    public void saveHeartBeat(String userId) {
        redisUtils.setex(Constants.REDIS_WS_USER_HEART_BEAT + userId, System.currentTimeMillis(), Constants.REDIS_HERT_BEAT_EXPIRE_TIME);
    }

    //连接断开，删除心跳
    public void removeHearBeat(String userId) {
        redisUtils.delete(Constants.REDIS_WS_USER_HEART_BEAT + userId);
    }

    //    获取心跳
    public Long getHeartBeat(String userId) {
        return (Long) redisUtils.get(Constants.REDIS_WS_USER_HEART_BEAT + userId);
    }

    //    记录TokenUserInfoDto
    public void saveTokenUserInfoDto(TokenUserInfoDto tokenUserInfoDto) {
        redisUtils.setex(Constants.REDIS_WS_USER_TOKEN + tokenUserInfoDto.getToken(), tokenUserInfoDto, Constants.EXPIRE_TIME_1DAY * 2);
        //    通过userId获取到token
        redisUtils.setex(Constants.REDIS_WS_USER_TOKEN_USERID + tokenUserInfoDto.getUserId(), tokenUserInfoDto.getToken(), Constants.EXPIRE_TIME_1DAY * 2);
    }

    public void cleanTokenUserInfoDto(String userId) {
        String token = (String) redisUtils.get(Constants.REDIS_WS_USER_TOKEN_USERID + userId);
        if (StringTools.isEmpty(token)) {
            return;
        }
        redisUtils.delete(Constants.REDIS_WS_USER_TOKEN + token);
    }

    //    获取TokenUserInfoDto
    public TokenUserInfoDto getTokenUserInfoDto(String token) {
        return (TokenUserInfoDto) redisUtils.get(Constants.REDIS_WS_USER_TOKEN + token);
    }

    //根据userId获取token
    public TokenUserInfoDto getTokenUserInfoDtoByUserId(String userId) {
        String token = (String) redisUtils.get(Constants.REDIS_WS_USER_TOKEN_USERID + userId);
        return getTokenUserInfoDto(token);
    }

    //    获取系统设置
    public SyssettingDto getSyssetting() {
        SyssettingDto syssettingDto = (SyssettingDto) redisUtils.get(Constants.REDIS_USER_SYSSETTING);
        return syssettingDto == null ? new SyssettingDto() : syssettingDto;
    }

    //    保存系统设置
    public void saveSyssetting(SyssettingDto dto) {
        redisUtils.set(Constants.REDIS_USER_SYSSETTING, dto);
    }

    public void saveContactList(String userId, List<String> contactList) {
        redisUtils.lpushAll(Constants.REDIS_WS_USER_CONTACT + userId, contactList, Constants.EXPIRE_TIME_1DAY);
    }

    public void removeUserContact(String userId, String contactId) {
        redisUtils.remove(Constants.REDIS_WS_USER_CONTACT + userId, contactId);
    }

    public void addUserContact(String userId, String contactId) {
        List<String> contactList = getContactList(userId);
        if (contactList.contains(contactId)) {
            return;
        }
        redisUtils.lpush(Constants.REDIS_WS_USER_CONTACT + userId, contactId, Constants.EXPIRE_TIME_1DAY);
    }

    public List<String> getContactList(String userId) {
        return (List<String>) redisUtils.getQueue(Constants.REDIS_WS_USER_CONTACT + userId);
    }
}

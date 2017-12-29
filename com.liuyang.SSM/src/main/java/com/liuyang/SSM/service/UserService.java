package com.liuyang.SSM.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.liuyang.SSM.entity.UserEntity;
import com.liuyang.SSM.entity.UserInfoResult;

@Service
public interface UserService {

	int insertUserEntity(List<UserEntity> list);

	int deleteUserByUserId(Integer userId);
	
	int updateUserInfo(UserEntity user);
	
	List<UserEntity> getUserInfoList(Map<String,Object> map);
	
    void putUserInfoToRedis(UserInfoResult result,final String field,Map<String, Object> paramMap);
    
    UserInfoResult getUserEntityListFromRedis(String key,String field);
    
    void deletUserEntityListFromRedis(String userName);
	
}

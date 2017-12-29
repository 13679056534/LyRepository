package com.liuyang.SSM.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.TypeReference;
import com.liuyang.SSM.dao.RedisDao;
import com.liuyang.SSM.dao.UserDao;
import com.liuyang.SSM.entity.UserEntity;
import com.liuyang.SSM.entity.UserInfoResult;
import com.liuyang.SSM.service.UserServiceAPI;



@Service
public class UserServiceAPIImpl implements UserServiceAPI{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private UserDao userDao;
	@Autowired
	private RedisDao redisDao;
	
	@Override
	public int insertUserEntity(List<UserEntity> list) {
		return userDao.insertUserEntity(list);
	}

	@Override
	public int deleteUserByUserId(Integer userId) {
		return userDao.deletUserEntityByPrimaryKey(userId);
	}

	@Override
	public int updateUserInfo(UserEntity user) {
		return userDao.updateUserInfoByPrimaryKeySelective(user);
	}

	@Override
	public List<UserEntity> getUserInfoList(Map<String,Object> map) {
		return userDao.getUserEntityListBrParam(map);
	}

	
	@Override
	public void putUserInfoToRedis(UserInfoResult result,final String field,Map<String, Object> paramMap) {
		if (null == result) {
			try {
				throw new Exception("参数为空");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
				String key = this.structureKey(paramMap);
				redisDao.hset(result,field,key);
				redisDao.setsKeySave(String.valueOf(paramMap.get("userName")),key);
		}
		
	}
	
	@Override
	public UserInfoResult getUserEntityListFromRedis(String key,String field){
		return redisDao.hget(key,field,new TypeReference<UserInfoResult>(){});
	}

	@Override
	public void deletUserEntityListFromRedis(String userName) {
		Set<String> keySet=redisDao.setsKeyGetAll(userName, new TypeReference<String>(){});
		if (CollectionUtils.isEmpty(keySet)) {
			logger.info("查询key失败，keyset为null");
			return;
		}
		if (keySet.size() > 0) {
			Iterator<String> iterator=keySet.iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				redisDao.delete(key);
			}
		}
		redisDao.delete(userName);
	}

	public String structureKey(Map<String, Object> paramsMap){
		String userInfiKey = "";
		String userName = String.valueOf(paramsMap.get("userName"));
		String userAge = String.valueOf(paramsMap.get("userAge"));
		userInfiKey = userName + ":" + userAge;
		return userInfiKey;
	}
	
}

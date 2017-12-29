package com.liuyang.SSM.UserTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.liuyang.SSM.dao.RedisDao;
import com.liuyang.SSM.entity.UserEntity;
import com.liuyang.SSM.entity.UserInfoResult;
import com.liuyang.SSM.service.UserService;


@ContextConfiguration(locations = { "classpath:config/springMVC/spring-mvc.xml","classpath:config/mybatis/spring-mybatis.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {
	@Autowired
	private UserService userService;
	@Autowired
	private RedisDao redisDao;
	
	@Test
	public void insertTest(){
		List<UserEntity> list = new ArrayList<UserEntity>();
		for (int i = 100; i < 105; i++) {
			UserEntity user = new UserEntity();
			user.setPassword("123456");
			user.setUserEmail("123456789@qq.com");
			user.setUserAge(i);
			user.setUserName("王五");
			list.add(user);
		}
		userService.insertUserEntity(list);
	}
	
	@Test
	public void deleteTest(){
		userService.deleteUserByUserId(2);
	}
	
	@Test
	public void updateUserInfoTest(){
		UserEntity user = new UserEntity();
		user.setUserId(2);
		user.setUserAge(100);
		user.setUserName("王五");
		userService.updateUserInfo(user);
	}
	
	@Test
	public void getUserEntityListTest(){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("userName", "张三");
		map.put("userAge", 231);
		List<UserEntity> resultList = new ArrayList<UserEntity>();
		resultList = userService.getUserInfoList(map);
		Assert.assertNotNull(resultList);
	}
	
	@Test
	public void putUserInfoToRedisTest(){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("userName", "王五");
		map.put("userEmail", "123456@qq.com");
		Map<String,Object> paramMap = new HashMap<String, Object>();
		List<UserEntity> resultList = new ArrayList<UserEntity>();
		resultList = userService.getUserInfoList(map);
		UserInfoResult result = new UserInfoResult();
		result.setUserList(resultList);
		paramMap.put("userName","王五");
		paramMap.put("userAge","104");
		userService.putUserInfoToRedis(result,"userInfoData",paramMap);
		}
	
	@Test
	public void getUserEntityListFromRedis(){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userName","王五");
		paramMap.put("userAge","104");
		String key = "王五:104";
		UserInfoResult result = userService.getUserEntityListFromRedis(key,"userInfoData");
	}
	
	@Test
	public void deletUserEntityListFromRedis(){
		userService.deletUserEntityListFromRedis("王五");
	}
}

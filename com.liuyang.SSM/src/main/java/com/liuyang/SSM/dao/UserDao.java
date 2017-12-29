package com.liuyang.SSM.dao;

import java.util.List;
import java.util.Map;


import com.liuyang.SSM.entity.UserEntity;


/**
 * UserDao
 * @author 刘阳
 *
 */
public interface UserDao {
	
	/**
	 * 条件查找user
	 * @param user
	 * @return
	 */
	UserEntity seletUserEntityByPrimaryKey(UserEntity user);
	
	/**
	 * 更新user信息
	 * @param user
	 * @return
	 */
	int updateUserInfoByPrimaryKey(UserEntity user);
	
	/**
	 * 部分更新user信息
	 * @param user
	 * @return
	 */
	int updateUserInfoByPrimaryKeySelective(UserEntity user);
	
	/**
	 * 通过主键UserId删除
	 * @param userId
	 * @return
	 */
	int deletUserEntityByPrimaryKey(Integer userId);
	
	/**
	 * 批量插入
	 * @param list
	 * @return
	 */
	int insertUserEntity(List<UserEntity> list);
	
	/**
	 * 批量获取userEntity
	 * @param map
	 * @return
	 */
	List<UserEntity> getUserEntityListBrParam(Map<String, Object> map);
}

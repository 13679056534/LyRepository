package com.liuyang.SSM.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;


/**
 * RedisServiceImpl
 * @author 刘阳
 *
 */
public class RedisDao {
	private static ShardedJedisPool  shardedJedisPool  = null;
	//采用Jedis提供的客户端分布式操作的方式，它采用的是采用一致性哈希算法
	private List<JedisShardInfo> shards = null;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 初始化shardedJedisPool
	 */
	public void initShardedJedisPool(){
		//此处可以添加多个redis服务器。
		shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo("127.0.0.1", 6379));
		
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(10);
		config.setMaxTotal(30);
		config.setMaxWaitMillis(3*1000);
	
		shardedJedisPool = new ShardedJedisPool(config, shards);
		
		System.out.println("shardedJedisPool初始化成功");
		
	}
	
	/**
	 * 获取缓存数据
	 */
	public <T> T hget(final String key,final String field,final TypeReference<T> type){
		String jsonString = "";
		try {
			ShardedJedis  jedis = shardedJedisPool.getResource();
			if (null == jedis) {
				throw new IOException("could not connect redis");
			}
			try {
				jsonString = jedis.hget(key,field);
				System.out.println("从缓存去除数据成功：" + jsonString);
			} finally{
				jedis.close();
			}
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
			return null;
		}
		return JSON.parseObject(jsonString,type);
	}
	
	/**
	 * 设置缓存数据
	 * @param result
	 * @param field
	 * @param key
	 */
	public <T> void hset(final T obj,final String field,String key){
		String jsonString = JSON.toJSONString(obj,
				SerializerFeature.WriteDateUseDateFormat,
				SerializerFeature.DisableCircularReferenceDetect);
		try {
			ShardedJedis  jedis = shardedJedisPool.getResource();
			if (null == jedis) {
				throw new IOException("could not connect redis");
			}
			try {
				jedis.hset(key,field,jsonString);
				System.out.println("放入缓存成功：[key="+key+"----value="+jsonString+"]");
			} finally{
				jedis.close();
			}
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 删除
	 * @param key
	 */
	public void delete(final String key) {
		try {
			ShardedJedis jedis = shardedJedisPool.getResource();
			if (null == jedis) {
				throw new IOException("could not connect redis");
			}try {
				jedis.del(key);
			} finally{
				jedis.close();
			}
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 保存key
	 * @param key
	 * @param obj
	 * @return
	 */
	public <T> Long setsKeySave(String key, T... obj) {
		if (null == obj) {
			return -1L;
		}
		String inputs[] = new String[obj.length];
		for (int i = 0; i < obj.length; i++) {
			inputs[i] = JSONObject.toJSONString(obj[i],
					SerializerFeature.WriteDateUseDateFormat,
					SerializerFeature.DisableCircularReferenceDetect);
		}
		try (ShardedJedis  jedis = shardedJedisPool.getResource()) {
			if (null == jedis) {
				throw new IOException("could not connect redis");
			}
			return jedis.sadd(key, inputs);
		} catch (IOException e) {
			logger.info(e.getMessage());
			return -1L;
		}
	}
	
	/**
	 * 获取所有的key,以set形式返回
	 * @param key
	 * @param type
	 * @return
	 */
	public <T> Set<T> setsKeyGetAll(String key, TypeReference<T> type) {
		try {
			ShardedJedis  jedis = shardedJedisPool.getResource();
			if (null == jedis) {
				throw new IOException("could not connect redis");
			}
			try {
				Set<String> outputs = jedis.smembers(key);
				Set<T> resultSet = new HashSet<T>(outputs.size());
				for (String temp : outputs) {
					resultSet.add(JSONObject.parseObject(temp, type));
				}
				return resultSet;
			} finally{
				jedis.close();
			}
		} catch (IOException e) {
			logger.info(e.getMessage());
			return null;
		}
		
	}
	
}

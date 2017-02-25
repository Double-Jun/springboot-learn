package com.example.util;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Tuple;

import java.util.*;

/**
 * @Author chenmingjun
 * @Date 2017/2/23 17:16
 */
@Component
public class RedisManage {

	private static final String host = "10.0.0.4";
	private static final int port = 6379;
	private static final int timeout = 0;
	private static final String password = "lw@2015";
	private static final int database = 8;

	private JedisPool jedisPool = null;

	public RedisManage() {
		init();
	}

	private void init() {
		if (jedisPool == null) {
			if (!"".equals(password)) {
				jedisPool = new JedisPool(new JedisPoolConfig(), host, port, timeout, password, database);
			} else {
				jedisPool = new JedisPool(new JedisPoolConfig(), host, port, 0, null, database);
			}
		}
	}

	public byte[] get(byte[] key) {
		byte[] value = null;
		try (Jedis jedis = jedisPool.getResource()) {
			value = jedis.get(key);
		}
		return value;
	}

	public byte[] set(byte[] key, byte[] value) {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.set(key, value);
		}
		return value;
	}

	public byte[] set(byte[] key, byte[] value, int expire) {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.set(key, value);
			if (expire != 0) {
				jedis.expire(key, expire);
			}
		}
		return value;
	}

	public void del(byte[] key) {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.del(key);
		}
	}

	public void flushDB() {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.flushDB();
		}
	}

	public Long dbSize() {
		Long dbSize = 0L;
		try (Jedis jedis = jedisPool.getResource()) {
			dbSize = jedis.dbSize();
		}
		return dbSize;
	}

	public Set<byte[]> keys(String pattern) {
		Set<byte[]> keys = null;
		try (Jedis jedis = jedisPool.getResource()) {
			keys = jedis.keys(pattern.getBytes());
		}
		return keys;
	}

	public Double incrScore(String key, String member) {
		try (Jedis jedis = jedisPool.getResource()) {
			return jedis.zincrby(key, 1d, member);
		}
	}

	public Map<String, Double> sortWithScore(String key, Integer offset, Integer size) {
		try (Jedis jedis = jedisPool.getResource()) {
			Map<String, Double> map = new HashMap<>();
			Set<Tuple> set = jedis.zrangeWithScores(key, offset, offset + size);
			for (Tuple tuple : set) {
				map.put(tuple.getElement(), tuple.getScore());
			}
			return map;
		}
	}

	public Long addAll(String key, String[] value) {
		try (Jedis jedis = jedisPool.getResource()) {
			return jedis.lpush(key, value);
		}
	}

	public List<String> getAll(String key) {
		try (Jedis jedis = jedisPool.getResource()) {
			return jedis.lrange(key, 0, -1);
		}
	}

	public String set2Map(String key, Map<String, String> values) {
		try (Jedis jedis = jedisPool.getResource()) {
			return jedis.hmset(key, values);
		}
	}

	public String set2Map(byte[] key, Map<byte[], byte[]> valueMap) {
		try (Jedis jedis = jedisPool.getResource()) {
			return jedis.hmset(key, valueMap);
		}
	}

	public String getFromMap(String key, String filed) {
		try (Jedis jedis = jedisPool.getResource()) {
			return jedis.hget(key, filed);
		}
	}

	public byte[] getFromMap(byte[] key, byte[] filed) {
		try (Jedis jedis = jedisPool.getResource()) {
			return jedis.hget(key, filed);
		}
	}

	public String hmsetByte(byte[] key, Map<byte[], byte[]> values) {
		try (Jedis jedis = jedisPool.getResource()) {
			return jedis.hmset(key, values);
		}
	}

	public byte[] hgetByte(byte[] key, byte[] filed) {
		try (Jedis jedis = jedisPool.getResource()) {
			return jedis.hget(key, filed);
		}
	}

	public long hdelByte(byte[] key, byte[] filed) {
		try (Jedis jedis = jedisPool.getResource()) {
			return jedis.hdel(key, filed);
		}
	}

	public Set<String> getAllKeys() {
		try (Jedis jedis = jedisPool.getResource()) {
			return jedis.keys("*");
		}
	}

	public void setList(byte[] key, byte[]... valueList) {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.lpush(key, valueList);
		}
	}

	public List<byte[]> getList(byte[] key) {
		try (Jedis jedis = jedisPool.getResource()) {
			return jedis.lrange(key, 0, -1);
		}
	}
}

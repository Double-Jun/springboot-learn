package com.example.service;

import com.example.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author chenmingjun
 * @Date 2017/2/23 14:50
 */
@Service
public class RedisService {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	public void set(String key, String value) throws Exception {
		Boolean hasKey = stringRedisTemplate.hasKey(key);
		if (hasKey) {
			throw new Exception("this key 【" + key + "】 has exist in redis");
		} else {
			ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
			valueOperations.set(key, value);
		}
	}

	public void set(String key, String... values) throws Exception {
		Boolean hasKey = stringRedisTemplate.hasKey(key);
		if (hasKey) {
			throw new Exception("this key 【" + key + "】 has exist in redis");
		} else {
			ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
			listOperations.rightPushAll(key, values);
		}
	}

	public String get(String key) {
		return stringRedisTemplate.opsForValue().get(key);
	}

	public List<String> getList(String key) {
		return stringRedisTemplate.opsForList().range(key, 0, 15);
	}

	public void del(String key) {
		stringRedisTemplate.delete(key);
	}

	public void setObject(String key, Person person) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(person);
		oos.flush();
		oos.close();
		byte[] bytes = bos.toByteArray();
		stringRedisTemplate.restore(key, bytes, 1, TimeUnit.DAYS);
		bos.close();
	}
}

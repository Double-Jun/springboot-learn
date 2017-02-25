package com.example.web;

import com.example.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author chenmingjun
 * @Date 2017/2/23 15:26
 */
@RestController
@RequestMapping("/redis")
public class RedisController {

	@Autowired
	private RedisService redisService;

	@RequestMapping(value = "/set/{key}/{value}")
	public String set(@PathVariable String key, @PathVariable String value) throws Exception {
		redisService.set(key, value);
		return "set " + key + " in redis success!";
	}

	@RequestMapping(value = "/get/{key}")
	public String get(@PathVariable String key) {
		return redisService.get(key);
	}

	@RequestMapping(value = "/getList/{key}")
	public List<String> getList(@PathVariable String key) {
		return redisService.getList(key);
	}

	@RequestMapping(value = "/del/{key}")
	public void del(@PathVariable String key) {
		redisService.del(key);
	}

}

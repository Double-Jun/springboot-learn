package com.example;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

/**
 * @Author chenmingjun
 * @Date 2017/2/24 18:22
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

	@Test
	public void testPing() {
		Jedis jedis = new Jedis("127.0.0.1", 6379);
		Assert.assertEquals("PONG", jedis.ping());
	}

}

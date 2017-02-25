package com.example;

import com.example.constact.RedisKeyConstant;
import com.example.entity.Person;
import com.example.service.RedisService;
import com.example.service.LocalAccountService;
import com.example.util.RedisManage;
import com.example.util.SerializeUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RootApplicationTests {

	@Autowired
	private RedisService redisService;
	@Autowired
	private RedisManage redisManage;
	@Autowired
	private LocalAccountService accountUtils;

	@Test
	public void testSetCollection() throws Exception {
		redisService.del("numbers2");
		redisService.set("numbers2", "1", "2", "3");
		List<String> numbers2 = redisService.getList("numbers2");
		String[] expectedArr = { "1", "2", "3" };
		Assert.assertArrayEquals(expectedArr, numbers2.toArray());
		redisService.del("numbers2");
	}

	@Test
	public void testSetObject() throws Exception {
		RedisManage redisManage = new RedisManage();
		Person person = getPerson(1);
		Person person2 = getPerson(2);
		String key = "zhangsan";
		redisManage.del(key.getBytes());
		redisManage.setList(key.getBytes(), SerializeUtils.serialize(person), SerializeUtils.serialize(person2));
		List<byte[]> all = redisManage.getList(key.getBytes());
		for (byte[] bytes : all) {
			Person deserialize = (Person) SerializeUtils.deserialize(bytes);
			System.out.println(deserialize);
		}

	}

	@Test
	public void testSetMap() throws Exception {
		RedisManage redisManage = new RedisManage();
		Map<String, String> map = new HashMap<>();
		map.put("a", "aaa");
		map.put("b", "bbb");
		map.put("c", "ccc");
		map.put("d", "ddd");
		redisManage.del("map".getBytes());
		redisManage.set2Map("map", map);
		String fromMap = redisManage.getFromMap("map", "a");
		Assert.assertEquals("aaa", fromMap);
	}

	@Test
	public void testSetAccount() throws Exception {
		long startTime = System.currentTimeMillis();
		Map<byte[], byte[]> personMap = new HashMap<>();
		for (int i = 0; i < 500000; i++) {
			personMap.put((RedisKeyConstant.ACCOUNT_MAP_FIELD_PRE + i).getBytes(), SerializeUtils.serialize(getPerson(i)));
		}
		System.out.println("======================");
		long time = System.currentTimeMillis();
		System.out.println("创建100000个对象耗时：" + (time - startTime) + "ms");
		redisManage.del(RedisKeyConstant.ACCOUNT_MAP_KEY);
		redisManage.set2Map(RedisKeyConstant.ACCOUNT_MAP_KEY, personMap);
		long endTime = System.currentTimeMillis();
		System.out.println("======================");
		System.out.println("写入Redis耗时：" + (endTime - time) + "ms");
		System.out.println("======================");
		byte[] accountBytes = redisManage.getFromMap(RedisKeyConstant.ACCOUNT_MAP_KEY, (RedisKeyConstant.ACCOUNT_MAP_FIELD_PRE + "1").getBytes());
		System.out.println(SerializeUtils.deserialize(accountBytes));
		System.out.println("======================");
		System.out.println("读取Redis耗时：" + (System.currentTimeMillis() - endTime) + "ms");
	}

	@Test
	public void testAccountUtils() throws Exception {
		testSetAccount();
		String token = "100";
		System.out.println("person:" + accountUtils.getPeron(token));
		String accountId = accountUtils.getCurrentAccountId(token);
		System.out.println("person id:" + accountId);
		String name = accountUtils.getAccountDisplayName(token);
		System.out.println("person name:" + name);
	}

	private Person getPerson(int i) {
		Person person = new Person();
		person.setId(String.valueOf(i));
		person.setName("张三(" + i + ")");
		person.setAge(21);
		person.setGender("男");
		person.setBirthday(new Date());
		return person;
	}

	@Test
	public void contextLoads() {
	}

}

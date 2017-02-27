package com.example.util;

import com.example.entity.Account;
import com.google.code.or.common.glossary.Column;
import com.google.code.or.common.glossary.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author chenmingjun
 * @Date 2017/2/24 14:03
 */
@Component
public class MySqlSyncRedis {

	private static final Logger LOGGER = LoggerFactory.getLogger(MySqlSyncRedis.class);
	//	@Autowired
	private RedisManage redisManage = new RedisManage();

	public Account buildAccount(Row row) {
		List<Column> columns = row.getColumns();
		Account account = new Account();
		account.setId(String.valueOf(columns.get(0)));
		account.setPinyin(String.valueOf(columns.get(1)));
		account.setIdentity_card(String.valueOf(columns.get(2)));
		account.setDisplay_name(String.valueOf(columns.get(3)));
		account.setGender((Integer) (columns.get(4).getValue()));
		return account;
	}

	public void updateRedis(String key, String value) {
		if (redisManage.exist(key)) {
			redisManage.del(key);
		}
		redisManage.set("accountId:" + key, value);
	}

}

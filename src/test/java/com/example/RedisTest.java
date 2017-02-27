package com.example;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.Account;
import com.example.util.MySqlSyncRedis;
import com.google.code.or.OpenReplicator;
import com.google.code.or.binlog.BinlogEventListener;
import com.google.code.or.binlog.BinlogEventV4;
import com.google.code.or.binlog.impl.event.DeleteRowsEvent;
import com.google.code.or.binlog.impl.event.UpdateRowsEvent;
import com.google.code.or.binlog.impl.event.WriteRowsEvent;
import com.google.code.or.common.glossary.Pair;
import com.google.code.or.common.glossary.Row;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author chenmingjun
 * @Date 2017/2/24 18:22
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(MySqlSyncRedis.class);

	@Test
	public void testPing() {
		Jedis jedis = new Jedis("127.0.0.1", 6379);
		Assert.assertEquals("PONG", jedis.ping());
	}

	@Test
	public void testSync() throws Exception {
		final MySqlSyncRedis obj = new MySqlSyncRedis();
		final OpenReplicator or = new OpenReplicator();
		or.setUser("root");
		or.setPassword("root");
		or.setHost("10.0.0.36");
		or.setPort(3306);
		or.setServerId(6789);
		//		or.setBinlogPosition(120);
		or.setBinlogFileName("mysql-bin.000061");
		or.setBinlogEventListener(new BinlogEventListener() {

			public void onEvents(BinlogEventV4 event) {
				//		    	LOGGER.info("{}", event);
				if (event instanceof UpdateRowsEvent) {
					UpdateRowsEvent updateRowsEvent = (UpdateRowsEvent) event;
					List<Pair<Row>> rows = updateRowsEvent.getRows();
					for (Pair<Row> row : rows) {
						Account account = obj.buildAccount(row.getAfter());
						String jsonString = JSONObject.toJSONString(account);
						obj.updateRedis(account.getId(), jsonString);
					}
				}
				if (event instanceof WriteRowsEvent) {

				}
				if (event instanceof DeleteRowsEvent) {

				}
			}

		});
		or.start();

		//
		LOGGER.info("press 'q' to stop");
		final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		for (String line = br.readLine(); line != null; line = br.readLine()) {
			if (line.equals("q")) {
				or.stop(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
				break;
			}
		}
	}
}

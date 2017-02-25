package com.example.util;

import com.google.code.or.OpenReplicator;
import com.google.code.or.binlog.BinlogEventListener;
import com.google.code.or.binlog.BinlogEventV4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * @Author chenmingjun
 * @Date 2017/2/24 14:03
 */
public class MySqlSyncRedis {

	public static void main(String[] args) throws Exception {
		final OpenReplicator or = new OpenReplicator();
		or.setUser("root");
		or.setPassword("root");
		or.setHost("10.0.0.36");
		or.setPort(3306);
		or.setServerId(6789);
		or.setBinlogPosition(4);
		or.setBinlogFileName("mysql-bin.000001");
		or.setBinlogEventListener(new BinlogEventListener() {

			public void onEvents(BinlogEventV4 event) {
				// your code goes here
				System.out.println("===============================");
				System.out.println(event.getHeader());
			}
		});
		or.start();

		System.out.println("press 'quit' or 'exit' to stop");
		final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		for (String line = br.readLine(); line != null; line = br.readLine())
			if (line.equals("q") || line.equals("exit")) {
				or.stop(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
				break;
			}
	}

}

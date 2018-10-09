package com.jx.hbase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jx.kconsume.JxKConsumeServer;
import com.jx.kconsume.biz.js.model.UserMarking;
import com.jx.kconsume.biz.js.model.UserMarkingBean;
import com.jx.kconsume.biz.js.service.UserMarkingService;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JxKConsumeServer.class) // 指定spring-boot的启动类
@Slf4j
public class UserMarkingTest {

	@Autowired
	@Qualifier("phoenixTemplate")
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private UserMarkingService 	userMarkingService;
	
	
	/**
	 * 查询
	 */
	@Test
	public void getUserMarkingId() {
//		System.out.println("UserMarkingId="+userMarkingService.getUserMarkingId());
		System.out.println("UserMarkingId="+userMarkingService.getUserMarkingIdByMarking("qr-2000","qr"));
		System.out.println("UserMarkingId="+userMarkingService.getUserMarkingIdByUserId("qr-2000","20000"));
		System.out.println("....end");
	}
	
	
	/**
	 * 查询
	 */
	@Test
	public void queryAll() {
		System.out.println("start....");
		List<UserMarking> testBean = jdbcTemplate.query("select * from user_marking", user_markingMapper);
		for (UserMarking b : testBean) {
			System.out.println(b);
		}
		System.out.println("....end");
	}

	/**
	 * 插入数据
	 */
	@Test
	public void userMarkingUpsert() {
		System.out.println("start....");
		UserMarkingBean userMarking=new UserMarkingBean();
		userMarking.setMark_user_id(1000);
		userMarking.setClient_id("qr-2000");
		userMarking.setUser_id("20000");
		userMarking.setUser_project("qr");
		int count =  userMarkingService.upsert(userMarking,"192.168.1.1");
		System.out.println(String.format("update count=%s", count));
		System.out.println("....end");

	}
	
	
	@Test
	public void upsert() {
		System.out.println("start....");
		Object[] param = new Object[] { "10000", "c_10000", "qr", "192.168.1.1", System.currentTimeMillis(),
				System.currentTimeMillis(), "10000", "192.168.1.1", "qr", System.currentTimeMillis() };
		String sql = "UPSERT INTO user_marking (user_id,client_id,user_project,last_ip,insert_date,update_date)  VALUES(?,?,?,?,?,?) ON DUPLICATE KEY UPDATE user_id=?,user_project=?,last_ip=?,update_date = ?";
		int count = jdbcTemplate.update(sql, param);
		System.out.println(String.format("update count=%s", count));
		System.out.println("....end");

	}

	RowMapper<UserMarking> user_markingMapper = new RowMapper<UserMarking>() {
		@Override
		public UserMarking mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserMarking bean = new UserMarking();
			bean.setUser_id(rs.getLong("user_id"));
			bean.setClient_id(rs.getString("client_id"));
			bean.setInsert_date(rs.getTimestamp("insert_date").getTime());
			bean.setUpdate_date(rs.getTimestamp("update_date").getTime());
			return bean;
		}
	};

}

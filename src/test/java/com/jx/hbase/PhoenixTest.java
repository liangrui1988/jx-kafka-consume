package com.jx.hbase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jx.hbase.bean.TestBean;
import com.jx.kconsume.JxKConsumeServer;
import com.jx.kconsume.biz.js.model.UserMarking;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JxKConsumeServer.class) // 指定spring-boot的启动类
@Slf4j
public class PhoenixTest {

	@Autowired
	@Qualifier("phoenixTemplate")
	private JdbcTemplate jdbcTemplate;

	@Test
	public void test1() {
		System.out.println("hello" + jdbcTemplate);
	}

	@Test
	public void getUser_marking() {
		System.out.println("xx");
		List<UserMarking> testBean = jdbcTemplate.query("select * from user_marking", user_markingMapper);
		for (UserMarking b : testBean) {
			System.out.println(b);
		}
		System.out.println("xx end");

	}

	@Test
	public void test2() {
		// System.setProperty("jute.maxbuffer", "409600000");
		System.setProperty("jute.maxbuffer", "12134861600");

		System.out.println("xx");
		List<TestBean> testBean = jdbcTemplate.query("select id,name from table_test", rowMapper);
		for (TestBean b : testBean) {
			System.out.println(b);
		}
		System.out.println("xx end");

	}

	RowMapper<TestBean> rowMapper = new RowMapper<TestBean>() {
		@Override
		public TestBean mapRow(ResultSet rs, int rowNum) throws SQLException {
			TestBean bean = new TestBean();
			bean.setId(rs.getInt("user_id"));
			bean.setName(rs.getString("name"));

			return bean;
		}
	};

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

	private static String driver = "org.apache.phoenix.jdbc.PhoenixDriver";

	public static void main(String[] args) throws SQLException {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Statement stmt = null;
		ResultSet rs = null;

		Connection con = DriverManager.getConnection("jdbc:phoenix:192.168.230.133:2181");
		// Connection con = DriverManager
		// .getConnection("jdbc:phoenix:thin:url=http://localhost:8765;serialization=PROTOBUF");
		//
		stmt = con.createStatement();
		String sql = "select id,name from table_test";
		rs = stmt.executeQuery(sql);
		while (rs.next()) {
			System.out.print("id:" + rs.getString("id"));
			System.out.println(",name:" + rs.getString("name"));
		}
		stmt.close();
		con.close();
	}

}

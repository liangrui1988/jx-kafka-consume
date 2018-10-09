package com.jx.kconsume.biz.js.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.jx.kconsume.biz.js.model.UserMarkingBean;

/**
 * 用户标识相关
 * 
 * @date 2018-09-03
 * @author liangrui
 *
 */
@Component
public class UserMarkingService {
	@Autowired
	@Qualifier("phoenixTemplate")
	private JdbcTemplate jdbcTemplate;

	/**
	 * 存在更新，不存在则插入
	 * 
	 * @param userMarking
	 * @param ip
	 * @return
	 */
	public int upsert(UserMarkingBean userMarking, String ip) {

		String userId = getUseIdByMarking(userMarking.getClient_id(), userMarking.getUser_project());

		int count = 0;
		if (StringUtils.isBlank(userId) || "-1".equals(userId)) {// 没有找到记录，第一次
			Object[] param = new Object[] { userMarking.getMark_user_id(), userMarking.getUser_id(),
					userMarking.getClient_id(), userMarking.getUser_project(), ip, System.currentTimeMillis(),
					System.currentTimeMillis(), userMarking.getUser_id(), userMarking.getUser_project(), ip,
					System.currentTimeMillis() };
			String sql = "UPSERT INTO user_marking (mark_user_id,user_id,client_id,user_project,last_ip,insert_date,update_date)  VALUES(?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE user_id=?,user_project=?,last_ip=?,update_date = ?";
			count = jdbcTemplate.update(sql, param);
		} else {// 用户已经存在，不需要改变，以免没有登录的时候防问，又被改为-1的情况
			count = upsertOnUserID(userMarking, ip);
		}
		if (count <= 0) {
			return -1;
		}
		return count;
	}

	/**
	 * 如果这个设备已经绑定了用户信息，再次传-1时，不需要改改为-1，保留原来的
	 * 
	 * @param userMarking
	 * @param ip
	 * @return
	 */
	public int upsertOnUserID(UserMarkingBean userMarking, String ip) {
		Object[] param = new Object[] { userMarking.getMark_user_id(), userMarking.getUser_id(),
				userMarking.getClient_id(), userMarking.getUser_project(), ip, System.currentTimeMillis(),
				System.currentTimeMillis(), userMarking.getUser_project(), ip, System.currentTimeMillis() };
		String sql = "UPSERT INTO user_marking (mark_user_id,user_id,client_id,user_project,last_ip,insert_date,update_date)  VALUES(?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE user_project=?,last_ip=?,update_date = ?";
		int count = jdbcTemplate.update(sql, param);
		if (count <= 0) {
			return -1;
		}
		return count;
	}

	/**
	 * 直接生成一个新的转换用户的id
	 * 
	 * @param userMarking
	 * @param ip
	 * @return
	 */
	public long getUserMarkingId() {
		String sql = "SELECT NEXT VALUE FOR user_marking_id_sequence";
		Long user_marking_id_sequence = null;
		try {
			user_marking_id_sequence = jdbcTemplate.queryForObject(sql, Long.class);
		} catch (EmptyResultDataAccessException e) {
			// return 0;
		}
		if (user_marking_id_sequence == null || user_marking_id_sequence <= 0) {
			return System.currentTimeMillis();// 为空，给当前时间
		}
		return user_marking_id_sequence;
	}

	/**
	 * 根据用户ID来查询UserMarkingId
	 * 
	 * @param userMarking
	 * @param ip
	 * @return
	 */
	public long getUserMarkingIdByUserId(String userID, String user_project) {
		String sql = "select mark_user_id from user_marking where user_id=? and user_project=? limit 1";
		Long user_marking_id_sequence = null;
		try {
			user_marking_id_sequence = jdbcTemplate.queryForObject(sql, new Object[] { userID, user_project },
					Long.class);
		} catch (EmptyResultDataAccessException e) {
			return 0;
		}
		if (user_marking_id_sequence == null) {
			return 0;
		}
		return user_marking_id_sequence;
	}

	/**
	 * 根据设备标识来查询UserMarkingId
	 * 
	 * @param userMarking
	 * @param ip
	 * @return
	 */
	public long getUserMarkingIdByMarking(String client_id, String user_project) {
		String sql = "select mark_user_id from user_marking where client_id=? and user_project=? ";
		Long user_marking_id_sequence = null;
		try {
			user_marking_id_sequence = jdbcTemplate.queryForObject(sql, new Object[] { client_id, user_project },
					Long.class);
		} catch (EmptyResultDataAccessException e) {
			return 0;
		}
		if (user_marking_id_sequence == null) {
			return 0;
		}
		return user_marking_id_sequence;
	}

	/**
	 * 根据设备来获取用户id
	 * 
	 * @param userMarking
	 * @param ip
	 * @return
	 */
	public String getUseIdByMarking(String client_id, String user_project) {
		String sql = "select user_id from user_marking where client_id=? and user_project=?";
		String UserID = "-1";
		try {
			UserID = jdbcTemplate.queryForObject(sql, new Object[] { client_id, user_project }, String.class);
		} catch (EmptyResultDataAccessException e) {
			return "-1";
		}
		return UserID;
	}

}

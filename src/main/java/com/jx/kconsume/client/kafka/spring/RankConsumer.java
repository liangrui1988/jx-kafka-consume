package com.jx.kconsume.client.kafka.spring;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 排行榜消费
 * 
 * @author ruiliang
 * @date 2018-10-09
 *
 */
@Component
public class RankConsumer {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("phoenixTemplate")
	private JdbcTemplate jdbcTemplate;

	/**
	 * 
	 * 排行榜消费
	 * 
	 * @param message
	 */
	@KafkaListener(topics = { "streams-rank" })
	public void consumer(String message) {
		if (StringUtils.isBlank(message)) {
			log.warn("consumer streams-rank topic message is 空");
			return;
		}
		if (!message.startsWith("{") || !message.endsWith("}")) {
			log.warn("consumer streams-rank topic message is not json");
			return;
		}
		log.info("consumer streams-rank topic message : {}", message);
		try {
			// 转json
			long time = System.currentTimeMillis();
			JSONObject mj = JSON.parseObject(message);
			if ("searchProduct".equals(mj.getString("rank_type"))) { // url解码
				String rank_id = mj.getString("rank_id");// search_word
				try {
					String rank_id_decode = java.net.URLDecoder.decode(rank_id, "UTF-8");
					mj.put("rank_id", rank_id_decode);
				} catch (UnsupportedEncodingException e) {
					log.error("url解码异常", e);
					e.printStackTrace();
				}
			}
			Object[] param = new Object[] { mj.getString("user_project"), mj.getString("rank_type"),
					mj.getString("rank_id"), mj.getString("rank_channel"), mj.getString("rank_name"),
					mj.getLong("rank_value"), time, mj.getLong("rank_value"), time };
			String sql = "UPSERT INTO common_rank (user_project,rank_type,rank_id,rank_channel,rank_name,rank_value,update_date) VALUES(?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE rank_value = rank_value+?,update_date=?";
			int count = jdbcTemplate.update(sql, param);
			if (count <= 0) {
				log.error("consumer streams-rank topic message 插入失败！");
			}
		} catch (Exception e) {
			log.error("consumer streams-rank topic message 异常", e);
			e.printStackTrace();
		}
	}
}

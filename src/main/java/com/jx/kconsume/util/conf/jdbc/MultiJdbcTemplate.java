package com.jx.kconsume.util.conf.jdbc;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 多数据源配置
 * 
 * @author ruiliang
 * @date 2018-07-31
 */
@Configuration
public class MultiJdbcTemplate {

	@Bean
	@Primary
	public JdbcTemplate primaryJdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean(name = "phoenixTemplate")
	public JdbcTemplate secondJdbcTemplate(@Qualifier("phoenixDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
}

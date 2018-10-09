package com.jx.kconsume.util.conf.jdbc;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 多数据源配置
 * 
 * @author ruiliang
 *
 */
@Configuration
// @PropertySource("classpath:application.properties")
public class MultiConfiguration {

	@Autowired
	private Environment env;

	/**
	 * 默认的数据源mysql
	 * 
	 * @return
	 */
	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource primaryDataSource() {
		return DataSourceBuilder.create().build();
	}

	/**
	 * phoenix
	 * 
	 * @return
	 */
	@Bean(name = "phoenixDataSource")
	@Qualifier("phoenixDataSource")
	// @ConfigurationProperties(prefix = "phoenix")
	public DataSource secondDataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(env.getProperty("phoenix.url"));
		dataSource.setDriverClassName(env.getProperty("phoenix.driver-class-name"));
		dataSource.setUsername(env.getProperty("phoenix.username"));//
		// phoenix的用户名默认为空
		dataSource.setPassword(env.getProperty("phoenix.password"));// phoenix的密码默认为空
		dataSource.setDefaultAutoCommit(Boolean.valueOf(env.getProperty("phoenix.default-auto-commit")));
		return dataSource;
	}
}
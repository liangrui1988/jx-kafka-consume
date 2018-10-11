package com.jx.kconsume;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

import lombok.extern.slf4j.Slf4j;

/**
 * 服务提供者
 */
@SpringBootApplication
@ImportResource({ "classpath:config/dubbo-provider.xml" })
//@EnableAspectJAutoProxy(proxyTargetClass = true)
//@EnableConfigurationProperties
@ComponentScan("com.jx")
@Slf4j
public class JxKConsumeServer extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(JxKConsumeServer.class, args);
		 log.info("-----------JxKConsumeServer 启动成功");
	}
	

}

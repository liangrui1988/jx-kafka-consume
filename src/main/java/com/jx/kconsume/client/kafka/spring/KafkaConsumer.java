package com.jx.kconsume.client.kafka.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 监听test主题,有消息就读取
	 * 
	 * @param message
	 */
	@KafkaListener(topics = { "streams-wordcount-output2" })
	public void consumer(String message) {
		System.out.println("test topic message : {}" + message);
		log.info("test topic message : {}", message);
	}
}

package com.jx.kafka.spring;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.concurrent.ListenableFuture;

import com.jx.kconsume.JxKConsumeServer;

import lombok.extern.slf4j.Slf4j;

/**
 * spring 封装测试
 * 
 * @author rui
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JxKConsumeServer.class) // 指定spring-boot的启动类
@Slf4j
public class ProducerTest {
	// protected final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	String topic = "streams-wordcount-output";

	public void sendMessage(String key, String data) {
		kafkaTemplate.send(new ProducerRecord<String, String>(topic, null, data));
	}

	@Test
	public void send() {
		kafkaTemplate.send(new ProducerRecord<String, String>(topic, null, "hello kafka 2"));
	}



	@Test
	public void sendReturn() {
		ListenableFuture<SendResult<String, String>> future = kafkaTemplate
				.send(new ProducerRecord<String, String>(topic, "keyaaa", "vvv"));
		future.addCallback(// 添加成功发送消息的回调和失败的回调
				result -> log.info("send message to {} success", topic),
				ex -> log.info("send message to {} failure,error message:{}", topic, ex.getMessage()));

	}



}

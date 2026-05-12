package com.karyam.operations.service.impl;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.karyam.event.dto.KafkaEvent;
import com.karyam.operations.config.props.KafkaProperties;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaProducer {
	
	@Autowired
	private KafkaProperties kafkaProperties;

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
	
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void publishProjectEvent(KafkaEvent<?> event) {
		log.info("Publishing {} event, Id: {}", event.getEventType(), event.getEventId());
		publishEvent(event, kafkaProperties.getProjectTopic());
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void publishLaborEvent(KafkaEvent<?> event) {
		log.info("Publishing {} event, Id: {}", event.getEventType(), event.getEventId());
		publishEvent(event, kafkaProperties.getLaborTopic());
	}
	
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void publishAttendanceEvent(KafkaEvent<?> event) {
		log.info("Publishing {} event, Id: {}", event.getEventType(), event.getEventId());
		publishEvent(event, kafkaProperties.getAttendanceTopic());
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void publishVendorEvent(KafkaEvent<?> event) {
		log.info("Publishing {} event, Id: {}", event.getEventType(), event.getEventId());
		publishEvent(event, kafkaProperties.getVendorTopic());
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void publishExpenseEvent(KafkaEvent<?> event) {
		log.info("Publishing {} event, Id: {}", event.getEventType(), event.getEventId());
		publishEvent(event, kafkaProperties.getExpenseTopic());
	}
	
	private void publishEvent(KafkaEvent<?> event, String topic) {
		
		CompletableFuture<SendResult<String,Object>> future = kafkaTemplate.send(topic, event.getEventId(), event);
		future.whenComplete((result, ex) -> {
			if (ex == null) {
				log.info("Successfully published Event: {}, Id: {}, to partition: {}, offset: {}",
						event.getEventType(),
						event.getEventId(),
						result.getRecordMetadata().partition(),
						result.getRecordMetadata().offset());
			} else {
				log.error("Failed to publish Event: {}, Id: {}", event.getEventType(), event.getEventId(), ex);
			}
		});
	}

}

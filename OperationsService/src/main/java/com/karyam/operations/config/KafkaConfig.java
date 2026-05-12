package com.karyam.operations.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin.NewTopics;

import com.karyam.operations.config.props.KafkaProperties;

@Configuration
public class KafkaConfig {
	
	@Autowired
	private KafkaProperties kafkaProperties;

	@Bean
	NewTopics createTopics() { 
		return new NewTopics(
				TopicBuilder.name(kafkaProperties.getProjectTopic())
				.partitions(2)
				.replicas(1)
				.build(),
				TopicBuilder.name(kafkaProperties.getLaborTopic())
				.partitions(2)
				.replicas(1)
				.build(),
				TopicBuilder.name(kafkaProperties.getAttendanceTopic())
				.partitions(1)
				.replicas(1)
				.build(),
				TopicBuilder.name(kafkaProperties.getVendorTopic())
				.partitions(2)
				.replicas(1)
				.build(),
				TopicBuilder.name(kafkaProperties.getExpenseTopic())
				.partitions(2)
				.replicas(1)
				.build());
	}
}

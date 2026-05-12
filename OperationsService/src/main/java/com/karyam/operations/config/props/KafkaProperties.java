package com.karyam.operations.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("karyam.kafka")
public class KafkaProperties {

	private String projectTopic;
	private String laborTopic;
	private String attendanceTopic;
	private String vendorTopic;
	private String expenseTopic;
}

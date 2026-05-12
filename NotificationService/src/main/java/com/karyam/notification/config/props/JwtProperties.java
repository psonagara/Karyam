package com.karyam.notification.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("karyam.jwt")
public class JwtProperties {

	private Long expTimeInMin;
	private String secreteKey;
}

package org.prgrms.wumo.global.config;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AwsConfig {

	private final String accessKey;

	private final String secretKey;

	private final String region;

	public AwsConfig(
			@Value("${cloud.aws.credentials.access-key:#{null}}") String accessKey,
			@Value("${cloud.aws.credentials.secret-key:#{null}}") String secretKey,
			@Value("${cloud.aws.region.static:#{null}}") String region) {
		this.accessKey = Objects.requireNonNull(accessKey, "AWS Access Key 정보를 불러오지 못했습니다.");
		this.secretKey = Objects.requireNonNull(secretKey, "AWS Secret Key 정보를 불러오지 못했습니다.");
		this.region = Objects.requireNonNull(region, "AWS Region 정보를 불러오지 못했습니다.");
	}

	@Bean
	public AmazonS3 amazonS3() {
		AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
		return AmazonS3ClientBuilder.standard()
				.withRegion(region)
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
				.build();
	}

}

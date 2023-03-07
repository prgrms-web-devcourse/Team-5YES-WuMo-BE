package org.prgrms.wumo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WumoFinalApplication {

	public static void main(String[] args) {
		SpringApplication.run(WumoFinalApplication.class, args);
	}

}

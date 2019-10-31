package gov.dot.its.datahub.webapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class DatahubApiApplication {

	private static final Logger logger = LoggerFactory.getLogger(DatahubApiApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DatahubApiApplication.class);

		logger.info("ITS DataHub WebAPI Started");
	}

}

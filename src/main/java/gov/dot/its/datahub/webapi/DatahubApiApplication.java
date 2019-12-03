package gov.dot.its.datahub.webapi;

import java.io.PrintStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableAutoConfiguration
public class DatahubApiApplication {

	private static final Logger logger = LoggerFactory.getLogger(DatahubApiApplication.class);

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(DatahubApiApplication.class);
		app.setLogStartupInfo(false);

		Banner banner = new Banner() {
			static final String MESSAGE_TEMPLATE = "%s = %s";
			static final String ES_TEMPLATE = "%s = %s://%s:%s";
			@Override
			public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
				out.println(new String(new char[80]).replace("\0", "_"));
				out.println();
				out.println("******                      *     *                  *     *                  *    ******  *** ");
				out.println("*     *   **   *****   **   *     * *    * *****     *  *  * ****** *****    * *   *     *  *  ");
				out.println("*     *  *  *    *    *  *  *     * *    * *    *    *  *  * *      *    *  *   *  *     *  *  ");
				out.println("*     * *    *   *   *    * ******* *    * *****     *  *  * *****  *****  *     * ******   *  ");
				out.println("*     * ******   *   ****** *     * *    * *    *    *  *  * *      *    * ******* *        *  ");
				out.println("*     * *    *   *   *    * *     * *    * *    *    *  *  * *      *    * *     * *        *  ");
				out.println("******  *    *   *   *    * *     *  ****  *****      ** **  ****** *****  *     * *       *** ");
				out.println();
				out.println("DataHub WebAPI");
				out.println();
				out.println(String.format(MESSAGE_TEMPLATE, "Port", environment.getProperty("server.port")));
				out.println(String.format(
						ES_TEMPLATE, "Elasticsearch",
						environment.getProperty("datahub.webapi.es.scheme"),
						environment.getProperty("datahub.webapi.es.host"),
						environment.getProperty("datahub.webapi.es.port")
						));
				out.println(String.format(MESSAGE_TEMPLATE, "Target Index", environment.getProperty("datahub.webapi.es.index")));
				out.println(String.format(MESSAGE_TEMPLATE, "Debug", environment.getProperty("datahub.webapi.debug")));

				out.println(new String(new char[80]).replace("\0", "_"));
			}

		};
		app.setBanner(banner);
		app.run();
		logger.info("ITS DataHub WebAPI Started");
	}

}

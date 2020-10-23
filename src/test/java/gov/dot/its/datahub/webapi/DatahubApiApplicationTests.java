package gov.dot.its.datahub.webapi;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DatahubApiApplicationTests {

	@Test
	public void testMain() {
		DatahubApiApplication.main(new String[]{});
		assertTrue(true);
	}

}

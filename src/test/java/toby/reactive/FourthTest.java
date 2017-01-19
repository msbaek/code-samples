package toby.reactive;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
public class FourthTest {

	@Test
	public void future_example() throws InterruptedException {
		ExecutorService es = Executors.newCachedThreadPool();

		es.execute(() -> {
			try {
				SECONDS.sleep(2);
			} catch (InterruptedException e) {
			}
			log.debug("Hello");
		});

		log.debug("Exit");
		SECONDS.sleep(2);
	}
}

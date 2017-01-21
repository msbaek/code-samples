package toby.reactive;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
public class FourthTest {

	@Test
	public void future_example() throws InterruptedException, ExecutionException {
		ExecutorService es = Executors.newCachedThreadPool();

		FutureTask<String> f = new FutureTask<>(() -> {
			SECONDS.sleep(2);
			log.debug("Hello");
			return "Hello";
		});

		es.execute(f);

		log.debug("isDone: {}", f.isDone());
		MILLISECONDS.sleep(2100);
		log.debug("Exit");
		log.debug("isDone: {}", f.isDone());
		log.debug("future: {}", f.get()); // blocking
	}
}

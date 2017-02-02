package toby.reactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
public class CallableLoadTest {
	static AtomicInteger counter = new AtomicInteger(0);

	public static void main(String[] args) throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(100);

		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:8080/rest";

		StopWatch main = new StopWatch();
		main.start();

		for (int i = 0; i < 100; i++) {
			executorService.execute(() -> {
				int idx = counter.addAndGet(1);
				log.info("Thread {}", idx);

				StopWatch sw = new StopWatch();
				sw.start();

				restTemplate.getForObject(url, String.class);

				sw.stop();
				log.info("Elapsed: {} {}", idx, sw.getTotalTimeSeconds());
			});
		}

		executorService.shutdown();
		executorService.awaitTermination(100, SECONDS);

		main.stop();
		log.info("Total: {}", main.getTotalTimeSeconds());
	}
}

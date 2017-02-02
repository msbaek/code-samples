package toby.reactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
public class CallableLoadTest {
	static AtomicInteger counter = new AtomicInteger(0);

	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
		ExecutorService executorService = Executors.newFixedThreadPool(100);

		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:8080/rest?idx={idx}";

		CyclicBarrier cb = new CyclicBarrier(101);

		for (int i = 0; i < 100; i++) {
			executorService.submit(() -> {
				int idx = counter.addAndGet(1);

				cb.await();

				log.info("Thread {}", idx);

				StopWatch sw = new StopWatch();
				sw.start();

				String res = restTemplate.getForObject(url, String.class, idx);

				sw.stop();
				log.info("Elapsed: {} {} / {} ", idx, sw.getTotalTimeSeconds(), res);
				return null;
			});
		}

		cb.await();

		StopWatch main = new StopWatch();
		main.start();

		executorService.shutdown();
		executorService.awaitTermination(100, SECONDS);

		main.stop();
		log.info("Total: {}", main.getTotalTimeSeconds());
	}
}

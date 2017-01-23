package toby.reactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

@SpringBootApplication
@Slf4j
@EnableAsync
public class SpringBootDemoApplication {
	@RestController
	public static class MyController {
		Queue<DeferredResult<String>> results = new ConcurrentLinkedDeque<>();

		@GetMapping("/dr")
		public DeferredResult<String> dr() throws InterruptedException {
			log.info("dr");
			DeferredResult<String> dr = new DeferredResult<>(600_000L);
			results.add(dr);
			return dr;
		}

		@GetMapping("/dr/count")
		public String drcount() {
			return String.valueOf(results.size());
		}

		@GetMapping("/dr/event")
		public String drevent(String msg) {
			for(DeferredResult<String> dr : results) {
				dr.setResult("Hello " + msg);
				results.remove(dr);
			}

			return "OK";
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDemoApplication.class, args);
	}
}

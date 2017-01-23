package toby.reactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.concurrent.TimeUnit.SECONDS;

@SpringBootApplication
@Slf4j
@EnableAsync
public class SpringBootDemoApplication {
	@RestController
	public static class MyController {
		@GetMapping("/async")
		public String async() throws InterruptedException {
			SECONDS.sleep(2);
			return "Hello from Async";
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDemoApplication.class, args);
	}
}

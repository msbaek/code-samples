package toby.reactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@SpringBootApplication
@Slf4j
@EnableAsync
public class SpringBootDemoApplication {
	@RestController
	public static class MyController {
		@RequestMapping("/emitter")
		public ResponseBodyEmitter emitter() {
			ResponseBodyEmitter emitter = new ResponseBodyEmitter();

			Executors.newSingleThreadExecutor().submit(() -> {
				try {
					for (int i = 1; i <= 50; i++) {
						emitter.send("<p>Stream " + i + "</p>");
						MILLISECONDS.sleep(100);
					}
					emitter.complete();
				} catch (Exception e) {
				}
			});

			return emitter;
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDemoApplication.class, args);
	}
}

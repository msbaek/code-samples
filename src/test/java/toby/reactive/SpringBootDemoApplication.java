package toby.reactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static java.util.concurrent.TimeUnit.SECONDS;

@SpringBootApplication
@Slf4j
public class SpringBootDemoApplication {
	@Component
	public static class MyService {
		public String hello() throws InterruptedException {
			log.info("hello");
			SECONDS.sleep(1);
			return "Hello";
		}
	}

	public static void main(String[] args) {
		try (ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringBootDemoApplication.class, args)) {
		}
	}

	@Autowired MyService myService;

	// executed when spring boot start
	@Bean ApplicationRunner run() {
		return args -> {
			log.info("run()");
			String result = myService.hello();
			log.info("exit with result={}", result);
		};
	}
}

package toby.reactive;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootDemoApplication {
	public static void main(String[] args) {
		try (ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringBootDemoApplication.class, args)) {
		}
	}

	// executed when spring boot start
	@Bean ApplicationRunner run() {
		return args -> {
		};
	}
}

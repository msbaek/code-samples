package toby.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.concurrent.TimeUnit.SECONDS;

@SpringBootApplication
public class RemoteService {
	@RestController
	public static class MyController {
		@RequestMapping("/service")
		public String rest(String req) throws InterruptedException {
			SECONDS.sleep(2);
			throw new RuntimeException("error in rest");
//			return req + "/service1";
		}

		@RequestMapping("/service2")
		public String rest2(String req) throws InterruptedException {
			SECONDS.sleep(2);
			return req + "/service2";
		}
	}

	public static void main(String[] args) {
		System.setProperty("SERVER_PORT", "8081");
		System.setProperty("server.tomcat.max-threads", "1000");
		SpringApplication.run(RemoteService.class, args);
	}
}

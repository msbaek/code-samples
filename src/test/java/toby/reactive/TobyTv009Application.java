package toby.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;

/**
 * 토비의 봄 TV 9회 - 스프링 리액티브 웹 개발 5부. 비동기 RestTemplate과 비동기 MVC의 결합
 */
@SpringBootApplication
public class TobyTv009Application {
	@RestController
	public static class MyController {
		AsyncRestTemplate rt = new AsyncRestTemplate();

		@RequestMapping("/rest")
		public ListenableFuture<ResponseEntity<String>> rest(int idx) {
			return rt.getForEntity("http://localhost:8081/service?req={req}", String.class, "hello" + idx);
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(TobyTv009Application.class, args);
	}
}

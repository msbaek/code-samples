package toby.reactive;

import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * 토비의 봄 TV 9회 - 스프링 리액티브 웹 개발 5부. 비동기 RestTemplate과 비동기 MVC의 결합
 */
@SpringBootApplication
public class TobyTv009Application {
	@RestController
	public static class MyController {
		AsyncRestTemplate rt = new AsyncRestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1)));

		@RequestMapping("/rest")
		public DeferredResult<String> rest(int idx) {
			DeferredResult<String> dr = new DeferredResult<>();
			ListenableFuture<ResponseEntity<String>> f1 = rt.getForEntity(
					"http://localhost:8081/service?req={req}", String.class, "hello" + idx);
			f1.addCallback(success -> {
				ListenableFuture<ResponseEntity<String>> f2 = rt.getForEntity(
								"http://localhost:8081/service2?req={req}", String.class, success.getBody());
				f2.addCallback(
						s -> {
							dr.setResult(s.getBody());
						},
						e -> {
							dr.setErrorResult(e.getMessage());
						}
				);
			},
			error -> {
				dr.setErrorResult(error.getMessage());
			});
			return dr;
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(TobyTv009Application.class, args);
	}
}

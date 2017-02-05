package toby.reactive;

import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 토비의 봄 TV 9회 - 스프링 리액티브 웹 개발 5부. 비동기 RestTemplate과 비동기 MVC의 결합
 */
@SpringBootApplication
public class TobyTv009Application {
	@RestController
	public static class MyController {
		AsyncRestTemplate rt = new AsyncRestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1)));

		@Autowired MyService myService;

		@RequestMapping("/rest")
		public DeferredResult<String> rest(int idx) {
			DeferredResult<String> dr = new DeferredResult<>();
			Completion
				.from(rt.getForEntity("http://localhost:8081/service?req={req}", String.class, "hello" + idx))
				.andApply(s -> rt.getForEntity("http://localhost:8081/service2?req={req}", String.class, s.getBody()))
				.andAccept(s -> dr.setResult(s.getBody()))
				;

//			ListenableFuture<ResponseEntity<String>> f1 = rt.getForEntity(
//					"http://localhost:8081/service?req={req}", String.class, "hello" + idx);
//			f1.addCallback(success -> {
//				ListenableFuture<ResponseEntity<String>> f2 = rt.getForEntity(
//								"http://localhost:8081/service2?req={req}", String.class, success.getBody());
//				f2.addCallback(
//						s -> {
//							ListenableFuture<String> f3 = myService.work(s.getBody());
//							f3.addCallback(
//									s1 -> {
//										dr.setResult(s1);
//									},
//									e -> {
//										dr.setErrorResult(e.getMessage());
//									}
//							);
//						},
//						e -> {
//							dr.setErrorResult(e.getMessage());
//						}
//				);
//			},
//			error -> {
//				dr.setErrorResult(error.getMessage());
//			});
			return dr;
		}
	}

	public static class Completion {
		Completion next;
		private Consumer<ResponseEntity<String>> con;
		private Function<ResponseEntity<String>, ListenableFuture<ResponseEntity<String>>> fn;

		public Completion() {
		}

		public static Completion from(ListenableFuture<ResponseEntity<String>> lf) {
			Completion c = new Completion();
			lf.addCallback(s -> c.complete(s), e -> c.error(e));
			return c;
		}

		public void andAccept(Consumer<ResponseEntity<String>> con) {
			Completion c = new AcceptCompletion(con);
			this.next = c;

		}

		public Completion andApply(Function<ResponseEntity<String>, ListenableFuture<ResponseEntity<String>>> fn) {
			Completion c = new ApplyCompletion(fn);
			this.next = c;
			return c;

		}

		protected void complete(ResponseEntity<String> s) {
			if(next != null)
				next.run(s);
		}

		protected void run(ResponseEntity<String> value) {
		}

		protected void error(Throwable e) {
		}
	}

	public static class ApplyCompletion extends Completion {
		private Function<ResponseEntity<String>, ListenableFuture<ResponseEntity<String>>> fn;

		public ApplyCompletion(Function<ResponseEntity<String>, ListenableFuture<ResponseEntity<String>>> fn) {
			this.fn = fn;
		}

		@Override
		protected void run(ResponseEntity<String> value) {
			ListenableFuture<ResponseEntity<String>> lf = fn.apply(value);
			lf.addCallback(s -> complete(s), e -> error(e));
		}
	}

	public static class AcceptCompletion extends Completion {
		private Consumer<ResponseEntity<String>> con;

		public AcceptCompletion(Consumer<ResponseEntity<String>> con) {
			this.con = con;
		}

		@Override
		protected void run(ResponseEntity<String> value) {
			con.accept(value);
		}
	}

	@Service
	public static class MyService {
		public ListenableFuture<String> work(String req) {
			return new AsyncResult<>(req + "/asyncwork");
		}
	}

	@Bean
	public ThreadPoolTaskExecutor myThreadPool() {
		ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
		te.setCorePoolSize(1);
		te.setMaxPoolSize(10);
		te.initialize();
		return te;
	}

	public static void main(String[] args) {
		SpringApplication.run(TobyTv009Application.class, args);
	}
}

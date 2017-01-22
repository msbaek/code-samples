package toby.reactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.SECONDS;

@SpringBootApplication
@Slf4j
@EnableAsync
public class SpringBootDemoApplication {
	@Component
	public static class MyService {
//		@Async
		@Async(value = "taskExecutor")
		public ListenableFuture<String> hello() throws InterruptedException {
			log.info("hello");
			SECONDS.sleep(2);
			return new AsyncResult<>("Hello");
		}
	}

	@Bean ThreadPoolTaskExecutor taskExecutor() {
		// default로 제공되는 SimpleAsyncTaskExecutor는 요청이 올 때마다 thread를 생성함
		// cpu, memory 측면에서 비효율적
		ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
		// core 만큼 초기 에 만들고 queue size만큼 큐잉하다가 maxsize만큼 생성
		te.setCorePoolSize(10);
		te.setMaxPoolSize(100);
		te.setQueueCapacity(200);
		te.setThreadNamePrefix("mythread");
		te.initialize();
		return te;
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
			ListenableFuture<String> f = myService.hello();
			f.addCallback(
				s -> log.info("success with result={}", s),
				e -> log.error("error={}", e.getMessage())
			);
			log.info("exit before callback");
		};
	}
}

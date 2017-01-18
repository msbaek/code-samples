package toby.reactive;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.time.Duration.ofMillis;
import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
public class ThirdTest {
	@Test
	public void scheduler_example() throws InterruptedException {
		Publisher<Integer> pub = sub -> {
			sub.onSubscribe(new Subscription() {
				@Override
				public void request(long n) {
					log.debug("request: {}", n);
					sub.onNext(1);
					sub.onNext(2);
					sub.onNext(3);
					sub.onNext(4);
					sub.onNext(5);
					sub.onComplete();
				}

				@Override
				public void cancel() {
					log.debug("cancel");
				}
			});
		};
		// end of pub

		// slow publisher, fast subscriber
		// 느린 publisher를 별도의 쓰레드에서 동작시킴
		// publisher.subscribe()
		// publisher가 느려서 별도의 쓰레드에서 호출
		// subscribe를 호출해서 이름이 subscribeOn
		Publisher subcribeOn_publisher = sub -> {
			ExecutorService executorService = Executors.newSingleThreadExecutor(new CustomizableThreadFactory() {
				@Override
				public String getThreadNamePrefix() {
					return "subscribeOn-";
				}
			});
			// // 하나 이상의 쓰레드가 요청되면 queue 넣고 대기 시킴
			executorService.execute(() -> pub.subscribe(sub));
		};

		// fast publisher, slow subscriber
		// 느린 subscriber(consumer)를 별도의 쓰레드에서 동작시킴
		// subscriber.onNext ...
		// subscriber가 느려서 별도의 쓰레드에서 호출
		// 실제 데이터가 publish되므로 이름이 publishOn
		Publisher<Integer> publishOn_publisher = sub -> {
			subcribeOn_publisher.subscribe(new Subscriber<Integer>() {
				ExecutorService executorService = Executors.newSingleThreadExecutor(new CustomizableThreadFactory() {
					@Override
					public String getThreadNamePrefix() {
						return "publishOn-";
					}
				});

				@Override
				public void onSubscribe(Subscription s) {
					sub.onSubscribe(s);
				}

				@Override
				public void onNext(Integer integer) {
					executorService.execute(() -> sub.onNext(integer));
				}

				@Override
				public void onError(Throwable t) {
					executorService.execute(() -> sub.onError(t));
				}

				@Override
				public void onComplete() {
					executorService.execute(() -> sub.onComplete());
				}
			});
		};

		// start of sub
		publishOn_publisher.subscribe(new Subscriber<Integer>() {
			@Override
			public void onSubscribe(Subscription s) {
				log.debug("onSubscribe");
				s.request(Long.MAX_VALUE);
			}

			@Override
			public void onNext(Integer integer) {
				log.debug("onNext: {}", integer);
			}

			@Override
			public void onError(Throwable t) {
				log.debug("onError: {}", t);
			}

			@Override
			public void onComplete() {
				log.debug("onComplete");
			}
		});

		log.debug("exit");

		SECONDS.sleep(1);
	}

	@Test
	public void flux_scheduler() throws InterruptedException {
		Flux.range(1, 10)
			.publishOn(Schedulers.newSingle("pub-"))
			.log()
			.subscribeOn(Schedulers.newSingle("sub-"))
			.subscribe(System.out::println);

		SECONDS.sleep(1);

		System.out.println("Exit");
	}

	@Test
	public void flux_interval() throws InterruptedException {
		Flux.interval(ofMillis(500))
			.subscribe(s -> log.debug("onNext: {}", s));

		log.debug("exit");

		SECONDS.sleep(3);

		// user thread: 1개라도 남아 있으면 jvm이 종료 안됨
		// daemon thread: 남아 있더라도 jvm 종료됨
	}
}

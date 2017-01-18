package toby.reactive;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Slf4j
public class ThirdTest {
	@Test
	public void scheduler_example() {
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

		pub.subscribe(new Subscriber<Integer>() {
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
	}
}

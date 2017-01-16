package toby.reactive;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class SecondTest {
	@Test
	public void pub_sub_test() {
		Publisher<Integer> pub = iterPub(Stream.iterate(1, s -> s + 1).limit(10).collect(Collectors.toList()));
		Publisher<Integer> mapPub = mapPub(pub, (Function<Integer, Integer>) s -> s + 10);
		Subscriber<Integer> sub = logSub();
		mapPub.subscribe(sub);
	}

	private Subscriber<Integer> logSub() {
		return new Subscriber<Integer>() {
			@Override
			public void onSubscribe(Subscription s) {
				s.request(Long.MAX_VALUE);
			}

			@Override
			public void onNext(Integer i) {
				log.debug("onNext: {}", i);
			}

			@Override
			public void onError(Throwable t) {
				log.debug("onError: {}", t);
			}

			@Override
			public void onComplete() {
				log.debug("onComplete");
			}
		};
	}

	private Publisher<Integer> iterPub(final List<Integer> iter) {
		return new Publisher<Integer>() {
			@Override
			public void subscribe(Subscriber<? super Integer> sub) {
				sub.onSubscribe(new Subscription() {
					@Override
					public void request(long n) {
						try {
							iter.forEach(s -> sub.onNext(s));
							sub.onComplete();
						} catch (Exception e) {
							sub.onError(e);
						}
					}

					@Override
					public void cancel() {
					}
				});
			}
		};
	}
}

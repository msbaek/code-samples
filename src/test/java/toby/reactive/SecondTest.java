package toby.reactive;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class SecondTest {
	@Test
	public void pub_sub_test() {
		Publisher<Integer> pub = iterPub(Stream.iterate(1, s -> s + 1).limit(10).collect(Collectors.toList()));
//		Publisher<Integer> mapPub = mapPub(pub, s -> s + 10);
//		Publisher<Integer> map2Pub = mapPub(mapPub, s -> -s);
//		Publisher<Integer> sumPub = sumPub(pub);
		Publisher<Integer> reducePub = reducePub(pub, 0, (BiFunction<Integer, Integer, Integer>)(a, b) -> a + b);
		Subscriber<Integer> sub = logSub();
		reducePub.subscribe(sub);
	}

	private Publisher<Integer> reducePub(Publisher<Integer> pub, int init, BiFunction<Integer, Integer, Integer> biFunction) {
		return new Publisher<Integer>() {
			@Override
			public void subscribe(Subscriber<? super Integer> sub) {
				pub.subscribe(new DelegateSub(sub) {
					public int result = init;

					@Override
					public void onNext(Integer i) {
						result = biFunction.apply(result, i);
					}

					@Override
					public void onComplete() {
						sub.onNext(result);
						sub.onComplete();
					}
				});
			}
		};
	}

	private Publisher<Integer> sumPub(Publisher<Integer> pub) {
		return new Publisher<Integer>() {
			@Override
			public void subscribe(Subscriber<? super Integer> sub) {
				pub.subscribe(new DelegateSub(sub) {
					public int sum = 0;

					@Override
					public void onNext(Integer i) {
						sum += i;
					}

					@Override
					public void onComplete() {
						sub.onNext(sum);
						sub.onComplete();
					}
				});
			}
		};
	}

	private Publisher<Integer> mapPub(Publisher<Integer> pub, Function<Integer, Integer> f) {
		return new Publisher<Integer>() {
			@Override
			public void subscribe(Subscriber<? super Integer> sub) {
				pub.subscribe(new DelegateSub(sub) {
					@Override
					public void onNext(Integer i) {
						sub.onNext(f.apply(i));
					}
				});
			}
		};
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

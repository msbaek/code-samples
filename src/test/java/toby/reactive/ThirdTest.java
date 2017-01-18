package toby.reactive;

import org.junit.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class ThirdTest {
	@Test
	public void scheduler_example() {
		Publisher<Integer> pub = new Publisher<Integer>() {
			@Override
			public void subscribe(Subscriber<? super Integer> sub) {
				sub.onSubscribe(new Subscription() {
					@Override
					public void request(long n) {
						sub.onNext(1);
						sub.onNext(2);
						sub.onNext(3);
						sub.onNext(4);
						sub.onNext(5);
						sub.onComplete();
					}

					@Override
					public void cancel() {
					}
				});
			}
		};
	}
}

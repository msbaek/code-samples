package rxjava.howdyrxjava;

import org.junit.Test;
import rx.Observable;
import rx.Subscriber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// https://medium.com/fuzz/howdy-rxjava-8f40fef88181#.kvx3vruqq
public class HowdyRxJavaTest {

	private final List<String> friends = Arrays.asList(
			"Brian",
			"Nate",
			"Neal",
			"Raju",
			"Sara",
			"Scott"
	);

	@Test
	public void imperative_vs_declarative() {
		List<Integer> lists = Arrays.asList(1, 2, 3, 4, 5);
		List<Integer> results = new ArrayList<>();
		for (Integer n : lists) {
			if (n % 2 == 0)
				results.add(n);
		}

		results = lists.stream().filter(s -> s % 2 == 0).collect(Collectors.toList());
	}

	@Test
	public void creating_observable() {
		// https://github.com/ReactiveX/RxJava/wiki/Creating-Observables
		List<Integer> lists = Arrays.asList(1, 2, 3, 4, 5);
		Observable<Integer> integerObservable = Observable.from(lists);

		Subscriber<Integer> integerSubscriber = new Subscriber<Integer>() {
			@Override
			public void onNext(Integer data) {
				System.out.printf("onNext: %d\n", data.intValue());
			}

			@Override
			public void onCompleted() {
				System.out.println("onComplete()");
			}

			@Override
			public void onError(Throwable e) {
				e.printStackTrace();
			}
		};

//		integerObservable.subscribe(integerSubscriber);
		integerObservable
				.filter(integer -> integer % 2 == 0)
				.map(integer -> integer * 10)
				.subscribe(integerSubscriber);
		// https://github.com/ReactiveX/RxJava/wiki/Alphabetical-List-of-Observable-Operators
	}
}

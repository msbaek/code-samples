package reactive_programming_with_rxjava;

import org.junit.Test;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

import java.time.LocalTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;
import static reactive_programming_with_rxjava.Ch02Test.Sound.DAH;
import static reactive_programming_with_rxjava.Ch02Test.Sound.DI;
import static rx.Observable.*;

public class Ch02Test {
	@Test
	public void using_range() {
		log("before");
		Observable //
				.range(5, 3) //
				.subscribe(i -> {
					log(i);
				});
		log("after");

	}

	private void log(Object message) {
		System.out.println(//
				Thread.currentThread().getName() + ": " + message);
	}

	@Test
	public void observable_create() throws InterruptedException {
		Observable<Integer> ints = Observable //
				// .create(new Observable.OnSubscribe<Integer>() { //
				// .create((Observable.OnSubscribe<Integer>) subscriber -> { //
				.create(subscriber -> { //
					Runnable r = () -> {
						log("create");
						while (!subscriber.isUnsubscribed()) {
							log("isUnsubscribed: " + subscriber.isUnsubscribed());
							subscriber.onNext(5);
							subscriber.onNext(6);
							subscriber.onNext(7);
							try {
								SECONDS.sleep(2);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						log("completed");
					};
					Thread thread = new Thread(r);
					thread.start();
					subscriber.add(Subscriptions.create(() -> log("isUnsubscribed: " + subscriber.isUnsubscribed())));
				});

		log("starting");
		Subscription s1 = ints.subscribe(i -> log("element: " + i));
		SECONDS.sleep(1);
		s1.unsubscribe();
		Subscription s2 = ints.subscribe(i -> log("element: " + i));
		SECONDS.sleep(1);
		s2.unsubscribe();
		log("exit");
		SECONDS.sleep(1);
	}

	class Rating {
	}

	Observable<Long> upload(UUID id) {
		return empty();
	}

	Observable<Rating> rate(UUID id) {
		return empty();
	}

	void store(UUID id) {
//		upload(id).subscribe(
//				bytes -> {
//				},
//				e -> log(e),
//				() -> rate(id)
//		);
//		upload(id)
//				.flatMap(
//						bytes -> empty(),
//						e -> Observable.error(e),
//						() -> rate(id)
//				);
	}

	Observable<Sound> toMorseCode(char ch) {
		switch (ch) {
			case 'a':
				return just(DI, DAH);
			case 'b':
				return just(DAH, DI, DI, DI);
			case 'c':
				return just(DAH, DI, DAH, DI); //...
			case 'p':
				return just(DI, DAH, DAH, DI);
			case 'r':
				return just(DI, DAH, DI);
			case 's':
				return just(DI, DI, DI);
			case 't':
				return just(DAH);
			default:
				return empty();
		}
	}

	enum Sound {DI, DAH}

	@Test
	public void just_with_flatMap() {
		just('S', 'p', 'a', 'r', 't', 'a')
				.map(Character::toLowerCase) // Observable<Character>
				.flatMap(this::toMorseCode); // Observable<Sound>
	}

	@Test
	public void delay_test() throws InterruptedException {
		Observable
				.just("Lorem", "ipsum", "dolor", "sit", "amet",
						"consectetur", "adipiscing", "elit")
				.flatMap(word ->
						timer(word.length(), SECONDS)
								.map(x -> word))
				.subscribe(s -> System.out.printf("%s\t%s\n", LocalTime.now(), s)
				);
		TimeUnit.SECONDS.sleep(15);
	}
}

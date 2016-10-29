package reactive_programming_with_rxjava;

import org.junit.Test;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static rx.Observable.just;

public class Ch06Test {
	@Test
	public void delayed_names() throws InterruptedException {
		Observable<String> names = //
		just("Mary", "Patricia", "Linda", "Barbara", //
				"Elizabeth", "Jennifer", "Maria", "Susan", //
				"Margaret", "Dorothy");

		Observable<Long> absoluteDelayMillis = //
		just(0.1, 0.6, 0.9, 1.1, 3.3, //
				3.4, 3.5, 3.6, 4.4, 4.8) //
				.map(d -> (long) (d * 1_000));

		Observable<String> delayedNames = names //
				.zipWith(absoluteDelayMillis, //
						(n, d) -> //
						just(n) //
						.delay(d, MILLISECONDS)) //
				.flatMap(o -> o);

		delayedNames //
				// .sample(1, SECONDS) //
				.throttleFirst(1, SECONDS) //
				.subscribe(System.out::println);

		SECONDS.sleep(10);
	}

	/**
	 * allows configuring how many oldest values from internal buffer to drop when buffer() pushes the list downstream.
	 */
	@Test
	public void buffering_events_to_list() {
		Observable //
				.range(1, 7) //
				.buffer(3) //
				.subscribe(integerList -> {
					System.out.println(integerList);
				});

		Observable //
				.range(1, 7) //
				.buffer(3, 1) //
				.subscribe(System.out::println);

		Observable<List<Integer>> odd = Observable //
				.range(1, 7) //
				.buffer(1, 2);
		odd.subscribe(System.out::println);
	}

	/**
	 * While throttleFirst() and throttleLast() were taking first and last events within a given period of time accordingly, one of the over‐ loaded
	 * versions of buffer batches all events in each time period.
	 */
	@Test
	public void buffering_by_time_periods() throws InterruptedException {
		Observable<String> names = just(//
				"Mary", "Patricia", "Linda", "Barbara", "Elizabeth",//
				"Jennifer", "Maria", "Susan", "Margaret", "Dorothy");
		Observable<Long> absoluteDelays = just(//
				0.1, 0.6, 0.9, 1.1, 3.3,//
				3.4, 3.5, 3.6, 4.4, 4.8//
		).map(d -> (long) (d * 1_000));

		Observable<String> delayedNames = Observable //
				.zip(names, absoluteDelays, (n, d) -> just(n).delay(d, MILLISECONDS)) //
				.flatMap(o -> o);

		delayedNames //
				.buffer(1, SECONDS) //
				.subscribe(System.out::println);

		SECONDS.sleep(5);

		/**
		 * One of the use cases for buffer() is counting the number of events per each time period, for example number of key events per second:
		 */
		Observable<KeyEvent> keyEvents = null;

		Observable<Integer> eventPerSecond = keyEvents //
				.buffer(1, SECONDS) //
				.map(List::size);
	}

	@Test
	public void only_look_at_certain_samples() {
		/**
		 * . during business hours (9:00 - 17:00) we take 100-millisecond long snapshots every second (processing approximately 10% of data)
		 * 
		 * . outside business hours we only look at 200-millisecond long snapshots taken every five seconds (4%)
		 */
		Observable<Duration> insideBusinessHours = Observable //
				.interval(1, SECONDS) // generate timer ticks every second
				.filter(x -> isBusinessHour()) // exclude these that are not within business hours
				.map(x -> Duration.ofMillis(100)); // interval() returns growing natural Long numbers? We do not need them, for convenience we replace
													// them with fixed duration of 100 milliseconds.
		// we get a steady clock ticking every second between 9:00 and 17:00

		Observable<Duration> outsideBusinessHours = Observable //
				.interval(5, SECONDS) //
				.filter(x -> !isBusinessHour()) //
				.map(x -> Duration.ofMillis(200));

		Observable<Duration> openings = Observable.merge(insideBusinessHours, outsideBusinessHours);
		openings.subscribe(duration -> {
			System.out.println(duration);
		});
	}

	private static final LocalTime BUSINESS_START = LocalTime.of(9, 0);
	private static final LocalTime BUSINESS_END = LocalTime.of(17, 0);

	private boolean isBusinessHour() {
		ZoneId zone = ZoneId.of("Europe/Warsaw");
		ZonedDateTime zdt = ZonedDateTime.now(zone);
		LocalTime localTime = zdt.toLocalTime();
		return !localTime.isBefore(BUSINESS_START) && !localTime.isAfter(BUSINESS_END);
	}

	/**
	 * When working with buffer() we build List instances over and over.
	 * 
	 * Why do we build these intermediate Lists rather then somehow consume events on the fly? This is where window() operator becomes useful.
	 * 
	 * You should prefer window() over buffer() if possible as the latter is less predictable in terms of memory usage.
	 */
	@Test
	public void moving_window() {
		Observable<KeyEvent> keyEvents = null;

		Observable<Integer> eventPerSecond = keyEvents //
				.window(1, SECONDS) //
				.flatMap(eventsInSecond -> eventsInSecond.count());
	}

	@Test
	public void skipping_stale_events_with_debounce() {
		// sample() picks one fairly arbitrary event once in a while.

		Observable<BigDecimal> prices = null; // tradingPlatform.pricesOf("NFLX");
		Observable<BigDecimal> debounced = prices.debounce(100, MILLISECONDS);
		/**
		 * debounce() ) (alias: ) throttleWithTimeout()) discards all events that are shortly followed by another event. In other words if given event
		 * is not followed by another event within a time window, that event is emitted.
		 */

		prices //
		.debounce(x -> {
			boolean goodPrice = x.compareTo(BigDecimal.valueOf(150)) > 0;
			return Observable//
					.empty()// return a unique Observable, which is empty.
					.delay(goodPrice ? 10 : 100, MILLISECONDS);
		});
	}

	// ------------------------------------------------------------------------------------------
	Observable<BigDecimal> pricesOf(String ticker) {
		return Observable //
				.interval(50, MILLISECONDS) //
				.flatMap(this::randomDelay) //
				.map(this::randomStockPrice) //
				.map(BigDecimal::valueOf);
	}

	Observable<Long> randomDelay(long x) {
		return Observable //
				.just(x) //
				.delay((long) (Math.random() * 100), MILLISECONDS);
	}

	double randomStockPrice(long x) {
		return 100 + Math.random() * 10 + //
				(Math.sin(x / 100.0)) * 60.0;
	}

	// ------------------------------------------------------------------------------------------

	@Test
	public void backpressure_in_rx_java() {
		// Observable //
		// .range(1, 1_000_000_000) // range() operator is not asynchronous by default
		myRange(1, 1_000_000_000) //
				.map(Dish::new) //
				.observeOn(Schedulers.io()) // ???
				.subscribe(dish -> {
					System.out.println("Wasing: " + dish);
					sleepMillis(50);
				}, Throwable::printStackTrace //
				);
		sleepMillis(10_000);
	}

	Observable<Integer> myRange(int from, int count) {
		return Observable.create(subscriber -> {
			int i = from;
			while (i < from + count) {
				if (!subscriber.isUnsubscribed())
					subscriber.onNext(i++);
				else
					return;
			}
			subscriber.onCompleted();
		});
	}

	private void sleepMillis(int millis) {
		try {
			MILLISECONDS.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	class Dish {
		private final byte[] oneKb = new byte[1_024];
		private final int id;

		Dish(int id) {
			this.id = id;
			System.out.println("Created: " + id);
		}

		public String toString() {
			return String.valueOf(id);
		}
	}

	@Test
	public void timing_out_when_events_do_not_occur() throws InterruptedException {
		confirmation() //
				.timeout(210, MILLISECONDS) //
				.forEach( //
						System.out::println, th -> {
							if ((th instanceof TimeoutException)) {
								System.out.println("Too long");
							} else {
								th.printStackTrace();
							}
						});

		SECONDS.sleep(3);
	}

	class Confirmation {
	}

	Observable<Confirmation> confirmation() {
		Observable<Confirmation> delayBeforeCompletion = //
		Observable //
				.<Confirmation> empty() //
				.delay(200, MILLISECONDS);

		return Observable //
				.just(new Confirmation()) //
				.delay(100, MILLISECONDS) //
				.concatWith(delayBeforeCompletion);
	}

	@Test
	public void schedulers_in_unit_testing() throws InterruptedException {
		TestScheduler scheduler = Schedulers.test();
		Observable<String> fast = Observable //
				.interval(10, MILLISECONDS, scheduler) //
				.map(x -> "F" + x) //
				.take(3);
		Observable<String> slow = Observable //
				.interval(50, MILLISECONDS, scheduler) //
				.map(x -> "S" + x);
		Observable<String> stream = Observable.concat(fast, slow);
		stream.subscribe(System.out::println);
		System.out.println("Subscribed");

		SECONDS.sleep(1); // no-effects
		System.out.println("After one second");
		scheduler.advanceTimeBy(25, MILLISECONDS); // 25 ms

		SECONDS.sleep(1); // no-effects
		System.out.println("After one more second");
		scheduler.advanceTimeBy(75, MILLISECONDS); // 100 ms

		SECONDS.sleep(1); // no-effects
		System.out.println("...and one more");
		scheduler.advanceTimeTo(200, MILLISECONDS); // 200 ms
	}

	@Test
	public void shouldApplyConcatMapInOrder() throws Exception {
		List<String> list = Observable //
				.range(1, 3) //
				.concatMap(x -> Observable.just(x, -x)) //
				.map(Object::toString) //
				.toList() //
				.toBlocking() //
				.single();

		assertThat(list).containsExactly("1", "-1", "2", "-2", "3", "-3");
	}

	@Test
	public void monitoring() {
		Observable<Instant> timestamps = Observable
				.fromCallable(() -> dbQuery())
				.doOnSubscribe(() -> info("subscribe()"))
				;

		timestamps
				.zipWith(timestamps.skip(1), Duration::between)
				.map(Object::toString)
				.subscribe(this::info);
	}

	private void info(String msg) {
		System.out.println(msg);
	}

	private Instant dbQuery() {
		return null;
	}
}

package reactive_programming_with_rxjava;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.sun.tools.javac.util.Pair;
import fpij.Person;
import org.junit.Test;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static rx.Observable.defer;
import static rx.Observable.from;

public class Ch04Test {

	private long startTimeMillis;
	private String calledPages = "";

	@Test
	public void blocking_observable() {
		// List<Person> people = listPeople();
		// Observable<Person> peopleStream = Observable.from(people);
		// Observable<List<Person>> peopleList = peopleStream.toList();
		// BlockingObservable<List<Person>> peopleBlocking = peopleList.toBlocking();
		// List<Person> result = peopleBlocking.single();

		startTimeMillis = System.currentTimeMillis();
		Observable<Person> observable = listPeople();//
		assertThat(getEllapsedTimeMillis(), is(greaterThan(1000L)));

		startTimeMillis = System.currentTimeMillis();
		observable //
				.toList() //
				.toBlocking() //
				.single();
		assertThat(getEllapsedTimeMillis(), is(lessThan(1000L)));
	}

	@Test
	public void deferred_observable() {
		startTimeMillis = System.currentTimeMillis();
		Observable<Person> observable = deferredListPeople();
		assertThat(getEllapsedTimeMillis(), is(lessThan(1000L)));

		startTimeMillis = System.currentTimeMillis();
		observable //
				.toList() //
				.toBlocking() //
				.single();
		assertThat(getEllapsedTimeMillis(), is(greaterThan(1000L)));
	}

	private long getEllapsedTimeMillis() {
		return System.currentTimeMillis() - startTimeMillis;
	}

	private Observable<Person> deferredListPeople() {
		return defer(this::listPeople);
	}

	private Observable<Person> listPeople() {
		List<Person> result = new ArrayList<>();
		IntStream.range(0, 10).forEach(i -> result.add(new Person("name" + i, 20 + i)));
		sleep(1000);
		return from(result);
	}

	private void sleep(long millis) {
		try {
			TimeUnit.MILLISECONDS.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void allPages_query() {
		allPeople(1) //
				.elementAt(9) //
				.subscribe(person -> System.out.printf("person=%s\n", person));
		assertThat(calledPages, is("1"));
		calledPages = "";

		allPeople(1) //
				.elementAt(11) //
				.subscribe(person -> System.out.printf("person=%s\n", person));
		assertThat(calledPages, is("12"));
	}

	private Observable<Person> allPeople(int initialPage) {
		return defer(() -> from(listPeople(initialPage)))//
				.concatWith( //
						defer(() -> allPeople(initialPage + 1)));
	}

	private List<Person> listPeople(int page) {
		calledPages += page;
		System.out.printf("listPeople called for page=%d\n", page);
		List<Person> result = new ArrayList<>();
		IntStream.range((page - 1) * 10, page * 10) //
				.forEach(i -> result.add(new Person("name" + i, 20 + i)));
		return result;
	}

	@Test
	public void lazy_paging_with_more_simple_solution() {
		Observable<List<Person>> allPages = Observable //
				.range(1, Integer.MAX_VALUE) //
				.map(this::listPeople) //
				.takeWhile(list -> !list.isEmpty());

		calledPages = "";
		allPages.elementAt(0).single() //
				.subscribe(person -> System.out.printf("person=%s\n", person));
		assertThat(calledPages, is("1"));

		calledPages = "";
		allPages.concatMap(Observable::from) // In order to flatten allPages to Observa ble<Person> we can use concatMap()
				.elementAt(11) //
				.subscribe(person -> System.out.printf("person=%s\n", person));
		assertThat(calledPages, is("12"));
	}

	ExecutorService poolA = Executors.newFixedThreadPool(10, threadFactory("Sched-A-%d"));
	Scheduler schedulerA = Schedulers.from(poolA);

	private ThreadFactory threadFactory(String pattern) {
		return new ThreadFactoryBuilder().setNameFormat(pattern).build();
	}

	@Test
	public void doOnNext() throws InterruptedException {
		log("Starting");
		final Observable<String> obs = simple();
		log("Created");
		obs //
		.doOnNext(this::log)//
				.map(x -> x + '1')//
				.doOnNext(this::log)//
				.map(x -> x + '2')//
				.subscribeOn(schedulerA)//
				.doOnNext(this::log)//
				.subscribe(//
						x -> log("Got " + x),//
						Throwable::printStackTrace,//
						() -> log("Completed")//
				);
		log("Exiting");

		SECONDS.sleep(1);
	}

	Observable<String> simple() {
		return Observable.create(subscriber -> {
			log("Subscribed");
			subscriber.onNext("A");
			subscriber.onNext("B");
			subscriber.onCompleted();
		});
	}

	private void log(Object message) {
		System.out.printf("%d\t|\t%s\t|\t%s\n", System.currentTimeMillis(), Thread.currentThread().getName(), message);
	}

	@Test
	public void group_by() throws InterruptedException {
		Observable //
				.just("bread", "butter", "egg", "milk", "tomato",//
						"cheese", "tomato", "egg", "egg") //
				.groupBy(prod -> prod)//
				.flatMap(grouped -> grouped//
						.count()//
						.map(quantity -> {
							String productName = grouped.getKey();
							return Pair.of(productName, quantity);
						})) //
				.subscribe( //
						pair -> System.out.println(pair) //
				);
	}
}

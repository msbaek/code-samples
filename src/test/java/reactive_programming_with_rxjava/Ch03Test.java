package reactive_programming_with_rxjava;

import org.junit.Test;

import static java.util.concurrent.TimeUnit.SECONDS;
import static rx.Observable.just;

public class Ch03Test {
	@Test
	public void flatMap_dont_gurantee_order_of_events() throws InterruptedException {
		just(10l, 1l) //
				.flatMap(x -> //
						just(x).delay(x, SECONDS)) //
				.subscribe(System.out::println);
		SECONDS.sleep(11);
	}
}

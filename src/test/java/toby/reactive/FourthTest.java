package toby.reactive;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.SECONDS;

public class FourthTest {

	@Test
	public void future_example() throws InterruptedException {
		ExecutorService es = Executors.newCachedThreadPool();

		SECONDS.sleep(2);
		System.out.println("Hello");

		System.out.println("Exit");
	}
}

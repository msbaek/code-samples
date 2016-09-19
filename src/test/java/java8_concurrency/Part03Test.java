package java8_concurrency;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.LongBinaryOperator;

import static java.util.stream.IntStream.range;
import static java8_concurrency.ExecutorServiceUtil.stop;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

// http://winterbe.com/posts/2015/05/22/java8-concurrency-tutorial-atomic-concurrent-map-examples/
public class Part03Test {

	private AtomicInteger atomicInteger;
	private ExecutorService executorService;

	@Before
	public void setUp() throws Exception {
		atomicInteger = new AtomicInteger(0);
		executorService = Executors.newFixedThreadPool(2);
	}

	@Test
	public void atomic_integer() {
		range(0, 1_000) //
				.forEach(i -> executorService.submit(atomicInteger::incrementAndGet));
		stop(executorService);
		assertThat(atomicInteger.get(), is(1_000));
	}

	@Test
	public void update_and_get() {
		range(0, 1_000) //
				.forEach(i -> executorService.submit(() -> atomicInteger.updateAndGet(n -> n + 2)));
		stop(executorService);
		assertThat(atomicInteger.get(), is(2_000));
	}

	@Test
	public void accumulate_and_get() {
		range(0, 1_000).forEach(i -> {
			executorService.submit(() -> atomicInteger.accumulateAndGet(i, (n, m) -> n + m));
		});
		stop(executorService);
		assertThat(atomicInteger.get(), is(499_500));
	}

	@Test
	public void long_adder() {
		/**
		 * This class is usually preferable over atomic numbers when updates from multiple threads are more common than reads
		 */
		LongAdder adder = new LongAdder();
		range(0, 1_000).forEach(i -> executorService.submit(adder::increment));
		stop(executorService);
		assertThat(adder.sumThenReset(), is(1_000l));
	}

	@Test
	public void long_accumulator() throws InterruptedException {
		LongBinaryOperator op = (x, y) -> 2 * x + y;
		LongAccumulator accumulator = new LongAccumulator(op, 1l);
		range(0, 10).forEach(i -> executorService.submit(() -> accumulator.accumulate(i)));
		stop(executorService);
		assertThat(accumulator.getThenReset(), is(2539l));
	}

	@Test
	public void concurrent_map() {
		ConcurrentMap<String, String> map = new ConcurrentHashMap<>();
		map.put("foo", "bar");
		map.put("han", "solo");
		map.putIfAbsent("r2", "d2");
		map.putIfAbsent("r2", "d3");
		map.put("c3", "p0");
		map.forEach((key, value) -> System.out.printf("%s = %s\n", key, value));
		System.out.printf("%s\n", map.getOrDefault("r3", "not exists"));
		map.replaceAll((key, value) -> "r2".equals(key) ? "d3" : value);
		System.out.printf("%s\n", map.get("r2"));
		map.compute("foo", (key, value) -> value + value); // compute() let's us transform a single entry. variants : computeIfAbsent(),
															// computeIfPresent().
		System.out.printf("%s\n", map.get("foo"));
		map.merge("foo", "boo", (oldValue, newValue) -> newValue + " was " + oldValue);
		System.out.printf("%s\n", map.get("foo"));
		/**
		 * ConcurrentHashMap
		 */
		System.out.println(ForkJoinPool.getCommonPoolParallelism());
		/**
		 * parallelismThreshold.
		 * 
		 * This threshold indicates the minimum collection size when the operation should be executed in parallel.
		 * 
		 * E.g. if you pass a threshold of 500 and the actual size of the map is 499 the operation will be performed sequentially on a single thread.
		 */
		((ConcurrentHashMap) map).forEach(1, (key, value) -> System.out.printf("%s = %s in %s\n", key, value, Thread.currentThread().getName()));
		String result = ((ConcurrentHashMap<String, String>) map).search(1, (key, value) -> {
			System.out.println(Thread.currentThread().getName());
			return "foo".equals(key) ? value : null;
		});
		System.out.printf("Result: %s\n", result);

		result = ((ConcurrentHashMap<String, String>) map).searchValues(1, value -> {
			System.out.println(Thread.currentThread().getName());
			return value.length() > 3 ? value : null;
		});
		System.out.printf("Result: %s\n", result);

		result = ((ConcurrentHashMap<String, String>) map).reduce( //
				1, //
				(key, value) -> {
					System.out.printf("Transform: %s\n", Thread.currentThread().getName());
					return key + "=" + value;
				}, (s1, s2) -> {
					System.out.printf("Reduce: %s\n", Thread.currentThread().getName());
					return s1 + ", " + s2;
				});
		System.out.printf("Result: %s\n", result);
	}
}

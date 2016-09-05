package java8_concurrency;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

// http://winterbe.com/posts/2015/04/07/java8-concurrency-tutorial-thread-executor-examples/
public class Part01Test {
	@Test
	public void runnable() {
		// Runnable task = () -> {
		// String name = Thread.currentThread().getName();
		// System.out.printf("Hello Thread [%s]\n", name);
		// };
		//
		// task.run();
		//
		// Thread thread = new Thread(task);
		// thread.start();
		//
		// System.out.println("Done !!");

		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(() -> {
			String name = Thread.currentThread().getName();
			System.out.printf("Hello Thread [%s]\n", name);
		});

		System.out.println("attempt to shutdown executor");
		executor.shutdown();
		ExecutorServiceUtil.stop(executor);
		System.out.println("Done !!");
	}

	@Test
	public void thread_with_sleep() {
		Runnable task = () -> {
			String name = Thread.currentThread().getName();
			System.out.printf("Foo for Thread [%s]\n", name);
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.printf("Bar for Thread [%s]\n", name);
		};

		Thread thread = new Thread(task);
		thread.start();
	}

	@Test
	public void callable() {
		Callable<Integer> task = () -> {
			TimeUnit.SECONDS.sleep(1);
			return 123;
		};

		ExecutorService executor = Executors.newFixedThreadPool(1);
		Future<Integer> future = executor.submit(task);

		System.out.println("future done ? " + future.isDone());

		Integer result = null;
		try {
//			result = future.get(); // block foreve
			result = future.get(2, TimeUnit.SECONDS); // block with timeout. java.util.concurrent.TimeoutException
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

		System.out.println("future done ? " + future.isDone());
		System.out.println("result: " + result);
	}

	@Test
	public void invoke_all_stream_map() throws InterruptedException {
		ExecutorService executorService = Executors.newWorkStealingPool();
		List<Callable<String>> callables = Arrays.asList(
				() -> "task1",
				() -> "task2",
				() -> "task3"
		);
		executorService.invokeAll(callables).stream()
				.map(future -> {
					try {
						return future.get();
					} catch (Exception e) {
						throw new IllegalStateException(e);
					}
				})
				.forEach(System.out::println);
	}
}

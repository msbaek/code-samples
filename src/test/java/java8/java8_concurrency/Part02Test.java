package java8.java8_concurrency;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

// http://winterbe.com/posts/2015/04/30/java8-concurrency-tutorial-synchronized-locks-examples/
public class Part02Test {
	private int count = 0;

	@Test
	public void increment_with_race_condition() {
		ExecutorService executorService = Executors.newFixedThreadPool(2);

		IntStream.range(0, 10_000) //
				.forEach(value -> executorService.submit(this::increment));
		ExecutorServiceUtil.stop(executorService);

		System.out.println(count);
		// race condition(10_000이 안되는)은 method level, block level synchronized(reentrant)로 해소 가능
	}

	/**
	 * ReentrantLock is a mutual exclusion lock with the same basic behavior as the implicit monitors accessed via the synchronized keyword but with
	 * extended capabilities. As the name suggests this lock implements reentrant characteristics just as implicit monitors.
	 */
	ReentrantLock reentrantLock = new ReentrantLock();

	private void increment() {
		reentrantLock.lock();
		try {
			count += 1;
		} finally {
			reentrantLock.unlock();
		}
		/**
		 * important to wrap your code into a try/finally block to ensure unlocking in case of exceptions.
		 */
		/**
		 * 아래와 같은 메소드들도 지원
		 * 
		 * <pre>
		 * System.out.println(&quot;Locked: &quot; + lock.isLocked());
		 * System.out.println(&quot;Held by me: &quot; + lock.isHeldByCurrentThread());
		 * boolean locked = lock.tryLock(); // an alternative to lock() tries to acquire the lock without pausing the current thread.
		 * System.out.println(&quot;Lock acquired: &quot; + locked);
		 * </pre>
		 */
	}

	// ReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * StampedLock
	 * 
	 * also support read and write locks just like in the example above. In contrast to ReadWriteLock the locking methods of a StampedLock return a
	 * stamp represented by a long value.
	 * 
	 * You can use these stamps to either release a lock or to check if the lock is still valid. Additionally stamped locks support another lock mode
	 * called optimistic locking.
	 * 
	 * <pre>
	 * long stamp = lock.tryOptimisticRead();
	 * System.out.println(&quot;Optimistic Lock Valid: &quot; + lock.validate(stamp));
	 * </pre>
	 * 
	 * <pre>
	 * executor.submit(() -&gt; {
	 * 	long stamp = lock.readLock();
	 * 	try {
	 * 		if (count == 0) {
	 * 			stamp = lock.tryConvertToWriteLock(stamp);
	 * 			if (stamp == 0L) {
	 * 				System.out.println(&quot;Could not convert to write lock&quot;);
	 * 				stamp = lock.writeLock();
	 * 			}
	 * 			count = 23;
	 * 		}
	 * 		System.out.println(count);
	 * 	} finally {
	 * 		lock.unlock(stamp);
	 * 	}
	 * });
	 * </pre>
	 */

	/**
	 * Semaphores
	 * 
	 * Whereas locks usually grant exclusive access to variables or resources, a semaphore is capable of maintaining whole sets of permits.
	 * 
	 * This is useful in different scenarios where you have to limit the amount concurrent access to certain parts of your application.
	 */

	/**
	 * *
	 * <pre>
	 * ExecutorService executor = Executors.newFixedThreadPool(10);
	 * 
	 * Semaphore semaphore = new Semaphore(5);
	 * 
	 * Runnable longRunningTask = () -> {
	 *     boolean permit = false;
	 *     try {
	 *         permit = semaphore.tryAcquire(1, TimeUnit.SECONDS);
	 *         if (permit) {
	 *             System.out.println("Semaphore acquired");
	 *             sleep(5);
	 *         } else {
	 *             System.out.println("Could not acquire semaphore");
	 *         }
	 *     } catch (InterruptedException e) {
	 *         throw new IllegalStateException(e);
	 *     } finally {
	 *         if (permit) {
	 *             semaphore.release();
	 *         }
	 *     }
	 * }
	 * 
	 * IntStream.range(0, 10)
	 *     .forEach(i -> executor.submit(longRunningTask));
	 * 
	 * stop(executor);
	 * </pre>
	 */
}

package java8_concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceUtil {
	public static void stop(ExecutorService executor) {
		System.out.println("attempt to shutdown executor");
		executor.shutdown();
		try {
			executor.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (!executor.isTerminated())
				System.out.println("cancel non-finished tasks");
			executor.shutdownNow();
			System.out.println("shutdown finished");
		}
		System.out.println("Done !!");
	}
}

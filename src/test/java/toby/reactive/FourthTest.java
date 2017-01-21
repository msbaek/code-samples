package toby.reactive;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
public class FourthTest {
	interface SuccessCallback {
		void onSuccess(String res);
	}

	public static class CallbackFuture extends FutureTask<String> {
		private final SuccessCallback successCallback;

		public CallbackFuture(Callable<String> callable, SuccessCallback successCallback) {
			super(callable);
			this.successCallback = successCallback;
		}

		@Override
		protected void done() {
			try {
				successCallback.onSuccess(get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void future_example() throws InterruptedException, ExecutionException {
		ExecutorService es = Executors.newCachedThreadPool();

		CallbackFuture f = new CallbackFuture(() -> {
			SECONDS.sleep(2);
			log.debug("async");
			return "Hello";
		}, res -> log.debug("result={}", res));

		es.execute(f);

		log.debug("isDone: {}", f.isDone());
		MILLISECONDS.sleep(2100);
		log.debug("Exit");
		log.debug("isDone: {}", f.isDone());
		log.debug("future: {}", f.get()); // blocking
	}
}

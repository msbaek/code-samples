package rxjava;

import org.junit.Test;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class RxTest {
	@Test
	public void just_fires_event_once_when_someone_start_to_subscribe() {
		System.out.println("create observable");
		Observable<String> observable = Observable.just("개미");
		System.out.println("do subscribe");
		observable.subscribe(new Subscriber<String>() {
			@Override
			public void onCompleted() {
				System.out.println("onCompleted");
			}

			@Override
			public void onError(Throwable e) {
				System.out.println("onError: " + e.getMessage());
			}

			@Override
			public void onNext(String s) {
				System.out.println("onNext: " + s);
			}
		});
	}

	@Test
	public void from_fires_events_in_array_or_iterable() {
		System.out.println("create observable");
		Observable<String> observable = Observable.from(new String[] { "개미", "매", "마루" });
		System.out.println("do subscribe");
		observable.subscribe(new Subscriber<String>() {
			@Override
			public void onCompleted() {
				System.out.println("onCompleted");
			}

			@Override
			public void onError(Throwable e) {
				System.out.println("onError: " + e.getMessage());
			}

			@Override
			public void onNext(String s) {
				System.out.println("onNext: " + s);
			}
		});
	}

	@Test
	public void defer_구독_하는_순간_특정_function을_실행하고_리턴받은_Observable의_이벤트를_전달() {
		System.out.println("create observable");
		Observable<String> observable = Observable.defer(() -> {
			System.out.println("defer function call");
			return Observable.just("Deferred Function Result");
		});
		System.out.println("do subscribe");
		observable.subscribe(new Subscriber<String>() {
			@Override
			public void onCompleted() {
				System.out.println("onCompleted");
			}

			@Override
			public void onError(Throwable e) {
				System.out.println("onError: " + e.getMessage());
			}

			@Override
			public void onNext(String s) {
				System.out.println("onNext: " + s);
			}
		});
	}

	@Test
	public void defer_비동기_테스트() {
		System.out.println(Thread.currentThread().getName() + ", create observable");

		Observable<String> observable = Observable.defer(() -> {
			System.out.println(Thread.currentThread().getName() + ", defer function call");
			return Observable.just("HelloWorld");
		});

		System.out.println(Thread.currentThread().getName() + ", do subscribe");

		observable.subscribeOn(Schedulers.computation()) // computation thread 에서 defer function 이 실행됩니다.
				.observeOn(Schedulers.newThread()) // 새로운 thread 에서 Subscriber 로 이벤트가 전달됩니다.
				.subscribe(new Subscriber<String>() {
					@Override
					public void onNext(String text) {
						System.out.println(Thread.currentThread().getName() + ", onNext : " + text);
					}

					@Override
					public void onCompleted() {
						System.out.println(Thread.currentThread().getName() + ", onCompleted");
					}

					@Override
					public void onError(Throwable e) {
						System.out.println(Thread.currentThread().getName() + ", onError : " + e.getMessage());
					}
				});

		System.out.println(Thread.currentThread().getName() + ", after subscribe");
		try {
			Thread.sleep(3 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

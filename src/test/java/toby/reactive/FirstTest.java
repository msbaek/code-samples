package toby.reactive;

import org.junit.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FirstTest {
	// Duality
	// Observer Pattern
	// Reactive Streams: will be included in Java9 API

	@Test
	public void iterable_test() {
		Iterable<Integer> iter = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		for (Integer i : iter) {
			System.out.println(i);
		}
		// 위 코드에서 10이 아니라 Integer.MAX_VALUE까지 값을 출력하기 쉽지 않다
		// 아래와 같이 Iterator를 구현하는 것이 수월하다
		Iterable<Integer> iter2 = new Iterable<Integer>() {
			@Override
			public Iterator<Integer> iterator() {
				return new Iterator<Integer>() {
					int i = 0;
					final static int MAX = 10;

					@Override
					public boolean hasNext() {
						return i < MAX;
					}

					@Override
					public Integer next() {
						return ++i;
					}
				};
			}
		};
		for (Integer i : iter2) {
			System.out.println(i);
		}
		// 위 Iterable 구현은 함수가 하나 뿐인 functional interface이니
		// lambda로 변환 가능
		Iterable<Integer> iter3 = () -> new Iterator<Integer>() {
			int i = 0;
			final static int MAX = 10;

			@Override
			public boolean hasNext() {
				return i < MAX;
			}

			@Override
			public Integer next() {
				return ++i;
			}
		};
		for (Integer i : iter3) {
			System.out.println(i);
		}
		// Iterable <—> Observable(쌍대성 - duality. 에릭 마이어)
		// pull push
		// observable: source로서 event/data를 observer에게 던지는 역할 수행
	}

	@Test
	public void int_observable() {
		class IntObservable extends Observable implements Runnable {
			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					setChanged();
					notifyObservers(i);
				}
			}
		}

		Observer o = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				System.out.println(Thread.currentThread().getName() + " " + arg);
			}
		};

		IntObservable observable = new IntObservable();
		observable.addObserver(o);

		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(observable);
		System.out.println(Thread.currentThread().getName() + " EXIT");
		executorService.shutdown();
		// 쌍대성
		// 리턴 -> 인자
		// i = it.next() -> notifyObservers(i);
		// int next(void) -> void notifyObservers(int)
		//
		//
		// iterable로 별도의 쓰레드에서 동작하도록 만드려면 복잡해짐. 푸시방식에선 쉽게 됨.
		//
		// MS에서 초기 react를 만들 때 기존 Observable에
		// 1. 종료 여부 (complete)를 알릴 수 있는 방법이 없다.
		// 2. 예외 처리(error)가 명확치 않다
		// 는 점에 착안했다.
		// 이렇게 2가지가 추가된 확장된 observer pattern이 react
		// http://www.reactive-streams.org/reactive-streams-1.0.0-javadoc/
	}

	@Test
	public void pub_sub_test() throws InterruptedException {
		// Publisher <- Observable
		// Subscriver <- Observer
		// https://github.com/reactive-streams/reactive-streams-jvm/blob/v1.0.0/README.md
		// Publisher:
		// a provider of a potentially unbounded number of sequenced elements
		// publishing them according to the demand received from its Subscriber(s).
		// onSubscribe onNext* (onError | onComplete)?
		//
		// https://api.monosnap.com/rpc/file/download?id=2pKICTl4vqhtHsLx4FvlIrg2L3leYW
		// subscriber는 onSubscribe(Subscription subscription) 메소드의 Subscription 객체를 통해서
		// publisher가 subscriber에게 발생시키는 이벤트를 조절할 수 있다 — backpressure
		ExecutorService executorService = Executors.newCachedThreadPool();
		Iterable<Integer> itr = Arrays.asList(1, 2, 3, 4, 5);
		Iterator<Integer> it = itr.iterator();
		Publisher<Integer> p = new Publisher() {
			@Override
			public void subscribe(Subscriber subscriber) {
				subscriber.onSubscribe(new Subscription() {
					@Override
					public void request(long n) {
						executorService.execute(() -> {
							int i = 0;
							while (i++ < n) {
								if (it.hasNext()) {
									subscriber.onNext(it.next());
								} else {
									subscriber.onComplete();
									break;
								}
							}
						});
					}

					@Override
					public void cancel() {
					}
				});
			}
		};

		Subscriber<Integer> s = new Subscriber<Integer>() {
			@Override
			public void onSubscribe(Subscription s) {
				System.out.println("onSubscribe");
				// s.request(Integer.MAX_VALUE);
				s.request(1);
			}

			@Override
			public void onNext(Integer integer) {
				System.out.println("onNext: " + integer);
			}

			@Override
			public void onError(Throwable t) {
				System.out.println("onError");
			}

			@Override
			public void onComplete() {
				System.out.println("onComplete");
			}
		};

		p.subscribe(s);
	}
}

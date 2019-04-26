package reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.Disposable;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class BaeldungReactorCoreTest {

    private List<Integer> elements;

    @Before
    public void setUp() throws Exception {
        elements = new ArrayList<>();
    }

    @Test
    public void collecting_elements() throws Exception {
        Flux.just(1, 2, 3, 4)
                .log()
                .subscribe(elements::add);
        assertThat(elements).containsExactly(1, 2, 3, 4);

//        elements = Flux.just(1, 2, 3, 4)
//                .log()
//                .collect(toList())
//                .subscribe()
//                .block();
        assertThat(elements).containsExactly(1, 2, 3, 4);
        /**
         * everything is running on the main thread
         *
         * 1. onSubscribe() – This is called when we subscribe to our stream
         * 2. request(unbounded) – When we call subscribe, behind the scenes we are creating a Subscription.
         * This subscription requests elements from the stream. In this case, it defaults to unbounded, meaning it requests every single element available
         * 3. onNext() – This is called on every single element
         * 4. onComplete() – This is called last, after receiving the last element. There’s actually a onError() as well, which would be called if there is an exception, but in this case, there isn’t
         */
        List<Integer> l = new ArrayList<>();
        Flux.just(1, 2, 3, 4)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        log.info("onSubscribe");
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        log.info("onNext={}", integer);
                        l.add(integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        log.info("onError={}", t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        log.info("onComplete()");
                    }
                });
        assertThat(l).containsExactly(1, 2, 3, 4);
    }

    @Test
    public void backpressure() throws Exception {
        Flux.just(1, 2, 3, 4)
                .log()
                .subscribe(new Subscriber<Integer>() {
                    private Subscription s;
                    int onNextAmount;

                    @Override
                    public void onSubscribe(Subscription s) {
                        this.s = s;
                        s.request(2);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        elements.add(integer);
                        onNextAmount++;
                        if (onNextAmount % 2 == 0) {
                            s.request(2);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
        assertThat(elements).containsExactly(1, 2, 3, 4);
    }

    @Test
    public void mapping() throws Exception {
        Flux.just(1, 2, 3, 4)
                .log()
                .map(i -> i * 2)
                .subscribe(elements::add);
        assertThat(elements).containsExactly(2, 4, 6, 8);
    }

    @Test
    public void combining_two_streams() throws Exception {
        List<String> elements = new ArrayList<>();
        Flux.just(1, 2, 3, 4)
                .log()
                .map(i -> i * 2)
                .zipWith(Flux.range(0, Integer.MAX_VALUE),
                        (second, first) -> String.format("First Flux: %d, Second Flux: %d", first, second))
                .subscribe(s -> elements.add(s));
        assertThat(elements).containsExactly(
                "First Flux: 0, Second Flux: 2",
                "First Flux: 1, Second Flux: 4",
                "First Flux: 2, Second Flux: 6",
                "First Flux: 3, Second Flux: 8"
        );
    }

    @Test
    public void creating_a_connectableFlux() throws Exception {
        ConnectableFlux<Object> publish = Flux.create(fluxSink -> {
            while (true) {
                fluxSink.next(System.currentTimeMillis());
            }
        })
                .publish();

        publish.subscribe(System.out::println);
        publish.subscribe(System.out::println);
//        publish.connect();
        /**
         * running this code, nothing will happen.
         * It’s not until we call connect(), that the Flux will start emitting.
         */

        publish = Flux.create(fluxSink -> {
            while (true) {
                fluxSink.next(System.currentTimeMillis());
            }
        })
                .sample(Duration.ofSeconds(1)) // values will only be pushed to our subscriber every two seconds
                .publish();

        publish.subscribe(System.out::println);
        publish.connect();
    }

    @Test
    public void concurrency() throws Exception {
        Disposable subscribe = Flux.just(1, 2, 3, 4)
                .log()
                .map(i -> i * 2)
                .publishOn(Schedulers.elastic())
                .subscribeOn(Schedulers.parallel())
                .subscribe(i -> {
                    elements.add(i);
                    log.debug("{} added to elements", i);
                });
        Thread.sleep(1000);
        log.debug("elements={}", elements.toString());
    }
}

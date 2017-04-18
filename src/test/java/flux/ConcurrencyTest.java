package flux;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
public class ConcurrencyTest {
    @Test
    public void subscribe_on_configure_the_subscriptions_to_be_handled_in_a_background_thread() throws Exception {
        Flux.range(1, 100)
                .log()
//                .map(String::toUpperCase)
//                .subscribeOn(Schedulers.newParallel("sub", 10))
//                .flatMap(s -> Mono.just(s * 100).subscribeOn(Schedulers.parallel()), 2)
                .flatMap(s -> Mono.fromCallable(() -> s * 100).subscribeOn(Schedulers.elastic()), 2)
                .subscribe(new Subscriber<Integer>() {
                    private int count = 0;
                    private Subscription subscription;

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription = s;
                        subscription.request(2);
                    }

                    @Override
                    public void onNext(Integer s) {
                        count++;
                        System.out.printf("onNext s=%d\n", s);
                        if (count >= 2) {
                            count = 0;
                            subscription.request(2);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("onError\n" + t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                })
        ;
        SECONDS.sleep(1);

        System.out.println("done");
    }

    @Test
    public void flux_flatMap_mono_collect_doOnSuccess_subscribe() throws Exception {
        long startMillis = System.currentTimeMillis();

        Flux.just("SKP", "11TF", "IAM", "11PAY")
                .log()
//                .flatMap(this::getSlugList, 4)
                .flatMap(this::getSlugList)
                .collect(this::initResult, this::add)
                .doOnSuccess(this::stop)
                .subscribe()
        ;

        long endMillis = System.currentTimeMillis();

        log.info("elapsed time millis={}", endMillis - startMillis);
        SECONDS.sleep(6);
    }

    private void stop(List<String> strings) {
        log.info("stop={}", strings);
    }

    private void add(List<String> acc, List<String> strings) {
        log.info("add acc={}, strings={}", acc, strings);
        acc.addAll(strings);
    }

    private List<String> initResult() {
        log.info("initResult()");
        return new ArrayList<>();
    }

    public Mono<List<String>> getSlugList(String projectKey) {
//        return Mono.just(getSlugs(projectKey)); // 14.158, 14.012
        return Mono.fromCallable(() -> getSlugs(projectKey))
                .subscribeOn(Schedulers.elastic()); // 154, 5.014
    }

    private List<String> getSlugs(String projectKey) {
        if ("SKP".equals(projectKey)) {
            sleep(5);
            return Arrays.asList("11st", "Syrup", "OCB");
        } else if ("11TF".equals(projectKey)) {
            sleep(4);
            return Arrays.asList("DEPT", "VINE");
        } else if ("IAM".equals(projectKey)) {
            sleep(3);
            return Arrays.asList("Samsung Pass", "Wallet");
        } else {
            sleep(2);
            return Arrays.asList("N/A");
        }
    }

    private void sleep(int seconds) {
        try {
            SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
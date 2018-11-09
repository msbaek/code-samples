package reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

// https://javatechnicalwealth.com/blog/reactive-flatmap/
@Slf4j
public class FlatMapTest {
    private List<String> toUpperCase(String s) {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("s: {}", s);
        return Arrays.asList(s.toUpperCase(), Thread.currentThread().getName(), String.valueOf(System.currentTimeMillis()));
    }

    @Test
    void flatMap() {
        Flux.just("a", "b", "c", "d", "e", "f", "g", "h", "i")
            .window(3)
            .flatMapSequential(s -> s.map(this::toUpperCase).subscribeOn(parallel()))
//            .concatMap(s -> s.map(this::toUpperCase).subscribeOn(parallel()))
            // to keep order
//            .flatMap(s -> s.map(this::toUpperCase).subscribeOn(parallel()))
//            .flatMap(s -> s.map(this::toUpperCase))
            // 9 sec --> 3 sec. with parallel
            .doOnNext(System.out::println)
            .blockLast();
    }
}

package reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

// https://www.infoq.com/articles/reactor-by-example
public class InfoQReactorTest {
    private static List<String> words = Arrays.asList(
            "the",
            "quick",
            "brown",
            "fox",
            "jumped",
            "over",
            "the",
            "lazy",
            "dog"
    );

    @Test
    public void simple_creation() throws Exception {
        Flux<String> fewWords = Flux.just("Hello", "World");
        Flux<String> manyWords = Flux.fromIterable(words);

        fewWords.subscribe(System.out::println);
        System.out.println();
        manyWords.subscribe(System.out::println);
    }

    @Test
    public void find_missing_letter() throws Exception {
        Flux<String> manyLetters = Flux.fromIterable(words)
                .flatMap(word -> Flux.fromArray(word.split("")))
                .distinct()
                .sort()
                .zipWith(Flux.range(1, Integer.MAX_VALUE),
                        (string, count) -> String.format("%2d. %s", count, string));

        Mono<String> missing = Mono.just("s");
        Flux<String> allLetters = Flux.fromIterable(words)
                .flatMap(word -> Flux.fromArray(word.split("")))
                .concatWith(missing)
                .distinct()
                .sort()
                .zipWith(Flux.range(1, Integer.MAX_VALUE),
                        (string, count) -> String.format("%2d. %s", count, string));
        allLetters.subscribe(System.out::println);

    }

    @Test
    public void sort_circuit() throws Exception {
        Flux<String> helloPauseWorld = Mono.just("Hello")
                .concatWith(Mono.just("World")
                        .delaySubscription(Duration.ofMillis(500)));
//        helloPauseWorld.subscribe(System.out::println);
        helloPauseWorld.toStream() // toIterable, toStream both produce blocking instance
                .forEach(System.out::println);
    }

    @Test
    public void first_emitting() throws Exception {
        Mono<String> a = Mono.just("oops I'm late")
                .delaySubscription(Duration.ofMillis(450));

        Flux<String> b = Flux.just("let's get", "the party", "started")
                .delaySubscription(Duration.ofMillis(400));

//        Flux.firstEmitting(a, b)
//                .toIterable()
//                .forEach(System.out::println);
    }
}

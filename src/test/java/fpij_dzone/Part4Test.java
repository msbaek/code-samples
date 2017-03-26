package fpij_dzone;

import lombok.Data;
import org.junit.Test;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * https://dzone.com/articles/an-introduction-to-functional-programming-in-java-8-part-4-splitter
 */
public class Part4Test {
    /**
     * Split a Stream via a Specific Filter
     */

    /**
     * The Basic (and Slow) Approach
     * O(2n)
     */
    public <T> void splitAndPerform(Collection<T> items, Predicate<T> splitBy, Consumer<T> passed, Consumer<T> notPassed) {
        items.stream()
                .filter(splitBy)
                .forEach(passed);
        items.stream()
                .filter(splitBy.negate())
                .forEach(notPassed);
    }

    /**
     * The Better Approach
     * 1. Split the Collection
     */
    public static class Splitter<T> {
        private List<T> passed;
        private List<T> notPassed;

        private Splitter(List<T> passed, List<T> notPassed) {
            this.passed = passed;
            this.notPassed = notPassed;
        }

        //  factory method pattern to make the creation of the Splitter nicer.
//        public static <T> Splitter<T> splitBy(Collection<T> items, Predicate<T> test) {
//            List<T> passed = new LinkedList<T>();
//            List<T> notPassed = new LinkedList<T>();
//            items.stream()
//                    .forEach(item -> {
//                        if (test.test(item)) {
//                            passed.add(item);
//                            return;
//                        }
//                        notPassed.add(item);
//                    });
//            return new Splitter<T>(passed, notPassed);
//        }

        // using partitioningBy
        public static <T> Splitter<T> splitBy(Collection<T> items, Predicate<T> test) {
            Map<Boolean, List<T>> map = items.stream()
                    .collect(Collectors.partitioningBy(test));
            return new Splitter<>(map.get(true), map.get(false));
        }

        // 2. Work With the Split Lists
        public Splitter<T> workWithPassed(Consumer<Stream<T>> func) {
            func.accept(passed.stream());
            return this;
        }

        public Splitter<T> workWithNotPassed(Consumer<Stream<T>> func) {
            func.accept(notPassed.stream());
            return this;
        }
    }

    @Test
    public void showing_numbers_but_squaring_all_odd_numbers() throws Exception {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        Splitter.splitBy(numbers, num -> num % 2 == 0)
                .workWithPassed(passed ->
                        passed.forEach(even -> System.out.println("" + even + " -> " + even)))
                .workWithNotPassed(notPassed ->
                        notPassed.map(odd -> odd * odd)
                                .forEach(odd -> System.out.println("" + Math.sqrt(odd) + " -> " + odd)
                                ));
    }

    @Data
    class Candidates {
        private String email;

        public boolean hasWon() {
            return false;
        }
    }

    static class Email {
        public static void send(String email, String message) {
        }
    }

    @Test
    public void sending_all_winners_a_confirmation_and_all_losers_a_cancellation() throws Exception {
        List<Candidates> candidates = Collections.emptyList();
        Splitter.splitBy(candidates, Candidates::hasWon)
                .workWithPassed(winners ->
                        winners.forEach(winner -> Email.send(winner.getEmail(), "You won!"))
                )
                .workWithNotPassed(losers ->
                        losers.forEach(loser -> Email.send(loser.getEmail(), "You lost, sorry!"))
                );
    }
}
